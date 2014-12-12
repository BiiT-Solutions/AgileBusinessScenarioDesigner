package com.biit.abcd.core.drools.rules;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import net.xeoh.plugins.base.Plugin;

import com.biit.abcd.core.PluginController;
import com.biit.abcd.core.drools.prattparser.ExpressionChainPrattParser;
import com.biit.abcd.core.drools.prattparser.PrattParser;
import com.biit.abcd.core.drools.prattparser.PrattParserException;
import com.biit.abcd.core.drools.prattparser.visitor.ITreeElement;
import com.biit.abcd.core.drools.prattparser.visitor.TreeElementGroupEndRuleConditionCreatorVisitor;
import com.biit.abcd.core.drools.prattparser.visitor.TreeElementMathExpressionVisitor;
import com.biit.abcd.core.drools.prattparser.visitor.exceptions.NotCompatibleTypeException;
import com.biit.abcd.core.drools.rules.exceptions.BetweenFunctionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.DateComparisonNotPossibleException;
import com.biit.abcd.core.drools.rules.exceptions.DroolsRuleCreationException;
import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.NullCustomVariableException;
import com.biit.abcd.core.drools.rules.exceptions.NullExpressionValueException;
import com.biit.abcd.core.drools.rules.exceptions.NullTreeObjectException;
import com.biit.abcd.core.drools.rules.exceptions.PluginInvocationException;
import com.biit.abcd.core.drools.rules.exceptions.RuleNotImplementedException;
import com.biit.abcd.core.drools.rules.exceptions.TreeObjectInstanceNotRecognizedException;
import com.biit.abcd.core.drools.rules.exceptions.TreeObjectParentNotValidException;
import com.biit.abcd.core.drools.utils.RulesUtils;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.abcd.persistence.entity.AnswerType;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.CustomVariableType;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.expressions.AvailableFunction;
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionFunction;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorLogic;
import com.biit.abcd.persistence.entity.expressions.ExpressionPluginMethod;
import com.biit.abcd.persistence.entity.expressions.ExpressionSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionValue;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueGlobalConstant;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueNumber;
import com.biit.abcd.persistence.entity.expressions.ExpressionValuePostalCode;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueString;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueSystemDate;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTimestamp;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.form.TreeObject;
import com.biit.plugins.interfaces.IPlugin;

public class DroolsParser {

	private static String andOperator(List<Expression> expressions) throws ExpressionInvalidException,
			NullTreeObjectException, TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException,
			NullCustomVariableException, NullExpressionValueException, BetweenFunctionInvalidException,
			DateComparisonNotPossibleException, DroolsRuleCreationException {
		StringBuilder result = new StringBuilder();

		ExpressionChain leftChain = (ExpressionChain) expressions.get(0);
		ExpressionChain rightChain = (ExpressionChain) expressions.get(2);

		result.append(processResultConditionsFromPrattParser(leftChain));
		result.append(processResultConditionsFromPrattParser(rightChain));

		return result.toString();
	}

	/**
	 * Parse conditions like => Question IN(answer1, ...) <br>
	 * Create drools rule like => Question(getAnswer() in (answer1, ...))
	 * 
	 * @param conditions
	 * @return LHS of the rule
	 * @throws TreeObjectParentNotValidException
	 * @throws TreeObjectInstanceNotRecognizedException
	 * @throws NullTreeObjectException
	 * @throws DroolsRuleCreationException
	 */
	private static String createInBetweenFunctionConditions(ExpressionChain conditions) throws NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, DroolsRuleCreationException {
		String droolsConditions = "";
		AvailableFunction functionParsed = ((ExpressionFunction) conditions.getExpressions().get(1)).getValue();
		List<Expression> operatorLeft = ((ExpressionChain) conditions.getExpressions().get(0)).getExpressions();
		ExpressionValueTreeObjectReference leftExpressionValueTreeObject = null;
		if ((operatorLeft.size() == 1) && (operatorLeft.get(0) instanceof ExpressionValueTreeObjectReference)) {
			leftExpressionValueTreeObject = (ExpressionValueTreeObjectReference) operatorLeft.get(0);
		}
		if (leftExpressionValueTreeObject != null) {
			// Create the array of values to consult
			List<ExpressionValue<?>> operatorValues = getFunctionParameters(conditions);
			// Check if the types inside match
			if (checkExpressionValueTypes(getExpressionValueTreeObjectAnswerFormat(leftExpressionValueTreeObject),
					operatorValues)) {
				List<String> functionValues = new ArrayList<>();
				String inValuesString = "";
				for (ExpressionValue<?> value : operatorValues) {
					if (value instanceof ExpressionValueCustomVariable) {
						inValuesString += "'"
								+ getDroolsVariableValueFromExpressionValueTreeObject((ExpressionValueCustomVariable) value)
								+ "', ";
						functionValues
								.add("'"
										+ getDroolsVariableValueFromExpressionValueTreeObject((ExpressionValueCustomVariable) value)
										+ "'");
					} else if (value instanceof ExpressionValueTreeObjectReference) {
						inValuesString += "'" + ((ExpressionValueTreeObjectReference) value).getReference().getName()
								+ "', ";
						functionValues.add("'" + ((ExpressionValueTreeObjectReference) value).getReference().getName()
								+ "'");
					} else if (value instanceof ExpressionValueNumber) {
						inValuesString += value.getValue() + ", ";
						functionValues.add(value.getValue().toString());
					} else {
						inValuesString += "'" + value.getValue() + "', ";
						functionValues.add("'" + value.getValue() + "'");
					}
				}
				if (inValuesString.length() > 2) {
					// Remove the last comma
					inValuesString = inValuesString.substring(0, inValuesString.length() - 2);
				}
				// Creates the rule
				if (!inValuesString.isEmpty()) {
					if (leftExpressionValueTreeObject instanceof ExpressionValueCustomVariable) {
						ExpressionValueCustomVariable leftVariable = (ExpressionValueCustomVariable) leftExpressionValueTreeObject;
						TreeObject leftReferenceParent = leftVariable.getReference().getParent();
						String variableName = leftVariable.getVariable().getName();
						// Get the drools conditions for the parent
						switch (leftVariable.getVariable().getScope()) {
						case FORM:
							putTreeObjectName(leftVariable.getReference(), leftVariable.getReference()
									.getUniqueNameReadable());
							// First common part of the condition
							droolsConditions += "\t$" + leftVariable.getReference().getUniqueNameReadable()
									+ " : SubmittedForm(isVariableDefined('" + variableName + "'), getVariableValue('"
									+ variableName + "')";
							switch (functionParsed) {
							case BETWEEN:
								if (functionValues.size() == 2) {
									droolsConditions += " >= " + functionValues.get(0) + " && < "
											+ functionValues.get(1) + ") ";
								} else {
									throw new DroolsRuleCreationException(
											"Invalid number of values in the between function", conditions);
								}
								break;
							case IN:
								droolsConditions += " in ( " + inValuesString + " )) ";
								break;
							default:
								// Never gets here
								throw new DroolsRuleCreationException("No valid function '" + functionParsed.toString()
										+ "'found.", conditions);
							}
							// End common part of the condition
							droolsConditions += "from $droolsForm.getSubmittedForm() \n";
							break;
						case CATEGORY:
						case GROUP:
						case QUESTION:
							droolsConditions += SimpleConditionsGenerator.getTreeObjectConditions(leftReferenceParent);
							String className = leftVariable.getReference().getClass().getSimpleName();
							// First common part of the condition
							droolsConditions += "\t$" + leftVariable.getReference().getUniqueNameReadable()
									+ " : Submitted" + className + "( "
									+ RulesUtils.returnSimpleTreeObjectNameFunction(leftVariable.getReference())
									+ "', isVariableDefined('" + variableName + "'), getVariableValue('" + variableName
									+ "')";

							switch (functionParsed) {
							case BETWEEN:
								if (functionValues.size() == 2) {
									droolsConditions += " >= " + functionValues.get(0) + " && < "
											+ functionValues.get(1) + ") ";

								} else {
									throw new DroolsRuleCreationException(
											"Invalid number of values in the between function", conditions);
								}
								break;
							case IN:
								droolsConditions += " in( " + inValuesString + " )) ";
								break;
							default:
								// Never gets here
								throw new DroolsRuleCreationException("No valid function '" + functionParsed.toString()
										+ "'found.", conditions);
							}
							// End common part of the condition
							droolsConditions += "from $" + leftReferenceParent.getUniqueNameReadable()
									+ ".getChildren(I" + className + ".class)\n";
							break;
						}
					} else if (leftExpressionValueTreeObject.getReference() instanceof Question) {
						TreeObject leftReference = leftExpressionValueTreeObject.getReference();
						TreeObject leftReferenceParent = leftReference.getParent();
						putTreeObjectName(leftReference, leftReference.getUniqueNameReadable());
						// Get the drools conditions for the parent
						droolsConditions += SimpleConditionsGenerator.getTreeObjectConditions(leftReferenceParent);
						Question leftQuestion = (Question) leftReference;
						// First common part of the condition
						droolsConditions += "t$" + leftQuestion.getUniqueNameReadable() + " : SubmittedQuestion("
								+ RulesUtils.returnSimpleTreeObjectNameFunction(leftQuestion) + "', getAnswer('"
								+ getTreeObjectAnswerType(leftQuestion) + "')";
						// Different part of the condition
						switch (functionParsed) {
						case BETWEEN:
							if (functionValues.size() == 2) {
								droolsConditions += " >= " + functionValues.get(0) + " && < " + functionValues.get(1)
										+ ") ";
							} else {
								throw new DroolsRuleCreationException(
										"Invalid number of values in the between function", conditions);
							}
							break;
						case IN:
							droolsConditions += " in( " + inValuesString + " )) ";
							break;
						default:
							// Never gets here
							throw new DroolsRuleCreationException("No valid function '" + functionParsed.toString()
									+ "'found.", conditions);
						}
						// End common part of the condition
						droolsConditions += "from $" + leftReferenceParent.getUniqueNameReadable()
								+ ".getChildren(IQuestion.class)" + RulesUtils.addFinalCommentsIfNeeded(leftQuestion)
								+ "\n";
					}
				} else {
					throw new DroolsRuleCreationException("No valid values found inside the function '"
							+ functionParsed.toString() + "'", conditions);
				}
			}
		} else {
			throw new DroolsRuleCreationException("No valid left reference found parsing the '"
					+ functionParsed.toString() + "' function", conditions);
		}
		return droolsConditions;
	}

	/**
	 * Checks the existence of a binding in drools with the the reference of the
	 * variable passed If there is no binding, creates a new one (i.e. $var :
	 * Question() ...)
	 * 
	 * @param expValVariable
	 * @throws TreeObjectParentNotValidException
	 * @throws TreeObjectInstanceNotRecognizedException
	 * @throws NullExpressionValueException
	 * @throws NullTreeObjectException
	 * @throws NullCustomVariableException
	 */
	private static String checkVariableAssignation(Expression expression) throws NullCustomVariableException,
			NullTreeObjectException, NullExpressionValueException, TreeObjectInstanceNotRecognizedException,
			TreeObjectParentNotValidException {
		if (expression instanceof ExpressionValueCustomVariable) {
			return SimpleConditionsGenerator
					.getTreeObjectCustomVariableConditions((ExpressionValueCustomVariable) expression);
		} else if (expression instanceof ExpressionValueTreeObjectReference) {
			return checkVariableAssignation((ExpressionValueTreeObjectReference) expression);
		}
		return "";
	}

	/**
	 * Checks the existence of a binding in drools with the the reference of the
	 * variable passed If there is no binding, creates a new one (i.e. $var :
	 * Question() ...)
	 * 
	 * @param expValVariable
	 * @throws TreeObjectParentNotValidException
	 * @throws TreeObjectInstanceNotRecognizedException
	 * @throws NullTreeObjectException
	 * @throws NullExpressionValueException
	 * @throws NullCustomVariableException
	 */
	private static String checkVariableAssignationWithoutScore(Expression expression)
			throws NullCustomVariableException, NullTreeObjectException, NullExpressionValueException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException {
		if (expression instanceof ExpressionValueCustomVariable) {
			return SimpleConditionsGenerator
					.getTreeObjectCustomVariableConditionsWithoutScoreCheck((ExpressionValueCustomVariable) expression);
		} else if (expression instanceof ExpressionValueTreeObjectReference) {
			return checkVariableAssignation((ExpressionValueTreeObjectReference) expression);
		}
		return "";
	}

	private static String checkVariableAssignation(ExpressionValueTreeObjectReference variable)
			throws NullTreeObjectException, TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException {
		return checkVariableAssignation(variable.getReference());
	}

	private static String checkVariableAssignation(TreeObject treeObject) throws NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException {
		if (treeObject != null) {
			if (getTreeObjectName(treeObject) == null) {
				// The variable don't exists and can't have a value assigned so
				// we have to create it
				return SimpleConditionsGenerator.getTreeObjectConditions(treeObject);
			}
			// The variable is assigned and we do nothing
			return "";
		} else {
			throw new NullTreeObjectException();
		}
	}

	/**
	 * Main method. Create Drools Rules from rules and diagram
	 * 
	 * @param conditions
	 *            conditions of a rule.
	 * @param actions
	 *            actions of a rule.
	 * @param forkConditions
	 *            conditions defined as a fork in a digram.
	 * @return
	 * @throws RuleNotImplementedException
	 * @throws NotCompatibleTypeException
	 * @throws ExpressionInvalidException
	 * @throws NullExpressionValueException
	 * @throws NullCustomVariableException
	 * @throws TreeObjectParentNotValidException
	 * @throws TreeObjectInstanceNotRecognizedException
	 * @throws NullTreeObjectException
	 * @throws BetweenFunctionInvalidException
	 * @throws DateComparisonNotPossibleException
	 * @throws PluginInvocationException
	 * @throws DroolsRuleCreationException
	 * @throws PrattParserException
	 * @throws NoSuchMethodException
	 */
	public static String createDroolsRule(List<Rule> rules) throws RuleNotImplementedException,
			NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException,
			NullExpressionValueException, BetweenFunctionInvalidException, DateComparisonNotPossibleException,
			PluginInvocationException, DroolsRuleCreationException, PrattParserException {
		StringBuilder parsedText = new StringBuilder();
		for (Rule rule : rules) {
			// orOperatorUsed = false;
			if (rule != null) {
				String parsedRule = createDroolsRule(rule);
				if (parsedRule != null) {
					parsedText.append(rule.getName());

					parsedText.append(RulesUtils.getWhenRuleString());
					if (rule instanceof DroolsRuleGroupEndRule) {
						parsedText.append(RulesUtils.getGroupEndRuleExtraCondition((DroolsRuleGroupEndRule) rule));
					}
					parsedText.append(parsedRule);
					if (rule instanceof DroolsRuleGroup) {
						if (!(rule instanceof DroolsRuleGroupEndRule)) {
							parsedText.append(RulesUtils.getThenRuleString());
						}
						parsedText.append(RulesUtils.getGroupRuleActions((DroolsRuleGroup) rule));
					}
					parsedText.append(RulesUtils.getEndRuleString());
				}
			}
		}
		return parsedText.toString();
	}

	/**
	 * Main method. Create Drools Rules from rules and diagram
	 * 
	 * @param conditions
	 *            conditions of a rule.
	 * @param actions
	 *            actions of a rule.
	 * @param forkConditions
	 *            conditions defined as a fork in a digram.
	 * @return
	 * @throws RuleNotImplementedException
	 * @throws NotCompatibleTypeException
	 * @throws ExpressionInvalidException
	 * @throws NullExpressionValueException
	 * @throws NullCustomVariableException
	 * @throws TreeObjectParentNotValidException
	 * @throws TreeObjectInstanceNotRecognizedException
	 * @throws NullTreeObjectException
	 * @throws BetweenFunctionInvalidException
	 * @throws DateComparisonNotPossibleException
	 * @throws PluginInvocationException
	 * @throws DroolsRuleCreationException
	 * @throws PrattParserException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private static String createDroolsRule(Rule rule) throws RuleNotImplementedException, NotCompatibleTypeException,
			ExpressionInvalidException, NullTreeObjectException, TreeObjectInstanceNotRecognizedException,
			TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException,
			DroolsRuleCreationException, PrattParserException {
		if (rule == null) {
			return null;
		}
		// We make sure the variables map is clear
		TreeObjectDroolsIdMap.clearMap();

		// System.out.println("RULE CONDITIONS: " + rule.getConditions());
		// System.out.println("RULE ACTIONS: " + rule.getActions());

		String result = "\t$droolsForm: DroolsForm()\n";
		// Obtain conditions if exists.
		if ((rule.getConditions() != null) && (rule.getConditions().getExpressions() != null)
				&& (!rule.getConditions().getExpressions().isEmpty())) {
			// Parse an special rule with OR and AND expressions
			if (rule instanceof DroolsRuleGroupEndRule) {
				// It needs the "and" due to the and/or expression parenthesis
				// that will follow
				result = result.replace("\n", " and\n");
				result += parseDroolsRuleGroupEndRuleConditions(rule);
			} else {
				result += parseConditions(rule.getConditions());
			}
		}
		if ((rule.getActions() != null) && (rule.getActions().getExpressions() != null)
				&& (!rule.getActions().getExpressions().isEmpty())) {
			String actionString = parseActions(rule.getActions());
			if (actionString != null) {
				if (rule instanceof DroolsRuleGroupEndRule) {
					result += RulesUtils.addAndToMultipleConditionsAction(actionString);
				} else {
					result += actionString;
				}
			} else {
				return null;
			}
		}
		result = RulesUtils.removeDuplicateLines(result);
		result = RulesUtils.checkForDuplicatedVariables(result);
		// result = RulesUtils.removeExtraParenthesis(result);
		// if (orOperatorUsed)
		// result = RulesUtils.fixOrCondition(result);

		return result;
	}

	/**
	 * Parses the final rule of the OR/AND combination<br>
	 * Based on the ids of the previous rules, it will create a combination of
	 * them following the structure defined by the user.
	 * 
	 * @return
	 * @throws NullExpressionValueException
	 * @throws NullCustomVariableException
	 * @throws TreeObjectParentNotValidException
	 * @throws TreeObjectInstanceNotRecognizedException
	 * @throws NullTreeObjectException
	 * @throws NotCompatibleTypeException
	 * @throws RuleNotImplementedException
	 */
	private static String parseDroolsRuleGroupEndRuleConditions(Rule rule) throws RuleNotImplementedException,
			NotCompatibleTypeException, NullTreeObjectException, TreeObjectInstanceNotRecognizedException,
			TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException {
		String result = "";

		DroolsRuleGroupEndRule endRule = (DroolsRuleGroupEndRule) rule;

		ITreeElement prattResult = endRule.getParserResult();
		if ((prattResult != null) && (prattResult.getExpressionChain() != null)) {
			// Tree visitor that creates the drools rule special and/or
			// conditions
			TreeElementGroupEndRuleConditionCreatorVisitor treeVisitor = new TreeElementGroupEndRuleConditionCreatorVisitor();
			try {
				prattResult.accept(treeVisitor);
				result += treeVisitor.getCompleteExpression().getRepresentation();
				// Replace rule identifiers from the old parsed string
				// (Needed because the rule has been parsed again and the ids
				// have changed)
				for (Entry<String, String> value : endRule.getMapEntry()) {
					result = result.replace(value.getKey(), value.getValue());
				}
				result += " and \n";
			} catch (NotCompatibleTypeException e) {
				AbcdLogger.errorMessage(RuleToDroolsRule.class.getName(), e);
			}
		}
		return result;
	}

	private static String equalsNotEqualsOperator(List<Expression> expressions, AvailableOperator availableOperator)
			throws NullTreeObjectException, TreeObjectInstanceNotRecognizedException,
			TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			DateComparisonNotPossibleException {
		String droolsConditions = "";

		List<Expression> operatorLeft = ((ExpressionChain) expressions.get(0)).getExpressions();
		Expression operator = expressions.get(1);
		List<Expression> operatorRight = ((ExpressionChain) expressions.get(2)).getExpressions();

		if ((operatorLeft.size() == 1) && (operatorLeft.get(0) instanceof ExpressionValueTreeObjectReference)
				&& (operatorRight.size() == 1) && (operatorRight.get(0) instanceof ExpressionValueTreeObjectReference)) {
			TreeObject treeObject1 = ((ExpressionValueTreeObjectReference) operatorLeft.get(0)).getReference();
			TreeObject treeObject2 = ((ExpressionValueTreeObjectReference) operatorRight.get(0)).getReference();
			// Question == Answer
			if ((treeObject1 instanceof Question) && (treeObject2 instanceof Answer)) {
				droolsConditions += questionAnswerEqualsCondition((Question) treeObject1, (Answer) treeObject2,
						availableOperator);
			}
		}
		// TreeObject.score == Value
		else if ((operatorLeft.size() == 1) && (operatorLeft.get(0) instanceof ExpressionValueCustomVariable)
				&& (operatorRight.size() == 1) && (operatorRight.get(0) instanceof ExpressionValueNumber)) {

			droolsConditions += treeObjectScoreLogicOperatorValueExpression(
					(ExpressionValueCustomVariable) operatorLeft.get(0), (ExpressionOperatorLogic) expressions.get(1),
					(ExpressionValueNumber) operatorRight.get(0));
		}
		// TreeObject.score == String
		else if ((operatorLeft.size() == 1) && (operatorLeft.get(0) instanceof ExpressionValueCustomVariable)
				&& (operatorRight.size() == 1) && (operatorRight.get(0) instanceof ExpressionValueString)) {
			droolsConditions += treeObjectScoreEqualsValueString((ExpressionValueCustomVariable) operatorLeft.get(0),
					(ExpressionOperatorLogic) expressions.get(1), (ExpressionValueString) operatorRight.get(0));
		}
		// Question INPUT
		else if ((operatorLeft.size() == 1) && (operatorLeft.get(0) instanceof ExpressionValueTreeObjectReference)
				&& (operator instanceof ExpressionOperatorLogic) && (operatorRight.size() == 1)
				&& (operatorRight.get(0) instanceof ExpressionValue)) {
			Object value = ((ExpressionValue<?>) operatorRight.get(0)).getValue();
			TreeObject leftTreeObject = ((ExpressionValueTreeObjectReference) operatorLeft.get(0)).getReference();
			if (leftTreeObject instanceof Question) {
				// Create the conditions for parents hierarchy
				droolsConditions += SimpleConditionsGenerator.getTreeObjectConditions(leftTreeObject.getParent());
				Question question = ((Question) leftTreeObject);
				switch (question.getAnswerType()) {
				case RADIO:
				case MULTI_CHECKBOX:
					// The flow don't get here, parsed previously in question
					// == answer
					break;
				case INPUT:
					switch (question.getAnswerFormat()) {
					case NUMBER:
					case POSTAL_CODE:
					case TEXT:
						droolsConditions += "\t$" + question.getUniqueNameReadable() + " : SubmittedQuestion("
								+ RulesUtils.returnSimpleTreeObjectNameFunction(question) + "', getAnswer('"
								+ getTreeObjectAnswerType(question) + "') " + operator.getValue()
								+ (question.getAnswerFormat().equals(AnswerFormat.NUMBER) ? " " : " '") + value
								+ (question.getAnswerFormat().equals(AnswerFormat.NUMBER) ? " " : "' ") + ") from $"
								+ question.getParent().getUniqueNameReadable() + ".getChildren(IQuestion.class)"
								+ RulesUtils.addFinalCommentsIfNeeded(question) + "\n";
						break;
					case DATE:
						if (((ExpressionValueTreeObjectReference) operatorLeft.get(0)).getUnit() != null) {
							switch (((ExpressionValueTreeObjectReference) operatorLeft.get(0)).getUnit()) {
							case YEARS:
								if (value instanceof Timestamp) {
									throw new DateComparisonNotPossibleException("Years and Date types incompatible",
											new ExpressionChain(expressions));
								} else {
									droolsConditions += questionDateYearsOperatorValueNumber(leftTreeObject,
											((ExpressionValueNumber) operatorRight.get(0)).getValue(),
											((ExpressionOperatorLogic) operator).getValue());
								}
								break;
							case MONTHS:
								if (value instanceof Timestamp) {
									throw new DateComparisonNotPossibleException("Months and Date types incompatible",
											new ExpressionChain(expressions));
								} else {
									droolsConditions += questionDateMonthsOperatorValueNumber(leftTreeObject,
											((ExpressionValueNumber) operatorRight.get(0)).getValue(),
											((ExpressionOperatorLogic) operator).getValue());
								}
								break;
							case DAYS:
								if (value instanceof Timestamp) {
									throw new DateComparisonNotPossibleException("Days and Date types incompatible",
											new ExpressionChain(expressions));
								} else {
									droolsConditions += questionDateDaysOperatorValueNumber(leftTreeObject,
											((ExpressionValueNumber) operatorRight.get(0)).getValue(),
											((ExpressionOperatorLogic) operator).getValue());
								}
								break;
							case DATE:
								if (value instanceof Timestamp) {
									droolsConditions += "\t$" + question.getUniqueNameReadable()
											+ " : SubmittedQuestion("
											+ RulesUtils.returnSimpleTreeObjectNameFunction(question)
											+ "', getAnswer('" + getTreeObjectAnswerType(question) + "') "
											+ operator.getValue() + " (new Date(" + ((Timestamp) value).getTime()
											+ ")) ) from $" + question.getParent().getUniqueNameReadable()
											+ ".getChildren(IQuestion.class)"
											+ RulesUtils.addFinalCommentsIfNeeded(question) + "\n";
								} else {
									throw new DateComparisonNotPossibleException(
											"The value to compare with the Date is not a Timestamp",
											new ExpressionChain(expressions));
								}
								break;
							}
						}
						break;
					}
					break;
				}
			}
		}
		return droolsConditions;
	}

	private static List<Expression> getExpressionChainVariables(ExpressionChain expressionChain) {
		List<Expression> variables = new ArrayList<Expression>();
		for (Expression expression : expressionChain.getExpressions()) {
			// Look for possible variables that need assignation
			if (expression instanceof ExpressionValue) {
				variables.add(expression);
			} else if (expression instanceof ExpressionChain) {
				variables.addAll(getExpressionChainVariables((ExpressionChain) expression));
			}
		}
		return variables;
	}

	/**
	 * Returns the type of answer for the question in the parameter
	 * 
	 * @return
	 */
	private static String getTreeObjectAnswerType(TreeObject treeObject) {
		if (treeObject instanceof Question) {
			Question question = (Question) treeObject;
			switch (question.getAnswerType()) {
			case RADIO:
			case MULTI_CHECKBOX:
				return AnswerFormat.TEXT.toString();
			case INPUT:
				return question.getAnswerFormat().toString();
			}
			return "";
		}
		return "";
	}

	private static String getTreeObjectName(TreeObject treeObject) {
		return TreeObjectDroolsIdMap.get(treeObject);
	}

	private static String mathAssignationAction(ExpressionChain actions, ITreeElement prattParserResult)
			throws RuleNotImplementedException, NotCompatibleTypeException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException,
			NullExpressionValueException {
		String ruleCore = "";
		List<Expression> chainList = actions.getExpressions();

		if (((ExpressionChain) chainList.get(0)).getExpressions().get(0) instanceof ExpressionValueCustomVariable) {
			ExpressionValueCustomVariable leftExpressionCustomVariable = (ExpressionValueCustomVariable) ((ExpressionChain) chainList
					.get(0)).getExpressions().get(0);
			// Check if the reference exists in the rule, if not, it creates
			// a new reference
			ruleCore += checkVariableAssignation(leftExpressionCustomVariable);
			ExpressionChain expressionChainToSearch = (ExpressionChain) actions.generateCopy();
			expressionChainToSearch.removeFirstExpression();
			List<Expression> variables = getExpressionChainVariables(expressionChainToSearch);
			for (Expression expression : variables) {
				if ((expression instanceof ExpressionValueCustomVariable)
						|| (expression instanceof ExpressionValueTreeObjectReference)) {
					ruleCore += checkVariableAssignation(expression);
				}
			}
			ruleCore += RulesUtils.getThenRuleString();
			String mathematicalExpression = "";
			TreeElementMathExpressionVisitor treePrint = new TreeElementMathExpressionVisitor();
			prattParserResult.accept(treePrint);
			if (treePrint != null) {
				mathematicalExpression = treePrint.getBuilder().toString();
			}
			ruleCore += "	$" + getTreeObjectName(leftExpressionCustomVariable.getReference()) + ".setVariableValue('"
					+ leftExpressionCustomVariable.getVariable().getName() + "', " + mathematicalExpression + ");\n";
			ruleCore += "	AbcdLogger.debug(\"DroolsRule\", \"Variable set ("
					+ leftExpressionCustomVariable.getReference().getName() + ", "
					+ leftExpressionCustomVariable.getVariable().getName() + ", " + mathematicalExpression + ")\");\n";

		}
		// }
		return ruleCore;
	}

	/**
	 * Expression parser. An expression is a rule without the condition part in
	 * the definition, but not in the drools engine.<br>
	 * Parse actions like => Cat.score = min(q1.score, q2.score, ...) <br>
	 * Create drools rule like => <br>
	 * &nbsp&nbsp&nbsp $var : List() from collect( some conditions )<br>
	 * &nbsp&nbsp&nbsp accumulate((Question($score : getScore()) from $var);
	 * $sol : min($value) )
	 * 
	 * @param actions
	 *            the expression being parsed
	 * @param forkConditions
	 * @return the rule
	 * @throws TreeObjectParentNotValidException
	 * @throws TreeObjectInstanceNotRecognizedException
	 * @throws NullTreeObjectException
	 * @throws NullExpressionValueException
	 * @throws NullCustomVariableException
	 * @throws DroolsRuleCreationException
	 */
	private static String assignationFunctionAction(ExpressionChain actions) throws NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException,
			NullExpressionValueException, DroolsRuleCreationException {
		String ruleCore = "";
		List<Expression> chainList = actions.getExpressions();
		if (((ExpressionChain) chainList.get(0)).getExpressions().get(0) instanceof ExpressionValueCustomVariable) {
			ExpressionValueCustomVariable leftExpressionCustomVariable = (ExpressionValueCustomVariable) ((ExpressionChain) chainList
					.get(0)).getExpressions().get(0);
			ruleCore += checkVariableAssignation(leftExpressionCustomVariable);

			// Get all the customVariables and treeObjects
			ExpressionChain expressionChainToSearch = (ExpressionChain) actions.generateCopy();
			expressionChainToSearch.removeFirstExpression();
			List<Expression> variables = getExpressionChainVariables(expressionChainToSearch);
			for (Expression expression : variables) {
				ruleCore += checkVariableAssignationWithoutScore(expression);
			}
			ruleCore += RulesUtils.getThenRuleString();

			ExpressionFunction function = (ExpressionFunction) actions.getExpressions().get(1);

			ruleCore += checkValueAssignedInCustomVariableInDrools(variables);
			// First part of the action 'setVariable'
			ruleCore += "\t$" + getTreeObjectName(leftExpressionCustomVariable.getReference()) + ".setVariableValue('"
					+ leftExpressionCustomVariable.getVariable().getName() + "', ";
			// Second part of the action 'setVariable' (depends on the function)
			switch (function.getValue()) {
			case MAX:
				ruleCore += "RulesOperators.calculateMaxValueFunction(variablesList));\n";
				break;
			case MIN:
				ruleCore += "RulesOperators.calculateMinValueFunction(variablesList));\n";
				break;
			case AVG:
				ruleCore += "RulesOperators.calculateAvgValueFunction(variablesList));\n";
				break;
			case SUM:
				ruleCore += "RulesOperators.calculateSumValueFunction(variablesList));\n";
				break;
			case PMT:
				ruleCore += "RulesOperators.calculatePmtValueFunction(variablesList));\n";
				break;
			default:
				throw new DroolsRuleCreationException("Error parsing an Action. Function '" + function.getValue()
						+ "' not found.", actions);
			}
			// Set the value calculated
			ruleCore += "\tAbcdLogger.debug(\"DroolsRule\", \"Variable set ("
					+ leftExpressionCustomVariable.getReference().getName() + ", \"" + "+$"
					+ getTreeObjectName(leftExpressionCustomVariable.getReference()) + ".getVariableValue('"
					+ leftExpressionCustomVariable.getVariable().getName() + "')+\")\");\n";
		}

		return ruleCore;
	}

	private static String checkValueAssignedInCustomVariableInDrools(List<Expression> variables) {
		String ruleCore = "";
		ruleCore += "\tList<Double> variablesList = new ArrayList<Double>();\n";
		for (Expression variable : variables) {
			if (variable instanceof ExpressionValueCustomVariable) {
				ExpressionValueCustomVariable expressionValueCustomVariable = (ExpressionValueCustomVariable) variable;
				ruleCore += "\tif(" + getDroolsVariableIdentifier(variable) + ".isVariableDefined('"
						+ expressionValueCustomVariable.getVariable().getName() + "')){";
				ruleCore += "\tvariablesList.add((Double)"
						+ getDroolsVariableValueFromExpressionValueTreeObject(expressionValueCustomVariable) + ");}\n";

			} else if (variable instanceof ExpressionValueTreeObjectReference) {
				ExpressionValueTreeObjectReference expressionValueTreeObject = (ExpressionValueTreeObjectReference) variable;
				ruleCore += "\tvariablesList.add((Double)"
						+ getDroolsVariableValueFromExpressionValueTreeObject(expressionValueTreeObject) + ");\n";

			} else if (variable instanceof ExpressionValueGlobalConstant) {
				GlobalVariable globalExpression = ((ExpressionValueGlobalConstant) variable).getValue();
				switch (globalExpression.getFormat()) {
				case NUMBER:
					ruleCore += "\tvariablesList.add((Double)" + globalExpression.getName() + ");\n";
					break;
				case TEXT:
				case POSTAL_CODE:
				case DATE:
					ruleCore += "\tvariablesList.add(" + globalExpression.getName() + ");\n";
					break;
				}
			} else if (variable instanceof ExpressionValue) {
				if (variable instanceof ExpressionValueNumber) {
					ruleCore += "\tvariablesList.add((Double)" + ((ExpressionValueNumber) variable).getValue() + ");\n";
				}
			}
		}
		return ruleCore;
	}

	private static String getDroolsVariableIdentifier(Expression expression) {
		if (expression instanceof ExpressionValueTreeObjectReference) {
			ExpressionValueTreeObjectReference expressionValue = (ExpressionValueTreeObjectReference) expression;
			TreeObject treeObject = expressionValue.getReference();
			String id = treeObject.getUniqueNameReadable();
			return "$" + id;
		}
		return "";
	}

	private static String getDroolsVariableValueFromExpressionValueTreeObject(
			ExpressionValueTreeObjectReference expressionValue) {
		TreeObject treeObject = expressionValue.getReference();

		String id = treeObject.getUniqueNameReadable();

		if (expressionValue instanceof ExpressionValueCustomVariable) {
			CustomVariable variable = ((ExpressionValueCustomVariable) expressionValue).getVariable();
			switch (variable.getType()) {
			case NUMBER:
				return "(Double)$" + id + ".getVariableValue('" + variable.getName() + "')";
			case DATE:
				return "(Date)$" + id + ".getVariableValue('" + variable.getName() + "')";
			case STRING:
				return "(String)$" + id + ".getVariableValue('" + variable.getName() + "')";
			}
		} else {
			// If it is a question of input type
			if ((treeObject instanceof Question) && ((Question) treeObject).getAnswerType().equals(AnswerType.INPUT)) {
				switch (((Question) treeObject).getAnswerFormat()) {
				case NUMBER:
					return "(Double)$" + id + ".getAnswer('" + AnswerFormat.NUMBER.toString() + "')";
				case DATE:
					if (expressionValue.getUnit() != null) {
						switch (expressionValue.getUnit()) {
						case YEARS:
							return "DateUtils.returnYearsDistanceFromDate( $" + id + ".getAnswer('"
									+ AnswerFormat.DATE.toString() + "'))";
						case MONTHS:
							return "DateUtils.returnMonthsDistanceFromDate( $" + id + ".getAnswer('"
									+ AnswerFormat.DATE.toString() + "'))";
						case DAYS:
							return "DateUtils.returnDaysDistanceFromDate( $" + id + ".getAnswer('"
									+ AnswerFormat.DATE.toString() + "'))";
						case DATE:
							return "$" + id + ".getAnswer('" + AnswerFormat.DATE.toString() + "')";
						}
					}
					break;
				case TEXT:
					return "$" + id + ".getAnswer('" + AnswerFormat.TEXT.toString() + "')";
				case POSTAL_CODE:
					return "$" + id + ".getAnswer('" + AnswerFormat.POSTAL_CODE.toString() + "')";
				}
			}
		}
		return "";
	}

	/**
	 * Parses and expressionChain using the Pratt parser
	 * 
	 * @param expressionChain
	 * @return An object with the expression chain parsed inside;
	 */
	private static ITreeElement calculatePrattParserResult(ExpressionChain expressionChain) throws PrattParserException {
		PrattParser prattParser = new ExpressionChainPrattParser(expressionChain);
		ITreeElement prattParserResult = null;
		prattParserResult = prattParser.parseExpression();
		return prattParserResult;
	}

	/**
	 * Parse the actions of the rule <br>
	 * Accepts actions with the patterns : <br>
	 * &nbsp&nbsp&nbsp Var = value | string <br>
	 * &nbsp&nbsp&nbsp Var = Var mathOperator value <br>
	 * &nbsp&nbsp&nbsp Var = value mathOperator Var
	 * 
	 * @param actions
	 *            list of expressions to be parsed
	 * @throws RuleNotImplementedException
	 * @throws NullExpressionValueException
	 * @throws NullCustomVariableException
	 * @throws TreeObjectParentNotValidException
	 * @throws TreeObjectInstanceNotRecognizedException
	 * @throws NullTreeObjectException
	 * @throws NotCompatibleTypeException
	 * @throws DroolsRuleCreationException
	 */
	private static String parseActions(ExpressionChain expressionChain) throws RuleNotImplementedException,
			NotCompatibleTypeException, NullTreeObjectException, TreeObjectInstanceNotRecognizedException,
			TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			PrattParserException, DroolsRuleCreationException {

		ITreeElement prattParserResult = calculatePrattParserResult(expressionChain);
		if (prattParserResult != null) {
			ExpressionChain prattParserResultExpressionChain = prattParserResult.getExpressionChain();

			if ((prattParserResultExpressionChain.getExpressions().get(0) instanceof ExpressionChain)
					&& (((ExpressionChain) prattParserResultExpressionChain.getExpressions().get(0)).getExpressions()
							.get(0) instanceof ExpressionValueCustomVariable)) {

				// In case the function is empty we don't need to generate the
				// rule
				if (prattParserResultExpressionChain.getExpressions().get(1) instanceof ExpressionSymbol) {
					return null;

				}
				// Its the first position because the parser removes the equals
				// in
				// the expression functions
				// Could be changed to standardize the behavior of the parser
				// but it
				// works fine
				else if (prattParserResultExpressionChain.getExpressions().get(1) instanceof ExpressionFunction) {
					switch (((ExpressionFunction) prattParserResultExpressionChain.getExpressions().get(1)).getValue()) {
					case MAX:
					case MIN:
					case AVG:
					case SUM:
					case PMT:
						return assignationFunctionAction(prattParserResultExpressionChain);
					default:
						break;
					}
				} else if (prattParserResultExpressionChain.getExpressions().get(1) instanceof ExpressionPluginMethod) {
					return parsePluginMethods(prattParserResultExpressionChain);
				}
				// Mathematical expression
				else {
					return mathAssignationAction(prattParserResultExpressionChain, prattParserResult);
				}
			}
		} else {
			throw new RuleNotImplementedException("Rule not implemented.", expressionChain);
		}
		return null;
	}

	/**
	 * Creates the specific method calls defined by the expression<br>
	 * These calls will be passed to the plugin controller and will be called
	 * from the drools engine
	 * 
	 * @param actions
	 * @return
	 * @throws PluginInvocationException
	 * @throws TreeObjectParentNotValidException
	 * @throws TreeObjectInstanceNotRecognizedException
	 * @throws NullExpressionValueException
	 * @throws NullTreeObjectException
	 * @throws NullCustomVariableException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private static String parsePluginMethods(ExpressionChain actions) throws NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException,
			NullExpressionValueException {
		String ruleCore = "";
		List<Expression> chainList = actions.getExpressions();

		if (((ExpressionChain) chainList.get(0)).getExpressions().get(0) instanceof ExpressionValueCustomVariable) {
			ExpressionValueCustomVariable leftExpressionCustomVariable = (ExpressionValueCustomVariable) ((ExpressionChain) chainList
					.get(0)).getExpressions().get(0);
			// Check if the reference exists in the rule, if not, it creates
			// a new reference
			ruleCore += checkVariableAssignation(leftExpressionCustomVariable);
			ExpressionChain expressionChainToSearch = (ExpressionChain) actions.generateCopy();
			expressionChainToSearch.removeFirstExpression();
			List<Expression> variables = getExpressionChainVariables(expressionChainToSearch);
			for (Expression expression : variables) {
				if ((expression instanceof ExpressionValueCustomVariable)
						|| (expression instanceof ExpressionValueTreeObjectReference)) {
					ruleCore += checkVariableAssignation(expression);
				}
			}
			ruleCore += RulesUtils.getThenRuleString();
			String pluginCall = createPluginMethodCall(actions);

			ruleCore += "\tObject callResult = " + pluginCall + ";\n";
			ruleCore += "\t$" + getTreeObjectName(leftExpressionCustomVariable.getReference()) + ".setVariableValue('"
					+ leftExpressionCustomVariable.getVariable().getName() + "', callResult);\n";
			ruleCore += "\tAbcdLogger.debug(\"DroolsRule\", \"Variable set ("
					+ leftExpressionCustomVariable.getReference().getName() + ", "
					+ leftExpressionCustomVariable.getVariable().getName() + ", callResult)\");\n";

		}
		// }
		return ruleCore;
	}

	private static String createPluginMethodCall(ExpressionChain actions) {
		ExpressionPluginMethod expressionPlugin = (ExpressionPluginMethod) actions.getExpressions().get(1);
		String pluginCall = "";
		Plugin pluginInterface = PluginController.getInstance().getPlugin(expressionPlugin.getPluginInterface());
		if (pluginInterface instanceof IPlugin) {
			String interfaceName = expressionPlugin.getPluginInterface().getCanonicalName();
			String pluginName = expressionPlugin.getPluginName();
			String methodName = expressionPlugin.getPluginMethodName();

			// Create the array of values to consult
			List<ExpressionValue<?>> funtionParameters = getFunctionParameters(actions);
			String parametersValue = null;
			if (!funtionParameters.isEmpty()) {
				List<String> parameters = transformFunctionParametersToString(funtionParameters);
				parametersValue = "";
				for (int i = 0; i < parameters.size() - 1; i++) {
					parametersValue += parameters.get(i) + ", ";
				}
				// Add last parameter
				parametersValue += parameters.get(parameters.size() - 1);
			}
			pluginCall = "PluginController.getInstance().executePluginMethod('" + interfaceName + "', '" + pluginName
					+ "', '" + methodName + "'" + (parametersValue != null ? ", " + parametersValue : "") + ")";
		}
		return pluginCall;
	}

	private static String parseConditions(ExpressionChain conditions) throws ExpressionInvalidException,
			NullTreeObjectException, TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException,
			NullCustomVariableException, NullExpressionValueException, BetweenFunctionInvalidException,
			DateComparisonNotPossibleException, DroolsRuleCreationException, PrattParserException {

		ITreeElement result = calculatePrattParserResult(conditions);
		// *******************************************************************************************************
		// After this point the expression chain is the result of the parsing
		// engine. The expression chain has AST (abstract syntax tree) form
		// *******************************************************************************************************
		if ((result != null) && (result.getExpressionChain() != null)) {
			return processResultConditionsFromPrattParser(result.getExpressionChain());
		} else {
			return "";
		}
	}

	private static String processResultConditionsFromPrattParser(ExpressionChain prattParserResultExpressionChain)
			throws ExpressionInvalidException, NullTreeObjectException, TreeObjectInstanceNotRecognizedException,
			TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, DroolsRuleCreationException {

		if ((prattParserResultExpressionChain != null) && (prattParserResultExpressionChain.getExpressions() != null)
				&& (!prattParserResultExpressionChain.getExpressions().isEmpty())) {
			List<Expression> expressions = prattParserResultExpressionChain.getExpressions();

			// Operators
			if ((expressions.size() > 1) && (expressions.get(1) instanceof ExpressionOperatorLogic)) {
				switch (((ExpressionOperatorLogic) expressions.get(1)).getValue()) {
				case EQUALS:
				case NOT_EQUALS:
					return equalsNotEqualsOperator(expressions,
							((ExpressionOperatorLogic) expressions.get(1)).getValue());
				case AND:
					return andOperator(expressions);
				case GREATER_EQUALS:
				case GREATER_THAN:
				case LESS_EQUALS:
				case LESS_THAN:
					return questionGeGtLeLtAnswer(expressions,
							((ExpressionOperatorLogic) expressions.get(1)).getValue());
				default:
					break;
				}
			} else if ((expressions.size() > 1) && (expressions.get(1) instanceof ExpressionFunction)) {
				switch (((ExpressionFunction) expressions.get(1)).getValue()) {
				case IN:
				case BETWEEN:
					return createInBetweenFunctionConditions(prattParserResultExpressionChain);
					// case IN:
					// return answersInQuestionCondition(expressions);
					// case BETWEEN:
					// return
					// questionBetweenAnswersCondition(prattParserResultExpressionChain);
				default:
					break;
				}
			} else if ((expressions.size() > 1) && (expressions.get(0) instanceof ExpressionSymbol)) {
				switch (((ExpressionSymbol) expressions.get(0)).getValue()) {
				case LEFT_BRACKET:
					// Parsing an expression of type "( something )"
					// Skip the parenthesis and parse again
					return processResultConditionsFromPrattParser((ExpressionChain) expressions.get(1));
				default:
					break;
				}
			}
			// NOT from FORK
			else if (expressions.get(0) instanceof ExpressionFunction) {
				if (((ExpressionFunction) expressions.get(0)).getValue().equals(AvailableFunction.NOT)) {
					return negatedExpressions(prattParserResultExpressionChain);
				}
			}
		}
		return "";
	}

	private static String negatedExpressions(ExpressionChain prattParserResultExpressionChain)
			throws ExpressionInvalidException, NullTreeObjectException, TreeObjectInstanceNotRecognizedException,
			TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, DroolsRuleCreationException {
		String ruleCore = "";

		if (prattParserResultExpressionChain.getExpressions().get(1) instanceof ExpressionChain) {
			String auxRule = processResultConditionsFromPrattParser((ExpressionChain) prattParserResultExpressionChain
					.getExpressions().get(1));
			String newLastLine = "\tnot(\n" + RulesUtils.getLastLine(auxRule) + "\t)";
			ruleCore = RulesUtils.replaceLastLine(auxRule, newLastLine);
		}
		return ruleCore;
	}

	private static void putTreeObjectName(TreeObject treeObject, String value) {
		TreeObjectDroolsIdMap.put(treeObject, value);
	}

	private static String questionAnswerEqualsCondition(Question question, Answer answer,
			AvailableOperator availableOperator) throws NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException {
		// String droolsConditions = "(\n";
		String droolsConditions = "";

		// Create the conditions for parents hierarchy
		TreeObject questionParent = question.getParent();
		droolsConditions += SimpleConditionsGenerator.getTreeObjectConditions(questionParent);

		putTreeObjectName(question, question.getUniqueNameReadable());
		droolsConditions += "	$" + question.getUniqueNameReadable() + " : SubmittedQuestion("
				+ RulesUtils.returnSimpleTreeObjectNameFunction(question) + "', getAnswer('"
				+ getTreeObjectAnswerType(question) + "')" + availableOperator.getValue().toString() + "'"
				+ answer.getName() + "') from $" + questionParent.getUniqueNameReadable()
				+ ".getChildren(IQuestion.class)" + RulesUtils.addFinalCommentsIfNeeded(question) + "\n";
		// return droolsConditions + ")\n";
		return droolsConditions;
	}

	private static boolean checkExpressionValueTypes(AnswerFormat leftReferenceType, List<ExpressionValue<?>> values) {
		for (ExpressionValue<?> value : values) {
			if (!getFormatType(value).equals(leftReferenceType))
				return false;
		}
		return true;
	}

	/**
	 * Retrieves the format of the variables inside the expression value
	 * 
	 * @param expressionValue
	 * @return
	 */
	private static AnswerFormat getFormatType(ExpressionValue<?> expressionValue) {
		if (expressionValue instanceof ExpressionValueNumber) {
			return AnswerFormat.NUMBER;
		}
		// Timestamp or SystemDate
		else if (expressionValue instanceof ExpressionValueTimestamp) {
			return AnswerFormat.DATE;

		} else if (expressionValue instanceof ExpressionValuePostalCode) {
			return AnswerFormat.POSTAL_CODE;

		} else if (expressionValue instanceof ExpressionValueString) {
			return AnswerFormat.TEXT;

		} else if (expressionValue instanceof ExpressionValueGlobalConstant) {
			return ((ExpressionValueGlobalConstant) expressionValue).getValue().getFormat();
		}
		// ExpressionValueTreeObjectReference or ExpressionValueCustomVariable
		else if (expressionValue instanceof ExpressionValueTreeObjectReference) {
			return getExpressionValueTreeObjectAnswerFormat((ExpressionValueTreeObjectReference) expressionValue);
		}
		// The remaining cases
		return null;
	}

	/**
	 * Returns the answer format corresponding to a tree object
	 * 
	 * @param expressionValueTreeObject
	 * @return
	 */
	private static AnswerFormat getExpressionValueTreeObjectAnswerFormat(
			ExpressionValueTreeObjectReference expressionValueTreeObject) {
		if (expressionValueTreeObject instanceof ExpressionValueCustomVariable) {
			switch (((ExpressionValueCustomVariable) expressionValueTreeObject).getVariable().getType()) {
			case NUMBER:
				return AnswerFormat.NUMBER;
			case STRING:
				return AnswerFormat.TEXT;
			case DATE:
				return AnswerFormat.DATE;
			}
		} else {
			TreeObject reference = expressionValueTreeObject.getReference();
			if (reference instanceof Question) {
				Question question = (Question) reference;
				if (question.getAnswerType().equals(AnswerType.INPUT)) {
					switch (question.getAnswerFormat()) {
					case DATE:
						if (expressionValueTreeObject.getUnit() != null) {
							switch (expressionValueTreeObject.getUnit()) {
							case YEARS:
							case MONTHS:
							case DAYS:
								return AnswerFormat.NUMBER;
							case DATE:
								return question.getAnswerFormat();
							}
						} else {
							return question.getAnswerFormat();
						}
						break;
					case NUMBER:
					case POSTAL_CODE:
					case TEXT:
						return question.getAnswerFormat();
					}
				} else {
					// Special case to match answers with radio button and multi
					// checkbox questions
					return AnswerFormat.NUMBER;
				}
			} else if (reference instanceof Answer) {
				// Special case to match answers with radio button and multi
				// checkbox questions
				return AnswerFormat.NUMBER;
			}
		}
		return null;
	}

	private static String questionDateDaysOperatorValueNumber(TreeObject question, Double value,
			AvailableOperator operator) {
		return "\t$" + question.getUniqueNameReadable() + " : SubmittedQuestion("
				+ RulesUtils.returnSimpleTreeObjectNameFunction(question)
				+ "', DateUtils.returnDaysDistanceFromDate(getAnswer('" + getTreeObjectAnswerType(question) + "')) "
				+ operator.getValue() + " " + value.intValue() + ") from $"
				+ question.getParent().getUniqueNameReadable() + ".getChildren(IQuestion.class)"
				+ RulesUtils.addFinalCommentsIfNeeded(question) + "\n";
	}

	private static String questionDateMonthsOperatorValueNumber(TreeObject question, Double value,
			AvailableOperator operator) {
		return "\t$" + question.getUniqueNameReadable() + " : SubmittedQuestion("
				+ RulesUtils.returnSimpleTreeObjectNameFunction(question)
				+ "', DateUtils.returnMonthsDistanceFromDate(getAnswer('" + getTreeObjectAnswerType(question) + "')) "
				+ operator.getValue() + " " + value.intValue() + ") from $"
				+ question.getParent().getUniqueNameReadable() + ".getChildren(IQuestion.class)"
				+ RulesUtils.addFinalCommentsIfNeeded(question) + "\n";
	}

	private static String questionDateOperatorValue(TreeObject leftReferenceParent, TreeObject leftQuestion,
			AvailableOperator operator, String droolsValue) throws NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException {
		String rule = "";
		// Check if the reference exists in the rule, if not, it creates
		// a new reference
		rule += checkVariableAssignation(leftQuestion);
		rule += "\t$" + leftQuestion.getUniqueNameReadable() + " : SubmittedQuestion("
				+ RulesUtils.returnSimpleTreeObjectNameFunction(leftQuestion)
				+ "', DateUtils.returnYearsDistanceFromDate(getAnswer('" + getTreeObjectAnswerType(leftQuestion)
				+ "')) " + operator.getValue() + droolsValue + ") from $" + leftReferenceParent.getUniqueNameReadable()
				+ ".getChildren(IQuestion.class)" + RulesUtils.addFinalCommentsIfNeeded(leftQuestion) + "\n";
		return rule;
	}

	private static String questionDateYearsOperatorValueNumber(TreeObject question, Double value,
			AvailableOperator operator) {
		return "\t$" + question.getUniqueNameReadable() + " : SubmittedQuestion("
				+ RulesUtils.returnSimpleTreeObjectNameFunction(question)
				+ "', DateUtils.returnYearsDistanceFromDate(getAnswer('" + getTreeObjectAnswerType(question) + "')) "
				+ operator.getValue() + " " + value.intValue() + ") from $"
				+ question.getParent().getUniqueNameReadable() + ".getChildren(IQuestion.class)"
				+ RulesUtils.addFinalCommentsIfNeeded(question) + "\n";
	}

	private static String questionGeGtLeLtAnswer(List<Expression> conditions, AvailableOperator operator)
			throws NullTreeObjectException, TreeObjectInstanceNotRecognizedException,
			TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException {
		String droolsConditions = "";
		List<Expression> left = ((ExpressionChain) conditions.get(0)).getExpressions();
		TreeObject leftTreeObject = null;

		if ((left.size() == 1) && !(left.get(0) instanceof ExpressionValueCustomVariable)) {
			leftTreeObject = ((ExpressionValueTreeObjectReference) left.get(0)).getReference();
			if (leftTreeObject != null) {
				List<Expression> rightExpressions = ((ExpressionChain) conditions.get(2)).getExpressions();

				// Comparison with a value
				if ((rightExpressions.size() == 1) && (rightExpressions.get(0) instanceof ExpressionValue)) {
					ExpressionValue<?> value = (ExpressionValue<?>) rightExpressions.get(0);
					String droolsValue = "";
					if (value instanceof ExpressionValueTreeObjectReference) {
						droolsConditions += checkVariableAssignation((ExpressionValueTreeObjectReference) value);
						droolsValue = getDroolsVariableValueFromExpressionValueTreeObject((ExpressionValueTreeObjectReference) value);

					} else {
						droolsValue = ((ExpressionValue<?>) value).getValue().toString();
					}

					if (value != null) {
						if (leftTreeObject instanceof Question) {
							// Create the conditions for parents hierarchy
							TreeObject leftTreeObjectParent = leftTreeObject.getParent();
							droolsConditions += SimpleConditionsGenerator.getTreeObjectConditions(leftTreeObjectParent);
							Question leftQuestion = (Question) leftTreeObject;
							if (leftQuestion.getAnswerType().equals(AnswerType.INPUT)) {
								switch (leftQuestion.getAnswerFormat()) {
								case DATE:
									droolsConditions += questionDateOperatorValue(leftTreeObjectParent, leftQuestion,
											operator, droolsValue);
									break;
								case NUMBER:
								case TEXT:
								case POSTAL_CODE:
									droolsConditions += "\t$" + leftQuestion.getUniqueNameReadable()
											+ " : SubmittedQuestion("
											+ RulesUtils.returnSimpleTreeObjectNameFunction(leftQuestion)
											+ "', getAnswer('" + getTreeObjectAnswerType(leftQuestion) + "') "
											+ operator.getValue() + " "
											+ (leftQuestion.getAnswerFormat().equals(AnswerFormat.NUMBER) ? "" : "'")
											+ droolsValue
											+ (leftQuestion.getAnswerFormat().equals(AnswerFormat.NUMBER) ? "" : "'")
											+ ") from $" + leftTreeObjectParent.getUniqueNameReadable()
											+ ".getChildren(IQuestion.class)"
											+ RulesUtils.addFinalCommentsIfNeeded(leftQuestion) + "\n";
									break;
								}
							}
						}
					}
				}
				// Comparison with dates
				else if ((leftTreeObject instanceof Question) && (rightExpressions.size() == 1)
						&& (rightExpressions.get(0) instanceof ExpressionValueTreeObjectReference)) {
					TreeObject rightTreeObject = ((ExpressionValueTreeObjectReference) rightExpressions.get(0))
							.getReference();
					Question leftQuestion = (Question) leftTreeObject;
					if ((rightTreeObject != null) && (rightTreeObject instanceof Question)) {
						Question rightQuestion = (Question) rightTreeObject;
						if (rightQuestion.getAnswerType().equals(AnswerType.INPUT)
								&& rightQuestion.getAnswerFormat().equals(AnswerFormat.DATE)) {

							// Create the conditions for parents hierarchy
							TreeObject leftQuestionParent = leftTreeObject.getParent();
							droolsConditions += SimpleConditionsGenerator.getTreeObjectConditions(leftQuestionParent);
							// Create the conditions for parents hierarchy
							TreeObject rightQuestionParent = rightTreeObject.getParent();
							droolsConditions += SimpleConditionsGenerator.getTreeObjectConditions(rightQuestionParent);

							droolsConditions += "\t$" + leftQuestion.getUniqueNameReadable()
									+ " : SubmittedQuestion(getAnswer('" + getTreeObjectAnswerType(leftQuestion)
									+ "') instanceof Date, "
									+ RulesUtils.returnSimpleTreeObjectNameFunction(leftQuestion) + "') from $"
									+ leftQuestionParent.getUniqueNameReadable() + ".getChildren(IQuestion.class) "
									+ RulesUtils.addFinalCommentsIfNeeded(leftQuestion) + "\n";
							droolsConditions += "\t$" + rightQuestion.getUniqueNameReadable()
									+ " : SubmittedQuestion(getAnswer('" + getTreeObjectAnswerType(rightQuestion)
									+ "') instanceof Date, "
									+ RulesUtils.returnSimpleTreeObjectNameFunction(rightQuestion) + "', getAnswer('"
									+ getTreeObjectAnswerType(rightQuestion) + "') " + operator.getValue() + " $"
									+ leftQuestion.getUniqueNameReadable() + ".getAnswer('"
									+ getTreeObjectAnswerType(rightQuestion) + "')) from $"
									+ rightQuestionParent.getUniqueNameReadable() + ".getChildren(IQuestion.class) "
									+ RulesUtils.addFinalCommentsIfNeeded(rightQuestion) + "\n";
						}
					}
				}
				// Comparison with system date (Special date)
				else if ((leftTreeObject instanceof Question) && (rightExpressions.size() == 1)
						&& (rightExpressions.get(0) instanceof ExpressionValueSystemDate)) {

					// Create the conditions for parents hierarchy
					TreeObject leftQuestionParent = leftTreeObject.getParent();
					droolsConditions += SimpleConditionsGenerator.getTreeObjectConditions(leftQuestionParent);

					droolsConditions += "\t$" + leftTreeObject.getUniqueNameReadable()
							+ " : SubmittedQuestion(getAnswer('" + getTreeObjectAnswerType(leftTreeObject)
							+ "') instanceof Date, " + RulesUtils.returnSimpleTreeObjectNameFunction(leftTreeObject)
							+ "', getAnswer('" + getTreeObjectAnswerType(leftTreeObject) + "') " + operator.getValue()
							+ " DateUtils.returnCurrentDate()) from $" + leftQuestionParent.getUniqueNameReadable()
							+ ".getChildren(IQuestion.class) " + RulesUtils.addFinalCommentsIfNeeded(leftTreeObject)
							+ "\n";
				}
			}
		} else {
			ExpressionValueCustomVariable customVariable = (ExpressionValueCustomVariable) ((ExpressionChain) conditions
					.get(0)).getExpressions().get(0);
			Expression valueExpression = ((ExpressionChain) conditions.get(2)).getExpressions().get(0);
			droolsConditions += treeObjectScoreLogicOperatorValueExpression(customVariable,
					(ExpressionOperatorLogic) conditions.get(1), (ExpressionValue<?>) valueExpression);
		}
		return droolsConditions;
	}

	/**
	 * Parse conditions like => Score (logic operator (==, <=, <, >=, >)) value. <br>
	 * Create drools rule like => Category(isVariableDefined('cScore'),
	 * getVariablevalue('cScore') == value )
	 * 
	 * @param expressionOperatorLogic
	 * 
	 * @param conditions
	 * @return LHS of the rule
	 * @throws TreeObjectParentNotValidException
	 * @throws TreeObjectInstanceNotRecognizedException
	 * @throws NullTreeObjectException
	 * @throws NullCustomVariableException
	 */
	private static String treeObjectScoreEqualsValueString(ExpressionValueCustomVariable var,
			ExpressionOperatorLogic expressionOperatorLogic, ExpressionValueString valueNumber)
			throws NullTreeObjectException, TreeObjectInstanceNotRecognizedException,
			TreeObjectParentNotValidException, NullCustomVariableException {
		String ruleCore = "";

		TreeObject treeObjectCustomVariable = var.getReference();
		if (treeObjectCustomVariable != null) {
			CustomVariable customVariable = var.getVariable();
			if (customVariable != null) {
				String varName = customVariable.getName();

				if (treeObjectCustomVariable instanceof Form) {
					putTreeObjectName(treeObjectCustomVariable, treeObjectCustomVariable.getUniqueNameReadable());
					ruleCore += "\t$" + treeObjectCustomVariable.getUniqueNameReadable()
							+ " : SubmittedForm(isVariableDefined('" + varName + "'), getVariableValue('" + varName
							+ "') " + expressionOperatorLogic.getValue().toString() + " '" + valueNumber.getValue()
							+ "') from $droolsForm.getSubmittedForm() \n";
				} else {
					String treeObjectClassName = treeObjectCustomVariable.getClass().getSimpleName();
					putTreeObjectName(treeObjectCustomVariable, treeObjectCustomVariable.getUniqueNameReadable());
					TreeObject parent = treeObjectCustomVariable.getParent();
					// Check conditions for the parent
					SimpleConditionsGenerator.getTreeObjectConditions(parent);
					ruleCore += "\t$" + treeObjectCustomVariable.getUniqueNameReadable() + " : Submitted"
							+ treeObjectClassName + "( "
							+ RulesUtils.returnSimpleTreeObjectNameFunction(treeObjectCustomVariable)
							+ "', isVariableDefined('" + varName + "'), getVariableValue('" + varName + "') "
							+ expressionOperatorLogic.getValue().toString() + " '" + valueNumber.getValue()
							+ "' ) from $" + parent.getUniqueNameReadable() + ".getChildren(I" + treeObjectClassName
							+ ".class) " + RulesUtils.addFinalCommentsIfNeeded(treeObjectCustomVariable) + "\n";
				}
				return ruleCore;
			} else {
				throw new NullCustomVariableException();
			}
		} else {
			throw new NullTreeObjectException();
		}
	}

	/**
	 * Parse conditions like => Score (logic operator (==, <=, <, >=, >)) value. <br>
	 * Create drools rule like => Category(isVariableDefined('cScore'),
	 * getVariablevalue('cScore') == value )
	 * 
	 * @param conditions
	 * @return LHS of the rule
	 * @throws TreeObjectParentNotValidException
	 * @throws TreeObjectInstanceNotRecognizedException
	 * @throws NullTreeObjectException
	 * @throws NullExpressionValueException
	 * @throws NullCustomVariableException
	 */
	private static String treeObjectScoreLogicOperatorValueExpression(
			ExpressionValueCustomVariable expressionValueCustomVariable, ExpressionOperatorLogic operator,
			ExpressionValue<?> expressionValue) throws NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException,
			NullExpressionValueException {
		String ruleCore = "";

		String droolsValue = "";
		if (expressionValue instanceof ExpressionValueTreeObjectReference) {
			ruleCore += checkVariableAssignation(expressionValue);
			droolsValue = getDroolsVariableValueFromExpressionValueTreeObject((ExpressionValueTreeObjectReference) expressionValue);

		} else {
			droolsValue = expressionValue.getValue().toString();
		}

		if (expressionValueCustomVariable != null) {
			TreeObject treeObjectCustomVariable = expressionValueCustomVariable.getReference();
			if (treeObjectCustomVariable != null) {
				CustomVariable customVariable = expressionValueCustomVariable.getVariable();
				if (customVariable != null) {
					CustomVariableType customVariableType = expressionValueCustomVariable.getVariable().getType();
					putTreeObjectName(treeObjectCustomVariable, treeObjectCustomVariable.getUniqueNameReadable());
					if (treeObjectCustomVariable instanceof Form) {
						ruleCore += "\t$" + treeObjectCustomVariable.getUniqueNameReadable()
								+ " : SubmittedForm( isVariableDefined('" + customVariable.getName()
								+ "'), getVariableValue('" + customVariable.getName() + "') "
								+ operator.getValue().toString()
								+ (customVariableType.equals(CustomVariableType.STRING) ? " '" : " ") + droolsValue
								+ (customVariableType.equals(CustomVariableType.STRING) ? "' " : " ")
								+ ") from $droolsForm.getSubmittedForm() \n";

					} else {
						String treeObjectClassName = treeObjectCustomVariable.getClass().getSimpleName();
						TreeObject parent = treeObjectCustomVariable.getParent();
						// Check conditions for the parent
						ruleCore += SimpleConditionsGenerator.getTreeObjectConditions(parent);
						ruleCore += "\t$" + treeObjectCustomVariable.getUniqueNameReadable() + " : Submitted"
								+ treeObjectClassName + "( "
								+ RulesUtils.returnSimpleTreeObjectNameFunction(treeObjectCustomVariable)
								+ "', isVariableDefined('" + customVariable.getName() + "'), getVariableValue('"
								+ customVariable.getName() + "') " + operator.getValue().toString()
								+ (customVariableType.equals(CustomVariableType.STRING) ? " '" : " ") + droolsValue
								+ (customVariableType.equals(CustomVariableType.STRING) ? "' " : " ") + ") from $"
								+ parent.getUniqueNameReadable() + ".getChildren(I" + treeObjectClassName + ".class) "
								+ RulesUtils.addFinalCommentsIfNeeded(treeObjectCustomVariable) + "\n";
					}

				} else {
					throw new NullCustomVariableException();
				}
			} else {
				throw new NullTreeObjectException();
			}
		} else {
			throw new NullExpressionValueException();
		}
		return ruleCore;
	}

	/**
	 * Returns the parameters of the functions as a list of expressions.
	 * 
	 * @param conditions
	 * @return
	 */
	private static List<ExpressionValue<?>> getFunctionParameters(ExpressionChain conditions) {
		List<ExpressionValue<?>> functionParameters = new ArrayList<ExpressionValue<?>>();
		for (int i = 2; i < conditions.getExpressions().size() - 1; i += 2) {
			List<Expression> operatorValue = ((ExpressionChain) conditions.getExpressions().get(i)).getExpressions();
			if ((operatorValue.size() == 1) && (operatorValue.get(0) instanceof ExpressionValue)) {
				functionParameters.add((ExpressionValue<?>) operatorValue.get(0));
			}
		}
		return functionParameters;
	}

	private static List<String> transformFunctionParametersToString(List<ExpressionValue<?>> functionParameters) {
		List<String> stringFunctionParameters = new ArrayList<>();
		for (ExpressionValue<?> parameter : functionParameters) {
			if (parameter instanceof ExpressionValueCustomVariable) {
				stringFunctionParameters
						.add("'"
								+ getDroolsVariableValueFromExpressionValueTreeObject((ExpressionValueCustomVariable) parameter)
								+ "'");
			} else if (parameter instanceof ExpressionValueTreeObjectReference) {
				stringFunctionParameters.add("'"
						+ ((ExpressionValueTreeObjectReference) parameter).getReference().getName() + "'");
			} else if (parameter instanceof ExpressionValueNumber) {
				stringFunctionParameters.add(parameter.getValue().toString());
			} else {
				stringFunctionParameters.add("'" + parameter.getValue() + "'");
			}
		}
		return stringFunctionParameters;
	}
}
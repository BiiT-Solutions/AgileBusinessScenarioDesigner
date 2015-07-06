package com.biit.abcd.core.drools.rules;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import net.xeoh.plugins.base.Plugin;

import com.biit.abcd.core.drools.prattparser.ExpressionChainPrattParser;
import com.biit.abcd.core.drools.prattparser.PrattParser;
import com.biit.abcd.core.drools.prattparser.exceptions.PrattParserException;
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
import com.biit.abcd.core.drools.utils.RuleGenerationUtils;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.abcd.persistence.entity.AnswerType;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.CustomVariableType;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Group;
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
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTimestamp;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.drools.DroolsHelper;
import com.biit.drools.plugins.PluginController;
import com.biit.form.entity.TreeObject;
import com.biit.plugins.interfaces.IPlugin;

public class DroolsParser {

	private static DroolsHelper droolsHelper;

	public static DroolsHelper getDroolsHelper() {
		return droolsHelper;
	}

	public static void setDroolsHelper(DroolsHelper droolsHelper) {
		DroolsParser.droolsHelper = droolsHelper;
	}

	/**
	 * This method manages the expression functions defined by the user.<br>
	 * The expression supported are: MAX, MIN, AVG, SUM y PMT
	 * @param actions
	 * @return The drools rule as a string
	 * @throws NullTreeObjectException
	 * @throws TreeObjectInstanceNotRecognizedException
	 * @throws TreeObjectParentNotValidException
	 * @throws NullCustomVariableException
	 * @throws NullExpressionValueException
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
			ruleCore += RuleGenerationUtils.getThenRuleString();

			ExpressionFunction function = (ExpressionFunction) actions.getExpressions().get(1);

			ruleCore += getVariablesListValuesForFunctions(leftExpressionCustomVariable, variables);
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
			ruleCore += "\tDroolsEngineLogger.debug(\"DroolsRule\", \"Variable set ("
					+ leftExpressionCustomVariable.getReference().getName() + ", \"" + "+$"
					+ getTreeObjectName(leftExpressionCustomVariable.getReference()) + ".getVariableValue('"
					+ leftExpressionCustomVariable.getVariable().getName() + "')+\")\");\n";
		}

		return ruleCore;
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

	private static boolean checkExpressionValueTypes(AnswerFormat leftReferenceType, List<ExpressionValue<?>> values) {
		for (ExpressionValue<?> value : values) {
			if (!getAnswerFormatType(value).equals(leftReferenceType))
				return false;
		}
		return true;
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

	/**
	 * Main method. Create Drools Rules from rules and diagram
	 * 
	 * @param conditions
	 *            conditions of a rule.
	 * @param actions
	 *            actions of a rule.
	 * @param forkConditions
	 *            conditions defined as a fork in a digram.
	 * @return String with the drools rule
	 * @throws PrattParserException
	 * @throws DroolsRuleCreationException
	 * @throws PluginInvocationException
	 * @throws DateComparisonNotPossibleException
	 * @throws BetweenFunctionInvalidException
	 * @throws NullExpressionValueException
	 * @throws NullCustomVariableException
	 * @throws TreeObjectParentNotValidException
	 * @throws TreeObjectInstanceNotRecognizedException
	 * @throws NullTreeObjectException
	 * @throws ExpressionInvalidException
	 * @throws NotCompatibleTypeException
	 * @throws RuleNotImplementedException
	 */
	public static String createDroolsRule(List<Rule> rules, DroolsHelper droolsHelper)
			throws RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException,
			NullTreeObjectException, TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException,
			NullCustomVariableException, NullExpressionValueException, BetweenFunctionInvalidException,
			DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException,
			PrattParserException {
		setDroolsHelper(droolsHelper);
		StringBuilder parsedText = new StringBuilder();
		for (Rule rule : rules) {
			if (rule != null) {
				// Parse the rules individually
				String parsedRule = createDroolsRule(rule);
				if (parsedRule != null) {
					parsedText.append(rule.getName());
					parsedText.append(RuleGenerationUtils.getWhenRuleString());
					
					// The rule
					if (rule instanceof DroolsRuleGroupEndRule) {
						parsedText.append(RuleGenerationUtils
								.getGroupEndRuleExtraCondition((DroolsRuleGroupEndRule) rule));
					}
					parsedText.append(parsedRule);
					if (rule instanceof DroolsRuleGroup) {
						if (!(rule instanceof DroolsRuleGroupEndRule)) {
							parsedText.append(RuleGenerationUtils.getThenRuleString());
						}
						parsedText.append(RuleGenerationUtils.getGroupRuleActions((DroolsRuleGroup) rule));
					}
					parsedText.append(RuleGenerationUtils.getEndRuleString());
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

		// Just in case we want the label and version of the drools files
		/**
		 * String formLabel = getDroolsHelper().getForm().getLabel(); String
		 * formVersion = getDroolsHelper().getForm().getVersion().toString();
		 * String result = "\t$droolsForm: DroolsForm(getLabel() == '" +
		 * formLabel + "', getVersion() == '" + formVersion + "')\n";
		 */
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
					result += RuleGenerationUtils.addAndToMultipleConditionsAction(actionString);
				} else {
					result += actionString;
				}
			} else {
				return null;
			}
		}
		result = RuleGenerationUtils.removeDuplicateLines(result);
		result = RuleGenerationUtils.checkForDuplicatedVariables(result);
		return result;
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
				for (ExpressionValue<?> value : operatorValues) {
					if (value instanceof ExpressionValueTreeObjectReference) {
						functionValues
								.add(getDroolsVariableValueFromExpressionValueTreeObject((ExpressionValueTreeObjectReference) value));
						droolsConditions += checkVariableAssignation((ExpressionValueTreeObjectReference) value);
					} else if (value instanceof ExpressionValueNumber) {
						functionValues.add(value.getValue().toString());

					} else if (value instanceof ExpressionValuePostalCode) {
						functionValues.add("'" + value.getValue().toString().toUpperCase() + "'");

					} else if (value instanceof ExpressionValueTimestamp) {
						functionValues.add("(new Date(" + ((ExpressionValueTimestamp) value).getValue().getTime()
								+ "l))");
					} else {
						functionValues.add("'" + value.getValue() + "'");
					}
				}

				// Creates the rule
				if (!functionValues.isEmpty()) {
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
									+ " : DroolsSubmittedForm(isVariableDefined('" + variableName
									+ "'), getVariableValue('" + variableName + "')";
							switch (functionParsed) {
							case BETWEEN:
								if (functionValues.size() == 2) {
									droolsConditions += " >= " + functionValues.get(0) + " && <= "
											+ functionValues.get(1) + ") ";
								} else {
									throw new DroolsRuleCreationException(
											"Invalid number of values in the between function", conditions);
								}
								break;
							case IN:
								String inValues = "";
								for (int i = 0; i < functionValues.size() - 1; i++) {
									inValues += functionValues.get(i) + ", ";
								}
								inValues += functionValues.get(functionValues.size() - 1);
								droolsConditions += " in ( " + inValues + " )) ";
								break;
							default:
								// Never gets here
								throw new DroolsRuleCreationException("No valid function '" + functionParsed.toString()
										+ "'found.", conditions);
							}
							// End common part of the condition
							droolsConditions += "from $droolsForm.getDroolsSubmittedForm() \n";
							break;
						case CATEGORY:
						case GROUP:
						case QUESTION:
							droolsConditions += SimpleConditionsGenerator.getTreeObjectConditions(leftReferenceParent);
							String className = leftVariable.getReference().getClass().getSimpleName();
							// First common part of the condition
							droolsConditions += "\t$"
									+ leftVariable.getReference().getUniqueNameReadable()
									+ " : DroolsSubmitted"
									+ className
									+ "( "
									+ RuleGenerationUtils.returnSimpleTreeObjectNameFunction(leftVariable
											.getReference()) + "', isVariableDefined('" + variableName
									+ "'), getVariableValue('" + variableName + "')";

							switch (functionParsed) {
							case BETWEEN:
								if (functionValues.size() == 2) {
									droolsConditions += " >= " + functionValues.get(0) + " && <= "
											+ functionValues.get(1) + ") ";

								} else {
									throw new DroolsRuleCreationException(
											"Invalid number of values in the between function", conditions);
								}
								break;
							case IN:
								String inValues = "";
								for (int i = 0; i < functionValues.size() - 1; i++) {
									inValues += functionValues.get(i) + ", ";
								}
								inValues += functionValues.get(functionValues.size() - 1);
								droolsConditions += " in ( " + inValues + " )) ";
								break;
							default:
								// Never gets here
								throw new DroolsRuleCreationException("No valid function '" + functionParsed.toString()
										+ "'found.", conditions);
							}
							// End common part of the condition
							droolsConditions += "from $" + leftReferenceParent.getUniqueNameReadable()
									+ ".getChildren(ISubmitted" + className + ".class)\n";
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
						droolsConditions += "\t$" + leftQuestion.getUniqueNameReadable()
								+ " : DroolsSubmittedQuestion("
								+ RuleGenerationUtils.returnSimpleTreeObjectNameFunction(leftQuestion) + "', "
								+ getTreeObjectAnswer(leftExpressionValueTreeObject);
						// Comparison part of the condition
						switch (functionParsed) {
						case BETWEEN:
							if (functionValues.size() == 2) {
								droolsConditions += " >= " + functionValues.get(0) + " && <= " + functionValues.get(1)
										+ ") ";
							} else {
								throw new DroolsRuleCreationException(
										"Invalid number of values in the between function", conditions);
							}
							break;
						case IN:
							String inValues = "";
							for (int i = 0; i < functionValues.size() - 1; i++) {
								inValues += functionValues.get(i) + ", ";
							}
							inValues += functionValues.get(functionValues.size() - 1);
							droolsConditions += " in ( " + inValues + " )) ";
							break;
						default:
							// Never gets here
							throw new DroolsRuleCreationException("No valid function '" + functionParsed.toString()
									+ "'found.", conditions);
						}
						// End common part of the condition
						droolsConditions += "from $" + leftReferenceParent.getUniqueNameReadable()
								+ ".getChildren(ISubmittedQuestion.class)"
								+ RuleGenerationUtils.addFinalCommentsIfNeeded(leftQuestion) + "\n";
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

	private static String createTreeObjectLogicOperatorCondition(Question question, Answer answer,
			AvailableOperator availableOperator) throws NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException {
		String droolsConditions = "";

		// Create the conditions for parents hierarchy
		TreeObject questionParent = question.getParent();
		droolsConditions += SimpleConditionsGenerator.getTreeObjectConditions(questionParent);

		putTreeObjectName(question, question.getUniqueNameReadable());
		droolsConditions += "	$" + question.getUniqueNameReadable() + " : DroolsSubmittedQuestion("
				+ RuleGenerationUtils.returnSimpleTreeObjectNameFunction(question) + "', getAnswer('"
				+ getTreeObjectAnswerType(question) + "')" + availableOperator.getValue().toString() + "'"
				+ answer.getName() + "') from $" + questionParent.getUniqueNameReadable()
				+ ".getChildren(ISubmittedQuestion.class)" + RuleGenerationUtils.addFinalCommentsIfNeeded(question)
				+ "\n";
		return droolsConditions;
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
	private static String createTreeObjectScoreLogicOperatorValueExpression(
			ExpressionValueCustomVariable expressionValueCustomVariable, ExpressionOperatorLogic operator,
			ExpressionValue<?> expressionValue) throws NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException,
			NullExpressionValueException {
		String ruleCore = "";
		String droolsValue = "";
		if (expressionValue instanceof ExpressionValueTreeObjectReference) {
			ruleCore += checkVariableAssignation(expressionValue);
			droolsValue = getDroolsVariableValueFromExpressionValueTreeObject((ExpressionValueTreeObjectReference) expressionValue);

		} else if (expressionValue instanceof ExpressionValueTimestamp) {
			droolsValue = "(new Date(" + ((ExpressionValueTimestamp) expressionValue).getValue().getTime() + "l))";

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
								+ " : DroolsSubmittedForm( isVariableDefined('" + customVariable.getName()
								+ "'), getVariableValue('" + customVariable.getName() + "') "
								+ operator.getValue().toString()
								+ (customVariableType.equals(CustomVariableType.STRING) ? " '" : " ") + droolsValue
								+ (customVariableType.equals(CustomVariableType.STRING) ? "' " : " ")
								+ ") from $droolsForm.getDroolsSubmittedForm() \n";

					} else {
						String treeObjectClassName = treeObjectCustomVariable.getClass().getSimpleName();
						TreeObject parent = treeObjectCustomVariable.getParent();
						// Check conditions for the parent
						ruleCore += SimpleConditionsGenerator.getTreeObjectConditions(parent);
						ruleCore += "\t$" + treeObjectCustomVariable.getUniqueNameReadable() + " : DroolsSubmitted"
								+ treeObjectClassName + "( "
								+ RuleGenerationUtils.returnSimpleTreeObjectNameFunction(treeObjectCustomVariable)
								+ "', isVariableDefined('" + customVariable.getName() + "'), getVariableValue('"
								+ customVariable.getName() + "') " + operator.getValue().toString()
								+ (customVariableType.equals(CustomVariableType.STRING) ? " '" : " ") + droolsValue
								+ (customVariableType.equals(CustomVariableType.STRING) ? "' " : " ") + ") from $"
								+ parent.getUniqueNameReadable() + ".getChildren(ISubmitted" + treeObjectClassName
								+ ".class) " + RuleGenerationUtils.addFinalCommentsIfNeeded(treeObjectCustomVariable)
								+ "\n";
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
	 * Retrieves the format of the variables inside the expression value
	 * 
	 * @param expressionValue
	 * @return
	 */
	private static AnswerFormat getAnswerFormatType(ExpressionValue<?> expressionValue) {
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
				if (expressionValue.getUnit() != null) {
					switch (expressionValue.getUnit()) {
					case YEARS:
					case MONTHS:
					case DAYS:
						return "DroolsDateUtils.return" + expressionValue.getUnit().getUnitName()
								+ "DistanceFromDate( $" + id + ".getVariableValue('" + variable.getName() + "'))";
					case DATE:
						return "$" + id + ".getVariableValue('" + variable.getName() + "')";
					}
				}
				break;
			case STRING:
				return "'$" + id + ".getVariableValue('" + variable.getName() + "')'";
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
						case MONTHS:
						case DAYS:
							return "DroolsDateUtils.return" + expressionValue.getUnit().getUnitName()
									+ "DistanceFromDate( $" + id + ".getAnswer('" + AnswerFormat.DATE.toString()
									+ "'))";
						case DATE:
							return "$" + id + ".getAnswer('" + AnswerFormat.DATE.toString() + "')";
						}
					}
					break;
				case TEXT:
					return "'$" + id + ".getAnswer('" + AnswerFormat.TEXT.toString() + "')'";
				case POSTAL_CODE:
					return "'$" + id + ".getAnswer('" + AnswerFormat.POSTAL_CODE.toString() + "')'";
				}
			} else if (treeObject instanceof Answer) {
				return "'" + treeObject.getName() + "'";
			}
		}
		return "";
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
				if (expressionValueTreeObject.getUnit() != null) {
					return AnswerFormat.NUMBER;
				} else
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
					// Special case to match questions with radio button and
					// multi
					// checkbox
					return AnswerFormat.NUMBER;
				}
			} else if (reference instanceof Answer) {
				// Special case to match answers
				return AnswerFormat.NUMBER;
			}
		}
		return null;
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

	/**
	 * Returns the type of answer for the question in the parameter
	 * 
	 * @return
	 */
	private static String getTreeObjectAnswer(ExpressionValueTreeObjectReference expressionValueTreeObject) {
		TreeObject treeObject = expressionValueTreeObject.getReference();
		if (treeObject instanceof Question) {
			Question question = (Question) treeObject;
			switch (question.getAnswerType()) {
			case RADIO:
			case MULTI_CHECKBOX:
				return "getAnswer('" + AnswerFormat.TEXT.toString() + "')";
			case INPUT:
				// Get the answer based on the type
				switch (((Question) treeObject).getAnswerFormat()) {
				case NUMBER:
					return "getAnswer('" + AnswerFormat.NUMBER.toString() + "')";
				case DATE:
					if (expressionValueTreeObject.getUnit() != null) {
						switch (expressionValueTreeObject.getUnit()) {
						case YEARS:
						case MONTHS:
						case DAYS:
							return "DroolsDateUtils.return" + expressionValueTreeObject.getUnit().getUnitName()
									+ "DistanceFromDate(getAnswer('" + AnswerFormat.DATE.toString() + "'))";
						case DATE:
							return "getAnswer('" + AnswerFormat.DATE.toString() + "')";
						}
					} else {
						return "getAnswer('" + AnswerFormat.DATE.toString() + "')";
					}
				case TEXT:
					return "getAnswer('" + AnswerFormat.TEXT.toString() + "')";
				case POSTAL_CODE:
					return "getAnswer('" + AnswerFormat.POSTAL_CODE.toString() + "')";
				}

			}
			return "";
		}
		return "";
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

	private static String getVariablesListValuesForFunctions(
			ExpressionValueCustomVariable leftExpressionCustomVariable, List<Expression> variables) {
		String ruleCore = "";
		ruleCore += "\tList<Double> variablesList = new ArrayList<Double>();\n";
		for (Expression variable : variables) {
			if (variable instanceof ExpressionValueCustomVariable) {
				ExpressionValueCustomVariable expressionValueCustomVariable = (ExpressionValueCustomVariable) variable;
				ruleCore += "\tif(" + getDroolsVariableIdentifier(variable) + ".isVariableDefined('"
						+ expressionValueCustomVariable.getVariable().getName() + "')){";
				ruleCore += "variablesList.add("
						+ getDroolsVariableValueFromExpressionValueTreeObject(expressionValueCustomVariable) + ");}\n";

				if (isRepeatableGroup(expressionValueCustomVariable.getReference())) {
					TreeObject parent = expressionValueCustomVariable.getReference().getParent();
					if (parent != null) {
						String parentId = "$" + parent.getUniqueNameReadable();
						ruleCore += "\tif(" + parentId + ".isVariableDefined('"
								+ leftExpressionCustomVariable.getVariable().getName() + "')){";
						ruleCore += "variablesList.add("
								+ getDroolsVariableValueFromExpressionValueTreeObject(leftExpressionCustomVariable)
								+ ");}\n";
					}
				}

			} else if (variable instanceof ExpressionValueTreeObjectReference) {
				ExpressionValueTreeObjectReference expressionValueTreeObject = (ExpressionValueTreeObjectReference) variable;
				ruleCore += "variablesList.add("
						+ getDroolsVariableValueFromExpressionValueTreeObject(expressionValueTreeObject) + ");\n";

			} else if (variable instanceof ExpressionValueGlobalConstant) {
				GlobalVariable globalExpression = ((ExpressionValueGlobalConstant) variable).getValue();
				switch (globalExpression.getFormat()) {
				case NUMBER:
					ruleCore += "variablesList.add((Double)" + globalExpression.getName() + ");\n";
					break;
				case POSTAL_CODE:
					ruleCore += "variablesList.add(((String)" + globalExpression.getName() + ").toUpperCase());\n";
					break;
				case TEXT:
					ruleCore += "variablesList.add(" + globalExpression.getName() + ");\n";
					break;
				case DATE:
					ruleCore += "variablesList.add(" + globalExpression.getName() + ");\n";
					break;
				}
			} else if (variable instanceof ExpressionValue) {
				if (variable instanceof ExpressionValueNumber) {
					ruleCore += "variablesList.add(" + ((ExpressionValueNumber) variable).getValue() + "d);\n";
				}
			}
		}
		return ruleCore;
	}

	private static boolean isRepeatableGroup(TreeObject treeObject) {
		if (treeObject instanceof Group) {
			if (((Group) treeObject).isRepeatable()) {
				return true;
			}
		}
		return false;
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
			ruleCore += RuleGenerationUtils.getThenRuleString();
			String mathematicalExpression = "";
			TreeElementMathExpressionVisitor treePrint = new TreeElementMathExpressionVisitor();
			prattParserResult.accept(treePrint);
			if (treePrint != null) {
				mathematicalExpression = treePrint.getBuilder().toString();
			}
			ruleCore += "\t$" + getTreeObjectName(leftExpressionCustomVariable.getReference()) + ".setVariableValue('"
					+ leftExpressionCustomVariable.getVariable().getName() + "', " + mathematicalExpression + ");\n";
			ruleCore += "\tDroolsEngineLogger.debug(\"DroolsRule\", \"Variable set ("
					+ leftExpressionCustomVariable.getReference().getName() + ", "
					+ leftExpressionCustomVariable.getVariable().getName() + ", " + mathematicalExpression + ")\");\n";

		}
		// }
		return ruleCore;
	}

	private static String negatedExpressions(ExpressionChain prattParserResultExpressionChain)
			throws ExpressionInvalidException, NullTreeObjectException, TreeObjectInstanceNotRecognizedException,
			TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, DroolsRuleCreationException {
		String ruleCore = "";

		if (prattParserResultExpressionChain.getExpressions().get(1) instanceof ExpressionChain) {
			String auxRule = processResultConditionsFromPrattParser((ExpressionChain) prattParserResultExpressionChain
					.getExpressions().get(1));
			String newLastLine = "\tnot(\n" + RuleGenerationUtils.getLastLine(auxRule) + "\t)";
			ruleCore = RuleGenerationUtils.replaceLastLine(auxRule, newLastLine);
		}
		return ruleCore;
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

	private static String parseLogicOperators(List<Expression> expressions, AvailableOperator availableOperator)
			throws NullTreeObjectException, TreeObjectInstanceNotRecognizedException,
			TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			DateComparisonNotPossibleException {
		StringBuilder droolsConditions = new StringBuilder();

		List<Expression> operatorLeft = ((ExpressionChain) expressions.get(0)).getExpressions();
		Expression operator = expressions.get(1);
		List<Expression> operatorRight = ((ExpressionChain) expressions.get(2)).getExpressions();
		// TreeObject.score (Logic operator) ExpressionValue
		if ((operatorLeft.size() == 1) && (operatorLeft.get(0) instanceof ExpressionValueCustomVariable)
				&& (operatorRight.size() == 1) && (operatorRight.get(0) instanceof ExpressionValue)) {
			droolsConditions.append(createTreeObjectScoreLogicOperatorValueExpression(
					(ExpressionValueCustomVariable) operatorLeft.get(0), (ExpressionOperatorLogic) expressions.get(1),
					(ExpressionValue<?>) operatorRight.get(0)));
		}
		// Question (Logic operator) Answer
		else if ((operatorLeft.size() == 1) && (operatorLeft.get(0) instanceof ExpressionValueTreeObjectReference)
				&& (operatorRight.size() == 1) && (operatorRight.get(0) instanceof ExpressionValueTreeObjectReference)) {
			TreeObject treeObject1 = ((ExpressionValueTreeObjectReference) operatorLeft.get(0)).getReference();
			TreeObject treeObject2 = ((ExpressionValueTreeObjectReference) operatorRight.get(0)).getReference();
			if ((treeObject1 instanceof Question) && (treeObject2 instanceof Answer)) {
				droolsConditions.append(createTreeObjectLogicOperatorCondition((Question) treeObject1,
						(Answer) treeObject2, availableOperator));
			}
		}
		// Question (Logic operator) ExpressionValue
		else if ((operatorLeft.size() == 1) && (operatorLeft.get(0) instanceof ExpressionValueTreeObjectReference)
				&& (operator instanceof ExpressionOperatorLogic) && (operatorRight.size() == 1)
				&& (operatorRight.get(0) instanceof ExpressionValue)) {
			Object value = ((ExpressionValue<?>) operatorRight.get(0)).getValue();
			TreeObject leftTreeObject = ((ExpressionValueTreeObjectReference) operatorLeft.get(0)).getReference();
			if (leftTreeObject instanceof Question) {
				// Create the conditions for parents hierarchy
				droolsConditions.append(SimpleConditionsGenerator.getTreeObjectConditions(leftTreeObject.getParent()));
				Question question = ((Question) leftTreeObject);
				String questionCondition = "";
				switch (question.getAnswerType()) {
				case RADIO:
				case MULTI_CHECKBOX:
					// The flow don't get here, parsed previously in question
					// == answer
					break;
				case INPUT:
					// Common part of NUMBER, POSTALCODE, TEXT condition
					questionCondition += "\t$" + question.getUniqueNameReadable() + " : DroolsSubmittedQuestion("
							+ RuleGenerationUtils.returnSimpleTreeObjectNameFunction(question) + "', getAnswer('"
							+ getTreeObjectAnswerType(question) + "') " + operator.getValue();
					switch (question.getAnswerFormat()) {
					case NUMBER:
						questionCondition += " " + value + " ) from $" + question.getParent().getUniqueNameReadable()
								+ ".getChildren(ISubmittedQuestion.class)"
								+ RuleGenerationUtils.addFinalCommentsIfNeeded(question) + "\n";
						break;
					case POSTAL_CODE:
						// Postal code comparisons should be in ALWAYS in upper
						// case
						questionCondition += " '" + value.toString().toUpperCase() + "' ) from $"
								+ question.getParent().getUniqueNameReadable()
								+ ".getChildren(ISubmittedQuestion.class)"
								+ RuleGenerationUtils.addFinalCommentsIfNeeded(question) + "\n";
						break;
					case TEXT:
						questionCondition += " '" + value.toString() + "' ) from $"
								+ question.getParent().getUniqueNameReadable()
								+ ".getChildren(ISubmittedQuestion.class)"
								+ RuleGenerationUtils.addFinalCommentsIfNeeded(question) + "\n";
						break;
					case DATE:
						// Clean the previous common value
						questionCondition = "";
						if (((ExpressionValueTreeObjectReference) operatorLeft.get(0)).getUnit() != null) {
							// Beginning common part of the DATE condition
							questionCondition += "\t$" + question.getUniqueNameReadable()
									+ " : DroolsSubmittedQuestion("
									+ RuleGenerationUtils.returnSimpleTreeObjectNameFunction(question) + "', ";

							switch (((ExpressionValueTreeObjectReference) operatorLeft.get(0)).getUnit()) {
							case YEARS:
							case MONTHS:
							case DAYS:
								String unitName = ((ExpressionValueTreeObjectReference) operatorLeft.get(0)).getUnit()
										.getUnitName();
								if (value instanceof Timestamp) {
									throw new DateComparisonNotPossibleException(unitName
											+ " and Date types incompatible", new ExpressionChain(expressions));
								} else {
									questionCondition += "DroolsDateUtils.return" + unitName
											+ "DistanceFromDate(getAnswer('" + getTreeObjectAnswerType(question)
											+ "')) " + ((ExpressionOperatorLogic) operator).getValue() + " "
											+ ((ExpressionValueNumber) operatorRight.get(0)).getValue().intValue()
											+ ") ";
								}
								break;
							case DATE:
								if (value instanceof Timestamp) {
									questionCondition += "getAnswer('" + getTreeObjectAnswerType(question) + "') "
											+ operator.getValue() + " (new Date(" + ((Timestamp) value).getTime()
											+ ")) ) ";
								} else {
									throw new DateComparisonNotPossibleException(
											"The value to compare with the Date is not a Timestamp",
											new ExpressionChain(expressions));
								}
								break;
							}
							// Ending common part of the DATE condition
							questionCondition += "from $" + question.getParent().getUniqueNameReadable()
									+ ".getChildren(ISubmittedQuestion.class)\n";
						}
						break;
					}
					break;
				}
				droolsConditions.append(questionCondition);
			}
		}
		return droolsConditions.toString();
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
			ruleCore += RuleGenerationUtils.getThenRuleString();
			String pluginCall = createPluginMethodCall(actions);

			ruleCore += "\tObject callResult = " + pluginCall + ";\n";
			ruleCore += "\t$" + getTreeObjectName(leftExpressionCustomVariable.getReference()) + ".setVariableValue('"
					+ leftExpressionCustomVariable.getVariable().getName() + "', callResult);\n";
			ruleCore += "\tDroolsEngineLogger.debug(\"DroolsRule\", \"Variable set ("
					+ leftExpressionCustomVariable.getReference().getName() + ", "
					+ leftExpressionCustomVariable.getVariable().getName() + ", callResult)\");\n";

		}
		// }
		return ruleCore;
	}

	private static String processResultConditionsFromPrattParser(ExpressionChain prattParserResultExpressionChain)
			throws ExpressionInvalidException, NullTreeObjectException, TreeObjectInstanceNotRecognizedException,
			TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, DroolsRuleCreationException {

		if ((prattParserResultExpressionChain != null) && (prattParserResultExpressionChain.getExpressions() != null)
				&& (!prattParserResultExpressionChain.getExpressions().isEmpty())) {
			List<Expression> expressions = prattParserResultExpressionChain.getExpressions();

			if ((expressions.size() > 1) && (expressions.get(1) instanceof ExpressionOperatorLogic)) {
				switch (((ExpressionOperatorLogic) expressions.get(1)).getValue()) {
				case EQUALS:
				case NOT_EQUALS:
				case GREATER_EQUALS:
				case GREATER_THAN:
				case LESS_EQUALS:
				case LESS_THAN:
					return parseLogicOperators(expressions, ((ExpressionOperatorLogic) expressions.get(1)).getValue());
				default:
					break;
				}
			} else if ((expressions.size() > 1) && (expressions.get(1) instanceof ExpressionFunction)) {
				switch (((ExpressionFunction) expressions.get(1)).getValue()) {
				case IN:
				case BETWEEN:
					return createInBetweenFunctionConditions(prattParserResultExpressionChain);
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

	private static void putTreeObjectName(TreeObject treeObject, String value) {
		TreeObjectDroolsIdMap.put(treeObject, value);
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
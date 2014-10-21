package com.biit.abcd.core.drools.rules;

import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.core.drools.prattparser.ExpressionChainPrattParser;
import com.biit.abcd.core.drools.prattparser.PrattParser;
import com.biit.abcd.core.drools.prattparser.PrattParserException;
import com.biit.abcd.core.drools.prattparser.visitor.ITreeElement;
import com.biit.abcd.core.drools.prattparser.visitor.TreeElementMathExpressionVisitor;
import com.biit.abcd.core.drools.prattparser.visitor.exceptions.NotCompatibleTypeException;
import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.NullCustomVariableException;
import com.biit.abcd.core.drools.rules.exceptions.NullExpressionValueException;
import com.biit.abcd.core.drools.rules.exceptions.NullTreeObjectException;
import com.biit.abcd.core.drools.rules.exceptions.RuleNotImplementedException;
import com.biit.abcd.core.drools.rules.exceptions.TreeObjectInstanceNotRecognizedException;
import com.biit.abcd.core.drools.rules.exceptions.TreeObjectParentNotValidException;
import com.biit.abcd.core.drools.utils.RulesUtils;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.abcd.persistence.entity.AnswerType;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.expressions.AvailableFunction;
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionFunction;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorLogic;
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

public class DroolsParser {

	private static boolean orOperatorUsed = false;

	private static String andOperator(List<Expression> expressions) throws ExpressionInvalidException,
			NullTreeObjectException, TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException,
			NullCustomVariableException, NullExpressionValueException {
		String result = "";

		ExpressionChain leftChain = (ExpressionChain) expressions.get(0);
		ExpressionChain rightChain = (ExpressionChain) expressions.get(2);

		result += processResultConditionsFromPrattParser(leftChain);
		result += processResultConditionsFromPrattParser(rightChain);

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
	 */
	private static String answersInQuestionCondition(List<Expression> conditions) throws NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException {
		String droolsConditions = "";
		List<Expression> operatorLeft = ((ExpressionChain) conditions.get(0)).getExpressions();
		TreeObject leftReference = null;
		if ((operatorLeft.size() == 1) && (operatorLeft.get(0) instanceof ExpressionValueTreeObjectReference)) {
			leftReference = ((ExpressionValueTreeObjectReference) operatorLeft.get(0)).getReference();
		}
		if (leftReference != null) {
			String inValues = "";
			// Store the values inside the IN condition in a String
			for (int i = 2; i < conditions.size(); i++) {
				List<Expression> inParameter = ((ExpressionChain) conditions.get(i)).getExpressions();
				if ((inParameter.size() == 1) && (inParameter.get(0) instanceof ExpressionValue)) {
					if (inParameter.get(0) instanceof ExpressionValueTreeObjectReference) {
						ExpressionValueTreeObjectReference inExpValTreeObj = (ExpressionValueTreeObjectReference) inParameter
								.get(0);
						if (inExpValTreeObj instanceof ExpressionValueCustomVariable) {
							inValues += "'" + getDroolsVariableValueFromExpressionValueTreeObject(inExpValTreeObj)
									+ "', ";
						} else {
							inValues += "'" + inExpValTreeObj.getReference().getName() + "', ";
						}
					}
				}
			}
			// Remove the last comma
			inValues = inValues.substring(0, inValues.length() - 2);
			// Creates the rule
			if (!inValues.isEmpty()) {
				if (leftReference instanceof Question) {
					TreeObject leftReferenceParent = leftReference.getParent();
					putTreeObjectName(leftReference, leftReference.getUniqueNameReadable());
					// Get the drools conditions for the parent
					droolsConditions += SimpleConditionsGenerator.getTreeObjectConditions(leftReferenceParent);
					Question leftQuestion = (Question) leftReference;
					droolsConditions += "	$" + leftQuestion.getUniqueNameReadable() + " : Question(getTag() == '"
							+ leftQuestion.getName() + "', getAnswer('" + getTreeObjectAnswerType(leftQuestion)
							+ "') in( " + inValues + " )) from $" + leftReferenceParent.getUniqueNameReadable()
							+ ".getQuestions()\n";
				}
			}
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
	 */
	public static String createDroolsRule(List<Rule> rules) throws RuleNotImplementedException,
			NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException,
			NullExpressionValueException {
		String parsedText = "";
		for (Rule rule : rules) {
			orOperatorUsed = false;
			if (rule != null) {
				String parsedRule = createDroolsRule(rule);
				if (parsedRule != null) {
					parsedText += rule.getName();
					parsedText += RulesUtils.getWhenRuleString();
					parsedText += parsedRule;
					parsedText += RulesUtils.getEndRuleString();
				}
			}
		}
		return parsedText;
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
	 */
	private static String createDroolsRule(Rule rule) throws RuleNotImplementedException, NotCompatibleTypeException,
			ExpressionInvalidException, NullTreeObjectException, TreeObjectInstanceNotRecognizedException,
			TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException {
		if (rule == null) {
			return null;
		}

		// We make sure the variables map is clear
		TreeObjectDroolsIdMap.clearMap();

		// System.out.println("RULE CONDITIONS: " + rule.getConditionChain());
		// System.out.println("RULE ACTIONS: " + rule.getActionChain());

		String result = "";
		// treeObjectDroolsname = new HashMap<TreeObject, String>();
		result += "\t$droolsForm: DroolsForm()\n";

		// Obtain conditions if exists.
		if ((rule.getConditionChain() != null) && (rule.getConditionChain().getExpressions() != null)
				&& (!rule.getConditionChain().getExpressions().isEmpty())) {
			result += parseConditions(rule.getConditionChain());
		}
		if ((rule.getActionChain() != null) && (rule.getActionChain().getExpressions() != null)
				&& (!rule.getActionChain().getExpressions().isEmpty())) {
			String actionString = parseActions(rule.getActionChain());
			if (actionString != null) {
				result += actionString;
			} else {
				return null;
			}
		}
		result = RulesUtils.removeDuplicateLines(result);
		result = RulesUtils.checkForDuplicatedVariables(result);
		// result = RulesUtils.removeExtraParenthesis(result);
		if (orOperatorUsed)
			result = RulesUtils.fixOrCondition(result);

		return result;
	}

	private static String equalsNotEqualsOperator(List<Expression> expressions, AvailableOperator availableOperator)
			throws NullTreeObjectException, TreeObjectInstanceNotRecognizedException,
			TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException {
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
				&& (operatorRight.get(0) instanceof ExpressionValueNumber)) {
			TreeObject leftTreeObject = ((ExpressionValueTreeObjectReference) operatorLeft.get(0)).getReference();
			if (leftTreeObject instanceof Question) {
				Double value = ((ExpressionValueNumber) operatorRight.get(0)).getValue();
				// Create the conditions for parents hierarchy
				droolsConditions += SimpleConditionsGenerator.getTreeObjectConditions(leftTreeObject.getParent());

				switch (((ExpressionValueTreeObjectReference) operatorLeft.get(0)).getUnit()) {
				case YEARS:
					droolsConditions += questionDateYearsOperatorValueNumber(leftTreeObject, value,
							((ExpressionOperatorLogic) operator).getValue());
					break;
				case MONTHS:
					droolsConditions += questionDateMonthsOperatorValueNumber(leftTreeObject, value,
							((ExpressionOperatorLogic) operator).getValue());
					break;
				case DAYS:
					droolsConditions += questionDateDaysOperatorValueNumber(leftTreeObject, value,
							((ExpressionOperatorLogic) operator).getValue());
					break;
				case DATE:
					// TODO
					// Date can not be compared with a value
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
	 */
	private static String assignationFunctionAction(ExpressionChain actions) throws NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException,
			NullExpressionValueException {
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

			String mathematicalExpression = "";

			ExpressionFunction function = (ExpressionFunction) actions.getExpressions().get(1);

			if (variables.size() == 1) {
				mathematicalExpression = getDroolsVariableValueFromExpressionValueTreeObject((ExpressionValueTreeObjectReference) variables
						.get(0));
				if (!mathematicalExpression.isEmpty()) {
					ruleCore += "\t$" + getTreeObjectName(leftExpressionCustomVariable.getReference())
							+ ".setVariableValue('" + leftExpressionCustomVariable.getVariable().getName() + "', "
							+ mathematicalExpression + ");\n";
					ruleCore += "\tAbcdLogger.debug(\"DroolsRule\", \"Variable set ("
							+ leftExpressionCustomVariable.getReference().getName() + ", "
							+ leftExpressionCustomVariable.getVariable().getName() + ", " + mathematicalExpression
							+ ")\");\n";
				}

			} else if (variables.size() > 1) {
				switch (function.getValue()) {
				case MAX:
					ruleCore += checkValueAssignedInCustomVariableInDrools(variables);
					ruleCore += "\tdouble maxValue = -1;\n";
					ruleCore += "\tfor(double variable: variablesList){\n";
					ruleCore += "\t\tif(maxValue > variable){ maxValue = variable; }\n";
					ruleCore += "\t}\n";
					ruleCore += "\tif(maxValue != -1){\n";
					ruleCore += "\t\t$" + getTreeObjectName(leftExpressionCustomVariable.getReference())
							+ ".setVariableValue('" + leftExpressionCustomVariable.getVariable().getName()
							+ "', maxValue);\n";
					ruleCore += "\t\tAbcdLogger.debug(\"DroolsRule\", \"Variable set ("
							+ leftExpressionCustomVariable.getReference().getName() + ", "
							+ leftExpressionCustomVariable.getVariable().getName() + ", maxValue)\");\t}\n";
					break;
				case MIN:
					ruleCore += checkValueAssignedInCustomVariableInDrools(variables);
					ruleCore += "\tdouble minValue = 1000000;\n";
					ruleCore += "\tfor(double variable: variablesList){\n";
					ruleCore += "\t\tif(minValue > variable){ minValue = variable; }\n";
					ruleCore += "\t}\n";
					ruleCore += "\tif(minValue != 1000000){\n";
					ruleCore += "\t\t$" + getTreeObjectName(leftExpressionCustomVariable.getReference())
							+ ".setVariableValue('" + leftExpressionCustomVariable.getVariable().getName()
							+ "', minValue);\n";
					ruleCore += "\t\tAbcdLogger.debug(\"DroolsRule\", \"Variable set ("
							+ leftExpressionCustomVariable.getReference().getName() + ", "
							+ leftExpressionCustomVariable.getVariable().getName() + ", minValue)\");\t}\n";
					break;
				case AVG:
					ruleCore += checkValueAssignedInCustomVariableInDrools(variables);
					ruleCore += "\tdouble avgValue = 0;\n";
					ruleCore += "\tfor(double variable: variablesList){\n";
					ruleCore += "\t\tavgValue+=variable\n";
					ruleCore += "\t}\n";
					ruleCore += "\tavgValue = avgValue/(double)variablesList.size()\n";

					ruleCore += "\t$" + getTreeObjectName(leftExpressionCustomVariable.getReference())
							+ ".setVariableValue('" + leftExpressionCustomVariable.getVariable().getName()
							+ "', avgValue);\n";
					ruleCore += "\tAbcdLogger.debug(\"DroolsRule\", \"Variable set ("
							+ leftExpressionCustomVariable.getReference().getName() + ", "
							+ leftExpressionCustomVariable.getVariable().getName() + ", avgValue)\");\n";
					break;
				case SUM:
					ruleCore += checkValueAssignedInCustomVariableInDrools(variables);
					ruleCore += "\tdouble sumValue = 0;\n";
					ruleCore += "\tfor(double variable: variablesList){\n";
					ruleCore += "\t\tsumValue+=variable\n";
					ruleCore += "\t}\n";

					ruleCore += "\t$" + getTreeObjectName(leftExpressionCustomVariable.getReference())
							+ ".setVariableValue('" + leftExpressionCustomVariable.getVariable().getName()
							+ "', sumValue);\n";
					ruleCore += "\tAbcdLogger.debug(\"DroolsRule\", \"Variable set ("
							+ leftExpressionCustomVariable.getReference().getName() + ", "
							+ leftExpressionCustomVariable.getVariable().getName() + ", sumValue)\");\n";
					break;
				case PMT:
					ruleCore += checkValueAssignedInCustomVariableInDrools(variables);
					ruleCore += "\tif(variablesList.size() == 3){\n";
					ruleCore += "\t\tdouble rate = variablesList.get(0);\n";
					ruleCore += "\t\tdouble term = variablesList.get(1);\n";
					ruleCore += "\t\tdouble amount = variablesList.get(2);\n";

					ruleCore += "\t\tdouble v = 1 + rate;\n";
					ruleCore += "\t\tdouble t = -term;\n";
					ruleCore += "\t\tdouble pmtValue = (amount*rate)/(1-Math.pow(v,t));\n";

					ruleCore += "\t\t$" + getTreeObjectName(leftExpressionCustomVariable.getReference())
							+ ".setVariableValue('" + leftExpressionCustomVariable.getVariable().getName()
							+ "', pmtValue);\n";
					ruleCore += "\t\tAbcdLogger.debug(\"DroolsRule\", \"Variable set ("
							+ leftExpressionCustomVariable.getReference().getName() + ", "
							+ leftExpressionCustomVariable.getVariable().getName() + ", pmtValue)\"); }\n";
					break;
				}
			}
		}

		return ruleCore;
	}

	private static String checkValueAssignedInCustomVariableInDrools(List<Expression> variables) {
		String ruleCore = "";
		ruleCore += "\tList<Double> variablesList = new ArrayList<>();\n";
		for (Expression variable : variables) {
			if (variable instanceof ExpressionValueCustomVariable) {
				ExpressionValueCustomVariable expressionValueCustomVariable = (ExpressionValueCustomVariable) variable;
				ruleCore += "\tif(" + getDroolsVariableIdentifier(variable) + ".isScoreSet('"
						+ expressionValueCustomVariable.getVariable().getName() + "')){";
				ruleCore += "\tvariablesList.add((double)"
						+ getDroolsVariableValueFromExpressionValueTreeObject(expressionValueCustomVariable) + ");}\n";

			} else if (variable instanceof ExpressionValueTreeObjectReference) {
				ExpressionValueTreeObjectReference expressionValueTreeObject = (ExpressionValueTreeObjectReference) variable;
				ruleCore += "\tvariablesList.add((double)"
						+ getDroolsVariableValueFromExpressionValueTreeObject(expressionValueTreeObject) + ");\n";

			} else if (variable instanceof ExpressionValueGlobalConstant) {
				GlobalVariable globalExpression = ((ExpressionValueGlobalConstant) variable).getVariable();
				switch (globalExpression.getFormat()) {
				case NUMBER:
					ruleCore += "\tvariablesList.add((Double)" + globalExpression.getName() + ");\n";
					break;
				case TEXT:
				case POSTAL_CODE:
				case DATE:
					// TODO
					break;
				}
			} else if (variable instanceof ExpressionValue) {
				if (variable instanceof ExpressionValueNumber) {
					ruleCore += "\tvariablesList.add((double)" + ((ExpressionValueNumber) variable).getValue() + ");\n";
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

	private static String orOperator(List<Expression> expressions) throws ExpressionInvalidException,
			NullTreeObjectException, TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException,
			NullCustomVariableException, NullExpressionValueException {
		// System.out.println("OR EXPRESSIONS: " + expressions);

		String result = "";

		ExpressionChain leftChain = (ExpressionChain) expressions.get(0);
		ExpressionChain rightChain = (ExpressionChain) expressions.get(2);

		String leftPart = processResultConditionsFromPrattParser(leftChain);
		String rigthPart = processResultConditionsFromPrattParser(rightChain);

		String leftPartLastLine = RulesUtils.getLastLine(leftPart);
		String rightPartLastLine = RulesUtils.getLastLine(rigthPart);
		String leftPartWithoutLastLine = RulesUtils.removeLastNLines(leftPart, 1);
		String rightPartWithoutLastLine = RulesUtils.removeLastNLines(rigthPart, 1);

		result += leftPartWithoutLastLine;
		result += rightPartWithoutLastLine;
		result += "\t(";
		result += leftPartLastLine;
		result += "\n\tor\n";
		result += rightPartLastLine;
		result += "\t)\n";

		orOperatorUsed = true;
		return result;
	}

	/**
	 * Parses and expressionChain using the Pratt parser
	 * 
	 * @param expressionChain
	 * @return An object with the expression chain parsed inside;
	 */
	private static ITreeElement calculatePrattParserResult(ExpressionChain expressionChain) {
		PrattParser prattParser = new ExpressionChainPrattParser(expressionChain);
		ITreeElement prattParserResult = null;
		try {
			prattParserResult = prattParser.parseExpression();
		} catch (PrattParserException ex) {
			AbcdLogger.errorMessage(DroolsParser.class.getName(), ex);
		}
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
	 * @return RHS of the rule, and sometimes a modified LHS
	 * @throws RuleNotImplementedException
	 * @throws NotCompatibleTypeException
	 * @throws NullExpressionValueException
	 * @throws NullCustomVariableException
	 * @throws TreeObjectParentNotValidException
	 * @throws TreeObjectInstanceNotRecognizedException
	 * @throws NullTreeObjectException
	 */
	private static String parseActions(ExpressionChain expressionChain) throws RuleNotImplementedException,
			NotCompatibleTypeException, NullTreeObjectException, TreeObjectInstanceNotRecognizedException,
			TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException {
		ITreeElement prattParserResult = calculatePrattParserResult(expressionChain);
		ExpressionChain prattParserResultExpressionChain = prattParserResult.getExpressionChain();

		if ((prattParserResultExpressionChain.getExpressions().get(0) instanceof ExpressionChain)
				&& (((ExpressionChain) prattParserResultExpressionChain.getExpressions().get(0)).getExpressions()
						.get(0) instanceof ExpressionValueCustomVariable)) {

			if (prattParserResultExpressionChain.getExpressions().get(1) instanceof ExpressionFunction) {
				switch (((ExpressionFunction) prattParserResultExpressionChain.getExpressions().get(1)).getValue()) {
				case MAX:
				case MIN:
				case AVG:
				case SUM:
				case PMT:
					return assignationFunctionAction(prattParserResultExpressionChain);
				}
			}
			// Mathematical expression
			else {
				return mathAssignationAction(prattParserResultExpressionChain, prattParserResult);
			}
		}
		throw new RuleNotImplementedException("Rule not implemented.", expressionChain);
	}

	private static String parseConditions(ExpressionChain conditions) throws ExpressionInvalidException,
			NullTreeObjectException, TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException,
			NullCustomVariableException, NullExpressionValueException {

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
			TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException {

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
				case OR:
					return orOperator(expressions);
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
					return answersInQuestionCondition(expressions);
				case BETWEEN:
					return questionBetweenAnswersCondition(prattParserResultExpressionChain);
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
			TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException {
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
		droolsConditions += "	$" + question.getUniqueNameReadable() + " : Question(getTag() == '" + question.getName()
				+ "', getAnswer('" + getTreeObjectAnswerType(question) + "')" + availableOperator.getValue().toString()
				+ "'" + answer.getName() + "') from $" + questionParent.getUniqueNameReadable() + ".getQuestions()\n";
		// return droolsConditions + ")\n";
		return droolsConditions;
	}

	/**
	 * Return true if all the type of the left reference matches all the other
	 * expression value types
	 * 
	 * @param leftReferenceType
	 * @param values
	 * @return
	 */
	private static boolean checkExpressionValueTypes(AnswerFormat leftReferenceType, ExpressionValue... values) {
		for (ExpressionValue value : values) {
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
	private static AnswerFormat getFormatType(ExpressionValue expressionValue) {
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
			return ((ExpressionValueGlobalConstant) expressionValue).getVariable().getFormat();
		}
		// ExpressionValueTreeObjectReference or ExpressionValueCustomVariable
		else if (expressionValue instanceof ExpressionValueTreeObjectReference) {
			return getTreeObjectAnswerFormat((ExpressionValueTreeObjectReference) expressionValue);
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
	private static AnswerFormat getTreeObjectAnswerFormat(ExpressionValueTreeObjectReference expressionValueTreeObject) {
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
						}
						break;
					case NUMBER:
					case POSTAL_CODE:
					case TEXT:
						return question.getAnswerFormat();
					}
				}
			}
		}
		return null;
	}

	/**
	 * Parse conditions like => Question BETWEEN(Answer1, answer2). <br>
	 * The values inside the between must be always numbers <br>
	 * Create drools rule like => Question( (getAnswer() >= answer.getValue())
	 * && (getAnswer() <= answer.getValue()))
	 * 
	 * @param conditions
	 * @return LHS of the rule
	 * @throws ExpressionInvalidException
	 * @throws TreeObjectParentNotValidException
	 * @throws TreeObjectInstanceNotRecognizedException
	 * @throws NullTreeObjectException
	 */
	private static String questionBetweenAnswersCondition(ExpressionChain conditions)
			throws ExpressionInvalidException, NullTreeObjectException, TreeObjectInstanceNotRecognizedException,
			TreeObjectParentNotValidException {
		String droolsConditions = "";
		List<Expression> operatorLeft = ((ExpressionChain) conditions.getExpressions().get(0)).getExpressions();
		TreeObject leftReference = null;

		if (operatorLeft.size() == 1) {
			if (operatorLeft.get(0) instanceof ExpressionValueTreeObjectReference) {
				ExpressionValueTreeObjectReference leftExpressionValueTreeObject = (ExpressionValueTreeObjectReference) operatorLeft
						.get(0);
				leftReference = leftExpressionValueTreeObject.getReference();
				if (leftReference != null) {

					List<Expression> firstExpressionValue = ((ExpressionChain) conditions.getExpressions().get(2))
							.getExpressions();
					List<Expression> secondExpressionValue = ((ExpressionChain) conditions.getExpressions().get(3))
							.getExpressions();

					if ((firstExpressionValue.size() == 1) && (secondExpressionValue.size() == 1)) {
						// Check if the types inside the between match
						if (checkExpressionValueTypes(getTreeObjectAnswerFormat(leftExpressionValueTreeObject),
								(ExpressionValue) firstExpressionValue.get(0),
								(ExpressionValue) secondExpressionValue.get(0))) {

							// Get the values of the between expression
							Object value1 = ((ExpressionValue) firstExpressionValue.get(0)).getValue();
							Object value2 = ((ExpressionValue) secondExpressionValue.get(0)).getValue();

							if ((value1 != null) && (value2 != null)) {
								if (leftReference instanceof Question) {
									// Create the conditions for parents
									// hierarchy
									TreeObject leftReferenceParent = leftReference.getParent();
									droolsConditions += SimpleConditionsGenerator
											.getTreeObjectConditions(leftReferenceParent);

									Question leftQuestion = (Question) leftReference;
									switch (leftQuestion.getAnswerType()) {
									case RADIO:
									case MULTI_CHECKBOX:
										// The flow shouldn't get in here.
										break;
									case INPUT:
										switch (leftQuestion.getAnswerFormat()) {
										case NUMBER:
											droolsConditions += "	$" + leftQuestion.getUniqueNameReadable()
													+ " : Question(getTag() == '" + leftQuestion.getName()
													+ "', getAnswer('" + getTreeObjectAnswerType(leftQuestion)
													+ "') >= " + value1 + " && < " + value2 + ") from $"
													+ leftReferenceParent.getUniqueNameReadable()
													+ ".getQuestions() \n";
											break;
										case POSTAL_CODE:
										case TEXT:
											droolsConditions += "	$" + leftQuestion.getUniqueNameReadable()
													+ " : Question(getTag() == '" + leftQuestion.getName()
													+ "', getAnswer('" + getTreeObjectAnswerType(leftQuestion)
													+ "') >= '" + value1 + "' && < '" + value2 + "') from $"
													+ leftReferenceParent.getUniqueNameReadable()
													+ ".getQuestions() \n";
											break;
										case DATE:
											String betweenDate = "";
											if (leftExpressionValueTreeObject.getUnit() != null) {
												switch (leftExpressionValueTreeObject.getUnit()) {
												case YEARS:
													betweenDate = "DateUtils.returnYearsDistanceFromDate(getAnswer('"
															+ AnswerFormat.DATE.toString() + "')) >= " + value1
															+ " && < " + value2;
													break;
												case MONTHS:
													betweenDate = "DateUtils.returnMonthsDistanceFromDate(getAnswer('"
															+ AnswerFormat.DATE.toString() + "')) >= " + value1
															+ " && < " + value2;
													break;
												case DAYS:
													betweenDate = "DateUtils.returnDaysDistanceFromDate(getAnswer('"
															+ AnswerFormat.DATE.toString() + "')) >= " + value1
															+ " && < " + value2;
													break;
												case DATE:
													betweenDate = "getAnswer('" + AnswerFormat.DATE.toString()
															+ "') >= " + value1 + " && < " + value2;
													break;
												}
											} else {
												AbcdLogger.warning(DroolsParser.class.getName(),
														"Question with format DATE don't have a selected unit");
											}
											droolsConditions += "	$" + leftQuestion.getUniqueNameReadable()
													+ " : Question( " + betweenDate + ") from $"
													+ leftReferenceParent.getUniqueNameReadable() + ".getQuestions()\n";
										}
										break;
									}
								} else {
									// We are managing a CustomVariable
									droolsConditions += customVariableBetweenValues(conditions);
								}
							}
						} else {
							throw new ExpressionInvalidException("Types inside the between function don't match",
									conditions);
						}
					}
				}
			}
		}
		return droolsConditions;
	}

	private static String customVariableBetweenValues(ExpressionChain conditions) {
		String droolsConditions = "";
		List<Expression> operatorLeft = ((ExpressionChain) conditions.getExpressions().get(0)).getExpressions();
		if ((operatorLeft.size() == 1) && (operatorLeft.get(0) instanceof ExpressionValueCustomVariable)) {

			ExpressionValueCustomVariable leftVariable = (ExpressionValueCustomVariable) operatorLeft.get(0);
			if (leftVariable != null) {

				List<Expression> firstExpressionValue = ((ExpressionChain) conditions.getExpressions().get(2))
						.getExpressions();
				List<Expression> secondExpressionValue = ((ExpressionChain) conditions.getExpressions().get(3))
						.getExpressions();
				Object value1 = ((ExpressionValue) firstExpressionValue.get(0)).getValue();
				Object value2 = ((ExpressionValue) secondExpressionValue.get(0)).getValue();

				if ((value1 != null) && (value2 != null)) {

					TreeObject leftReferenceParent = leftVariable.getReference().getParent();
					String varName = leftVariable.getVariable().getName();

					switch (leftVariable.getVariable().getScope()) {
					case FORM:
						putTreeObjectName(leftVariable.getReference(), leftVariable.getReference()
								.getUniqueNameReadable());
						switch (getTreeObjectAnswerFormat(leftVariable)) {
						case DATE:
						case NUMBER:
							droolsConditions += "	$" + leftVariable.getReference().getUniqueNameReadable()
									+ " : SubmittedForm(isScoreSet('" + varName + "'), getVariableValue('" + varName
									+ "') >= " + value1 + " && < " + value2
									+ ") from $droolsForm.getSubmittedForm() \n";
							break;
						case POSTAL_CODE:
						case TEXT:
							droolsConditions += "	$" + leftVariable.getReference().getUniqueNameReadable()
									+ " : SubmittedForm(isScoreSet('" + varName + "'), getVariableValue('" + varName
									+ "') >= '" + value1 + "' && < '" + value2
									+ "') from $droolsForm.getSubmittedForm() \n";
							break;
						}
						break;
					case CATEGORY:
						switch (getTreeObjectAnswerFormat(leftVariable)) {
						case DATE:
						case NUMBER:
							droolsConditions += "	$" + leftVariable.getReference().getUniqueNameReadable()
									+ " : Category( getTag() == '" + leftVariable.getReference().getName()
									+ "', isScoreSet('" + varName + "'), getVariableValue('" + varName + "') >= "
									+ value1 + " && < " + value2 + ") from $"
									+ leftReferenceParent.getUniqueNameReadable() + ".getCategories() \n";
							break;
						case POSTAL_CODE:
						case TEXT:
							droolsConditions += "	$" + leftVariable.getReference().getUniqueNameReadable()
									+ " : Category( getTag() == '" + leftVariable.getReference().getName()
									+ "', isScoreSet('" + varName + "'), getVariableValue('" + varName + "') >= '"
									+ value1 + "' && < '" + value2 + "') from $"
									+ leftReferenceParent.getUniqueNameReadable() + ".getCategories() \n";
							break;
						}
						break;
					case GROUP:
						switch (getTreeObjectAnswerFormat(leftVariable)) {
						case DATE:
						case NUMBER:
							droolsConditions += "	$" + leftVariable.getReference().getUniqueNameReadable()
									+ " : Group( getTag() == '" + leftVariable.getReference().getName()
									+ "', isScoreSet('" + varName + "'), getVariableValue('" + varName + "') >= "
									+ value1 + " && < " + value2 + ") from $"
									+ leftReferenceParent.getUniqueNameReadable() + ".getGroups() \n";
							break;
						case POSTAL_CODE:
						case TEXT:
							droolsConditions += "	$" + leftVariable.getReference().getUniqueNameReadable()
									+ " : Group( getTag() == '" + leftVariable.getReference().getName()
									+ "', isScoreSet('" + varName + "'), getVariableValue('" + varName + "') >= '"
									+ value1 + "' && < '" + value2 + "') from $"
									+ leftReferenceParent.getUniqueNameReadable() + ".getGroups() \n";
							break;
						}
						break;
					case QUESTION:
						switch (getTreeObjectAnswerFormat(leftVariable)) {
						case DATE:
						case NUMBER:
							droolsConditions += "	$" + leftVariable.getReference().getUniqueNameReadable()
									+ " : Question( getTag() == '" + leftVariable.getReference().getName()
									+ "', isScoreSet('" + varName + "'), getVariableValue('" + varName + "') >= "
									+ value1 + " && < " + value2 + ") from $"
									+ leftReferenceParent.getUniqueNameReadable() + ".getQuestions() \n";
							break;
						case POSTAL_CODE:
						case TEXT:
							droolsConditions += "	$" + leftVariable.getReference().getUniqueNameReadable()
									+ " : Question( getTag() == '" + leftVariable.getReference().getName()
									+ "', isScoreSet('" + varName + "'), getVariableValue('" + varName + "') >= '"
									+ value1 + "' && < '" + value2 + "') from $"
									+ leftReferenceParent.getUniqueNameReadable() + ".getQuestions() \n";
							break;
						}
						break;
					}
				}
			}
		}
		return droolsConditions;
	}

	private static String questionDateDaysOperatorValueNumber(TreeObject question, Double value,
			AvailableOperator operator) {
		return "\t$" + question.getUniqueNameReadable() + " : Question(getTag()== '" + question.getName()
				+ "', DateUtils.returnDaysDistanceFromDate(getAnswer('" + getTreeObjectAnswerType(question) + "')) "
				+ operator.getValue() + " " + value.intValue() + ") from $"
				+ question.getParent().getUniqueNameReadable() + ".getQuestions()\n";
	}

	private static String questionDateMonthsOperatorValueNumber(TreeObject question, Double value,
			AvailableOperator operator) {
		return "\t$" + question.getUniqueNameReadable() + " : Question(getTag()== '" + question.getName()
				+ "', DateUtils.returnMonthsDistanceFromDate(getAnswer('" + getTreeObjectAnswerType(question) + "')) "
				+ operator.getValue() + " " + value.intValue() + ") from $"
				+ question.getParent().getUniqueNameReadable() + ".getQuestions()\n";
	}

	private static String questionDateOperatorValue(TreeObject leftReferenceParent, TreeObject leftQuestion,
			AvailableOperator operator, String droolsValue) throws NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException {
		String rule = "";
		// Check if the reference exists in the rule, if not, it creates
		// a new reference
		rule += checkVariableAssignation(leftQuestion);
		rule += "\t$" + leftQuestion.getUniqueNameReadable() + " : Question(getTag() == '" + leftQuestion.getName()
				+ "', DateUtils.returnYearsDistanceFromDate(getAnswer('" + getTreeObjectAnswerType(leftQuestion)
				+ "')) " + operator.getValue() + droolsValue + ") from $" + leftReferenceParent.getUniqueNameReadable()
				+ ".getQuestions()\n";
		return rule;
	}

	private static String questionDateYearsOperatorValueNumber(TreeObject question, Double value,
			AvailableOperator operator) {
		return "\t$" + question.getUniqueNameReadable() + " : Question(getTag()== '" + question.getName()
				+ "', DateUtils.returnYearsDistanceFromDate(getAnswer('" + getTreeObjectAnswerType(question) + "')) "
				+ operator.getValue() + " " + value.intValue() + ") from $"
				+ question.getParent().getUniqueNameReadable() + ".getQuestions()\n";
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
					ExpressionValue value = (ExpressionValue) rightExpressions.get(0);
					String droolsValue = "";
					if (value instanceof ExpressionValueTreeObjectReference) {
						droolsConditions += checkVariableAssignation((ExpressionValueTreeObjectReference) value);
						droolsValue = getDroolsVariableValueFromExpressionValueTreeObject((ExpressionValueTreeObjectReference) value);

					} else {
						droolsValue = ((ExpressionValue) value).getValue().toString();
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
									droolsConditions += "	$" + leftQuestion.getUniqueNameReadable()
											+ " : Question(getTag()== '" + leftQuestion.getName() + "', getAnswer('"
											+ getTreeObjectAnswerType(leftQuestion) + "') " + operator.getValue() + " "
											+ droolsValue + ") from $" + leftTreeObjectParent.getUniqueNameReadable()
											+ ".getQuestions()\n";
									break;
								case TEXT:
								case POSTAL_CODE:
									droolsConditions += "	$" + leftQuestion.getUniqueNameReadable()
											+ " : Question(getTag()== '" + leftQuestion.getName() + "', getAnswer('"
											+ getTreeObjectAnswerType(leftQuestion) + "') " + operator.getValue() + " "
											+ droolsValue + ") from $" + leftTreeObjectParent.getUniqueNameReadable()
											+ ".getQuestions()\n";
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

							droolsConditions += "	$" + leftQuestion.getUniqueNameReadable() + " : Question(getAnswer('"
									+ getTreeObjectAnswerType(leftQuestion) + "') instanceof Date, getTag() == '"
									+ leftQuestion.getName() + "') from $" + leftQuestionParent.getUniqueNameReadable()
									+ ".getQuestions() \n";
							droolsConditions += "	$" + rightQuestion.getUniqueNameReadable()
									+ " : Question(getAnswer('" + getTreeObjectAnswerType(rightQuestion)
									+ "') instanceof Date, getTag() == '" + rightQuestion.getName() + "', getAnswer('"
									+ getTreeObjectAnswerType(rightQuestion) + "') " + operator.getValue() + " $"
									+ leftQuestion.getUniqueNameReadable() + ".getAnswer('"
									+ getTreeObjectAnswerType(rightQuestion) + "')) from $"
									+ rightQuestionParent.getUniqueNameReadable() + ".getQuestions() \n";
						}
					}
				}
				// Comparison with system date (Special date)
				else if ((leftTreeObject instanceof Question) && (rightExpressions.size() == 1)
						&& (rightExpressions.get(0) instanceof ExpressionValueSystemDate)) {

					// Create the conditions for parents hierarchy
					TreeObject leftQuestionParent = leftTreeObject.getParent();
					droolsConditions += SimpleConditionsGenerator.getTreeObjectConditions(leftQuestionParent);

					droolsConditions += "	$" + leftTreeObject.getUniqueNameReadable() + " : Question(getAnswer('"
							+ getTreeObjectAnswerType(leftTreeObject) + "') instanceof Date, getTag() == '"
							+ leftTreeObject.getName() + "', getAnswer('" + getTreeObjectAnswerType(leftTreeObject)
							+ "') " + operator.getValue() + " DateUtils.returnCurrentDate()) from $"
							+ leftQuestionParent.getUniqueNameReadable() + ".getQuestions() \n";
				}
			}
		} else {
			ExpressionValueCustomVariable customVariable = (ExpressionValueCustomVariable) ((ExpressionChain) conditions
					.get(0)).getExpressions().get(0);
			Expression valueExpression = ((ExpressionChain) conditions.get(2)).getExpressions().get(0);
			droolsConditions += treeObjectScoreLogicOperatorValueExpression(customVariable,
					(ExpressionOperatorLogic) conditions.get(1), (ExpressionValue) valueExpression);
		}
		return droolsConditions;
	}

	/**
	 * Parse conditions like => Score (logic operator (==, <=, <, >=, >)) value. <br>
	 * Create drools rule like => Category(isScoreSet('cScore'),
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
					ruleCore += "	$" + treeObjectCustomVariable.getUniqueNameReadable()
							+ " : SubmittedForm(isScoreSet('" + varName + "'), getVariableValue('" + varName + "') "
							+ expressionOperatorLogic.getValue().toString() + " '" + valueNumber.getValue()
							+ "') from $droolsForm.getSubmittedForm() \n";
				} else {
					String treeObjectClassName = treeObjectCustomVariable.getClass().getSimpleName();
					putTreeObjectName(treeObjectCustomVariable, treeObjectCustomVariable.getUniqueNameReadable());
					TreeObject parent = treeObjectCustomVariable.getParent();
					// Check conditions for the parent
					SimpleConditionsGenerator.getTreeObjectConditions(parent);
					ruleCore += "\t$" + treeObjectCustomVariable.getUniqueNameReadable() + " : " + treeObjectClassName
							+ "( isScoreSet('" + varName + "'), getVariableValue('" + varName + "') "
							+ expressionOperatorLogic.getValue().toString() + " '" + valueNumber.getValue()
							+ "' ) from $" + parent.getUniqueNameReadable() + ".get" + treeObjectClassName + "('"
							+ treeObjectCustomVariable.getName() + "') \n";
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
	 * Create drools rule like => Category(isScoreSet('cScore'),
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
			ExpressionValue expressionValue) throws NullTreeObjectException, TreeObjectInstanceNotRecognizedException,
			TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException {
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
					switch (expressionValueCustomVariable.getVariable().getType()) {
					case NUMBER:
					case DATE:
						if (treeObjectCustomVariable instanceof Form) {
							putTreeObjectName(treeObjectCustomVariable,
									treeObjectCustomVariable.getUniqueNameReadable());
							ruleCore += "\t$" + treeObjectCustomVariable.getUniqueNameReadable()
									+ " : SubmittedForm( isScoreSet('" + customVariable.getName()
									+ "'), getVariableValue('" + customVariable.getName() + "') "
									+ operator.getValue().toString() + " " + droolsValue
									+ ") from $droolsForm.getSubmittedForm() \n";
						} else {
							String treeObjectClassName = treeObjectCustomVariable.getClass().getSimpleName();
							putTreeObjectName(treeObjectCustomVariable,
									treeObjectCustomVariable.getUniqueNameReadable());
							TreeObject parent = treeObjectCustomVariable.getParent();
							// Check conditions for the parent
							SimpleConditionsGenerator.getTreeObjectConditions(parent);
							ruleCore += "\t$" + treeObjectCustomVariable.getUniqueNameReadable() + " : "
									+ treeObjectClassName + "( isScoreSet('" + customVariable.getName()
									+ "'), getVariableValue('" + customVariable.getName() + "') "
									+ operator.getValue().toString() + " " + droolsValue + ") from $"
									+ parent.getUniqueNameReadable() + ".get" + treeObjectClassName + "('"
									+ treeObjectCustomVariable.getName() + "') \n";
						}
						break;
					case STRING:
						if (treeObjectCustomVariable instanceof Form) {
							putTreeObjectName(treeObjectCustomVariable,
									treeObjectCustomVariable.getUniqueNameReadable());
							ruleCore += "\t$" + treeObjectCustomVariable.getUniqueNameReadable()
									+ " : SubmittedForm( isScoreSet('" + customVariable.getName()
									+ "'), getVariableValue('" + customVariable.getName() + "') "
									+ operator.getValue().toString() + " '" + droolsValue
									+ "') from $droolsForm.getSubmittedForm() \n";

						} else {
							String treeObjectClassName = treeObjectCustomVariable.getClass().getSimpleName();
							putTreeObjectName(treeObjectCustomVariable,
									treeObjectCustomVariable.getUniqueNameReadable());
							TreeObject parent = treeObjectCustomVariable.getParent();
							// Check conditions for the parent
							SimpleConditionsGenerator.getTreeObjectConditions(parent);
							ruleCore += "\t$" + treeObjectCustomVariable.getUniqueNameReadable() + " : "
									+ treeObjectClassName + "( isScoreSet('" + customVariable.getName()
									+ "'), getVariableValue('" + customVariable.getName() + "') "
									+ operator.getValue().toString() + " " + droolsValue + ") from $"
									+ parent.getUniqueNameReadable() + ".get" + treeObjectClassName + "('"
									+ treeObjectCustomVariable.getName() + "') \n";
						}
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
}
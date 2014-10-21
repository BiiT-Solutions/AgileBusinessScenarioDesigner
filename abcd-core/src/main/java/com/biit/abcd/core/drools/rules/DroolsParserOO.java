package com.biit.abcd.core.drools.rules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.ss.formula.functions.FinanceLib;

import com.biit.abcd.core.drools.prattparser.ExpressionChainPrattParser;
import com.biit.abcd.core.drools.prattparser.PrattParser;
import com.biit.abcd.core.drools.prattparser.PrattParserException;
import com.biit.abcd.core.drools.prattparser.visitor.ITreeElement;
import com.biit.abcd.core.drools.prattparser.visitor.TreeElementMathExpressionVisitor;
import com.biit.abcd.core.drools.prattparser.visitor.TreeElementVariableCreatorVisitor;
import com.biit.abcd.core.drools.prattparser.visitor.exceptions.NotCompatibleTypeException;
import com.biit.abcd.core.drools.rules.exceptions.RuleNotImplementedException;
import com.biit.abcd.core.drools.utils.RulesUtils;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.abcd.persistence.entity.AnswerType;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Group;
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
import com.biit.abcd.persistence.entity.expressions.ExpressionValueString;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueSystemDate;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.form.TreeObject;

public class DroolsParserOO {

	private static boolean orOperatorUsed = false;

	private static HashMap<TreeObject, String> treeObjectDroolsname = new HashMap<TreeObject, String>();

	/**
	 * Adds condition rows to the rule that manages the assignation of a
	 * variable in the action<br>
	 * 
	 * @param variable
	 *            variable to be added to the LHS of the rule
	 * @return LHS of the rule modified with the new variables
	 */
	private static String addConditionVariable(TreeObject treeObject) {
		String ruleCore = "";

		// Add the variable assignation to the rule
		if (treeObject != null) {
			if (treeObject instanceof Form) {
				ruleCore += simpleFormCondition((Form) treeObject);

			} else if (treeObject instanceof Category) {
				ruleCore += simpleCategoryConditions((Category) treeObject);

			} else if (treeObject instanceof Group) {
				ruleCore += simpleGroupConditions((Group) treeObject);

			} else if (treeObject instanceof Question) {
				ruleCore += simpleQuestionConditions((Question) treeObject);
			}
		}
		return ruleCore;
	}

	private static String andOperator(List<Expression> expressions) {
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
	 */
	private static String answersInQuestionCondition(List<Expression> conditions) {
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

				TreeObject leftReferenceParent = leftReference.getParent();
				putTreeObjectName(leftReference, leftReference.getUniqueNameReadable().toString());
				// Check the parent
				if (leftReferenceParent instanceof Form) {
					droolsConditions += simpleFormCondition((Form) leftReferenceParent);
				} else if (leftReferenceParent instanceof Category) {
					droolsConditions += simpleCategoryConditions((Category) leftReferenceParent);
				} else if (leftReferenceParent instanceof Group) {
					droolsConditions += simpleGroupConditions((Group) leftReferenceParent);
				}
				if (leftReference instanceof Question) {
					Question leftQuestion = (Question) leftReference;
					droolsConditions += "	$" + leftQuestion.getUniqueNameReadable().toString()
							+ " : Question(getTag() == '" + leftQuestion.getName() + "', getAnswer('"
							+ getTreeObjectAnswerType(leftQuestion) + "') in( " + inValues + " )) from $"
							+ leftReferenceParent.getUniqueNameReadable().toString() + ".getQuestions()\n";
				}
			}
		}
		return droolsConditions;
	}

	/**
	 * Receives a list with the three parameters needed to calculate the PMT
	 * function, (Rate, Months, Present value)<br>
	 * We make use of the Apache POI lib<br>
	 * For more information:
	 * http://poi.apache.org/apidocs/org/apache/poi/ss/formula
	 * /functions/FinanceLib.html
	 * 
	 * @param actions
	 * @return
	 */
	private static String calculatePMT(List<Expression> actions) {
		String pmt = "";
		Double rate = null;
		Double months = null;
		Double presentValue = null;
		if (actions.get(0) instanceof ExpressionChain) {
			rate = (((ExpressionValueNumber) ((ExpressionChain) actions.get(0)).getExpressions().get(0)).getValue() / 100.) / 12.;
			months = ((ExpressionValueNumber) ((ExpressionChain) actions.get(1)).getExpressions().get(0)).getValue();
			presentValue = ((ExpressionValueNumber) ((ExpressionChain) actions.get(2)).getExpressions().get(0))
					.getValue();
		} else {
			rate = (((ExpressionValueNumber) actions.get(0)).getValue() / 100.) / 12.;
			months = ((ExpressionValueNumber) actions.get(1)).getValue();
			presentValue = ((ExpressionValueNumber) actions.get(2)).getValue();
		}
		// Rate, months, present value, future value, beginning of the period
		if (((rate != null) & (months != null)) || (presentValue != null)) {
			Double pmtVal = FinanceLib.pmt(rate, months, presentValue, 0, false);
			// pmt = String.valueOf(pmtVal);
			pmt = String.format("%.2f", pmtVal);
			// Remove the minus sign (absolute format) and replace the possible
			// commas with dots (drools doesn't accept commas)
			pmt = pmt.replace(",", ".").replace("-", "");
		}
		return pmt;
	}

	private static String checkCustomVariableAssignation(ExpressionValueCustomVariable variable) {
		if (variable != null) {
			TreeObject treeObject = variable.getReference();
			if (getTreeObjectName(variable.getReference()) == null) {
				// The variable don't exists and can't have a value assigned
				String ruleCore = "";

				// Add the variable assignation to the rule
				if (treeObject != null) {
					if (treeObject instanceof Category) {
						ruleCore += simpleCategoryCustomVariableConditions(variable);

					} else if (treeObject instanceof Group) {
						ruleCore += simpleGroupCustomVariableConditions(variable);

					} else if (treeObject instanceof Question) {
						ruleCore += simpleQuestionCustomVariableConditions(variable);
					}
				}
				return ruleCore;
			}
		}
		return "";
	}

	private static String checkCustomVariableAssignationWithoutScore(ExpressionValueCustomVariable variable) {
		if (variable != null) {
			TreeObject treeObject = variable.getReference();
			if (getTreeObjectName(variable.getReference()) == null) {
				// The variable don't exists and can't have a value assigned
				String ruleCore = "";

				// Add the variable assignation to the rule
				if (treeObject != null) {
					if (treeObject instanceof Category) {
						ruleCore += simpleCategoryCustomVariableConditionsWithoutScore(variable);

					} else if (treeObject instanceof Group) {
						ruleCore += simpleGroupCustomVariableConditionsWithoutScore(variable);

					} else if (treeObject instanceof Question) {
						ruleCore += simpleQuestionCustomVariableConditionsWithoutScore(variable);
					}
				}
				return ruleCore;
			}
		}
		return "";
	}

	/**
	 * Checks the existence of a binding in drools with the the reference of the
	 * variable passed If there is no binding, creates a new one (i.e. $var :
	 * Question() ...)
	 * 
	 * @param expValVariable
	 */
	private static String checkVariableAssignation(Expression expression) {
		if (expression instanceof ExpressionValueCustomVariable) {
			return checkCustomVariableAssignation((ExpressionValueCustomVariable) expression);
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
	 */
	private static String checkVariableAssignationWithoutScore(Expression expression) {
		if (expression instanceof ExpressionValueCustomVariable) {
			return checkCustomVariableAssignationWithoutScore((ExpressionValueCustomVariable) expression);
		} else if (expression instanceof ExpressionValueTreeObjectReference) {
			return checkVariableAssignation((ExpressionValueTreeObjectReference) expression);
		}
		return "";
	}

	private static String checkVariableAssignation(ExpressionValueTreeObjectReference variable) {
		return checkVariableAssignation(variable.getReference());
	}

	private static String checkVariableAssignation(TreeObject treeObject) {
		if (treeObject != null) {
			if (getTreeObjectName(treeObject) == null) {
				// The variable don't exists and can't have a value assigned
				return addConditionVariable(treeObject);
			}
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
	 * @return
	 * @throws RuleNotImplementedException
	 * @throws NotCompatibleTypeException
	 */
	public static String createDroolsRule(List<Rule> rules) throws RuleNotImplementedException,
			NotCompatibleTypeException {
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
	 */
	private static String createDroolsRule(Rule rule) throws RuleNotImplementedException, NotCompatibleTypeException {
		if (rule == null) {
			return null;
		}

		System.out.println("OO RULE CONDITIONS: " + rule.getConditions());
		System.out.println("OO RULE ACTIONS: " + rule.getActions());

		String result = "";
		treeObjectDroolsname = new HashMap<TreeObject, String>();
		result += "\t$droolsForm: DroolsForm()\n";

		// Obtain conditions if exists.
		if ((rule.getConditions() != null) && (rule.getConditions().getExpressions() != null)
				&& (!rule.getConditions().getExpressions().isEmpty())) {

			ITreeElement prattResult = calculatePrattParserResult(rule.getConditions());
			System.out.println("CONDITION PRATT RESULT: " + prattResult.getExpressionChain());
			createDroolsVariables(prattResult);

			result += parseConditions(rule.getConditions());
		}
		if ((rule.getActions() != null) && (rule.getActions().getExpressions() != null)
				&& (!rule.getActions().getExpressions().isEmpty())) {

			ITreeElement prattResult = calculatePrattParserResult(rule.getActions());
			System.out.println("ACTION PRATT RESULT: " + prattResult.getExpressionChain());
			createDroolsVariables(prattResult);

			String actionString = analyzePrattParserResult(prattResult);
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

	private static String equalsNotEqualsOperator(List<Expression> expressions, AvailableOperator availableOperator) {
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
			Double value = ((ExpressionValueNumber) operatorRight.get(0)).getValue();

			TreeObject leftTreeObjectParent = leftTreeObject.getParent();
			putTreeObjectName(leftTreeObject, leftTreeObject.getUniqueNameReadable().toString());
			// Check the parent
			if (leftTreeObjectParent instanceof Form) {
				droolsConditions += simpleFormCondition((Form) leftTreeObjectParent);
			} else if (leftTreeObjectParent instanceof Category) {
				droolsConditions += simpleCategoryConditions((Category) leftTreeObjectParent);
			} else if (leftTreeObjectParent instanceof Group) {
				droolsConditions += simpleGroupConditions((Group) leftTreeObjectParent);
			}

			if (leftTreeObject instanceof Question) {
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
		return treeObjectDroolsname.get(treeObject);
	}

	private static String mathAssignationAction(ExpressionChain actions, ITreeElement prattParserResult)
			throws RuleNotImplementedException, NotCompatibleTypeException {
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
	 */
	private static String assignationFunctionAction(ExpressionChain actions) {
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

	private static String orOperator(List<Expression> expressions) {
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
			AbcdLogger.errorMessage(DroolsParserOO.class.getName(), ex);
		}
		return prattParserResult;
	}

	/**
	 * Parse the rule actions
	 * 
	 * @param prattParserResult
	 * @return
	 * @throws RuleNotImplementedException
	 * @throws NotCompatibleTypeException
	 */
	private static String analyzePrattParserResult(ITreeElement prattParserResult) throws RuleNotImplementedException,
			NotCompatibleTypeException {
		// Retrieve the expression hierarchy created by the Pratt parser
		ExpressionChain prattParserExpressionChain = prattParserResult.getExpressionChain();

		if ((prattParserExpressionChain.getExpressions().get(0) instanceof ExpressionChain)
				&& (((ExpressionChain) prattParserExpressionChain.getExpressions().get(0)).getExpressions().get(0) instanceof ExpressionValueCustomVariable)) {

			if (prattParserExpressionChain.getExpressions().get(1) instanceof ExpressionFunction) {
				switch (((ExpressionFunction) prattParserExpressionChain.getExpressions().get(1)).getValue()) {
				case MAX:
				case MIN:
				case AVG:
				case SUM:
				case PMT:
					return assignationFunctionAction(prattParserExpressionChain);
				}
			}
			// Mathematical expression
			else {
				return mathAssignationAction(prattParserExpressionChain, prattParserResult);
			}
		}
		throw new RuleNotImplementedException("Rule not implemented.", prattParserExpressionChain);
	}

	private static String parseConditions(ExpressionChain conditions) {

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

	private static String assignationFunctionPmtAction(List<Expression> actions) {
		String ruleCore = "";
		ExpressionValueCustomVariable var = (ExpressionValueCustomVariable) actions.get(0);
		// Check if the reference exists in the rule, if not, it creates a new
		// reference
		ruleCore += checkVariableAssignation(var);

		ruleCore += RulesUtils.getThenRuleString();

		ExpressionFunction auxFunc = (ExpressionFunction) actions.get(2);
		switch (auxFunc.getValue()) {
		case PMT:
			actions = Arrays.asList(actions.get(3), actions.get(5), actions.get(7));
			String value = calculatePMT(actions);
			ruleCore += "	$" + getTreeObjectName(var.getReference()) + ".setVariableValue('"
					+ var.getVariable().getName() + "', " + value + ");\n";
			ruleCore += "	AbcdLogger.debug(\"DroolsRule\", \"Variable set (" + var.getReference().getName() + ", "
					+ var.getVariable().getName() + ", " + value + ")\");\n";
			break;
		default:
			break;
		}
		return ruleCore;
	}

	/**
	 * Creates the drools variables used in the parsed rule
	 * 
	 * @param expressionChain
	 * @throws NotCompatibleTypeException
	 */
	private static void createDroolsVariables(ITreeElement prattParserResult) throws NotCompatibleTypeException {
		List<DroolsVariable> ruleVariables = new ArrayList<DroolsVariable>();
		TreeElementVariableCreatorVisitor variableCreator = new TreeElementVariableCreatorVisitor();

		System.out.println("CREATING VARIABLES: " + prattParserResult.getExpressionChain());

		prattParserResult.accept(variableCreator);
		if (variableCreator != null) {
			ruleVariables = variableCreator.getVariables();
		}

		for (DroolsVariable variable : ruleVariables) {
			System.out.println("VARIABLE: " + variable.getName());
		}

	}

	private static String processResultConditionsFromPrattParser(ExpressionChain prattParserResultExpressionChain) {

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
					return questionBetweenAnswersCondition(expressions);
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

	private static String negatedExpressions(ExpressionChain prattParserResultExpressionChain) {
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
		treeObjectDroolsname.put(treeObject, value);
	}

	private static String questionAnswerEqualsCondition(Question question, Answer answer,
			AvailableOperator availableOperator) {
		// String droolsConditions = "(\n";
		String droolsConditions = "";
		// Check the parent of the question
		TreeObject questionParent = question.getParent();
		putTreeObjectName(question, question.getUniqueNameReadable().toString());
		if (questionParent instanceof Category) {
			droolsConditions += simpleCategoryConditions((Category) questionParent);
		} else if (questionParent instanceof Group) {
			droolsConditions += simpleGroupConditions((Group) questionParent);
		}
		putTreeObjectName(question, question.getUniqueNameReadable().toString());
		droolsConditions += "	$" + question.getUniqueNameReadable().toString() + " : Question(getTag() == '"
				+ question.getName() + "', getAnswer('" + getTreeObjectAnswerType(question) + "')"
				+ availableOperator.getValue().toString() + "'" + answer.getName() + "') from $"
				+ questionParent.getUniqueNameReadable().toString() + ".getQuestions()\n";
		// return droolsConditions + ")\n";
		return droolsConditions;
	}

	/**
	 * Parse conditions like => Question BETWEEN(Answer1, answer2). <br>
	 * The values inside the between must be always numbers <br>
	 * Create drools rule like => Question( (getAnswer() >= answer.getValue())
	 * && (getAnswer() <= answer.getValue()))
	 * 
	 * @param conditions
	 * @return LHS of the rule
	 */
	private static String questionBetweenAnswersCondition(List<Expression> conditions) {
		String droolsConditions = "";
		List<Expression> operatorLeft = ((ExpressionChain) conditions.get(0)).getExpressions();
		TreeObject leftReference = null;
		if ((operatorLeft.size() == 1) && (operatorLeft.get(0) instanceof ExpressionValueTreeObjectReference)) {

			ExpressionValueTreeObjectReference leftExpressionValueTreeObject = (ExpressionValueTreeObjectReference) operatorLeft
					.get(0);
			leftReference = leftExpressionValueTreeObject.getReference();
			if (leftReference != null) {

				List<Expression> firstExpressionValue = ((ExpressionChain) conditions.get(2)).getExpressions();
				List<Expression> secondExpressionValue = ((ExpressionChain) conditions.get(3)).getExpressions();

				if ((firstExpressionValue.size() == 1) && (secondExpressionValue.size() == 1)) {
					// Get the values of the between expression
					Object value1 = ((ExpressionValue) firstExpressionValue.get(0)).getValue();
					Object value2 = ((ExpressionValue) secondExpressionValue.get(0)).getValue();

					if ((value1 != null) && (value2 != null)) {
						TreeObject leftReferenceParent = leftReference.getParent();
						if (leftReferenceParent != null) {
							putTreeObjectName(leftReference, leftReference.getUniqueNameReadable().toString());
							// Check the parent
							if (leftReferenceParent instanceof Form) {
								droolsConditions += simpleFormCondition((Form) leftReferenceParent);
							} else if (leftReferenceParent instanceof Category) {
								droolsConditions += simpleCategoryConditions((Category) leftReferenceParent);
							} else if (leftReferenceParent instanceof Group) {
								droolsConditions += simpleGroupConditions((Group) leftReferenceParent);
							}
						}
						if (leftReference instanceof Question) {
							Question leftQuestion = (Question) leftReference;
							switch (leftQuestion.getAnswerType()) {
							case RADIO:
							case MULTI_CHECKBOX:
								// The flow shouldn't get in here.
								break;
							case INPUT:
								switch (leftQuestion.getAnswerFormat()) {
								case NUMBER:
								case POSTAL_CODE:
								case TEXT:
									droolsConditions += "	$" + leftQuestion.getUniqueNameReadable().toString()
											+ " : Question(getTag() == '" + leftQuestion.getName() + "', getAnswer('"
											+ getTreeObjectAnswerType(leftQuestion) + "') >= " + value1 + " && < "
											+ value2 + ") from $"
											+ leftReferenceParent.getUniqueNameReadable().toString()
											+ ".getQuestions() \n";
									break;
								case DATE:
									String betweenDate = "";
									if (leftExpressionValueTreeObject.getUnit() != null) {
										switch (leftExpressionValueTreeObject.getUnit()) {
										case YEARS:
											betweenDate = "DateUtils.returnYearsDistanceFromDate(getAnswer('"
													+ AnswerFormat.DATE.toString() + "')) >= " + value1 + " && < "
													+ value2;
											break;
										case MONTHS:
											betweenDate = "DateUtils.returnMonthsDistanceFromDate(getAnswer('"
													+ AnswerFormat.DATE.toString() + "')) >= " + value1 + " && < "
													+ value2;
											break;
										case DAYS:
											betweenDate = "DateUtils.returnDaysDistanceFromDate(getAnswer('"
													+ AnswerFormat.DATE.toString() + "')) >= " + value1 + " && < "
													+ value2;
											break;
										case DATE:
											betweenDate = "getAnswer('" + AnswerFormat.DATE.toString() + "') >= "
													+ value1 + " && < " + value2;
											break;
										}
									} else {
										AbcdLogger.warning(DroolsParserOO.class.getName(),
												"Question with format DATE don't have a selected unit");
									}
									droolsConditions += "	$" + leftQuestion.getUniqueNameReadable().toString()
											+ " : Question( " + betweenDate + ") from $"
											+ leftReferenceParent.getUniqueNameReadable().toString()
											+ ".getQuestions()\n";
								}
								break;
							}
						} else {
							// We are managing a CustomVariable
							droolsConditions += customVariableBetweenValues(conditions);
						}
					}
				}
			}
		}
		return droolsConditions;
	}

	private static String customVariableBetweenValues(List<Expression> conditions) {
		String droolsConditions = "";
		List<Expression> operatorLeft = ((ExpressionChain) conditions.get(0)).getExpressions();
		if ((operatorLeft.size() == 1) && (operatorLeft.get(0) instanceof ExpressionValueCustomVariable)) {

			ExpressionValueCustomVariable leftVariable = (ExpressionValueCustomVariable) operatorLeft.get(0);
			if (leftVariable != null) {

				List<Expression> firstExpressionValue = ((ExpressionChain) conditions.get(2)).getExpressions();
				List<Expression> secondExpressionValue = ((ExpressionChain) conditions.get(3)).getExpressions();
				Object value1 = ((ExpressionValue) firstExpressionValue.get(0)).getValue();
				Object value2 = ((ExpressionValue) secondExpressionValue.get(0)).getValue();

				if ((value1 != null) && (value2 != null)) {

					TreeObject leftReferenceParent = leftVariable.getReference().getParent();
					String varName = leftVariable.getVariable().getName();

					switch (leftVariable.getVariable().getScope()) {
					case FORM:
						putTreeObjectName(leftVariable.getReference(), leftVariable.getReference()
								.getUniqueNameReadable().toString());
						droolsConditions += "	$" + leftVariable.getReference().getUniqueNameReadable().toString()
								+ " : SubmittedForm(isScoreSet('" + varName + "'), getVariableValue('" + varName
								+ "') >= " + value1 + " && < " + value2 + ") from $droolsForm.getSubmittedForm() \n";
						break;
					case CATEGORY:
						droolsConditions += "	$" + leftVariable.getReference().getUniqueNameReadable().toString()
								+ " : Category( getTag() == '" + leftVariable.getReference().getName()
								+ "', isScoreSet('" + varName + "'), getVariableValue('" + varName + "') >= " + value1
								+ " && < " + value2 + ") from $"
								+ leftReferenceParent.getUniqueNameReadable().toString() + ".getCategories() \n";
						break;
					case GROUP:
						droolsConditions += "	$" + leftVariable.getReference().getUniqueNameReadable().toString()
								+ " : Group( getTag() == '" + leftVariable.getReference().getName() + "', isScoreSet('"
								+ varName + "'), getVariableValue('" + varName + "') >= " + value1 + " && < " + value2
								+ ") from $" + leftReferenceParent.getUniqueNameReadable().toString()
								+ ".getGroups() \n";
						break;
					default:
						break;
					}
				}
			}
		}
		return droolsConditions;
	}

	private static String questionDateDaysOperatorValueNumber(TreeObject question, Double value,
			AvailableOperator operator) {
		return "\t$" + question.getUniqueNameReadable().toString() + " : Question(getTag()== '" + question.getName()
				+ "', DateUtils.returnDaysDistanceFromDate(getAnswer('" + getTreeObjectAnswerType(question) + "')) "
				+ operator.getValue() + " " + value.intValue() + ") from $"
				+ question.getParent().getUniqueNameReadable().toString() + ".getQuestions()\n";
	}

	private static String questionDateMonthsOperatorValueNumber(TreeObject question, Double value,
			AvailableOperator operator) {
		return "\t$" + question.getUniqueNameReadable().toString() + " : Question(getTag()== '" + question.getName()
				+ "', DateUtils.returnMonthsDistanceFromDate(getAnswer('" + getTreeObjectAnswerType(question) + "')) "
				+ operator.getValue() + " " + value.intValue() + ") from $"
				+ question.getParent().getUniqueNameReadable().toString() + ".getQuestions()\n";
	}

	private static String questionDateOperatorValue(TreeObject leftReferenceParent, TreeObject leftQuestion,
			AvailableOperator operator, String droolsValue) {
		// TODO
		String rule = "";
		// Check if the reference exists in the rule, if not, it creates
		// a new reference
		rule += checkVariableAssignation(leftQuestion);
		rule += "\t$" + leftQuestion.getUniqueNameReadable().toString() + " : Question(getTag() == '"
				+ leftQuestion.getName() + "', DateUtils.returnYearsDistanceFromDate(getAnswer('"
				+ getTreeObjectAnswerType(leftQuestion) + "')) " + operator.getValue() + droolsValue + ") from $"
				+ leftReferenceParent.getUniqueNameReadable().toString() + ".getQuestions()\n";
		return rule;
	}

	private static String questionDateYearsOperatorValueNumber(TreeObject question, Double value,
			AvailableOperator operator) {
		return "\t$" + question.getUniqueNameReadable().toString() + " : Question(getTag()== '" + question.getName()
				+ "', DateUtils.returnYearsDistanceFromDate(getAnswer('" + getTreeObjectAnswerType(question) + "')) "
				+ operator.getValue() + " " + value.intValue() + ") from $"
				+ question.getParent().getUniqueNameReadable().toString() + ".getQuestions()\n";
	}

	private static String questionGeGtLeLtAnswer(List<Expression> conditions, AvailableOperator operator) {
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
						TreeObject leftTreeObjectParent = leftTreeObject.getParent();
						putTreeObjectName(leftTreeObject, leftTreeObject.getUniqueNameReadable().toString());
						// Check the parent
						if (leftTreeObjectParent instanceof Form) {
							droolsConditions += simpleFormCondition((Form) leftTreeObjectParent);
						} else if (leftTreeObjectParent instanceof Category) {
							droolsConditions += simpleCategoryConditions((Category) leftTreeObjectParent);
						} else if (leftTreeObjectParent instanceof Group) {
							droolsConditions += simpleGroupConditions((Group) leftTreeObjectParent);
						}
						if (leftTreeObject instanceof Question) {
							Question leftQuestion = (Question) leftTreeObject;
							if (leftQuestion.getAnswerType().equals(AnswerType.INPUT)) {
								switch (leftQuestion.getAnswerFormat()) {
								case DATE:
									droolsConditions += questionDateOperatorValue(leftTreeObjectParent, leftQuestion,
											operator, droolsValue);
									break;
								case NUMBER:
									droolsConditions += "	$" + leftQuestion.getUniqueNameReadable().toString()
											+ " : Question(getTag()== '" + leftQuestion.getName() + "', getAnswer('"
											+ getTreeObjectAnswerType(leftQuestion) + "') " + operator.getValue() + " "
											+ droolsValue + ") from $"
											+ leftTreeObjectParent.getUniqueNameReadable().toString()
											+ ".getQuestions()\n";
									break;
								case TEXT:
								case POSTAL_CODE:
									droolsConditions += "	$" + leftQuestion.getUniqueNameReadable().toString()
											+ " : Question(getTag()== '" + leftQuestion.getName() + "', getAnswer('"
											+ getTreeObjectAnswerType(leftQuestion) + "') " + operator.getValue() + " "
											+ droolsValue + ") from $"
											+ leftTreeObjectParent.getUniqueNameReadable().toString()
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
					TreeObject rightTo = ((ExpressionValueTreeObjectReference) rightExpressions.get(0)).getReference();
					Question leftQuestion = (Question) leftTreeObject;
					if ((rightTo != null) && (rightTo instanceof Question)) {
						Question rightQuestion = (Question) rightTo;
						if (rightQuestion.getAnswerType().equals(AnswerType.INPUT)
								&& rightQuestion.getAnswerFormat().equals(AnswerFormat.DATE)) {

							TreeObject leftQuestionParent = leftQuestion.getParent();
							putTreeObjectName(leftQuestion, leftQuestion.getUniqueNameReadable().toString());
							// Check the parent
							if (leftQuestionParent instanceof Form) {
								droolsConditions += simpleFormCondition((Form) leftQuestionParent);
							} else if (leftQuestionParent instanceof Category) {
								droolsConditions += simpleCategoryConditions((Category) leftQuestionParent);
							} else if (leftQuestionParent instanceof Group) {
								droolsConditions += simpleGroupConditions((Group) leftQuestionParent);
							}

							TreeObject rightQuestionParent = rightQuestion.getParent();
							putTreeObjectName(rightQuestion, rightQuestion.getUniqueNameReadable().toString());
							// Check the parent
							if (rightQuestionParent instanceof Category) {
								droolsConditions += simpleCategoryConditions((Category) rightQuestionParent);
							} else if (rightQuestionParent instanceof Group) {
								droolsConditions += simpleGroupConditions((Group) rightQuestionParent);
							}
							droolsConditions += "	$" + leftQuestion.getUniqueNameReadable().toString()
									+ " : Question(getAnswer('" + getTreeObjectAnswerType(leftQuestion)
									+ "') instanceof Date, getTag() == '" + leftQuestion.getName() + "') from $"
									+ leftQuestionParent.getUniqueNameReadable().toString() + ".getQuestions() \n";
							droolsConditions += "	$" + rightQuestion.getUniqueNameReadable().toString()
									+ " : Question(getAnswer('" + getTreeObjectAnswerType(rightQuestion)
									+ "') instanceof Date, getTag() == '" + rightQuestion.getName() + "', getAnswer('"
									+ getTreeObjectAnswerType(rightQuestion) + "') " + operator.getValue() + " $"
									+ leftQuestion.getUniqueNameReadable().toString() + ".getAnswer('"
									+ getTreeObjectAnswerType(rightQuestion) + "')) from $"
									+ rightQuestionParent.getUniqueNameReadable().toString() + ".getQuestions() \n";
						}
					}
				}
				// Comparison with system date (Special date)
				else if ((leftTreeObject instanceof Question) && (rightExpressions.size() == 1)
						&& (rightExpressions.get(0) instanceof ExpressionValueSystemDate)) {
					Question leftQuestion = (Question) leftTreeObject;
					TreeObject leftQuestionParent = leftQuestion.getParent();
					putTreeObjectName(leftQuestion, leftQuestion.getUniqueNameReadable().toString());
					// Check the parent
					if (leftQuestionParent instanceof Category) {
						droolsConditions += simpleCategoryConditions((Category) leftQuestionParent);
					} else if (leftQuestionParent instanceof Group) {
						droolsConditions += simpleGroupConditions((Group) leftQuestionParent);
					}
					droolsConditions += "	$" + leftQuestion.getUniqueNameReadable().toString()
							+ " : Question(getAnswer('" + getTreeObjectAnswerType(leftQuestion)
							+ "') instanceof Date, getTag() == '" + leftQuestion.getName() + "', getAnswer('"
							+ getTreeObjectAnswerType(leftQuestion) + "') " + operator.getValue()
							+ " DateUtils.returnCurrentDate()) from $"
							+ leftQuestionParent.getUniqueNameReadable().toString() + ".getQuestions() \n";
				}
			}
		} else {
			ExpressionValueCustomVariable customVariable = (ExpressionValueCustomVariable) ((ExpressionChain) conditions
					.get(0)).getExpressions().get(0);
			Expression valueExpression = ((ExpressionChain) conditions.get(2)).getExpressions().get(0);
			droolsConditions += treeObjectScoreLogicOperatorValueExpression(customVariable,
					(ExpressionOperatorLogic) conditions.get(1), valueExpression);
		}
		return droolsConditions;
	}

	private static String simpleCategoryConditions(Category category) {
		String conditions = "";
		Form form = (Form) category.getParent();
		conditions += simpleFormCondition(form);

		if (getTreeObjectName(category) == null) {
			putTreeObjectName(category, category.getUniqueNameReadable().toString());
			conditions += "	$" + category.getUniqueNameReadable().toString() + " : Category() from $"
					+ form.getUniqueNameReadable().toString() + ".getCategory('" + category.getName() + "') \n";
		}
		return conditions;
	}

	private static String simpleCategoryCustomVariableConditions(ExpressionValueCustomVariable customVariable) {
		Category category = (Category) customVariable.getReference();
		String conditions = "";
		Form form = (Form) category.getParent();
		conditions += simpleFormCondition(form);

		if (getTreeObjectName(category) == null) {
			putTreeObjectName(category, category.getUniqueNameReadable().toString());
			conditions += "	$" + category.getUniqueNameReadable().toString() + " : Category( isScoreSet('"
					+ customVariable.getVariable().getName() + "')) from $" + form.getUniqueNameReadable().toString()
					+ ".getCategory('" + category.getName() + "') \n";
		}
		return conditions;
	}

	private static String simpleCategoryCustomVariableConditionsWithoutScore(
			ExpressionValueCustomVariable customVariable) {
		Category category = (Category) customVariable.getReference();
		String conditions = "";
		Form form = (Form) category.getParent();
		conditions += simpleFormCondition(form);

		if (getTreeObjectName(category) == null) {
			putTreeObjectName(category, category.getUniqueNameReadable().toString());
			conditions += "	$" + category.getUniqueNameReadable().toString() + " : Category() from $"
					+ form.getUniqueNameReadable().toString() + ".getCategory('" + category.getName() + "') \n";
		}
		return conditions;
	}

	private static String simpleFormCondition(Form form) {
		String droolsRule = "";
		if (getTreeObjectName(form) == null) {
			putTreeObjectName(form, form.getUniqueNameReadable().toString());
			droolsRule += "	$" + form.getUniqueNameReadable().toString()
					+ " : SubmittedForm() from $droolsForm.getSubmittedForm() \n";
		}
		return droolsRule;
	}

	private static String simpleGroupConditions(Group group) {
		String conditions = "";
		TreeObject groupParent = group.getParent();
		if (groupParent instanceof Category) {
			Category category = (Category) groupParent;
			conditions += simpleCategoryConditions(category);
			if (getTreeObjectName(group) == null) {
				putTreeObjectName(group, group.getUniqueNameReadable().toString());
				conditions += "	$" + group.getUniqueNameReadable().toString() + " : Group() from $"
						+ category.getUniqueNameReadable().toString() + ".getGroup('" + group.getName() + "') \n";
			}
		} else if (groupParent instanceof Group) {
			Group groupGroup = (Group) groupParent;
			conditions += simpleGroupConditions(groupGroup);
			if (getTreeObjectName(group) == null) {
				putTreeObjectName(group, group.getUniqueNameReadable().toString());
				conditions += "	$" + group.getUniqueNameReadable().toString() + " : Group() from $"
						+ groupGroup.getUniqueNameReadable().toString() + ".getGroup('" + group.getName() + "') \n";
			}
		}
		return conditions;
	}

	private static String simpleGroupCustomVariableConditions(ExpressionValueCustomVariable customVariable) {
		Group group = (Group) customVariable.getReference();
		String conditions = "";
		TreeObject groupParent = group.getParent();
		if (groupParent instanceof Category) {
			Category category = (Category) groupParent;
			conditions += simpleCategoryConditions(category);
			if (getTreeObjectName(group) == null) {
				putTreeObjectName(group, group.getUniqueNameReadable().toString());
				conditions += "	$" + group.getUniqueNameReadable().toString() + " : Group( isScoreSet('"
						+ customVariable.getVariable().getName() + "')) from $"
						+ category.getUniqueNameReadable().toString() + ".getGroup('" + group.getName() + "') \n";
			}
		} else if (groupParent instanceof Group) {
			Group groupGroup = (Group) groupParent;
			conditions += simpleGroupConditions(groupGroup);
			if (getTreeObjectName(group) == null) {
				putTreeObjectName(group, group.getUniqueNameReadable().toString());
				conditions += "	$" + group.getUniqueNameReadable().toString() + " : Group( isScoreSet('"
						+ customVariable.getVariable().getName() + "')) from $"
						+ groupGroup.getUniqueNameReadable().toString() + ".getGroup('" + group.getName() + "') \n";
			}
		}
		return conditions;
	}

	private static String simpleGroupCustomVariableConditionsWithoutScore(ExpressionValueCustomVariable customVariable) {
		Group group = (Group) customVariable.getReference();
		String conditions = "";
		TreeObject groupParent = group.getParent();
		if (groupParent instanceof Category) {
			Category category = (Category) groupParent;
			conditions += simpleCategoryConditions(category);
			if (getTreeObjectName(group) == null) {
				putTreeObjectName(group, group.getUniqueNameReadable().toString());
				conditions += "	$" + group.getUniqueNameReadable().toString() + " : Group() from $"
						+ category.getUniqueNameReadable().toString() + ".getGroup('" + group.getName() + "') \n";
			}
		} else if (groupParent instanceof Group) {
			Group groupGroup = (Group) groupParent;
			conditions += simpleGroupConditions(groupGroup);
			if (getTreeObjectName(group) == null) {
				putTreeObjectName(group, group.getUniqueNameReadable().toString());
				conditions += "	$" + group.getUniqueNameReadable().toString() + " : Group() from $"
						+ groupGroup.getUniqueNameReadable().toString() + ".getGroup('" + group.getName() + "') \n";
			}
		}
		return conditions;
	}

	private static String simpleQuestionConditions(Question question) {
		String conditions = "";
		TreeObject questionParent = question.getParent();
		if (questionParent instanceof Category) {
			Category category = (Category) questionParent;
			conditions += simpleCategoryConditions(category);

			if (getTreeObjectName(question) == null) {
				putTreeObjectName(question, question.getUniqueNameReadable().toString());
				conditions += "	$" + question.getUniqueNameReadable().toString() + " : Question(getTag() == '"
						+ question.getName() + "') from $" + category.getUniqueNameReadable().toString()
						+ ".getQuestions() \n";
			}

		} else if (questionParent instanceof Group) {
			Group group = (Group) questionParent;
			conditions += simpleGroupConditions(group);

			if (getTreeObjectName(question) == null) {
				putTreeObjectName(question, question.getUniqueNameReadable().toString());
				conditions += "	$" + question.getUniqueNameReadable().toString() + " : Question(getTag() == '"
						+ question.getName() + "') from $" + group.getUniqueNameReadable().toString()
						+ ".getQuestions() \n";
			}
		}
		return conditions;
	}

	private static String simpleQuestionCustomVariableConditions(ExpressionValueCustomVariable customVariable) {
		Question question = (Question) customVariable.getReference();
		String conditions = "";
		TreeObject questionParent = question.getParent();
		if (questionParent instanceof Category) {
			Category category = (Category) questionParent;
			conditions += simpleCategoryConditions(category);

			if (getTreeObjectName(question) == null) {
				putTreeObjectName(question, question.getUniqueNameReadable().toString());
				conditions += "	$" + question.getUniqueNameReadable().toString() + " : Question(getTag() == '"
						+ question.getName() + "', isScoreSet('" + customVariable.getVariable().getName()
						+ "' )) from $" + category.getUniqueNameReadable().toString() + ".getQuestions() \n";
			}

		} else if (questionParent instanceof Group) {
			Group group = (Group) questionParent;
			conditions += simpleGroupConditions(group);

			if (getTreeObjectName(question) == null) {
				putTreeObjectName(question, question.getUniqueNameReadable().toString());
				conditions += "	$" + question.getUniqueNameReadable().toString() + " : Question(getTag() == '"
						+ question.getName() + "', isScoreSet('" + customVariable.getVariable().getName()
						+ "')) from $" + group.getUniqueNameReadable().toString() + ".getQuestions() \n";
			}
		}
		return conditions;
	}

	private static String simpleQuestionCustomVariableConditionsWithoutScore(
			ExpressionValueCustomVariable customVariable) {
		Question question = (Question) customVariable.getReference();
		String conditions = "";
		TreeObject questionParent = question.getParent();
		if (questionParent instanceof Category) {
			Category category = (Category) questionParent;
			conditions += simpleCategoryConditions(category);

			if (getTreeObjectName(question) == null) {
				putTreeObjectName(question, question.getUniqueNameReadable().toString());
				conditions += "	$" + question.getUniqueNameReadable().toString() + " : Question(getTag() == '"
						+ question.getName() + "') from $" + category.getUniqueNameReadable().toString()
						+ ".getQuestions() \n";
			}

		} else if (questionParent instanceof Group) {
			Group group = (Group) questionParent;
			conditions += simpleGroupConditions(group);

			if (getTreeObjectName(question) == null) {
				putTreeObjectName(question, question.getUniqueNameReadable().toString());
				conditions += "	$" + question.getUniqueNameReadable().toString() + " : Question(getTag() == '"
						+ question.getName() + "') from $" + group.getUniqueNameReadable().toString()
						+ ".getQuestions() \n";
			}
		}
		return conditions;
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
	 */
	private static String treeObjectScoreEqualsValueString(ExpressionValueCustomVariable var,
			ExpressionOperatorLogic expressionOperatorLogic, ExpressionValueString valueNumber) {
		String ruleCore = "";

		TreeObject scope = var.getReference();
		String varName = var.getVariable().getName();

		if (scope instanceof Form) {
			putTreeObjectName(scope, scope.getUniqueNameReadable().toString());
			ruleCore += "	$" + scope.getUniqueNameReadable().toString() + " : SubmittedForm(isScoreSet('" + varName
					+ "'), getVariableValue('" + varName + "') " + expressionOperatorLogic.getValue().toString() + " '"
					+ valueNumber.getValue() + "') from $droolsForm.getSubmittedForm() \n";

		} else if (scope instanceof Category) {
			TreeObject form = scope.getParent();
			putTreeObjectName(scope, scope.getUniqueNameReadable().toString());
			ruleCore += simpleFormCondition((Form) form);
			ruleCore += "	$" + scope.getUniqueNameReadable().toString() + " : Category( isScoreSet('" + varName
					+ "'), getVariableValue('" + varName + "') " + expressionOperatorLogic.getValue().toString() + " '"
					+ valueNumber.getValue() + "') from $" + form.getUniqueNameReadable().toString() + ".getCategory('"
					+ scope.getName() + "') \n";

		} else if (scope instanceof Group) {
			TreeObject groupParent = scope.getParent();
			putTreeObjectName(scope, scope.getUniqueNameReadable().toString());
			if (groupParent instanceof Category) {
				ruleCore += simpleCategoryConditions((Category) groupParent);
			} else if (groupParent instanceof Group) {
				ruleCore += simpleGroupConditions((Group) groupParent);
			}
			ruleCore += "	$" + scope.getUniqueNameReadable().toString() + " : Group( isScoreSet('" + varName
					+ "'), getVariableValue('" + varName + "') " + expressionOperatorLogic.getValue().toString() + " '"
					+ valueNumber.getValue() + "' ) from $" + groupParent.getUniqueNameReadable().toString()
					+ ".getGroup('" + scope.getName() + "') \n";

		} else if (scope instanceof Question) {
			TreeObject questionParent = scope.getParent();
			putTreeObjectName(scope, scope.getUniqueNameReadable().toString());
			if (questionParent instanceof Category) {
				ruleCore += simpleCategoryConditions((Category) questionParent);
			} else if (questionParent instanceof Group) {
				ruleCore += simpleGroupConditions((Group) questionParent);
			}
			ruleCore += "	$" + scope.getUniqueNameReadable().toString() + " : Question( isScoreSet('" + varName
					+ "'), getVariableValue('" + varName + "') " + expressionOperatorLogic.getValue().toString() + " '"
					+ valueNumber.getValue() + "' ) from $" + questionParent.getUniqueNameReadable().toString()
					+ ".getQuestions() \n";
		}
		return ruleCore;
	}

	/**
	 * Parse conditions like => Score (logic operator (==, <=, <, >=, >)) value. <br>
	 * Create drools rule like => Category(isScoreSet('cScore'),
	 * getVariablevalue('cScore') == value )
	 * 
	 * @param conditions
	 * @return LHS of the rule
	 */
	private static String treeObjectScoreLogicOperatorValueExpression(ExpressionValueCustomVariable var,
			ExpressionOperatorLogic operator, Expression value) {
		String ruleCore = "";

		String droolsValue = "";
		if (value instanceof ExpressionValueTreeObjectReference) {
			ruleCore += checkVariableAssignation(value);
			droolsValue = getDroolsVariableValueFromExpressionValueTreeObject((ExpressionValueTreeObjectReference) value);

		} else {
			droolsValue = ((ExpressionValue) value).getValue().toString();
		}

		TreeObject scope = var.getReference();
		String varName = var.getVariable().getName();

		switch (var.getVariable().getType()) {
		case NUMBER:
			if (scope instanceof Form) {
				putTreeObjectName(scope, scope.getUniqueNameReadable().toString());
				ruleCore += "	$" + scope.getUniqueNameReadable().toString() + " : SubmittedForm(isScoreSet('" + varName
						+ "'), getVariableValue('" + varName + "') " + operator.getValue().toString() + " "
						+ droolsValue + ") from $droolsForm.getSubmittedForm() \n";

			} else if (scope instanceof Category) {
				TreeObject form = scope.getParent();
				putTreeObjectName(scope, scope.getUniqueNameReadable().toString());
				ruleCore += simpleFormCondition((Form) form);
				ruleCore += "	$" + scope.getUniqueNameReadable().toString() + " : Category( isScoreSet('" + varName
						+ "'), getVariableValue('" + varName + "') " + operator.getValue().toString() + " "
						+ droolsValue + ") from $" + form.getUniqueNameReadable().toString() + ".getCategory('"
						+ scope.getName() + "') \n";

			} else if (scope instanceof Group) {
				TreeObject groupParent = scope.getParent();
				putTreeObjectName(scope, scope.getUniqueNameReadable().toString());
				if (groupParent instanceof Category) {
					ruleCore += simpleCategoryConditions((Category) groupParent);
				} else if (groupParent instanceof Group) {
					ruleCore += simpleGroupConditions((Group) groupParent);
				}
				ruleCore += "	$" + scope.getUniqueNameReadable().toString() + " : Group( isScoreSet('" + varName
						+ "'), getVariableValue('" + varName + "') " + operator.getValue().toString() + " "
						+ droolsValue + " ) from $" + groupParent.getUniqueNameReadable().toString() + ".getGroup('"
						+ scope.getName() + "') \n";

			} else if (scope instanceof Question) {
				TreeObject questionParent = scope.getParent();
				putTreeObjectName(scope, scope.getUniqueNameReadable().toString());
				if (questionParent instanceof Category) {
					ruleCore += simpleCategoryConditions((Category) questionParent);
				} else if (questionParent instanceof Group) {
					ruleCore += simpleGroupConditions((Group) questionParent);
				}
				ruleCore += "	$" + scope.getUniqueNameReadable().toString() + " : Question( isScoreSet('" + varName
						+ "'), getVariableValue('" + varName + "') " + operator.getValue().toString() + " "
						+ droolsValue + " ) from $" + questionParent.getUniqueNameReadable().toString()
						+ ".getQuestions() \n";
			}
			break;
		case DATE:
		case STRING:
			if (scope instanceof Form) {
				putTreeObjectName(scope, scope.getUniqueNameReadable().toString());
				ruleCore += "	$" + scope.getUniqueNameReadable().toString() + " : SubmittedForm(isScoreSet('" + varName
						+ "'), getVariableValue('" + varName + "') " + operator.getValue().toString() + " "
						+ droolsValue + ") from $droolsForm.getSubmittedForm() \n";

			} else if (scope instanceof Category) {
				TreeObject form = scope.getParent();
				putTreeObjectName(scope, scope.getUniqueNameReadable().toString());
				ruleCore += simpleFormCondition((Form) form);
				ruleCore += "	$" + scope.getUniqueNameReadable().toString() + " : Category( isScoreSet('" + varName
						+ "'), getVariableValue('" + varName + "') " + operator.getValue().toString() + " "
						+ droolsValue + ") from $" + form.getUniqueNameReadable().toString() + ".getCategory('"
						+ scope.getName() + "') \n";

			} else if (scope instanceof Group) {
				TreeObject groupParent = scope.getParent();
				putTreeObjectName(scope, scope.getUniqueNameReadable().toString());
				if (groupParent instanceof Category) {
					ruleCore += simpleCategoryConditions((Category) groupParent);
				} else if (groupParent instanceof Group) {
					ruleCore += simpleGroupConditions((Group) groupParent);
				}
				ruleCore += "	$" + scope.getUniqueNameReadable().toString() + " : Group( isScoreSet('" + varName
						+ "'), getVariableValue('" + varName + "') " + operator.getValue().toString() + " "
						+ droolsValue + " ) from $" + groupParent.getUniqueNameReadable().toString() + ".getGroup('"
						+ scope.getName() + "') \n";

			} else if (scope instanceof Question) {
				TreeObject questionParent = scope.getParent();
				putTreeObjectName(scope, scope.getUniqueNameReadable().toString());
				if (questionParent instanceof Category) {
					ruleCore += simpleCategoryConditions((Category) questionParent);
				} else if (questionParent instanceof Group) {
					ruleCore += simpleGroupConditions((Group) questionParent);
				}
				ruleCore += "	$" + scope.getUniqueNameReadable().toString() + " : Question( isScoreSet('" + varName
						+ "'), getVariableValue('" + varName + "') " + operator.getValue().toString() + " "
						+ droolsValue + " ) from $" + questionParent.getUniqueNameReadable().toString()
						+ ".getQuestions() \n";
			}
		}
		return ruleCore;
	}
}
package com.biit.abcd.core.drools.rules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.ss.formula.functions.FinanceLib;

import com.biit.abcd.core.drools.prattparser.ExpressionChainParser;
import com.biit.abcd.core.drools.prattparser.ParseException;
import com.biit.abcd.core.drools.prattparser.Parser;
import com.biit.abcd.core.drools.prattparser.visitor.ITreeElement;
import com.biit.abcd.core.drools.prattparser.visitor.TreeElementMathExpressionVisitor;
import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
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
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorMath;
import com.biit.abcd.persistence.entity.expressions.ExpressionValue;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueGenericCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueNumber;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueString;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueSystemDate;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.form.TreeObject;

public class DroolsParser {

	private boolean cleaningNeeded = true;
	private HashMap<TreeObject, String> treeObjectDroolsname;

	public DroolsParser() {
		treeObjectDroolsname = new HashMap<TreeObject, String>();
	}

	/**
	 * Adds condition rows to the rule that manages the assignation of a variable in the action<br>
	 * 
	 * @param variable
	 *            variable to be added to the LHS of the rule
	 * @return LHS of the rule modified with the new variables
	 */
	private String addConditionVariable(TreeObject treeObject) {
		String ruleCore = "";

		// Add the variable assignation to the rule
		if (treeObject != null) {
			if (treeObject instanceof Category) {
				ruleCore += this.simpleCategoryConditions((Category) treeObject);

			} else if (treeObject instanceof Group) {
				ruleCore += this.simpleGroupConditions((Group) treeObject);

			} else if (treeObject instanceof Question) {
				ruleCore += this.simpleQuestionConditions((Question) treeObject);
			}
		}
		return ruleCore;
	}

	private String andOperator(List<Expression> expressions) {
		String result = "";

		ExpressionChain leftChain = (ExpressionChain) expressions.get(0);
		ExpressionChain rightChain = (ExpressionChain) expressions.get(2);

		// result += "(\n";
		result += this.processParserResult(leftChain);
		// result += "and\n";
		result += this.processParserResult(rightChain);
		// result += ")\n";

		return result;
	}

	/**
	 * Parse conditions like => Question IN(answer1, ...) <br>
	 * Create drools rule like => Question(getAnswer() in (answer1, ...))
	 * 
	 * @param conditions
	 * @return LHS of the rule
	 */
	private String answersInQuestionCondition(List<Expression> conditions) {
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
							// TODO
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
				this.putTreeObjectName(leftReference, leftReference.getUniqueNameReadable().toString());
				// Check the parent
				if (leftReferenceParent instanceof Form) {
					droolsConditions += this.simpleFormCondition((Form) leftReferenceParent);
				} else if (leftReferenceParent instanceof Category) {
					droolsConditions += this.simpleCategoryConditions((Category) leftReferenceParent);
				} else if (leftReferenceParent instanceof Group) {
					droolsConditions += this.simpleGroupConditions((Group) leftReferenceParent);
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
	 * Parse actions like => Score = stringValue || Score = numberValue || Score = Function (parameters ...)<br>
	 * Create drools rule like => setVariableValue(scopeOfVariable, variableName, stringVariableValue )
	 * 
	 * @param actions
	 *            the action of the rule being parsed
	 * @return the RHS of the rule
	 */
	private String assignationAction(ExpressionChain actionChain) {
		String ruleCore = "";
		List<Expression> actions = actionChain.getExpressions();
		ExpressionValueCustomVariable var = (ExpressionValueCustomVariable) ((ExpressionChain) actions.get(0))
				.getExpressions().get(0);
		// Check if the reference exists in the rule, if not, it creates a new
		// reference
		ruleCore += this.checkVariableAssignation(var);

		ruleCore += RulesUtils.getThenRuleString();

		Object auxVal = actions.get(1);
		if ((auxVal instanceof ExpressionChain)) {
			auxVal = ((ExpressionChain) auxVal).getExpressions().get(0);
			ExpressionValue value = (ExpressionValue) auxVal;
			if (auxVal instanceof ExpressionValueString) {

				String variableValue = value.getValue().toString().replace("\"", "\\\"").replace("\'", "\\\'");

				ruleCore += "	$" + this.getTreeObjectName(var.getReference()) + ".setVariableValue('"
						+ var.getVariable().getName() + "', '" + variableValue + "');\n";
				ruleCore += "	AbcdLogger.debug(\"DroolsRule\", \"Variable set (" + var.getReference().getName() + ", "
						+ var.getVariable().getName() + ", " + variableValue + ")\");\n";

			} else if (auxVal instanceof ExpressionValueNumber) {
				ruleCore += "	$" + this.getTreeObjectName(var.getReference()) + ".setVariableValue('"
						+ var.getVariable().getName() + "', " + value.getValue() + ");\n";
				ruleCore += "	AbcdLogger.debug(\"DroolsRule\", \"Variable set (" + var.getReference().getName() + ", "
						+ var.getVariable().getName() + ", " + value.getValue() + ")\");\n";
			}
		} else if (auxVal instanceof ExpressionFunction) {
			switch (((ExpressionFunction) auxVal).getValue()) {
			case PMT:
				String value = this.calculatePMT(actions.subList(2, 5));
				ruleCore += "	$" + this.getTreeObjectName(var.getReference()) + ".setVariableValue('"
						+ var.getVariable().getName() + "', " + value + ");\n";
				ruleCore += "	AbcdLogger.debug(\"DroolsRule\", \"Variable set (" + var.getReference().getName() + ", "
						+ var.getVariable().getName() + ", " + value + ")\");\n";
				break;
			default:
				break;
			}
		}
		return ruleCore;
	}

	/**
	 * Receives a list with the three parameters needed to calculate the PMT function, (Rate, Months, Present value)<br>
	 * We make use of the Apache POI lib<br>
	 * For more information: http://poi.apache.org/apidocs/org/apache/poi/ss/formula /functions/FinanceLib.html
	 * 
	 * @param actions
	 * @return
	 */
	private String calculatePMT(List<Expression> actions) {
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

	private String checkCustomVariableAssignation(ExpressionValueCustomVariable variable) {
		if (variable != null) {
			TreeObject treeObject = variable.getReference();
			if (this.getTreeObjectName(variable.getReference()) == null) {
				// The variable don't exists and can't have a value assigned
				String ruleCore = "";

				// Add the variable assignation to the rule
				if (treeObject != null) {
					if (treeObject instanceof Category) {
						ruleCore += this.simpleCategoryCustomVariableConditions(variable);

					} else if (treeObject instanceof Group) {
						ruleCore += this.simpleGroupCustomVariableConditions(variable);

					} else if (treeObject instanceof Question) {
						ruleCore += this.simpleQuestionCustomVariableConditions(variable);
					}
				}
				return ruleCore;
			}
		}
		return "";
	}

	/**
	 * Checks the existence of a binding in drools with the the reference of the variable passed If there is no binding,
	 * creates a new one (i.e. $var : Question() ...)
	 * 
	 * @param expValVariable
	 */
	private String checkVariableAssignation(Expression expression) {
		if (expression instanceof ExpressionValueCustomVariable) {
			return this.checkCustomVariableAssignation((ExpressionValueCustomVariable) expression);
		} else if (expression instanceof ExpressionValueTreeObjectReference) {
			return this.checkVariableAssignation((ExpressionValueTreeObjectReference) expression);
		}
		return "";
	}

	private String checkVariableAssignation(ExpressionValueTreeObjectReference variable) {
		return this.checkVariableAssignation(variable.getReference());
	}

	private String checkVariableAssignation(TreeObject treeObject) {
		if (treeObject != null) {
			if (this.getTreeObjectName(treeObject) == null) {
				// The variable don't exists and can't have a value assigned
				return this.addConditionVariable(treeObject);
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
	 */
	public String createDroolsRule(List<Rule> rules) throws RuleNotImplementedException {
		String parsedText = "";
		for (Rule rule : rules) {
			parsedText += createDroolsRule(rule.generateCopy());
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
	 */
	private String createDroolsRule(Rule rule) throws RuleNotImplementedException {

		if (rule == null) {
			return "";
		}

		treeObjectDroolsname = new HashMap<TreeObject, String>();
		String result = "";

		// Condition to avoid putting the drools form in the recursive generic
		// expressions. It is introduced in everything else
		if (rule.getActionChain() == null
				|| !(rule.getActionChain().getExpressions().get(0) instanceof ExpressionValueGenericCustomVariable)) {
			result += "\t$droolsForm: DroolsForm()\n";
		}

		// Obtain conditions if exists.
		if ((rule.getConditionChain() != null) && (rule.getConditionChain().getExpressions() != null)
				&& (!rule.getConditionChain().getExpressions().isEmpty())) {
			result += parseConditions(rule.getConditionChain());
		}
		if ((rule.getActionChain() != null) && (rule.getActionChain().getExpressions() != null)
				&& (!rule.getActionChain().getExpressions().isEmpty())) {
//			// No conditions: expression rule
//			if ((rule.getConditionChain() == null) || (rule.getConditionChain().getExpressions() == null)
//					|| rule.getConditionChain().getExpressions().isEmpty()) {
//				result += parseExpressions(rule.getActionChain());
//			}
//			// Normal rule with actions and conditions. Conditions are obtained before, obtain actions.
//			else {
//				result += parseActions(rule.getActionChain());
//			}
			
			result += parseActions(rule.getActionChain());
		}
		if (this.cleaningNeeded) {
			result = RulesUtils.newRemoveDuplicateLines(result);
			result = RulesUtils.checkForDuplicatedVariables(result);
			result = RulesUtils.removeExtraParenthesis(result);
		}
		return result;
	}

	/**
	 * Parse conditions like => Question BETWEEN(Answer1, answer2). <br>
	 * The values inside the between must be always numbers <br>
	 * Create drools rule like => Question( (getAnswer() >= answer.getValue()) && (getAnswer() <= answer.getValue()))
	 * 
	 * @param conditions
	 * @return LHS of the rule
	 */
	private String customVariableBetweenAnswersCondition(List<Expression> conditions) {

		String droolsConditions = "";
		List<Expression> operatorLeft = ((ExpressionChain) conditions.get(0)).getExpressions();
		if ((operatorLeft.size() == 1) && (operatorLeft.get(0) instanceof ExpressionValueCustomVariable)) {

			ExpressionValueCustomVariable leftVariable = (ExpressionValueCustomVariable) operatorLeft.get(0);
			if (leftVariable != null) {

				List<Expression> firstExpressionValue = ((ExpressionChain) conditions.get(2)).getExpressions();
				List<Expression> secondExpressionValue = ((ExpressionChain) conditions.get(3)).getExpressions();
				if ((firstExpressionValue.size() == 1)
						&& (firstExpressionValue.get(0) instanceof ExpressionValueNumber)
						&& (secondExpressionValue.size() == 1)
						&& (secondExpressionValue.get(0) instanceof ExpressionValueNumber)) {
					// Get the values of the between expression
					Double value1 = ((ExpressionValueNumber) firstExpressionValue.get(0)).getValue();
					Double value2 = ((ExpressionValueNumber) secondExpressionValue.get(0)).getValue();
					if ((value1 != null) && (value2 != null)) {

						TreeObject leftReferenceParent = leftVariable.getReference().getParent();
						if (leftReferenceParent != null) {
							this.putTreeObjectName(leftVariable.getReference(), leftVariable.getUniqueNameReadable()
									.toString());
							// Check the parent
							if (leftReferenceParent instanceof Form) {
								droolsConditions += this.simpleFormCondition((Form) leftReferenceParent);
							} else if (leftReferenceParent instanceof Category) {
								droolsConditions += this.simpleCategoryConditions((Category) leftReferenceParent);
							} else if (leftReferenceParent instanceof Group) {
								droolsConditions += this.simpleGroupConditions((Group) leftReferenceParent);
							}
						}
						if (leftVariable.getReference() instanceof Question) {
							Question leftQuestion = (Question) leftVariable.getReference();
							switch (leftQuestion.getAnswerType()) {
							case RADIO:
							case MULTI_CHECKBOX:
								// The flow don't get here.
								// It does not
								break;
							case INPUT:
								switch (leftQuestion.getAnswerFormat()) {
								case NUMBER:
									droolsConditions += "	$" + leftQuestion.getUniqueNameReadable().toString()
											+ " : Question(getTag() == '" + leftQuestion.getName() + "', getAnswer('"
											+ getTreeObjectAnswerType(leftQuestion) + "') >= " + value1 + " && < "
											+ value2 + ") from $"
											+ leftReferenceParent.getUniqueNameReadable().toString()
											+ ".getQuestions() \n";
									break;
								case POSTAL_CODE:
								case TEXT:
									// The system should not get here because it
									// is forbidden by the jexeval
									break;
								case DATE:
									String betweenDate = "";
									if (leftVariable.getUnit() != null) {
										switch (leftVariable.getUnit()) {
										case YEARS:
											betweenDate = "DateUtils.returnYearsDistanceFromDate(getAnswer('"
													+ AnswerFormat.DATE.toString() + "')) >= " + value1.intValue()
													+ " && < " + value2.intValue();
											break;
										case MONTHS:
											betweenDate = "DateUtils.returnMonthsDistanceFromDate(getAnswer('"
													+ AnswerFormat.DATE.toString() + "')) >= " + value1.intValue()
													+ " && < " + value2.intValue();
											break;
										case DAYS:
											betweenDate = "DateUtils.returnDaysDistanceFromDate(getAnswer('"
													+ AnswerFormat.DATE.toString() + "')) >= " + value1.intValue()
													+ " && < " + value2.intValue();
											break;
										case DATE:
											// TODO
											// betweenDate = "getAnswer("
											// + AnswerFormat.DATE.toString()
											// +
											// ") instanceof Date, DateUtils.returnDaysDistanceFromDate(getAnswer("
											// + AnswerFormat.DATE.toString() +
											// ")) >= " + value1.intValue()
											// + " && < " + value2.intValue();
											break;
										}
									} else {
										AbcdLogger.warning(this.getClass().getName(),
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
							String varName = leftVariable.getVariable().getName();

							switch (leftVariable.getVariable().getScope()) {
							case FORM:
								this.putTreeObjectName(leftVariable.getReference(), leftVariable.getReference()
										.getUniqueNameReadable().toString());
								droolsConditions += "	$"
										+ leftVariable.getReference().getUniqueNameReadable().toString()
										// + " : SubmittedForm(isScoreSet('" +
										// varName +
										// "'), getNumberVariableValue('"
										+ " : SubmittedForm(isScoreSet('" + varName + "'), getVariableValue('"
										+ varName + "') >= " + value1 + " && < " + value2
										+ ") from $droolsForm.getSubmittedForm() \n";
								break;
							case CATEGORY:
								droolsConditions += "	$"
										+ leftVariable.getReference().getUniqueNameReadable().toString()
										+ " : Category( getTag() == '"
										+ leftVariable.getReference().getName()
										// + "', isScoreSet('" + varName +
										// "'), getNumberVariableValue('" +
										// varName
										+ "', isScoreSet('" + varName + "'), getVariableValue('" + varName + "') >= "
										+ value1 + " && < " + value2 + ") from $"
										+ leftReferenceParent.getUniqueNameReadable().toString()
										+ ".getCategories() \n";
								break;
							case GROUP:
								droolsConditions += "	$"
										+ leftVariable.getReference().getUniqueNameReadable().toString()
										+ " : Group( getTag() == '"
										+ leftVariable.getReference().getName()
										// + "', isScoreSet('" + varName +
										// "'), getNumberVariableValue('" +
										// varName
										+ "', isScoreSet('" + varName + "'), getVariableValue('" + varName + "') >= "
										+ value1 + " && < " + value2 + ") from $"
										+ leftReferenceParent.getUniqueNameReadable().toString() + ".getGroups() \n";
								break;
							default:
								break;
							}
						}
					}
				}
			}
		}
		return droolsConditions;
	}

	private String equalsNotEqualsOperator(List<Expression> expressions, AvailableOperator availableOperator) {
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
				droolsConditions += this.questionAnswerEqualsCondition((Question) treeObject1, (Answer) treeObject2,
						availableOperator);
			}
		}
		// TreeObject.score == Value
		else if ((operatorLeft.size() == 1) && (operatorLeft.get(0) instanceof ExpressionValueCustomVariable)
				&& (operatorRight.size() == 1) && (operatorRight.get(0) instanceof ExpressionValueNumber)) {

			droolsConditions += this.treeObjectScoreLogicOperatorValueNumber(
					(ExpressionValueCustomVariable) operatorLeft.get(0), (ExpressionOperatorLogic) expressions.get(1),
					(ExpressionValueNumber) operatorRight.get(0));
		}
		// TreeObject.score == String
		else if ((operatorLeft.size() == 1) && (operatorLeft.get(0) instanceof ExpressionValueCustomVariable)
				&& (operatorRight.size() == 1) && (operatorRight.get(0) instanceof ExpressionValueString)) {
			droolsConditions += this.treeObjectScoreEqualsValueString(
					(ExpressionValueCustomVariable) operatorLeft.get(0), (ExpressionOperatorLogic) expressions.get(1),
					(ExpressionValueString) operatorRight.get(0));
		}
		// Question INPUT
		else if ((operatorLeft.size() == 1) && (operatorLeft.get(0) instanceof ExpressionValueTreeObjectReference)
				&& (operator instanceof ExpressionOperatorLogic) && (operatorRight.size() == 1)
				&& (operatorRight.get(0) instanceof ExpressionValueNumber)) {
			TreeObject leftTreeObject = ((ExpressionValueTreeObjectReference) operatorLeft.get(0)).getReference();
			Double value = ((ExpressionValueNumber) operatorRight.get(0)).getValue();

			TreeObject leftTreeObjectParent = leftTreeObject.getParent();
			this.putTreeObjectName(leftTreeObject, leftTreeObject.getUniqueNameReadable().toString());
			// Check the parent
			if (leftTreeObjectParent instanceof Form) {
				droolsConditions += this.simpleFormCondition((Form) leftTreeObjectParent);
			} else if (leftTreeObjectParent instanceof Category) {
				droolsConditions += this.simpleCategoryConditions((Category) leftTreeObjectParent);
			} else if (leftTreeObjectParent instanceof Group) {
				droolsConditions += this.simpleGroupConditions((Group) leftTreeObjectParent);
			}

			if (leftTreeObject instanceof Question) {
				switch (((ExpressionValueTreeObjectReference) operatorLeft.get(0)).getUnit()) {
				case YEARS:
					droolsConditions += this.questionDateYearsOperatorValueNumber(leftTreeObject, value,
							((ExpressionOperatorLogic) operator).getValue());
					break;
				case MONTHS:
					droolsConditions += this.questionDateMonthsOperatorValueNumber(leftTreeObject, value,
							((ExpressionOperatorLogic) operator).getValue());
					break;
				case DAYS:
					droolsConditions += this.questionDateDaysOperatorValueNumber(leftTreeObject, value,
							((ExpressionOperatorLogic) operator).getValue());
					break;
				case DATE:
					// TODO
					break;
				}
			}
		}
		return droolsConditions;
	}

	/**
	 * Generic expression parser.<br>
	 * Works like the expression parser but allows generic variables <br>
	 * 
	 * @param actions
	 *            the expression being parsed
	 * @param forkConditions
	 * @return the rule
	 * @throws RuleNotImplementedException
	 */
	private String genericAssignationFunctionAction(ExpressionChain actions, String forkConditions)
			throws RuleNotImplementedException {
		String ruleCore = "";
		// we have to generate a set of rules defined by the generic variable
		ruleCore += this.genericRuleSet(actions, forkConditions);
		this.cleaningNeeded = false;
		return ruleCore;
	}

	private String genericFunctionParameters(String ruleCore, List<Expression> actions) {
		// LHS
		// We will have some expression of type Category.score = (Min | Max |
		// Avg) of some values
		ExpressionValueCustomVariable variableToCalculate = (ExpressionValueCustomVariable) actions.get(0);
		// The rule is different if the variable to assign is a Form, a Category
		// a Group or a Question
		TreeObject leftTreeObject = variableToCalculate.getReference();
		int varIndex = 1;
		String scopeClass = "";
		TreeObject parent = null;
		CustomVariable customVariable = null;
		for (int i = 3; i < actions.size(); i++) {
			Expression expression = actions.get(i);
			if (expression instanceof ExpressionValueCustomVariable) {
				ExpressionValueCustomVariable aux = (ExpressionValueCustomVariable) expression;
				TreeObject to = aux.getReference();
				customVariable = aux.getVariable();
				if (to instanceof Question) {
					scopeClass = "Question";
					parent = to.getParent();
				} else if (to instanceof Group) {
					scopeClass = "Group";
					parent = to.getParent();
				} else if (to instanceof Category) {
					scopeClass = "Category";
					parent = to.getParent();
				}
				if (varIndex == 1) {
					ruleCore += "	$var : List( size>0 ) from collect( " + scopeClass + "(isScoreSet('"
							+ customVariable.getName() + "'), getTag() == '" + to.getName() + "' || ";
				} else {
					ruleCore += "== '" + to.getName() + "' || ";
				}
				varIndex++;
			}
			// Generic variable inside the function
			else if (expression instanceof ExpressionValueGenericCustomVariable) {
				ExpressionValueGenericCustomVariable expValGenericCustomVariable = (ExpressionValueGenericCustomVariable) expression;
				List<TreeObject> treeObjects = null;
				customVariable = expValGenericCustomVariable.getVariable();
				switch (expValGenericCustomVariable.getType()) {
				case CATEGORY:
					if (leftTreeObject instanceof Form) {
						scopeClass = "Category";
						treeObjects = leftTreeObject.getAll(Category.class);
					}
					break;
				case GROUP:
					// If it has more than one parameter in the function like =>
					// Cat = Fuc( group , quest )
					if (actions.size() > 5) {
						return maxMinAvgAssignationFunctionActionWithSeveralGenerics(ruleCore, actions);
					} else if (leftTreeObject instanceof Category) {
						scopeClass = "Group";
						// treeObjects = leftTreeObject.getAll(Group.class);
						// We only want the questions for the category
						if (leftTreeObject.getChildren() != null && !leftTreeObject.getChildren().isEmpty()) {
							treeObjects = new ArrayList<TreeObject>();
							for (TreeObject child : leftTreeObject.getChildren()) {
								if (child instanceof Group) {
									treeObjects.add(child);
								}
							}
						}

						// // To guarantee the nested groups variable
						// assignation
						// if (treeObjects != null && !treeObjects.isEmpty()) {
						// for (TreeObject auxTreeobject : treeObjects) {
						// ruleCore += checkVariableAssignation(auxTreeobject);
						// }
						// }

					}
					break;
				case QUESTION_CATEGORY:
					// If it has more than one parameter in the function like =>
					// Cat = Fuc( group , quest )
					if (actions.size() > 5) {
						return maxMinAvgAssignationFunctionActionWithSeveralGenerics(ruleCore, actions);
					} else if (leftTreeObject instanceof Category) {
						scopeClass = "Question";
						// We only want the questions for the category
						if (leftTreeObject.getChildren() != null && !leftTreeObject.getChildren().isEmpty()) {
							treeObjects = new ArrayList<TreeObject>();
							for (TreeObject child : leftTreeObject.getChildren()) {
								if (child instanceof Question) {
									treeObjects.add(child);
								}
							}
						}
					}
					break;
				case QUESTION_GROUP:
					if (leftTreeObject instanceof Group) {
						scopeClass = "Question";
						// We only want the questions for the group
						if (leftTreeObject.getChildren() != null && !leftTreeObject.getChildren().isEmpty()) {
							treeObjects = new ArrayList<TreeObject>();
							for (TreeObject child : leftTreeObject.getChildren()) {
								if (child instanceof Question) {
									treeObjects.add(child);
								}
							}
						}
					}
					break;
				}
				int genericVarIndex = 1;
				if (treeObjects != null) {
					for (TreeObject to : treeObjects) {
						if (genericVarIndex == 1) {
							ruleCore += "	$var : List( size>0 ) from collect( " + scopeClass + "(isScoreSet('"
									+ customVariable.getName() + "'), getTag() == '" + to.getName() + "' || ";
						} else {
							ruleCore += "== '" + to.getName() + "' || ";
						}
						genericVarIndex++;
						parent = to.getParent();
					}
				}
			}
		}
		// Finish the line of the condition
		if (parent != null) {
			ruleCore = ruleCore.substring(0, ruleCore.length() - 3);
			if (scopeClass.equals("Question")) {
				ruleCore += ") from $" + parent.getUniqueNameReadable().toString() + ".getQuestions()) \n";
			} else if (scopeClass.equals("Group")) {
				ruleCore += ") from $" + parent.getUniqueNameReadable().toString() + ".getGroups()) \n";
			} else if (scopeClass.equals("Category")) {
				ruleCore += ") from $" + parent.getUniqueNameReadable().toString() + ".getCategories())\n";
			}

			String getVarValue = "getVariableValue('" + customVariable.getName() + "')";
			ExpressionFunction function = (ExpressionFunction) actions.get(2);
			if (function.getValue().equals(AvailableFunction.MAX)) {
				ruleCore += "	accumulate( " + scopeClass + "($value : " + getVarValue
						+ ") from $var; $sol : max($value)) \n";
			} else if (function.getValue().equals(AvailableFunction.MIN)) {
				ruleCore += "	accumulate( " + scopeClass + "($value : " + getVarValue
						+ ") from $var; $sol : min($value)) \n";
			} else if (function.getValue().equals(AvailableFunction.AVG)) {
				ruleCore += "	accumulate( " + scopeClass + "($value : " + getVarValue
						+ ") from $var; $sol : average($value)) \n";
			} else if (function.getValue().equals(AvailableFunction.SUM)) {
				ruleCore += "	accumulate( " + scopeClass + "($value : " + getVarValue
						+ ") from $var; $sol : sum($value)) \n";
			}

			ruleCore = RulesUtils.removeDuplicateLines(ruleCore);
			ruleCore += RulesUtils.getThenRuleString();

			// RHS
			if (variableToCalculate != null) {
				ruleCore += "	$" + this.getTreeObjectName(variableToCalculate.getReference()) + ".setVariableValue('"
						+ variableToCalculate.getVariable().getName() + "', $sol);\n";

				ruleCore += "	AbcdLogger.debug(\"DroolsRule\", \"TEST (" + variableToCalculate.getReference().getName()
						+ ", " + variableToCalculate.getVariable().getName() + ", \" + $var +\")\");\n";

				ruleCore += "	AbcdLogger.debug(\"DroolsRule\", \"Variable set ("
						+ variableToCalculate.getReference().getName() + ", "
						+ variableToCalculate.getVariable().getName() + ", \" + $sol +\")\");\n";
			}
		} else {
			// So the rule don't fail
			ruleCore += RulesUtils.getThenRuleString();
		}
		return ruleCore;
	}

	/**
	 * Creates a set of expressions and passes it back to the expression parser
	 * 
	 * @param expressions
	 * @param forkConditions
	 * @return
	 * @throws RuleNotImplementedException
	 */
	private String genericRuleSet(ExpressionChain expressionChain, String forkConditions)
			throws RuleNotImplementedException {
		String ruleCore = "";
		ExpressionValueGenericCustomVariable genericVarToCalculate = (ExpressionValueGenericCustomVariable) expressionChain
				.getExpressions().get(0);

		List<TreeObject> treeObjects = new ArrayList<TreeObject>();
		switch (genericVarToCalculate.getType()) {
		case CATEGORY:
			treeObjects.addAll(genericVarToCalculate.getVariable().getForm().getChildren());
			break;
		case GROUP:
			for (TreeObject category : genericVarToCalculate.getVariable().getForm().getChildren()) {
				treeObjects.addAll(category.getAll(Group.class));
			}
			break;
		case QUESTION_GROUP:
		case QUESTION_CATEGORY:
			break;
		}
		// For each category, we generate the expression to create a new rule
		if (treeObjects != null && !treeObjects.isEmpty()) {
			for (TreeObject category : treeObjects) {
				ExpressionValueCustomVariable expValCat = new ExpressionValueCustomVariable(category,
						genericVarToCalculate.getVariable());
				// Remove the generic
				expressionChain.getExpressions().remove(0);
				// Add the specific
				expressionChain.getExpressions().add(0, expValCat);

				try {
					ruleCore += ExpressionParser.parse(expressionChain, forkConditions);
					// System.out.println(ruleCore);
				} catch (ExpressionInvalidException e) {
					e.printStackTrace();
				}
			}
		}
		return ruleCore;
	}

	private List<Expression> getExpressionChainVariables(ExpressionChain expressionChain) {
		List<Expression> variables = new ArrayList<Expression>();
		for (Expression expression : expressionChain.getExpressions()) {
			// Look for possible variables that need assignation
			if ((expression instanceof ExpressionValueCustomVariable)
					|| (expression instanceof ExpressionValueTreeObjectReference)) {
				variables.add(expression);
			} else if (expression instanceof ExpressionChain) {
				variables.addAll(this.getExpressionChainVariables((ExpressionChain) expression));
			}
		}
		return variables;
	}

	/**
	 * Returns the type of answer for the question in the parameter
	 * 
	 * @return
	 */
	public String getTreeObjectAnswerType(TreeObject treeObject) {
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

	private String getTreeObjectName(TreeObject treeObject) {
		return this.treeObjectDroolsname.get(treeObject);
	}

	private String mathAssignationAction(ExpressionChain actions, String forkConditions)
			throws RuleNotImplementedException {
		String ruleCore = "";
		if (forkConditions != null) {
			ruleCore += forkConditions;
		}
		Parser parser = new ExpressionChainParser(actions);
		ITreeElement result = null;
		try {
			result = parser.parseExpression();
		} catch (ParseException ex) {
			AbcdLogger.errorMessage(this.getClass().getName(), ex);
		}
		if (result != null) {
			ExpressionChain mathematicalChain = result.getExpressionChain();
			List<Expression> chainList = mathematicalChain.getExpressions();

			if (((ExpressionChain) chainList.get(0)).getExpressions().get(0) instanceof ExpressionValueCustomVariable) {
				ExpressionValueCustomVariable var = (ExpressionValueCustomVariable) ((ExpressionChain) chainList.get(0))
						.getExpressions().get(0);
				// Check if the reference exists in the rule, if not, it creates
				// a new reference
				ruleCore += this.checkVariableAssignation(var);

				List<Expression> variables = this.getExpressionChainVariables(mathematicalChain);
				for (Expression expression : variables) {
					if ((expression instanceof ExpressionValueCustomVariable)
							|| (expression instanceof ExpressionValueTreeObjectReference)) {
						ruleCore += this.checkVariableAssignation(expression);
					}
				}
				ruleCore += RulesUtils.getThenRuleString();
				String mathematicalExpression = "";

				TreeElementMathExpressionVisitor treePrint = new TreeElementMathExpressionVisitor();
				result.accept(treePrint);
				if (treePrint != null) {
					mathematicalExpression = treePrint.getBuilder().toString();
				}

				ruleCore += "	$" + this.getTreeObjectName(var.getReference()) + ".setVariableValue('"
						+ var.getVariable().getName() + "', " + mathematicalExpression + ");\n";
				ruleCore += "	AbcdLogger.debug(\"DroolsRule\", \"Variable set (" + var.getReference().getName() + ", "
						+ var.getVariable().getName() + ", " + mathematicalExpression + ")\");\n";

			}
		}
		return ruleCore;
	}

	/**
	 * Expression parser. An expression is a rule without the condition part in the definition, but not in the drools
	 * engine.<br>
	 * Parse actions like => Cat.score = min(q1.score, q2.score, ...) <br>
	 * Create drools rule like => <br>
	 * &nbsp&nbsp&nbsp $var : List() from collect( some conditions )<br>
	 * &nbsp&nbsp&nbsp accumulate((Question($score : getScore()) from $var); $sol : min($value) )
	 * 
	 * @param actions
	 *            the expression being parsed
	 * @param forkConditions
	 * @return the rule
	 */
	private String maxMinAvgAssignationFunctionAction(List<Expression> actions, String forkConditions) {

		// System.out.println("ACTIONS LIST: " + actions);

		String ruleCore = "";
		if (forkConditions != null) {
			ruleCore += forkConditions;
		}
		// LHS
		// We will have some expression of type Category.score = (Min | Max |
		// Avg) of some values
		ExpressionValueCustomVariable variableToCalculate = (ExpressionValueCustomVariable) actions.get(0);
		// The rule is different if the variable to assign is a Form, a Category
		// a Group or a Question
		TreeObject leftTreeObject = variableToCalculate.getReference();
		if (leftTreeObject instanceof Form) {
			ruleCore += this.simpleFormCondition((Form) leftTreeObject);

		} else if (leftTreeObject instanceof Category) {
			ruleCore += this.simpleCategoryConditions((Category) leftTreeObject);

		} else if (leftTreeObject instanceof Group) {
			ruleCore += this.simpleGroupConditions((Group) leftTreeObject);

		} else if (leftTreeObject instanceof Question) {
			ruleCore += this.simpleQuestionConditions((Question) leftTreeObject);
		}
		ruleCore += genericFunctionParameters(ruleCore, actions);
		return ruleCore;
	}

	/**
	 * Used when parsing functions with more than one generic parameter
	 * 
	 * @param actions
	 * @param rule
	 * @return
	 */
	private String maxMinAvgAssignationFunctionActionWithSeveralGenerics(String rule, List<Expression> actions) {

		String ruleCore = rule;
		// LHS
		// We will have some expression of type Category.score = (Min | Max |
		// Avg) of some values
		ExpressionValueCustomVariable variableToCalculate = (ExpressionValueCustomVariable) actions.get(0);
		TreeObject leftTreeObject = variableToCalculate.getReference();
		// Function to execute
		ExpressionFunction function = (ExpressionFunction) actions.get(2);

		// Maximum two generics allowed
		List<ExpressionValueGenericCustomVariable> genericExpressionList = new ArrayList<ExpressionValueGenericCustomVariable>();
		genericExpressionList.add((ExpressionValueGenericCustomVariable) actions.get(3));
		genericExpressionList.add((ExpressionValueGenericCustomVariable) actions.get(5));
		TreeObject parent = null;
		int genericIndex = 1;
		for (ExpressionValueGenericCustomVariable genericExpressionVariable : genericExpressionList) {
			String scopeClass = "";
			CustomVariable customVariable = null;

			List<TreeObject> treeObjects = null;
			customVariable = genericExpressionVariable.getVariable();
			switch (genericExpressionVariable.getType()) {
			case CATEGORY:
				break;
			case GROUP:
				if (leftTreeObject instanceof Category) {
					scopeClass = "Group";
					if (leftTreeObject.getChildren() != null && !leftTreeObject.getChildren().isEmpty()) {
						treeObjects = new ArrayList<TreeObject>();
						for (TreeObject child : leftTreeObject.getChildren()) {
							if (child instanceof Group) {
								treeObjects.add(child);
							}
						}
					}
					// treeObjects = leftTreeObject.getAll(Group.class);
					// // To guarantee the nested groups variable assignation
					// if (treeObjects != null && !treeObjects.isEmpty()) {
					// for (TreeObject auxTreeobject : treeObjects) {
					// ruleCore += checkVariableAssignation(auxTreeobject);
					// }
					// }
				}
				break;
			case QUESTION_CATEGORY:
				if (leftTreeObject instanceof Category) {
					scopeClass = "Question";
					// We only want the questions for the category
					if (leftTreeObject.getChildren() != null && !leftTreeObject.getChildren().isEmpty()) {
						treeObjects = new ArrayList<TreeObject>();
						for (TreeObject child : leftTreeObject.getChildren()) {
							if (child instanceof Question) {
								treeObjects.add(child);
							}
						}
					}
				}
				break;
			case QUESTION_GROUP:
				if (leftTreeObject instanceof Group) {
					scopeClass = "Question";
					// We only want the questions for the category
					if (leftTreeObject.getChildren() != null && !leftTreeObject.getChildren().isEmpty()) {
						treeObjects = new ArrayList<TreeObject>();
						for (TreeObject child : leftTreeObject.getChildren()) {
							if (child instanceof Question) {
								treeObjects.add(child);
							}
						}
					}
				}
				break;
			}
			int genericVarIndex = 1;
			if (treeObjects != null && !treeObjects.isEmpty()) {
				for (TreeObject to : treeObjects) {
					if (genericVarIndex == 1) {
						ruleCore += "	$var" + genericIndex + " : List() from collect( " + scopeClass + "(isScoreSet('"
								+ customVariable.getName() + "'), getTag() == '" + to.getName() + "' || ";
					} else {
						ruleCore += "== '" + to.getName() + "' || ";
					}
					genericVarIndex++;
					parent = to.getParent();
				}
			} else {
				// We don't want to modify the original expression chain
				List<Expression> actionsToModify = new ArrayList<Expression>();
				for (Expression expression : actions) {
					Expression copyExpression = expression.generateCopy();
					actionsToModify.add(copyExpression);
				}

				// No questions or groups inside the category.
				// Thus, this is a simple generic assignation function
				// Return the generic parsed without the genericTreeObject (and
				// the comma) with no children
				if (genericIndex == 1) {
					actionsToModify.remove(4);
					actionsToModify.remove(3);
				} else {
					// We also need to remove the last two lines
					ruleCore = RulesUtils.removeLastNLines(ruleCore, 2);
					actionsToModify.remove(5);
					actionsToModify.remove(4);
				}
				// System.out.println("NOT TREE OBJECTS FOUND");
				return genericFunctionParameters(ruleCore, actionsToModify);
			}

			if (parent != null) {
				// Finish the line of the condition
				ruleCore = ruleCore.substring(0, ruleCore.length() - 3);
				if (scopeClass.equals("Question")) {
					ruleCore += ") from $" + parent.getUniqueNameReadable().toString() + ".getQuestions()) \n";
				} else if (scopeClass.equals("Group")) {
					ruleCore += ") from $" + parent.getUniqueNameReadable().toString() + ".getGroups()) \n";
				}

				String getVarValue = "getVariableValue('" + customVariable.getName() + "')";
				switch (function.getValue()) {
				case MAX:
					ruleCore += "	accumulate( " + scopeClass + "($value" + genericIndex + " : " + getVarValue
							+ ") from $var" + genericIndex + "; $sol" + genericIndex + " : max($value" + genericIndex
							+ ")) \n";
					break;
				case MIN:
					ruleCore += "	accumulate( " + scopeClass + "($value" + genericIndex + " : " + getVarValue
							+ ") from $var" + genericIndex + "; $sol" + genericIndex + " : min($value" + genericIndex
							+ ")) \n";
					break;
				case AVG:
					ruleCore += "	accumulate( " + scopeClass + "($value" + genericIndex + " : " + getVarValue
							+ ") from $var" + genericIndex + "; $sol" + genericIndex + " : average($value"
							+ genericIndex + ")) \n";
					break;
				case SUM:
					ruleCore += "	accumulate( " + scopeClass + "($value" + genericIndex + " : " + getVarValue
							+ ") from $var" + genericIndex + "; $sol" + genericIndex + " : sum($value" + genericIndex
							+ ")) \n";
					break;
				}
			}
			genericIndex++;
		}
		if (parent != null) {
			ruleCore = RulesUtils.removeDuplicateLines(ruleCore);
			ruleCore += RulesUtils.getThenRuleString();
			// RHS
			if (variableToCalculate != null) {
				switch (function.getValue()) {
				case MAX:
					ruleCore += "	Double sol = ((Double)$sol1 > (Double)$sol2) ? (Double)$sol1 : (Double)$sol2;\n";
					break;
				case MIN:
					ruleCore += "	Double sol = ((Double)$sol1 > (Double)$sol2) ? (Double)$sol2 : (Double)$sol1;\n";
					break;
				case AVG:
					ruleCore += "	Double sol = ((Double)$sol1 + (Double)$sol2)/2.0;\n";
					break;
				case SUM:
					ruleCore += "	Double sol = (Double)$sol1 + (Double)$sol2;\n";
					break;
				}

				ruleCore += "	$" + this.getTreeObjectName(variableToCalculate.getReference()) + ".setVariableValue('"
						+ variableToCalculate.getVariable().getName() + "', sol);\n";
				ruleCore += "	AbcdLogger.debug(\"DroolsRule\", \"Variable set ("
						+ variableToCalculate.getReference().getName() + ", "
						+ variableToCalculate.getVariable().getName() + ", \" + sol +\")\");\n";
			}
		} else {
			// So the rule don't fail
			ruleCore += RulesUtils.getThenRuleString();
		}
		return ruleCore;
	}

	/**
	 * Parse actions like => Score = Score (+|-|/|*) numberValue <br>
	 * Create drools rule like => setVariableValue(scopeOfVariable, variableName,
	 * getVariablevalue(variableName)+numberValue ) <br>
	 * The variables to the right and to the left of the assignation can be different (i.e. a = b+1) <br>
	 * 
	 * @param actions
	 *            the action of the rule being parsed
	 * @return the RHS of the rule
	 */
	private String modifyVariableAction(List<Expression> actions) {
		String ruleCore = "";
		ExpressionValueCustomVariable var = (ExpressionValueCustomVariable) actions.get(0);
		ExpressionOperatorMath operator = (ExpressionOperatorMath) actions.get(3);

		// Get the variable name
		String customVarName = var.getVariable().getName();
		// Check if the reference exists in the rule, if not, it creates a new
		// reference
		ruleCore += this.checkVariableAssignation(var);

		if (actions.get(2) instanceof ExpressionValueCustomVariable) {
			ExpressionValueCustomVariable var2 = (ExpressionValueCustomVariable) actions.get(2);
			ruleCore += this.checkVariableAssignation(var2);
			ExpressionValueNumber valueNumber = (ExpressionValueNumber) actions.get(4);

			ruleCore += RulesUtils.getThenRuleString();
			ruleCore += "	$" + this.getTreeObjectName(var.getReference())
					+ ".setVariableValue('"
					+ customVarName
					// + "', " + "(Double)$" +
					// this.getTreeObjectName(var2.getReference()) +
					// ".getNumberVariableValue('"
					+ "', " + "(Double)$" + this.getTreeObjectName(var2.getReference()) + ".getVariableValue('"
					+ customVarName + "') " + operator.getValue() + " " + valueNumber.getValue() + ");\n";
			ruleCore += "	AbcdLogger.debug(\"DroolsRule\", \"Variable updated (" + var.getReference().getName() + ", "
					+ customVarName + ", " + operator.getValue() + valueNumber.getValue() + ")\");\n";

		} else if (actions.get(2) instanceof ExpressionValueNumber) {
			ExpressionValueNumber valueNumber = (ExpressionValueNumber) actions.get(2);
			ExpressionValueCustomVariable var2 = (ExpressionValueCustomVariable) actions.get(4);
			ruleCore += this.checkVariableAssignation(var2);

			ruleCore += RulesUtils.getThenRuleString();
			ruleCore += "	$" + this.getTreeObjectName(var.getReference())
					+ ".setVariableValue('"
					+ customVarName
					// + "', " + "(Double)$" +
					// this.getTreeObjectName(var2.getReference()) +
					// ".getNumberVariableValue('"
					+ "', " + "(Double)$" + this.getTreeObjectName(var2.getReference()) + ".getVariableValue('"
					+ customVarName + "') " + operator.getValue() + " " + valueNumber.getValue() + ");\n";
			ruleCore += "	AbcdLogger.debug(\"DroolsRule\", \"Variable updated (" + var.getReference().getName() + ", "
					+ customVarName + ", " + operator.getValue() + valueNumber.getValue() + ")\");\n";

		}
		return ruleCore;
	}

	private String orOperator(List<Expression> expressions) {
		String result = "";

		ExpressionChain leftChain = (ExpressionChain) expressions.get(0);
		ExpressionChain rightChain = (ExpressionChain) expressions.get(2);

		// result += "(\n";
		result += this.processParserResult(leftChain);
		result += "or\n";
		result += this.processParserResult(rightChain);
		// result += ")\n";

		return result;
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
	 */
	private String parseActions(ExpressionChain expressionChain) throws RuleNotImplementedException {
		List<Expression> actions = expressionChain.getExpressions();
		Parser parser = new ExpressionChainParser(actions);
		ITreeElement result = null;
		try {
			result = parser.parseExpression();
		} catch (ParseException ex) {
			AbcdLogger.errorMessage(this.getClass().getName(), ex);
		}

		if (actions.size() > 2) {
			// Action type assignation => (cat.scoreText = "someText") || (cat.score =
			// someValue)
			if ((actions.get(0) instanceof ExpressionValueCustomVariable)
					&& (actions.get(1) instanceof ExpressionOperatorMath)
					&& (((ExpressionOperatorMath) actions.get(1)).getValue().equals(AvailableOperator.ASSIGNATION))
					&& ((actions.get(2) instanceof ExpressionValueString)
							|| (actions.get(2) instanceof ExpressionValueNumber) || (actions.get(2) instanceof ExpressionFunction))) {
				return this.assignationAction(result.getExpressionChain());
			}
			// Action type update => cat.scoreText = cat.scoreText + 1
			else if ((actions.get(0) instanceof ExpressionValueCustomVariable)
					&& (actions.get(1) instanceof ExpressionOperatorMath)
					&& (((ExpressionOperatorMath) actions.get(1)).getValue().equals(AvailableOperator.ASSIGNATION))
					&& ((actions.get(2) instanceof ExpressionValueCustomVariable) || (actions.get(2) instanceof ExpressionValueNumber))
					&& (actions.get(3) instanceof ExpressionOperatorMath)
					&& ((actions.get(4) instanceof ExpressionValueCustomVariable) || (actions.get(4) instanceof ExpressionValueNumber))) {
				return this.modifyVariableAction(actions);

			}
		}
		throw new RuleNotImplementedException("Rule not implemented.", expressionChain);
	}

	private String parseConditions(ExpressionChain conditions) {
		Parser parser = new ExpressionChainParser(conditions.getExpressions());
		ITreeElement result = null;
		try {
			result = parser.parseExpression();
		} catch (ParseException ex) {
			AbcdLogger.errorMessage(this.getClass().getName(), ex);
		}

		// *******************************************************************************************************
		// After this point the expression chain is the result of the parsing
		// engine. The expression chain has AST (abstract syntax tree) form
		// *******************************************************************************************************
		if ((result != null) && (result.getExpressionChain() != null)) {
			return processParserResult(result.getExpressionChain());
		} else {
			return "";
		}
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
	 */
	private String parseExpressions(ExpressionChain expressionChain) throws RuleNotImplementedException {
		// A.k.a Expression
		// Action type => cat.score = min(q1.score, q2.score, ...)
		if ((expressionChain.getExpressions().get(0) instanceof ExpressionValueCustomVariable)
				&& (expressionChain.getExpressions().get(1) instanceof ExpressionOperatorMath)
				&& (((ExpressionOperatorMath) expressionChain.getExpressions().get(1)).getValue()
						.equals(AvailableOperator.ASSIGNATION))
				&& (expressionChain.getExpressions().get(2) instanceof ExpressionFunction)) {
			switch (((ExpressionFunction) expressionChain.getExpressions().get(2)).getValue()) {
			case MAX:
			case MIN:
			case AVG:
				return this.maxMinAvgAssignationFunctionAction(expressionChain.getExpressions(), forkConditions);
			case PMT:
				return this.pmtAssignationFunctionAction(expressionChain.getExpressions(), forkConditions);
			}

			// Generic calculation
		} else if ((expressionChain.getExpressions().get(0) instanceof ExpressionValueGenericCustomVariable)
				&& (expressionChain.getExpressions().get(1) instanceof ExpressionOperatorMath)
				&& (((ExpressionOperatorMath) expressionChain.getExpressions().get(1)).getValue()
						.equals(AvailableOperator.ASSIGNATION))
				&& (expressionChain.getExpressions().get(2) instanceof ExpressionFunction)) {
			return this.genericAssignationFunctionAction(expressionChain, forkConditions);
		}

		// Mathematical expression
		else if ((expressionChain.getExpressions().get(0) instanceof ExpressionValueCustomVariable)
				&& (expressionChain.getExpressions().get(1) instanceof ExpressionOperatorMath)
				&& (((ExpressionOperatorMath) expressionChain.getExpressions().get(1)).getValue()
						.equals(AvailableOperator.ASSIGNATION))) {
			return this.mathAssignationAction(expressionChain, forkConditions);
		}

		throw new RuleNotImplementedException("Rule not implemented.", expressionChain);
	}

	private String pmtAssignationFunctionAction(List<Expression> actions, String forkConditions) {
		String ruleCore = "";
		if (forkConditions != null) {
			ruleCore += forkConditions;
		}
		ExpressionValueCustomVariable var = (ExpressionValueCustomVariable) actions.get(0);
		// Check if the reference exists in the rule, if not, it creates a new
		// reference
		ruleCore += this.checkVariableAssignation(var);

		ruleCore += RulesUtils.getThenRuleString();

		ExpressionFunction auxFunc = (ExpressionFunction) actions.get(2);
		switch (auxFunc.getValue()) {
		case PMT:
			actions = Arrays.asList(actions.get(3), actions.get(5), actions.get(7));
			String value = this.calculatePMT(actions);
			ruleCore += "	$" + this.getTreeObjectName(var.getReference()) + ".setVariableValue('"
					+ var.getVariable().getName() + "', " + value + ");\n";
			ruleCore += "	AbcdLogger.debug(\"DroolsRule\", \"Variable set (" + var.getReference().getName() + ", "
					+ var.getVariable().getName() + ", " + value + ")\");\n";
			break;
		default:
			break;
		}
		return ruleCore;
	}

	private String processParserResult(ExpressionChain parsedExpression) {
		// All the expressions without functions should pass this condition and
		// some generic functions too
		if ((parsedExpression != null) && (parsedExpression.getExpressions().size() == 3)) {
			List<Expression> expressions = parsedExpression.getExpressions();

			// Operators
			if (expressions.get(1) instanceof ExpressionOperatorLogic) {
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
			} else if (expressions.get(1) instanceof ExpressionFunction) {
				switch (((ExpressionFunction) expressions.get(1)).getValue()) {
				case MIN:
					// return this.genericMinFunction(expressions);
					break;
				// In case the IN element has only one value
				case IN:
					return this.answersInQuestionCondition(expressions);
				default:
					break;
				}
			}
		}
		// Function expressions
		else if ((parsedExpression != null) && (parsedExpression.getExpressions().size() > 3)) {
			List<Expression> expressions = parsedExpression.getExpressions();
			if ((expressions.get(1) instanceof ExpressionFunction)) {
				switch (((ExpressionFunction) expressions.get(1)).getValue()) {
				case BETWEEN:
					if (((ExpressionChain) expressions.get(0)).getExpressions().get(0) instanceof ExpressionValueCustomVariable) {
						return this.customVariableBetweenAnswersCondition(expressions);
					} else if (((ExpressionChain) expressions.get(0)).getExpressions().get(0) instanceof ExpressionValueTreeObjectReference) {
						return this.questionBetweenAnswersCondition(expressions);
					}
				case IN:
					return this.answersInQuestionCondition(expressions);
				default:
					break;
				}
			}
		}
		return "";
	}

	private void putTreeObjectName(TreeObject treeObject, String value) {
		this.treeObjectDroolsname.put(treeObject, value);
	}

	private String questionAnswerEqualsCondition(Question question, Answer answer, AvailableOperator availableOperator) {
		// String droolsConditions = "(\n";
		String droolsConditions = "";
		// Check the parent of the question
		TreeObject questionParent = question.getParent();
		this.putTreeObjectName(question, question.getUniqueNameReadable().toString());
		if (questionParent instanceof Category) {
			droolsConditions += this.simpleCategoryConditions((Category) questionParent);
		} else if (questionParent instanceof Group) {
			droolsConditions += this.simpleGroupConditions((Group) questionParent);
		}
		this.putTreeObjectName(question, question.getUniqueNameReadable().toString());
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
	 * Create drools rule like => Question( (getAnswer() >= answer.getValue()) && (getAnswer() <= answer.getValue()))
	 * 
	 * @param conditions
	 * @return LHS of the rule
	 */
	private String questionBetweenAnswersCondition(List<Expression> conditions) {

		// System.out.println("QUESTION BETWEEN");
		// System.out.println("CONDITIONS: " + conditions);

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
				if ((firstExpressionValue.size() == 1)
						&& (firstExpressionValue.get(0) instanceof ExpressionValueNumber)
						&& (secondExpressionValue.size() == 1)
						&& (secondExpressionValue.get(0) instanceof ExpressionValueNumber)) {
					// Get the values of the between expression
					Double value1 = ((ExpressionValueNumber) firstExpressionValue.get(0)).getValue();
					Double value2 = ((ExpressionValueNumber) secondExpressionValue.get(0)).getValue();
					if ((value1 != null) && (value2 != null)) {

						TreeObject leftReferenceParent = leftReference.getParent();
						this.putTreeObjectName(leftReference, leftReference.getUniqueNameReadable().toString());
						// Check the parent
						if (leftReferenceParent instanceof Form) {
							droolsConditions += this.simpleFormCondition((Form) leftReferenceParent);
						} else if (leftReferenceParent instanceof Category) {
							droolsConditions += this.simpleCategoryConditions((Category) leftReferenceParent);
						} else if (leftReferenceParent instanceof Group) {
							droolsConditions += this.simpleGroupConditions((Group) leftReferenceParent);
						}
						if (leftReference instanceof Question) {
							Question leftQuestion = (Question) leftReference;
							switch (leftQuestion.getAnswerType()) {
							case RADIO:
							case MULTI_CHECKBOX:
								// The flow don't get here.
								// It does not
								break;
							case INPUT:
								switch (leftQuestion.getAnswerFormat()) {
								case NUMBER:
									droolsConditions += "	$" + leftQuestion.getUniqueNameReadable().toString()
											+ " : Question(getTag() == '" + leftQuestion.getName() + "', getAnswer('"
											+ getTreeObjectAnswerType(leftQuestion) + "') >= " + value1 + " && < "
											+ value2 + ") from $"
											+ leftReferenceParent.getUniqueNameReadable().toString()
											+ ".getQuestions() \n";
									break;
								case POSTAL_CODE:
								case TEXT:
									// The system should not get here because it
									// is forbidden by the jexeval
									break;
								case DATE:
									String betweenDate = "";
									if (leftExpressionValueTreeObject.getUnit() != null) {
										switch (leftExpressionValueTreeObject.getUnit()) {
										case YEARS:
											betweenDate = "DateUtils.returnYearsDistanceFromDate(getAnswer('"
													+ AnswerFormat.DATE.toString() + "')) >= " + value1.intValue()
													+ " && < " + value2.intValue();
											break;
										case MONTHS:
											betweenDate = "DateUtils.returnMonthsDistanceFromDate(getAnswer('"
													+ AnswerFormat.DATE.toString() + "')) >= " + value1.intValue()
													+ " && < " + value2.intValue();
											break;
										case DAYS:
											betweenDate = "DateUtils.returnDaysDistanceFromDate(getAnswer('"
													+ AnswerFormat.DATE.toString() + "')) >= " + value1.intValue()
													+ " && < " + value2.intValue();
											break;
										case DATE:
											break;
										}
									} else {
										AbcdLogger.warning(this.getClass().getName(),
												"Question with format DATE don't have a selected unit");
									}
									droolsConditions += "	$" + leftQuestion.getUniqueNameReadable().toString()
											+ " : Question( " + betweenDate + ") from $"
											+ leftReferenceParent.getUniqueNameReadable().toString()
											+ ".getQuestions()\n";
								}
								break;
							}
						}
						// if (leftReference instanceof Question) {
						// Question leftQuestion = (Question) leftReference;
						// switch (leftQuestion.getAnswerType()) {
						// case INPUT:
						// switch (leftQuestion.getAnswerFormat()) {
						// case DATE:
						// String instanceOfDate = "getAnswer('" +
						// getTreeObjectAnswerType(leftQuestion)
						// + "') instanceof Date";
						// String greatEqualsDate = "getAnswer('" +
						// getTreeObjectAnswerType(leftQuestion)
						// + "') <= DateUtils.returnCurrentDateMinusYears(" +
						// value1.intValue() + ")";
						// String lessEqualsDate = "getAnswer('" +
						// getTreeObjectAnswerType(leftQuestion)
						// + "') > DateUtils.returnCurrentDateMinusYears(" +
						// value2.intValue() + ")";
						// droolsConditions += "	$" +
						// leftQuestion.getUniqueNameReadable().toString()
						// + " : Question( " + instanceOfDate + ", " +
						// greatEqualsDate + ", "
						// + lessEqualsDate + ") from $"
						// +
						// leftReferenceParent.getUniqueNameReadable().toString()
						// + ".getQuestions()\n";
						// // droolsConditions += "and\n";
						// default:
						// break;
						// }
						// break;
						// default:
						// droolsConditions += "	$" +
						// leftQuestion.getUniqueNameReadable().toString()
						// + " : Question(getTag() == '" +
						// leftQuestion.getName() + "', getAnswer('"
						// + getTreeObjectAnswerType(leftQuestion) + "') >= " +
						// value1 + " && < " + value2
						// + ") from $" +
						// leftReferenceParent.getUniqueNameReadable().toString()
						// + ".getQuestions() \n";
						// // droolsConditions += "and\n";
						// break;
						// }
						// }
					}
				}
			}
		}
		return droolsConditions;
	}

	private String questionDateDaysOperatorValueNumber(TreeObject question, Double value, AvailableOperator operator) {
		return "	$" + question.getUniqueNameReadable().toString() + " : Question(getTag()== '" + question.getName()
				+ "', DateUtils.returnDaysDistanceFromDate(getAnswer('" + getTreeObjectAnswerType(question) + "')) "
				+ operator.getValue() + " " + value.intValue() + ") from $"
				+ question.getParent().getUniqueNameReadable().toString() + ".getQuestions()\n";
	}

	private String questionDateMonthsOperatorValueNumber(TreeObject question, Double value, AvailableOperator operator) {
		return "	$" + question.getUniqueNameReadable().toString() + " : Question(getTag()== '" + question.getName()
				+ "', DateUtils.returnMonthsDistanceFromDate(getAnswer('" + getTreeObjectAnswerType(question) + "')) "
				+ operator.getValue() + " " + value.intValue() + ") from $"
				+ question.getParent().getUniqueNameReadable().toString() + ".getQuestions()\n";
	}

	private String questionDateOperatorValue(TreeObject leftReferenceParent, TreeObject leftQuestion,
			AvailableOperator operator, Double value) {
		String rule = "";

		// Check if the reference exists in the rule, if not, it creates
		// a new reference
		rule += this.checkVariableAssignation(leftQuestion);

		rule += "	$" + leftQuestion.getUniqueNameReadable().toString() + " : Question(getTag() == '"
				+ leftQuestion.getName() + "', DateUtils.returnYearsDistanceFromDate(getAnswer('"
				+ getTreeObjectAnswerType(leftQuestion) + "')) " + operator.getValue() + value.intValue() + ") from $"
				+ leftReferenceParent.getUniqueNameReadable().toString() + ".getQuestions()\n";
		return rule;
	}

	private String questionDateYearsOperatorValueNumber(TreeObject question, Double value, AvailableOperator operator) {
		return "	$" + question.getUniqueNameReadable().toString() + " : Question(getTag()== '" + question.getName()
				+ "', DateUtils.returnYearsDistanceFromDate(getAnswer('" + getTreeObjectAnswerType(question) + "')) "
				+ operator.getValue() + " " + value.intValue() + ") from $"
				+ question.getParent().getUniqueNameReadable().toString() + ".getQuestions()\n";
	}

	private String questionGeGtLeLtAnswer(List<Expression> conditions, AvailableOperator operator) {
		String droolsConditions = "";
		List<Expression> left = ((ExpressionChain) conditions.get(0)).getExpressions();
		TreeObject leftTreeObject = null;

		if ((left.size() == 1) && !(left.get(0) instanceof ExpressionValueCustomVariable)) {
			leftTreeObject = ((ExpressionValueTreeObjectReference) left.get(0)).getReference();
			if (leftTreeObject != null) {
				List<Expression> rightExpressions = ((ExpressionChain) conditions.get(2)).getExpressions();

				// Comparison with a number
				if ((rightExpressions.size() == 1) && (rightExpressions.get(0) instanceof ExpressionValueNumber)) {
					// Get the values of the between expression
					Double value = ((ExpressionValueNumber) rightExpressions.get(0)).getValue();
					if (value != null) {
						TreeObject leftTreeObjectParent = leftTreeObject.getParent();
						this.putTreeObjectName(leftTreeObject, leftTreeObject.getUniqueNameReadable().toString());
						// Check the parent
						if (leftTreeObjectParent instanceof Form) {
							droolsConditions += this.simpleFormCondition((Form) leftTreeObjectParent);
						} else if (leftTreeObjectParent instanceof Category) {
							droolsConditions += this.simpleCategoryConditions((Category) leftTreeObjectParent);
						} else if (leftTreeObjectParent instanceof Group) {
							droolsConditions += this.simpleGroupConditions((Group) leftTreeObjectParent);
						}
						if (leftTreeObject instanceof Question) {
							Question leftQuestion = (Question) leftTreeObject;
							if (leftQuestion.getAnswerType().equals(AnswerType.INPUT)) {
								switch (leftQuestion.getAnswerFormat()) {
								case DATE:
									droolsConditions += this.questionDateOperatorValue(leftTreeObjectParent,
											leftQuestion, operator, value);
									break;
								case NUMBER:
									droolsConditions += "	$" + leftQuestion.getUniqueNameReadable().toString()
											+ " : Question(getTag()== '" + leftQuestion.getName() + "', getAnswer('"
											+ getTreeObjectAnswerType(leftQuestion) + "') " + operator.getValue() + " "
											+ value.intValue() + ") from $"
											+ leftTreeObjectParent.getUniqueNameReadable().toString()
											+ ".getQuestions()\n";
									break;
								case TEXT:
								case POSTAL_CODE:
									droolsConditions += "	$" + leftQuestion.getUniqueNameReadable().toString()
											+ " : Question(getTag()== '" + leftQuestion.getName() + "', getAnswer('"
											+ getTreeObjectAnswerType(leftQuestion) + "') " + operator.getValue() + " "
											+ value.intValue() + ") from $"
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
							this.putTreeObjectName(leftQuestion, leftQuestion.getUniqueNameReadable().toString());
							// Check the parent
							if (leftQuestionParent instanceof Category) {
								droolsConditions += this.simpleCategoryConditions((Category) leftQuestionParent);
							} else if (leftQuestionParent instanceof Group) {
								droolsConditions += this.simpleGroupConditions((Group) leftQuestionParent);
							}

							TreeObject rightQuestionParent = rightQuestion.getParent();
							this.putTreeObjectName(rightQuestion, rightQuestion.getUniqueNameReadable().toString());
							// Check the parent
							if (rightQuestionParent instanceof Category) {
								droolsConditions += this.simpleCategoryConditions((Category) rightQuestionParent);
							} else if (rightQuestionParent instanceof Group) {
								droolsConditions += this.simpleGroupConditions((Group) rightQuestionParent);
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
					this.putTreeObjectName(leftQuestion, leftQuestion.getUniqueNameReadable().toString());
					// Check the parent
					if (leftQuestionParent instanceof Category) {
						droolsConditions += this.simpleCategoryConditions((Category) leftQuestionParent);
					} else if (leftQuestionParent instanceof Group) {
						droolsConditions += this.simpleGroupConditions((Group) leftQuestionParent);
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
			ExpressionValueNumber valueNumber = (ExpressionValueNumber) ((ExpressionChain) conditions.get(2))
					.getExpressions().get(0);
			droolsConditions += this.treeObjectScoreLogicOperatorValueNumber(customVariable,
					(ExpressionOperatorLogic) conditions.get(1), valueNumber);
		}
		return droolsConditions;
	}

	private String simpleCategoryConditions(Category category) {
		String conditions = "";
		Form form = (Form) category.getParent();
		conditions += this.simpleFormCondition(form);

		if (this.getTreeObjectName(category) == null) {
			this.putTreeObjectName(category, category.getUniqueNameReadable().toString());
			conditions += "	$" + category.getUniqueNameReadable().toString() + " : Category() from $"
					+ form.getUniqueNameReadable().toString() + ".getCategory('" + category.getName() + "') \n";
		}
		return conditions;
	}

	private String simpleCategoryCustomVariableConditions(ExpressionValueCustomVariable customVariable) {
		Category category = (Category) customVariable.getReference();
		String conditions = "";
		Form form = (Form) category.getParent();
		conditions += this.simpleFormCondition(form);

		if (this.getTreeObjectName(category) == null) {
			this.putTreeObjectName(category, category.getUniqueNameReadable().toString());
			conditions += "	$" + category.getUniqueNameReadable().toString() + " : Category( isScoreSet('"
					+ customVariable.getVariable().getName() + "')) from $" + form.getUniqueNameReadable().toString()
					+ ".getCategory('" + category.getName() + "') \n";
		}
		return conditions;
	}

	private String simpleFormCondition(Form form) {
		String droolsRule = "";
		if (this.getTreeObjectName(form) == null) {
			this.putTreeObjectName(form, form.getUniqueNameReadable().toString());
			droolsRule += "	$" + form.getUniqueNameReadable().toString()
					+ " : SubmittedForm() from $droolsForm.getSubmittedForm() \n";
		}
		return droolsRule;
	}

	private String simpleGroupConditions(Group group) {
		String conditions = "";
		TreeObject groupParent = group.getParent();
		if (groupParent instanceof Category) {
			Category category = (Category) groupParent;
			conditions += this.simpleCategoryConditions(category);
			if (this.getTreeObjectName(group) == null) {
				this.putTreeObjectName(group, group.getUniqueNameReadable().toString());
				conditions += "	$" + group.getUniqueNameReadable().toString() + " : Group() from $"
						+ category.getUniqueNameReadable().toString() + ".getGroup('" + group.getName() + "') \n";
			}
		} else if (groupParent instanceof Group) {
			Group groupGroup = (Group) groupParent;
			conditions += this.simpleGroupConditions(groupGroup);
			if (this.getTreeObjectName(group) == null) {
				this.putTreeObjectName(group, group.getUniqueNameReadable().toString());
				conditions += "	$" + group.getUniqueNameReadable().toString() + " : Group() from $"
						+ groupGroup.getUniqueNameReadable().toString() + ".getGroup('" + group.getName() + "') \n";
			}
		}
		return conditions;
	}

	private String simpleGroupCustomVariableConditions(ExpressionValueCustomVariable customVariable) {
		Group group = (Group) customVariable.getReference();
		String conditions = "";
		TreeObject groupParent = group.getParent();
		if (groupParent instanceof Category) {
			Category category = (Category) groupParent;
			conditions += this.simpleCategoryConditions(category);
			if (this.getTreeObjectName(group) == null) {
				this.putTreeObjectName(group, group.getUniqueNameReadable().toString());
				conditions += "	$" + group.getUniqueNameReadable().toString() + " : Group( isScoreSet('"
						+ customVariable.getVariable().getName() + "')) from $"
						+ category.getUniqueNameReadable().toString() + ".getGroup('" + group.getName() + "') \n";
			}
		} else if (groupParent instanceof Group) {
			Group groupGroup = (Group) groupParent;
			conditions += this.simpleGroupConditions(groupGroup);
			if (this.getTreeObjectName(group) == null) {
				this.putTreeObjectName(group, group.getUniqueNameReadable().toString());
				conditions += "	$" + group.getUniqueNameReadable().toString() + " : Group( isScoreSet('"
						+ customVariable.getVariable().getName() + "')) from $"
						+ groupGroup.getUniqueNameReadable().toString() + ".getGroup('" + group.getName() + "') \n";
			}
		}
		return conditions;
	}

	private String simpleQuestionConditions(Question question) {
		String conditions = "";
		TreeObject questionParent = question.getParent();
		if (questionParent instanceof Category) {
			Category category = (Category) questionParent;
			conditions += this.simpleCategoryConditions(category);

			if (this.getTreeObjectName(question) == null) {
				this.putTreeObjectName(question, question.getUniqueNameReadable().toString());
				conditions += "	$" + question.getUniqueNameReadable().toString() + " : Question(getTag() == '"
						+ question.getName() + "') from $" + category.getUniqueNameReadable().toString()
						+ ".getQuestions() \n";
			}

		} else if (questionParent instanceof Group) {
			Group group = (Group) questionParent;
			conditions += this.simpleGroupConditions(group);

			if (this.getTreeObjectName(question) == null) {
				this.putTreeObjectName(question, question.getUniqueNameReadable().toString());
				conditions += "	$" + question.getUniqueNameReadable().toString() + " : Question(getTag() == '"
						+ question.getName() + "') from $" + group.getUniqueNameReadable().toString()
						+ ".getQuestions() \n";
			}
		}
		return conditions;
	}

	private String simpleQuestionCustomVariableConditions(ExpressionValueCustomVariable customVariable) {
		Question question = (Question) customVariable.getReference();
		String conditions = "";
		TreeObject questionParent = question.getParent();
		if (questionParent instanceof Category) {
			Category category = (Category) questionParent;
			conditions += this.simpleCategoryConditions(category);

			if (this.getTreeObjectName(question) == null) {
				this.putTreeObjectName(question, question.getUniqueNameReadable().toString());
				conditions += "	$" + question.getUniqueNameReadable().toString() + " : Question(getTag() == '"
						+ question.getName() + "', isScoreSet('" + customVariable.getVariable().getName()
						+ "' )) from $" + category.getUniqueNameReadable().toString() + ".getQuestions() \n";
			}

		} else if (questionParent instanceof Group) {
			Group group = (Group) questionParent;
			conditions += this.simpleGroupConditions(group);

			if (this.getTreeObjectName(question) == null) {
				this.putTreeObjectName(question, question.getUniqueNameReadable().toString());
				conditions += "	$" + question.getUniqueNameReadable().toString() + " : Question(getTag() == '"
						+ question.getName() + "', isScoreSet('" + customVariable.getVariable().getName()
						+ "')) from $" + group.getUniqueNameReadable().toString() + ".getQuestions() \n";
			}
		}
		return conditions;
	}

	/**
	 * Parse conditions like => Score (logic operator (==, <=, <, >=, >)) value. <br>
	 * Create drools rule like => Category(isScoreSet('cScore'), getVariablevalue('cScore') == value )
	 * 
	 * @param expressionOperatorLogic
	 * 
	 * @param conditions
	 * @return LHS of the rule
	 */
	private String treeObjectScoreEqualsValueString(ExpressionValueCustomVariable var,
			ExpressionOperatorLogic expressionOperatorLogic, ExpressionValueString valueNumber) {
		String ruleCore = "";

		TreeObject scope = var.getReference();
		String varName = var.getVariable().getName();

		if (scope instanceof Form) {
			this.putTreeObjectName(scope, scope.getUniqueNameReadable().toString());
			ruleCore += "	$" + scope.getUniqueNameReadable().toString() + " : SubmittedForm(isScoreSet('" + varName
					+ "'), getVariableValue('" + varName + "') " + expressionOperatorLogic.getValue().toString() + " '"
					+ valueNumber.getValue() + "') from $droolsForm.getSubmittedForm() \n";

		} else if (scope instanceof Category) {
			TreeObject form = scope.getParent();
			this.putTreeObjectName(scope, scope.getUniqueNameReadable().toString());
			ruleCore += this.simpleFormCondition((Form) form);
			ruleCore += "	$" + scope.getUniqueNameReadable().toString() + " : Category( isScoreSet('" + varName
					+ "'), getVariableValue('" + varName + "') " + expressionOperatorLogic.getValue().toString() + " '"
					+ valueNumber.getValue() + "') from $" + form.getUniqueNameReadable().toString() + ".getCategory('"
					+ scope.getName() + "') \n";

		} else if (scope instanceof Group) {
			TreeObject groupParent = scope.getParent();
			this.putTreeObjectName(scope, scope.getUniqueNameReadable().toString());
			if (groupParent instanceof Category) {
				ruleCore += this.simpleCategoryConditions((Category) groupParent);
			} else if (groupParent instanceof Group) {
				ruleCore += this.simpleGroupConditions((Group) groupParent);
			}
			ruleCore += "	$" + scope.getUniqueNameReadable().toString() + " : Group( isScoreSet('" + varName
					+ "'), getVariableValue('" + varName + "') " + expressionOperatorLogic.getValue().toString() + " '"
					+ valueNumber.getValue() + "' ) from $" + groupParent.getUniqueNameReadable().toString()
					+ ".getGroup('" + scope.getName() + "') \n";

		} else if (scope instanceof Question) {
			TreeObject questionParent = scope.getParent();
			this.putTreeObjectName(scope, scope.getUniqueNameReadable().toString());
			if (questionParent instanceof Category) {
				ruleCore += this.simpleCategoryConditions((Category) questionParent);
			} else if (questionParent instanceof Group) {
				ruleCore += this.simpleGroupConditions((Group) questionParent);
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
	 * Create drools rule like => Category(isScoreSet('cScore'), getVariablevalue('cScore') == value )
	 * 
	 * @param conditions
	 * @return LHS of the rule
	 */
	private String treeObjectScoreLogicOperatorValueNumber(ExpressionValueCustomVariable var,
			ExpressionOperatorLogic operator, ExpressionValueNumber valueNumber) {
		String ruleCore = "";

		TreeObject scope = var.getReference();
		String varName = var.getVariable().getName();

		switch (var.getVariable().getType()) {
		case NUMBER:
			if (scope instanceof Form) {
				this.putTreeObjectName(scope, scope.getUniqueNameReadable().toString());
				ruleCore += "	$" + scope.getUniqueNameReadable().toString() + " : SubmittedForm(isScoreSet('"
						+ varName
						// + "'), getNumberVariableValue('" + varName + "') " +
						// operator.getValue().toString() + " "
						+ "'), getVariableValue('" + varName + "') " + operator.getValue().toString() + " "
						+ valueNumber.getValue() + ") from $droolsForm.getSubmittedForm() \n";

			} else if (scope instanceof Category) {
				TreeObject form = scope.getParent();
				this.putTreeObjectName(scope, scope.getUniqueNameReadable().toString());
				ruleCore += this.simpleFormCondition((Form) form);
				ruleCore += "	$"
						+ scope.getUniqueNameReadable().toString()
						+ " : Category( isScoreSet('"
						+ varName
						// + "'), getNumberVariableValue('" + varName + "') " +
						// operator.getValue().toString() + " "
						+ "'), getVariableValue('" + varName + "') " + operator.getValue().toString() + " "
						+ valueNumber.getValue() + ") from $" + form.getUniqueNameReadable().toString()
						+ ".getCategory('" + scope.getName() + "') \n";

			} else if (scope instanceof Group) {
				TreeObject groupParent = scope.getParent();
				this.putTreeObjectName(scope, scope.getUniqueNameReadable().toString());
				if (groupParent instanceof Category) {
					ruleCore += this.simpleCategoryConditions((Category) groupParent);
				} else if (groupParent instanceof Group) {
					ruleCore += this.simpleGroupConditions((Group) groupParent);
				}
				ruleCore += "	$"
						+ scope.getUniqueNameReadable().toString()
						+ " : Group( isScoreSet('"
						+ varName
						// + "'), getNumberVariableValue('" + varName + "') " +
						// operator.getValue().toString() + " "
						+ "'), getVariableValue('" + varName + "') " + operator.getValue().toString() + " "
						+ valueNumber.getValue() + " ) from $" + groupParent.getUniqueNameReadable().toString()
						+ ".getGroup('" + scope.getName() + "') \n";

			} else if (scope instanceof Question) {
				TreeObject questionParent = scope.getParent();
				this.putTreeObjectName(scope, scope.getUniqueNameReadable().toString());
				if (questionParent instanceof Category) {
					ruleCore += this.simpleCategoryConditions((Category) questionParent);
				} else if (questionParent instanceof Group) {
					ruleCore += this.simpleGroupConditions((Group) questionParent);
				}
				ruleCore += "	$"
						+ scope.getUniqueNameReadable().toString()
						+ " : Question( isScoreSet('"
						+ varName
						// + "'), getNumberVariableValue('" + varName + "') " +
						// operator.getValue().toString() + " "
						+ "'), getVariableValue('" + varName + "') " + operator.getValue().toString() + " "
						+ valueNumber.getValue() + " ) from $" + questionParent.getUniqueNameReadable().toString()
						+ ".getQuestions() \n";
			}
			break;
		case DATE:
		case STRING:
			if (scope instanceof Form) {
				this.putTreeObjectName(scope, scope.getUniqueNameReadable().toString());
				ruleCore += "	$" + scope.getUniqueNameReadable().toString() + " : SubmittedForm(isScoreSet('" + varName
						+ "'), getVariableValue('" + varName + "') " + operator.getValue().toString() + " "
						+ valueNumber.getValue() + ") from $droolsForm.getSubmittedForm() \n";

			} else if (scope instanceof Category) {
				TreeObject form = scope.getParent();
				this.putTreeObjectName(scope, scope.getUniqueNameReadable().toString());
				ruleCore += this.simpleFormCondition((Form) form);
				ruleCore += "	$" + scope.getUniqueNameReadable().toString() + " : Category( isScoreSet('" + varName
						+ "'), getVariableValue('" + varName + "') " + operator.getValue().toString() + " "
						+ valueNumber.getValue() + ") from $" + form.getUniqueNameReadable().toString()
						+ ".getCategory('" + scope.getName() + "') \n";

			} else if (scope instanceof Group) {
				TreeObject groupParent = scope.getParent();
				this.putTreeObjectName(scope, scope.getUniqueNameReadable().toString());
				if (groupParent instanceof Category) {
					ruleCore += this.simpleCategoryConditions((Category) groupParent);
				} else if (groupParent instanceof Group) {
					ruleCore += this.simpleGroupConditions((Group) groupParent);
				}
				ruleCore += "	$" + scope.getUniqueNameReadable().toString() + " : Group( isScoreSet('" + varName
						+ "'), getVariableValue('" + varName + "') " + operator.getValue().toString() + " "
						+ valueNumber.getValue() + " ) from $" + groupParent.getUniqueNameReadable().toString()
						+ ".getGroup('" + scope.getName() + "') \n";

			} else if (scope instanceof Question) {
				TreeObject questionParent = scope.getParent();
				this.putTreeObjectName(scope, scope.getUniqueNameReadable().toString());
				if (questionParent instanceof Category) {
					ruleCore += this.simpleCategoryConditions((Category) questionParent);
				} else if (questionParent instanceof Group) {
					ruleCore += this.simpleGroupConditions((Group) questionParent);
				}
				ruleCore += "	$" + scope.getUniqueNameReadable().toString() + " : Question( isScoreSet('" + varName
						+ "'), getVariableValue('" + varName + "') " + operator.getValue().toString() + " "
						+ valueNumber.getValue() + " ) from $" + questionParent.getUniqueNameReadable().toString()
						+ ".getQuestions() \n";
			}
		}
		return ruleCore;
	}
}
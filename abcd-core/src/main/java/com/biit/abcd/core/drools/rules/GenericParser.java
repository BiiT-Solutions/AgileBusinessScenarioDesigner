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
import com.biit.form.TreeObject;

public class GenericParser {

	private boolean cleaningNeeded = true;
	private HashMap<TreeObject, String> treeObjectDroolsname;
	private boolean droolsFormDefined = false;

	public GenericParser() {
		this.treeObjectDroolsname = new HashMap<TreeObject, String>();
	}

	/**
	 * Adds condition rows to the rule that manages the assignation of a
	 * variable in the action<br>
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

		result += "(\n";
		result += this.processParserResult(leftChain);
		result += "and\n";
		result += this.processParserResult(rightChain);
		result += ")\n";

		return result;
	}

	private String orOperator(List<Expression> expressions) {
		String result = "";

		ExpressionChain leftChain = (ExpressionChain) expressions.get(0);
		ExpressionChain rightChain = (ExpressionChain) expressions.get(2);

		result += "(\n";
		result += this.processParserResult(leftChain);
		result += "or\n";
		result += this.processParserResult(rightChain);
		result += ")\n";

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
				this.putTreeObjectName(leftReference, leftReference.getComparationIdNoDash().toString());
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
					droolsConditions += "	$" + leftQuestion.getComparationIdNoDash().toString()
							+ " : Question( getTag() == '" + leftQuestion.getName() + "', getAnswer() in( " + inValues
							+ " )) from $" + leftReferenceParent.getComparationIdNoDash().toString()
							+ ".getQuestions()\n";
				}
			}
		}
		return droolsConditions;
	}

	/**
	 * Parse actions like => Score = stringValue || Score = numberValue || Score
	 * = Function (parameters ...)<br>
	 * Create drools rule like => setVariableValue(scopeOfVariable,
	 * variableName, stringVariableValue )
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
				ruleCore += "	$" + this.getTreeObjectName(var.getReference()) + ".setVariableValue('"
						+ var.getVariable().getName() + "', '" + value.getValue() + "');\n";
				ruleCore += "	AbcdLogger.debug(\"DroolsRule\", \"Variable set (" + var.getReference().getName() + ", "
						+ var.getVariable().getName() + ", " + value.getValue() + ")\");\n";

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
	 * Checks the existence of a binding in drools with the the reference of the
	 * variable passed If there is no binding, creates a new one (i.e. $var :
	 * Question() ...)
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

	public String createDroolsRule(ExpressionChain conditions, ExpressionChain actions, String extraConditions)
			throws RuleNotImplementedException {

		// System.out.println("CONDITIONS: " + conditions);
		// System.out.println("ACTIONS: " + actions);
		
		this.treeObjectDroolsname.clear();
		String ruleCore = "";
		ruleCore += "	$droolsForm: DroolsForm() and\n";
		
		if (extraConditions != null) {
			ruleCore += extraConditions;
		}
		if (conditions != null) {
			ruleCore += this.parseConditions(conditions);
		}
		if (actions != null) {
			// Expression rule
			if (conditions == null) {
				ruleCore += this.parseExpressions(actions, extraConditions);
			}
			// Normal rule
			else {
				ruleCore += this.parseActions(actions);
			}
		}
		if (this.cleaningNeeded) {
			ruleCore = RulesUtils.newRemoveDuplicateLines(ruleCore);
			ruleCore = RulesUtils.checkForDuplicatedVariables(ruleCore);
			ruleCore = RulesUtils.removeExtraParenthesis(ruleCore);
			
		}
		return ruleCore;
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
							this.putTreeObjectName(leftVariable.getReference(), leftVariable.getComparationIdNoDash()
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
							case INPUT:
								switch (leftQuestion.getAnswerFormat()) {
								case DATE:
									if (leftVariable.getUnit() != null) {
										switch (leftVariable.getUnit()) {
										case YEARS:
											break;
										case MONTHS:
											break;
										case DAYS:
											break;
										case DATE:
											break;
										}
									}

									String instanceOfDate = "getAnswer() instanceof Date";
									String greatEqualsDate = "getAnswer() <= DateUtils.returnCurrentDateMinusYears("
											+ value1.intValue() + ")";
									String lessEqualsDate = "getAnswer() > DateUtils.returnCurrentDateMinusYears("
											+ value2.intValue() + ")";
									droolsConditions += "	$" + leftQuestion.getComparationIdNoDash().toString()
											+ " : Question( " + instanceOfDate + ", " + greatEqualsDate + ", "
											+ lessEqualsDate + ") from $"
											+ leftReferenceParent.getComparationIdNoDash().toString()
											+ ".getQuestions()\n";
									droolsConditions += "and\n";
								default:
									break;
								}
								break;
							default:
								droolsConditions += "	$" + leftQuestion.getComparationIdNoDash().toString()
										+ " : Question( getTag() == '" + leftQuestion.getName() + "', getAnswer() >= "
										+ value1 + " && < " + value2 + ") from $"
										+ leftReferenceParent.getComparationIdNoDash().toString()
										+ ".getQuestions() \n";
								droolsConditions += "and\n";
								break;
							}
						} else {
							String varName = leftVariable.getVariable().getName();

							switch (leftVariable.getVariable().getScope()) {
							case FORM:
								this.putTreeObjectName(leftVariable.getReference(), leftVariable.getReference()
										.getComparationIdNoDash().toString());
									droolsConditions += "	$"
											+ leftVariable.getReference().getComparationIdNoDash().toString()
											+ " : SubmittedForm( isScoreSet('" + varName
											+ "'), getNumberVariableValue('" + varName + "') >= " + value1 + " && < "
											+ value2 + ") from $droolsForm.getSubmittedForm() \n";
								break;
							case CATEGORY:
								droolsConditions += "	$"
										+ leftVariable.getReference().getComparationIdNoDash().toString()
										+ " : Category( isScoreSet('" + varName + "'), getNumberVariableValue('"
										+ varName + "') >= " + value1 + " && < " + value2 + ") from $"
										+ leftReferenceParent.getComparationIdNoDash().toString()
										+ ".getCategories() \n";
								break;
							case GROUP:
								droolsConditions += "	$"
										+ leftVariable.getReference().getComparationIdNoDash().toString()
										+ " : Group( isScoreSet('" + varName + "'), getNumberVariableValue('" + varName
										+ "') >= " + value1 + " && < " + value2 + ") from $"
										+ leftReferenceParent.getComparationIdNoDash().toString() + ".getGroups() \n";
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

	private String equalsOperator(List<Expression> expressions) {
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
				droolsConditions += this.questionAnswerEqualsCondition((Question) treeObject1, (Answer) treeObject2);
			}
		}
		// TreeObject.score == ValueNumber
		else if ((operatorLeft.size() == 1) && (operatorLeft.get(0) instanceof ExpressionValueCustomVariable)
				&& (operatorRight.size() == 1) && (operatorRight.get(0) instanceof ExpressionValueNumber)) {

			droolsConditions += this.treeObjectScoreLogicOperatorValueNumber(
					(ExpressionValueCustomVariable) operatorLeft.get(0), (ExpressionOperatorLogic) expressions.get(1),
					(ExpressionValueNumber) operatorRight.get(0));
		}
		// Question INPUT
		else if ((operatorLeft.size() == 1) && (operatorLeft.get(0) instanceof ExpressionValueTreeObjectReference)
				&& (operator instanceof ExpressionOperatorLogic) && (operatorRight.size() == 1)
				&& (operatorRight.get(0) instanceof ExpressionValueNumber)) {
			TreeObject leftTreeObject = ((ExpressionValueTreeObjectReference) operatorLeft.get(0)).getReference();
			Double value = ((ExpressionValueNumber) operatorRight.get(0)).getValue();

			TreeObject leftTreeObjectParent = leftTreeObject.getParent();
			this.putTreeObjectName(leftTreeObject, leftTreeObject.getComparationIdNoDash().toString());
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
	 * @param extraConditions
	 * @return the rule
	 * @throws RuleNotImplementedException
	 */
	private String genericAssignationFunctionAction(ExpressionChain actions, String extraConditions)
			throws RuleNotImplementedException {
		String ruleCore = "";
		// we have to generate a set of rules defined by the generic variable
		ruleCore += this.genericRuleSet(actions, extraConditions);
		this.cleaningNeeded = false;
		return ruleCore;
	}

	/**
	 * Creates a set of expressions and passes it back to the expression parser
	 * 
	 * @param expressions
	 * @param extraConditions
	 * @return
	 * @throws RuleNotImplementedException
	 */
	private String genericRuleSet(ExpressionChain expressionChain, String extraConditions)
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
				treeObjects.addAll(category.getChildren());
			}
			break;
		default:
			break;
		}
		// For each category, we generate the expression to create a new rule
		if (treeObjects != null) {
			for (TreeObject category : treeObjects) {
				ExpressionValueCustomVariable expValCat = new ExpressionValueCustomVariable(category,
						genericVarToCalculate.getVariable());
				// Remove the generic
				expressionChain.getExpressions().remove(0);
				// Add the specific
				expressionChain.getExpressions().add(0, expValCat);

				try {
					ruleCore += new ExpressionParser().parse(expressionChain, extraConditions);
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

	private String getTreeObjectName(TreeObject treeObject) {
		return this.treeObjectDroolsname.get(treeObject);
	}

	private String mathAssignationAction(ExpressionChain actions, String extraConditions)
			throws RuleNotImplementedException {
		String ruleCore = "";
		if (extraConditions != null) {
			ruleCore += extraConditions;
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
	 * @param extraConditions
	 * @return the rule
	 */
	private String maxMinAvgAssignationFunctionAction(List<Expression> actions, String extraConditions) {
		String ruleCore = "";
		if (extraConditions != null) {
			ruleCore += extraConditions;
		}
		// LHS
		// We will have some expression of type Category.score = (Min | Max |
		// Avg) of some values
		ExpressionValueCustomVariable variableToCalculate = (ExpressionValueCustomVariable) actions.get(0);
		// The rule is different if the variable to assign is a Form, a Category
		// or a Group
		TreeObject leftTreeObject = variableToCalculate.getReference();
		if (leftTreeObject instanceof Form) {
			ruleCore += this.simpleFormCondition((Form) leftTreeObject);

		} else if (leftTreeObject instanceof Category) {
			ruleCore += this.simpleCategoryConditions((Category) leftTreeObject);

		} else if (leftTreeObject instanceof Group) {
			ruleCore += this.simpleGroupConditions((Group) leftTreeObject);
		}

		int varIndex = 1;
		String scopeClass = "";
		TreeObject parent = null;
		CustomVariable cVar = null;
		for (int i = 3; i < actions.size(); i++) {
			Expression expression = actions.get(i);
			if (expression instanceof ExpressionValueCustomVariable) {
				ExpressionValueCustomVariable aux = (ExpressionValueCustomVariable) expression;
				TreeObject to = aux.getReference();
				cVar = aux.getVariable();
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
					ruleCore += "	$var : List() from collect( " + scopeClass + "(isScoreSet('" + cVar.getName()
							+ "'), getTag() == '" + to.getName() + "' || ";
				} else {
					ruleCore += "== '" + to.getName() + "' || ";
				}
				varIndex++;
			}
			// Generic variable inside the function
			else if (expression instanceof ExpressionValueGenericCustomVariable) {
				ExpressionValueGenericCustomVariable aux = (ExpressionValueGenericCustomVariable) expression;
				List<TreeObject> treeObjects = new ArrayList<TreeObject>();
				cVar = aux.getVariable();
				switch (aux.getType()) {
				case CATEGORY:
					scopeClass = "Category";
					for (TreeObject to : leftTreeObject.getChildren()) {
						if (to instanceof Category) {
							treeObjects.add(to);
						}
					}
					break;
				case GROUP:
					scopeClass = "Group";
					for (TreeObject to : leftTreeObject.getChildren()) {
						if (to instanceof Group) {
							treeObjects.add(to);
						}
					}
					break;
				case QUESTION_CATEGORY:
				case QUESTION_GROUP:
					scopeClass = "Question";
					for (TreeObject to : leftTreeObject.getChildren()) {
						if (to instanceof Question) {
							treeObjects.add(to);
						}
					}
					break;
				}
				int genericVarIndex = 1;
				for (TreeObject to : treeObjects) {
					if (genericVarIndex == 1) {
						ruleCore += "	$var : List() from collect( " + scopeClass + "(isScoreSet('" + cVar.getName()
								+ "'), getTag() == '" + to.getName() + "' || ";
					} else {
						ruleCore += "== '" + to.getName() + "' || ";
					}
					genericVarIndex++;
					parent = to.getParent();
				}
			}
		}
		// Finish the line of the condition
		ruleCore = ruleCore.substring(0, ruleCore.length() - 3);
		if (scopeClass.equals("Question")) {
			ruleCore += ") from $" + parent.getComparationIdNoDash().toString() + ".getQuestions()) \n";
		} else if (scopeClass.equals("Group")) {
			ruleCore += ") from $" + parent.getComparationIdNoDash().toString() + ".getGroups()) \n";
		} else if (scopeClass.equals("Category")) {
			ruleCore += ") from $" + parent.getComparationIdNoDash().toString() + ".getCategories())\n";
		}

		String getVarValue = "getVariableValue('" + cVar.getName() + "')";
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
			// ruleCore += "	System.out.println(\"Variable set (" +
			// variableToCalculate.getReference().getName() + ", "
			// + variableToCalculate.getVariable().getName() +
			// ", \" + $sol +\")\");\n";
			ruleCore += "	AbcdLogger.debug(\"DroolsRule\", \"Variable set ("
					+ variableToCalculate.getReference().getName() + ", " + variableToCalculate.getVariable().getName()
					+ ", \" + $sol +\")\");\n";
		}
		return ruleCore;
	}

	/**
	 * Parse actions like => Score = Score (+|-|/|*) numberValue <br>
	 * Create drools rule like => setVariableValue(scopeOfVariable,
	 * variableName, getVariablevalue(variableName)+numberValue ) <br>
	 * The variables to the right and to the left of the assignation can be
	 * different (i.e. a = b+1) <br>
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
			ruleCore += "	$" + this.getTreeObjectName(var.getReference()) + ".setVariableValue('" + customVarName
					+ "', " + "(Double)$" + this.getTreeObjectName(var2.getReference()) + ".getNumberVariableValue('"
					+ customVarName + "') " + operator.getValue() + " " + valueNumber.getValue() + ");\n";
			// ruleCore += "	System.out.println( \"Variable updated (" +
			// var.getReference().getName() + ", "
			// + customVarName + ", " + operator.getValue() +
			// valueNumber.getValue() + ")\");\n";
			ruleCore += "	AbcdLogger.debug(\"DroolsRule\", \"Variable updated (" + var.getReference().getName() + ", "
					+ customVarName + ", " + operator.getValue() + valueNumber.getValue() + ")\");\n";

		} else if (actions.get(2) instanceof ExpressionValueNumber) {
			ExpressionValueNumber valueNumber = (ExpressionValueNumber) actions.get(2);
			ExpressionValueCustomVariable var2 = (ExpressionValueCustomVariable) actions.get(4);
			ruleCore += this.checkVariableAssignation(var2);

			ruleCore += RulesUtils.getThenRuleString();
			ruleCore += "	$" + this.getTreeObjectName(var.getReference()) + ".setVariableValue('" + customVarName
					+ "', " + "(Double)$" + this.getTreeObjectName(var2.getReference()) + ".getNumberVariableValue('"
					+ customVarName + "') " + operator.getValue() + " " + valueNumber.getValue() + ");\n";
			// ruleCore += "	System.out.println( \"Variable updated (" +
			// var.getReference().getName() + ", "
			// + customVarName + ", " + operator.getValue() +
			// valueNumber.getValue() + ")\");\n";
			ruleCore += "	AbcdLogger.debug(\"DroolsRule\", \"Variable updated (" + var.getReference().getName() + ", "
					+ customVarName + ", " + operator.getValue() + valueNumber.getValue() + ")\");\n";

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
			// Action type => (cat.scoreText = "someText") || (cat.score =
			// someValue)
			if ((actions.get(0) instanceof ExpressionValueCustomVariable)
					&& (actions.get(1) instanceof ExpressionOperatorMath)
					&& (((ExpressionOperatorMath) actions.get(1)).getValue().equals(AvailableOperator.ASSIGNATION))
					&& ((actions.get(2) instanceof ExpressionValueString)
							|| (actions.get(2) instanceof ExpressionValueNumber) || (actions.get(2) instanceof ExpressionFunction))) {
				return this.assignationAction(result.getExpressionChain());
			}
			// Action type => cat.scoreText = cat.scoreText + 1
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
			return this.processParserResult(result.getExpressionChain());
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
	private String parseExpressions(ExpressionChain expressionChain, String extraConditions)
			throws RuleNotImplementedException {
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
				return this.maxMinAvgAssignationFunctionAction(expressionChain.getExpressions(), extraConditions);
			case PMT:
				return this.pmtAssignationFunctionAction(expressionChain.getExpressions(), extraConditions);
			}

		} else if ((expressionChain.getExpressions().get(0) instanceof ExpressionValueGenericCustomVariable)
				&& (expressionChain.getExpressions().get(1) instanceof ExpressionOperatorMath)
				&& (((ExpressionOperatorMath) expressionChain.getExpressions().get(1)).getValue()
						.equals(AvailableOperator.ASSIGNATION))
				&& (expressionChain.getExpressions().get(2) instanceof ExpressionFunction)) {
			return this.genericAssignationFunctionAction(expressionChain, extraConditions);
		}
		// Generic calculation
		else if ((expressionChain.getExpressions().get(0) instanceof ExpressionValueCustomVariable)
				&& (expressionChain.getExpressions().get(1) instanceof ExpressionOperatorMath)
				&& (((ExpressionOperatorMath) expressionChain.getExpressions().get(1)).getValue()
						.equals(AvailableOperator.ASSIGNATION))) {
			return this.mathAssignationAction(expressionChain, extraConditions);
		}

		throw new RuleNotImplementedException("Rule not implemented.", expressionChain);
	}

	private String pmtAssignationFunctionAction(List<Expression> actions, String extraConditions) {
		String ruleCore = "";
		if (extraConditions != null) {
			ruleCore += extraConditions;
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

		System.out.println("PARSED EXPRESSION: " + parsedExpression);

		if ((parsedExpression != null) && (parsedExpression.getExpressions().size() == 3)) {
			List<Expression> expressions = parsedExpression.getExpressions();

			// Operators
			if (expressions.get(1) instanceof ExpressionOperatorLogic) {
				switch (((ExpressionOperatorLogic) expressions.get(1)).getValue()) {
				case EQUALS:
					return this.equalsOperator(expressions);
				case AND:
					return this.andOperator(expressions);
					// case OR:
					// return this.orOperator(expressions);
				case GREATER_EQUALS:
				case GREATER_THAN:
				case LESS_EQUALS:
				case LESS_THAN:
					return this.questionGeGtLeLtAnswer(expressions,
							((ExpressionOperatorLogic) expressions.get(1)).getValue());
				default:
					break;
				}
			} else if (expressions.get(1) instanceof ExpressionFunction) {
				switch (((ExpressionFunction) expressions.get(1)).getValue()) {
				case MIN:
					// return this.genericMinFunction(expressions);
					break;
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

	private String questionAnswerEqualsCondition(Question question, Answer answer) {
		String droolsConditions = "(\n";
		// Check the parent of the question
		TreeObject questionParent = question.getParent();
		this.putTreeObjectName(question, question.getComparationIdNoDash().toString());
		if (questionParent instanceof Category) {
			droolsConditions += this.simpleCategoryConditions((Category) questionParent);
		} else if (questionParent instanceof Group) {
			droolsConditions += this.simpleGroupConditions((Group) questionParent);
		}
		this.putTreeObjectName(question, question.getComparationIdNoDash().toString());
		droolsConditions += "	$" + question.getComparationIdNoDash().toString() + " : Question( getTag() == '"
				+ question.getName() + "', getAnswer() == '" + answer.getName() + "') from $"
				+ questionParent.getComparationIdNoDash().toString() + ".getQuestions()\n";
		return droolsConditions + ")\n";
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
	private String questionBetweenAnswersCondition(List<Expression> conditions) {

		// System.out.println("QUESTION BETWEEN");
		// System.out.println("CONDITIONS: " + conditions);

		String droolsConditions = "";
		List<Expression> operatorLeft = ((ExpressionChain) conditions.get(0)).getExpressions();
		TreeObject leftReference = null;
		if ((operatorLeft.size() == 1) && (operatorLeft.get(0) instanceof ExpressionValueTreeObjectReference)) {

			leftReference = ((ExpressionValueTreeObjectReference) operatorLeft.get(0)).getReference();
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
						this.putTreeObjectName(leftReference, leftReference.getComparationIdNoDash().toString());
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
							case INPUT:
								switch (leftQuestion.getAnswerFormat()) {
								case DATE:
									String instanceOfDate = "getAnswer() instanceof Date";
									String greatEqualsDate = "getAnswer() <= DateUtils.returnCurrentDateMinusYears("
											+ value1.intValue() + ")";
									String lessEqualsDate = "getAnswer() > DateUtils.returnCurrentDateMinusYears("
											+ value2.intValue() + ")";
									droolsConditions += "	$" + leftQuestion.getComparationIdNoDash().toString()
											+ " : Question( " + instanceOfDate + ", " + greatEqualsDate + ", "
											+ lessEqualsDate + ") from $"
											+ leftReferenceParent.getComparationIdNoDash().toString()
											+ ".getQuestions()\n";
									droolsConditions += "and\n";
								default:
									break;
								}
								break;
							default:
								droolsConditions += "	$" + leftQuestion.getComparationIdNoDash().toString()
										+ " : Question( getTag() == '" + leftQuestion.getName() + "', getAnswer() >= "
										+ value1 + " && < " + value2 + ") from $"
										+ leftReferenceParent.getComparationIdNoDash().toString()
										+ ".getQuestions() \n";
								droolsConditions += "and\n";
								break;
							}
						}
					}
				}
			}
		}
		return droolsConditions;
	}

	private String questionDateDaysOperatorValueNumber(TreeObject question, Double value, AvailableOperator operator) {
		return "	$" + question.getComparationIdNoDash().toString()
				+ " : Question(getAnswer() instanceof Date, DateUtils.returnDaysDistanceFromDate(getAnswer()) "
				+ operator.getValue() + " " + value.intValue() + ") from $"
				+ question.getParent().getComparationIdNoDash().toString() + ".getQuestions()\n";
	}

	private String questionDateMonthsOperatorValueNumber(TreeObject question, Double value, AvailableOperator operator) {
		return "	$" + question.getComparationIdNoDash().toString()
				+ " : Question(getAnswer() instanceof Date, DateUtils.returnMonthDistanceFromDate(getAnswer()) "
				+ operator.getValue() + " " + value.intValue() + ") from $"
				+ question.getParent().getComparationIdNoDash().toString() + ".getQuestions()\n";
	}

	private String questionDateYearsOperatorValueNumber(TreeObject question, Double value, AvailableOperator operator) {
		return "	$" + question.getComparationIdNoDash().toString()
				+ " : Question(getAnswer() instanceof Date, DateUtils.returnYearDistanceFromDate(getAnswer()) "
				+ operator.getValue() + " " + value.intValue() + ") from $"
				+ question.getParent().getComparationIdNoDash().toString() + ".getQuestions()\n";
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
						this.putTreeObjectName(leftTreeObject, leftTreeObject.getComparationIdNoDash().toString());
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
							if (leftQuestion.getAnswerType().equals(AnswerType.INPUT)
									&& leftQuestion.getAnswerFormat().equals(AnswerFormat.DATE)) {
								droolsConditions += this.questionDateOperatorValue(leftTreeObjectParent, leftQuestion,
										operator, value);

								// switch (operator) {
								// case GREATER_EQUALS:
								// return
								// this.questionValueDateGreaterEqualsAnswer(leftTreeObjectParent,
								// leftQuestion, value);
								// case GREATER_THAN:
								// return
								// this.questionValueDateGreaterThanAnswer(leftTreeObjectParent,
								// leftQuestion,
								// value);
								// case LESS_EQUALS:
								// return
								// this.questionValueDateLessEqualsAnswer(leftTreeObjectParent,
								// leftQuestion,
								// value);
								// case LESS_THAN:
								// return
								// this.questionValueDateLessThanAnswer(leftTreeObjectParent,
								// leftQuestion,
								// value);
								// default:
								// break;
								// }
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
							this.putTreeObjectName(leftQuestion, leftQuestion.getComparationIdNoDash().toString());
							// Check the parent
							if (leftQuestionParent instanceof Category) {
								droolsConditions += this.simpleCategoryConditions((Category) leftQuestionParent);
							} else if (leftQuestionParent instanceof Group) {
								droolsConditions += this.simpleGroupConditions((Group) leftQuestionParent);
							}

							TreeObject rightQuestionParent = rightQuestion.getParent();
							this.putTreeObjectName(rightQuestion, rightQuestion.getComparationIdNoDash().toString());
							// Check the parent
							if (rightQuestionParent instanceof Category) {
								droolsConditions += this.simpleCategoryConditions((Category) rightQuestionParent);
							} else if (rightQuestionParent instanceof Group) {
								droolsConditions += this.simpleGroupConditions((Group) rightQuestionParent);
							}
							droolsConditions += "	$" + leftQuestion.getComparationIdNoDash().toString()
									+ " : Question(getAnswer() instanceof Date, getTag() == '" + leftQuestion.getName()
									+ "') from $" + leftQuestionParent.getComparationIdNoDash().toString()
									+ ".getQuestions() and\n";
							droolsConditions += "	$" + rightQuestion.getComparationIdNoDash().toString()
									+ " : Question(getAnswer() instanceof Date, getTag() == '"
									+ rightQuestion.getName() + "', getAnswer() " + operator.getValue() + " $"
									+ leftQuestion.getComparationIdNoDash().toString() + ".getAnswer()) from $"
									+ rightQuestionParent.getComparationIdNoDash().toString() + ".getQuestions() and\n";
						}
					}
				}
				// Comparison with system date (Special date)
				else if ((leftTreeObject instanceof Question) && (rightExpressions.size() == 1)
						&& (rightExpressions.get(0) instanceof ExpressionValueSystemDate)) {
					Question leftQuestion = (Question) leftTreeObject;
					TreeObject leftQuestionParent = leftQuestion.getParent();
					this.putTreeObjectName(leftQuestion, leftQuestion.getComparationIdNoDash().toString());
					// Check the parent
					if (leftQuestionParent instanceof Category) {
						droolsConditions += this.simpleCategoryConditions((Category) leftQuestionParent);
					} else if (leftQuestionParent instanceof Group) {
						droolsConditions += this.simpleGroupConditions((Group) leftQuestionParent);
					}
					droolsConditions += "	$" + leftQuestion.getComparationIdNoDash().toString()
							+ " : Question(getAnswer() instanceof Date, getTag() == '" + leftQuestion.getName()
							+ "', getAnswer() " + operator.getValue() + " DateUtils.returnCurrentDate()) from $"
							+ leftQuestionParent.getComparationIdNoDash().toString() + ".getQuestions() and\n";
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

	private String questionDateOperatorValue(TreeObject leftReferenceParent, TreeObject leftQuestion,
			AvailableOperator operator, Double value) {
		String rule = "";

		// Check if the reference exists in the rule, if not, it creates
		// a new reference
		rule += this.checkVariableAssignation(leftQuestion);

		rule += "	$" + leftQuestion.getComparationIdNoDash().toString()
				+ " : Question(getAnswer() instanceof Date, DateUtils.returnYearDistanceFromDate(getAnswer()) "
				+ operator.getValue() + value.intValue() + ") from $"
				+ leftReferenceParent.getComparationIdNoDash().toString() + ".getQuestions()\n";
		return rule;
	}

	// private String questionValueDateGreaterEqualsAnswer(TreeObject
	// leftReferenceParent, TreeObject leftQuestion,
	// Double value) {
	// return "	$" + leftQuestion.getComparationIdNoDash().toString()
	// +
	// " : Question(getAnswer() instanceof Date, getAnswer() <= DateUtils.returnCurrentDateMinusYears("
	// + value.intValue() + ")) from $" +
	// leftReferenceParent.getComparationIdNoDash().toString()
	// + ".getQuestions()\n";
	// }
	//
	// private String questionValueDateGreaterThanAnswer(TreeObject
	// leftReferenceParent, TreeObject leftQuestion,
	// Double value) {
	// return "	$" + leftQuestion.getComparationIdNoDash().toString()
	// +
	// " : Question(getAnswer() instanceof Date, getAnswer() < DateUtils.returnCurrentDateMinusYears("
	// + value.intValue() + ")) from $" +
	// leftReferenceParent.getComparationIdNoDash().toString()
	// + ".getQuestions()\n";
	// }
	//
	// private String questionValueDateLessEqualsAnswer(TreeObject
	// leftReferenceParent, TreeObject leftQuestion,
	// Double value) {
	// return "	$" + leftQuestion.getComparationIdNoDash().toString()
	// +
	// " : Question(getAnswer() instanceof Date, getAnswer() >= DateUtils.returnCurrentDateMinusYears("
	// + value.intValue() + ")) from $" +
	// leftReferenceParent.getComparationIdNoDash().toString()
	// + ".getQuestions()\n";
	// }
	//
	// private String questionValueDateLessThanAnswer(TreeObject
	// leftReferenceParent, TreeObject leftQuestion, Double value) {
	// return "	$" + leftQuestion.getComparationIdNoDash().toString()
	// +
	// " : Question(getAnswer() instanceof Date, getAnswer() > DateUtils.returnCurrentDateMinusYears("
	// + value.intValue() + ")) from $" +
	// leftReferenceParent.getComparationIdNoDash().toString()
	// + ".getQuestions()\n";
	// }

	private String simpleCategoryConditions(Category category) {
		String conditions = "";
		Form form = (Form) category.getParent();
		conditions += this.simpleFormCondition(form);

		if (this.getTreeObjectName(category) == null) {
			this.putTreeObjectName(category, category.getComparationIdNoDash().toString());
			conditions += "	$" + category.getComparationIdNoDash().toString() + " : Category() from $"
					+ form.getComparationIdNoDash().toString() + ".getCategory('" + category.getName() + "') and\n";
		}
		return conditions;
	}

	private String simpleCategoryCustomVariableConditions(ExpressionValueCustomVariable customVariable) {
		Category category = (Category) customVariable.getReference();
		String conditions = "";
		Form form = (Form) category.getParent();
		conditions += this.simpleFormCondition(form);

		if (this.getTreeObjectName(category) == null) {
			this.putTreeObjectName(category, category.getComparationIdNoDash().toString());
			conditions += "	$" + category.getComparationIdNoDash().toString() + " : Category( isScoreSet('"
					+ customVariable.getVariable().getName() + "')) from $" + form.getComparationIdNoDash().toString()
					+ ".getCategory('" + category.getName() + "') and\n";
		}
		return conditions;
	}

	private String simpleFormCondition(Form form) {
		String droolsRule = "";
		if (this.getTreeObjectName(form) == null) {
			this.putTreeObjectName(form, form.getComparationIdNoDash().toString());
			
				droolsRule += "	$" + form.getComparationIdNoDash().toString()
						+ " : SubmittedForm() from $droolsForm.getSubmittedForm() and\n";
		}
		return droolsRule;
	}

//	private String simpleFormCustomVariableCondition(ExpressionValueCustomVariable customVariable) {
//		String droolsRule = "";
//		Form form = (Form) customVariable.getReference();
//		if (this.getTreeObjectName(form) == null) {
//			this.putTreeObjectName(form, form.getComparationIdNoDash().toString());
//			droolsRule += "	$droolsForm : DroolsForm() and\n";
//			droolsRule += "	$" + form.getComparationIdNoDash().toString() + " : SubmittedForm( isScoreSet('"
//					+ customVariable.getVariable().getName() + "')) from $droolsForm.getSubmittedForm() and\n";
//		}
//		return "";
//	}

	private String simpleGroupConditions(Group group) {
		String conditions = "";
		Category category = (Category) group.getParent();
		conditions += this.simpleCategoryConditions(category);

		if (this.getTreeObjectName(group) == null) {
			this.putTreeObjectName(group, group.getComparationIdNoDash().toString());
			conditions += "	$" + group.getComparationIdNoDash().toString() + " : Group() from $"
					+ category.getComparationIdNoDash().toString() + ".getGroup('" + group.getName() + "') and\n";
		}
		return conditions;
	}

	private String simpleGroupCustomVariableConditions(ExpressionValueCustomVariable customVariable) {
		Group group = (Group) customVariable.getReference();
		String conditions = "";
		Category category = (Category) group.getParent();
		conditions += this.simpleCategoryConditions(category);

		if (this.getTreeObjectName(group) == null) {
			this.putTreeObjectName(group, group.getComparationIdNoDash().toString());
			conditions += "	$" + group.getComparationIdNoDash().toString() + " : Group( isScoreSet('"
					+ customVariable.getVariable().getName() + "')) from $"
					+ category.getComparationIdNoDash().toString() + ".getGroup('" + group.getName() + "') and\n";
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
				this.putTreeObjectName(question, question.getComparationIdNoDash().toString());
				conditions += "	$" + question.getComparationIdNoDash().toString() + " : Question( getTag() == '"
						+ question.getName() + "') from $" + category.getComparationIdNoDash().toString()
						+ ".getQuestions() and\n";
			}

		} else if (questionParent instanceof Group) {
			Group group = (Group) questionParent;
			conditions += this.simpleGroupConditions(group);

			if (this.getTreeObjectName(question) == null) {
				this.putTreeObjectName(question, question.getComparationIdNoDash().toString());
				conditions += "	$" + question.getComparationIdNoDash().toString() + " : Question( getTag() == '"
						+ question.getName() + "') from $" + group.getComparationIdNoDash().toString()
						+ ".getQuestions() and\n";
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
				this.putTreeObjectName(question, question.getComparationIdNoDash().toString());
				conditions += "	$" + question.getComparationIdNoDash().toString() + " : Question( getTag() == '"
						+ question.getName() + "', isScoreSet('" + customVariable.getVariable().getName()
						+ "' )) from $" + category.getComparationIdNoDash().toString() + ".getQuestions() and\n";
			}

		} else if (questionParent instanceof Group) {
			Group group = (Group) questionParent;
			conditions += this.simpleGroupConditions(group);

			if (this.getTreeObjectName(question) == null) {
				this.putTreeObjectName(question, question.getComparationIdNoDash().toString());
				conditions += "	$" + question.getComparationIdNoDash().toString() + " : Question( getTag() == '"
						+ question.getName() + "', isScoreSet('" + customVariable.getVariable().getName()
						+ "')) from $" + group.getComparationIdNoDash().toString() + ".getQuestions() and\n";
			}
		}
		return conditions;
	}

	/**
	 * Parse conditions like => Score (logic operator (==, <=, <, >=, >)) value. <br>
	 * Create drools rule like => Category(isScoreSet('cScore'),
	 * getVariablevalue('cScore') == value )
	 * 
	 * @param conditions
	 * @return LHS of the rule
	 */
	private String treeObjectScoreLogicOperatorValueNumber(ExpressionValueCustomVariable var,
			ExpressionOperatorLogic operator, ExpressionValueNumber valueNumber) {
		String ruleCore = "";

		TreeObject scope = var.getReference();
		String varName = var.getVariable().getName();

		if (scope instanceof Form) {
			this.putTreeObjectName(scope, scope.getComparationIdNoDash().toString());
				ruleCore += "	$" + scope.getComparationIdNoDash().toString() + " : SubmittedForm( isScoreSet('" + varName
						+ "'), getNumberVariableValue('" + varName + "') " + operator.getValue().toString() + " "
						+ valueNumber.getValue() + ") from $droolsForm.getSubmittedForm() \n";

		} else if (scope instanceof Category) {
			TreeObject form = scope.getParent();
			this.putTreeObjectName(scope, scope.getComparationIdNoDash().toString());
			ruleCore += this.simpleFormCondition((Form) form);
			ruleCore += "	$" + scope.getComparationIdNoDash().toString() + " : Category( isScoreSet('" + varName
					+ "'), getNumberVariableValue('" + varName + "') " + operator.getValue().toString() + " "
					+ valueNumber.getValue() + ") from $" + form.getComparationIdNoDash().toString() + ".getCategory('"
					+ scope.getName() + "') and\n";

		} else if (scope instanceof Group) {
			TreeObject category = scope.getParent();
			this.putTreeObjectName(scope, scope.getComparationIdNoDash().toString());
			ruleCore += this.simpleCategoryConditions((Category) category);
			ruleCore += "	$" + scope.getComparationIdNoDash().toString() + " : Group( isScoreSet('" + varName
					+ "'), getNumberVariableValue('" + varName + "') " + operator.getValue().toString() + " "
					+ valueNumber.getValue() + " ) from $" + category.getComparationIdNoDash().toString()
					+ ".getGroup('" + scope.getName() + "') and\n";

		} else if (scope instanceof Question) {
			TreeObject questionParent = scope.getParent();
			this.putTreeObjectName(scope, scope.getComparationIdNoDash().toString());
			if (questionParent instanceof Category) {
				ruleCore += this.simpleCategoryConditions((Category) questionParent);
			} else if (questionParent instanceof Group) {
				ruleCore += this.simpleGroupConditions((Group) questionParent);
			}
			ruleCore += "	$" + scope.getComparationIdNoDash().toString() + " : Question( isScoreSet('" + varName
					+ "'), getNumberVariableValue('" + varName + "') " + operator.getValue().toString() + " "
					+ valueNumber.getValue() + " ) from $" + questionParent.getComparationIdNoDash().toString()
					+ ".getQuestions() and\n";
		}
		return ruleCore;
	}
	
	public void setDroolsFormDefined(boolean droolsFormDefined){
		this.droolsFormDefined = droolsFormDefined;
	}
}
package com.biit.abcd.core.drools.rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.biit.abcd.core.drools.prattparser.ExpressionChainParser;
import com.biit.abcd.core.drools.prattparser.ParseException;
import com.biit.abcd.core.drools.prattparser.Parser;
import com.biit.abcd.core.drools.prattparser.visitor.ITreeElement;
import com.biit.abcd.core.drools.prattparser.visitor.ITreeElementVisitor;
import com.biit.abcd.core.drools.prattparser.visitor.TreeElementDroolsVisitor;
import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
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
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.form.TreeObject;

public class GenericParser {

	private HashMap<TreeObject, String> treeObjectDroolsname;
	private boolean cleaningNeeded = true;

	public GenericParser() {
		this.treeObjectDroolsname = new HashMap<TreeObject, String>();
	}

	public void putTreeObjectName(TreeObject treeObject, String value) {
		this.treeObjectDroolsname.put(treeObject, value);
	}

	public String getTreeObjectName(TreeObject treeObject) {
		return this.treeObjectDroolsname.get(treeObject);
	}

	public String createDroolsRule(List<Expression> conditions, List<Expression> actions, String extraConditions) {
		String ruleCore = "";
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
			ruleCore = Utils.newRemoveDuplicateLines(ruleCore);
		}
		return ruleCore;
	}

	private String parseConditions(List<Expression> conditions) {
		Parser parser = new ExpressionChainParser(conditions);
		ITreeElement resultVisitor = null;
		try {
			resultVisitor = parser.parseExpression();
			ITreeElementVisitor treePrint = new TreeElementDroolsVisitor();
			resultVisitor.accept(treePrint);
		} catch (ParseException ex) {
			AbcdLogger.errorMessage(this.getClass().getName(), ex);
		}

		// *******************************************************************************************************
		// After this point the expression chain is the result of the parsing
		// engine. The expression chain has AST (abstract syntax tree) form
		// *******************************************************************************************************

		return this.processParserResult(resultVisitor.getExpressionChain());

		// // Condition of type (something AND something AND ...) or (something
		// OR
		// // something OR ...)
		// // Currently no combinations of ANDs and ORs are allowed
		// if (conditions.size() > 6) {
		// int startAndConditionIndex = 0;
		// int endAndConditionIndex = 0;
		// int startOrConditionIndex = 0;
		// int endOrConditionIndex = 0;
		// // Creation of substrings defined by the AND or the OR expressions
		// // Each subCondition list is parsed individually
		// List<List<Expression>> andSubConditions = new
		// ArrayList<List<Expression>>();
		// List<List<Expression>> orSubConditions = new
		// ArrayList<List<Expression>>();
		// for (Expression condition : conditions) {
		// if (condition instanceof ExpressionOperatorLogic) {
		// switch (((ExpressionOperatorLogic) condition).getValue()) {
		// case AND:
		// andSubConditions.add(conditions.subList(startAndConditionIndex,
		// endAndConditionIndex));
		// startAndConditionIndex = endAndConditionIndex + 1;
		// break;
		// case OR:
		// orSubConditions.add(conditions.subList(startOrConditionIndex,
		// endOrConditionIndex));
		// startOrConditionIndex = endOrConditionIndex + 1;
		// break;
		// default:
		// break;
		// }
		// }
		// endAndConditionIndex++;
		// endOrConditionIndex++;
		// }
		// // Add the last subCondition
		// andSubConditions.add(conditions.subList(startAndConditionIndex,
		// endAndConditionIndex));
		// // Add the last subCondition
		// orSubConditions.add(conditions.subList(startOrConditionIndex,
		// endOrConditionIndex));
		// if (startAndConditionIndex != 0) {
		// return this.conditionsWithAnd(andSubConditions);
		// } else if (startOrConditionIndex != 0) {
		// System.out.println("OR PARSING");
		// return this.conditionsWithOr(orSubConditions);
		// }
		// }
		// // Condition of type (Question | Answer)
		// if ((conditions.size() == 2) && (conditions.get(0) instanceof
		// ExpressionValueTreeObjectReference)
		// && (conditions.get(1) instanceof ExpressionChain)) {
		// TreeObject questionObject = ((ExpressionValueTreeObjectReference)
		// conditions.get(0)).getReference();
		// if ((questionObject != null) && (questionObject instanceof Question))
		// {
		// List<Expression> answerExpressions = ((ExpressionChain)
		// conditions.get(1)).getExpressions();
		// return this.questionAnswerEqualsCondition((Question) questionObject,
		// answerExpressions);
		// }
		// }
		// // Condition of type (Question | Answer)
		// if ((conditions.size() == 2) && (conditions.get(0) instanceof
		// ExpressionValueTreeObjectReference)
		// && (conditions.get(1) instanceof ExpressionValueTreeObjectReference))
		// {
		// TreeObject questionObject = ((ExpressionValueTreeObjectReference)
		// conditions.get(0)).getReference();
		// if ((questionObject != null) && (questionObject instanceof Question))
		// {
		// return this.questionAnswerEqualsCondition((Question) questionObject,
		// (ExpressionValueTreeObjectReference) conditions.get(1));
		//
		// // List<Expression> answerExpressions =
		// // ((ExpressionChain)conditions.get(1)).getExpressions();
		// // return
		// // this.questionAnswerEqualsCondition((Question)questionObject,
		// // answerExpressions);
		// }
		// }
		// // Condition of type (cat.score == value)
		// else if ((conditions.size() == 3) && (conditions.get(0) instanceof
		// ExpressionValueCustomVariable)
		// && (conditions.get(1) instanceof ExpressionOperatorLogic)
		// && (((ExpressionOperatorLogic)
		// conditions.get(1)).getValue().equals(AvailableOperator.EQUALS))
		// && (conditions.get(2) instanceof ExpressionValueNumber)) {
		// return this.scoreEqualsCondition(conditions);
		// }
		// // Condition of type (question1 == answer11)
		// else if ((conditions.size() == 3) && (conditions.get(0) instanceof
		// ExpressionValueTreeObjectReference)
		// && (conditions.get(1) instanceof ExpressionOperatorLogic)
		// && (((ExpressionOperatorLogic)
		// conditions.get(1)).getValue().equals(AvailableOperator.EQUALS))
		// && (conditions.get(2) instanceof ExpressionValueTreeObjectReference))
		// {
		// return this.questionAnswerEqualsCondition(conditions);
		// }
		// // Condition of type (question1 BETWEEN (answer11, answer12))
		// else if ((conditions.size() == 6) && (conditions.get(0) instanceof
		// ExpressionValueTreeObjectReference)
		// && (conditions.get(1) instanceof ExpressionFunction)
		// && (((ExpressionFunction)
		// conditions.get(1)).getValue().equals(AvailableFunction.BETWEEN))
		// && (conditions.get(2) instanceof ExpressionValueNumber)
		// && (conditions.get(4) instanceof ExpressionValueNumber)) {
		// return this.questionBetweenAnswersCondition(conditions);
		// }
		// // Condition of type (question1 IN (answer11, answer12, ...))
		// else if ((conditions.size() > 3) && (conditions.get(0) instanceof
		// ExpressionValueTreeObjectReference)
		// && (conditions.get(1) instanceof ExpressionFunction)
		// && (((ExpressionFunction)
		// conditions.get(1)).getValue().equals(AvailableFunction.IN))) {
		//
		// return this.answersInQuestionCondition(conditions);
		// }
		// return "";
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
					return this.equalsOperator(expressions);
				case AND:
					return this.andOperator(expressions);
				case GREATER_EQUALS:
					return this.questionGeGtLeLtAnswer(expressions, AvailableOperator.GREATER_EQUALS);
				case GREATER_THAN:
					return this.questionGeGtLeLtAnswer(expressions, AvailableOperator.GREATER_THAN);
				case LESS_EQUALS:
					return this.questionGeGtLeLtAnswer(expressions, AvailableOperator.LESS_EQUALS);
				case LESS_THAN:
					return this.questionGeGtLeLtAnswer(expressions, AvailableOperator.LESS_THAN);
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
					return this.questionBetweenAnswersCondition(expressions);
				case IN:
					return this.answersInQuestionCondition(expressions);
				default:
					break;
				}
			}
		}
		return "";
	}

	private String equalsOperator(List<Expression> expressions) {
		List<Expression> operatorLeft = ((ExpressionChain) expressions.get(0)).getExpressions();
		List<Expression> operatorRight = ((ExpressionChain) expressions.get(2)).getExpressions();

		if ((operatorLeft.size() == 1) && (operatorLeft.get(0) instanceof ExpressionValueTreeObjectReference)
				&& (operatorRight.size() == 1) && (operatorRight.get(0) instanceof ExpressionValueTreeObjectReference)) {
			TreeObject treeObject1 = ((ExpressionValueTreeObjectReference) operatorLeft.get(0)).getReference();
			TreeObject treeObject2 = ((ExpressionValueTreeObjectReference) operatorRight.get(0)).getReference();
			// Question == Answer
			if ((treeObject1 instanceof Question) && (treeObject2 instanceof Answer)) {
				return this.questionAnswerEqualsCondition((Question) treeObject1, (Answer) treeObject2);
			}
		}
		// TreeObject.score == ValueNumber
		else if ((operatorLeft.size() == 1) && (operatorLeft.get(0) instanceof ExpressionValueCustomVariable)
				&& (operatorRight.size() == 1) && (operatorRight.get(0) instanceof ExpressionValueNumber)) {

			return this.treeObjectScoreEqualsValueNumber((ExpressionValueCustomVariable) operatorLeft.get(0),
					(ExpressionValueNumber) operatorRight.get(0));
		}
		return "";
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
	 */
	private String parseActions(List<Expression> actions) {
		// Action type => (cat.scoreText = "someText") || (cat.score =
		// someValue)
		if ((actions.size() == 3)
				&& (actions.get(0) instanceof ExpressionValueCustomVariable)
				&& (actions.get(1) instanceof ExpressionOperatorMath)
				&& (((ExpressionOperatorMath) actions.get(1)).getValue().equals(AvailableOperator.ASSIGNATION))
				&& ((actions.get(2) instanceof ExpressionValueString) || (actions.get(2) instanceof ExpressionValueNumber))) {
			return this.assignationAction(actions);

		}
		// Action type => cat.scoreText = cat.scoreText + 1
		else if ((actions.size() == 5)
				&& (actions.get(0) instanceof ExpressionValueCustomVariable)
				&& (actions.get(1) instanceof ExpressionOperatorMath)
				&& (((ExpressionOperatorMath) actions.get(1)).getValue().equals(AvailableOperator.ASSIGNATION))
				&& ((actions.get(2) instanceof ExpressionValueCustomVariable) || (actions.get(2) instanceof ExpressionValueNumber))
				&& (actions.get(3) instanceof ExpressionOperatorMath)
				&& ((actions.get(4) instanceof ExpressionValueCustomVariable) || (actions.get(4) instanceof ExpressionValueNumber))) {
			return this.modifyVariableAction(actions);

		}
		return "";
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
	 */
	private String parseExpressions(List<Expression> actions, String extraConditions) {
		System.out.println("PARSING EXPRESSION: " + actions);
		// A.k.a Expression
		// Action type => cat.score = min(q1.score, q2.score, ...)
		if ((actions.get(0) instanceof ExpressionValueCustomVariable)
				&& (actions.get(1) instanceof ExpressionOperatorMath)
				&& (((ExpressionOperatorMath) actions.get(1)).getValue().equals(AvailableOperator.ASSIGNATION))
				&& (actions.get(2) instanceof ExpressionFunction)) {
			System.out.println("PARSING NORMAL EXPRESSIONS");
			return this.assignationFunctionAction(actions, extraConditions);

		} else if ((actions.get(0) instanceof ExpressionValueGenericCustomVariable)
				&& (actions.get(1) instanceof ExpressionOperatorMath)
				&& (((ExpressionOperatorMath) actions.get(1)).getValue().equals(AvailableOperator.ASSIGNATION))
				&& (actions.get(2) instanceof ExpressionFunction)) {
			System.out.println("PARSING GENERIC EXPRESSIONS");
			return this.genericAssignationFunctionAction(actions, extraConditions);
		}
		return "";
	}

	// /**
	// * Parse conditions of type Question == answer. <br>
	// * Create drools rule of type
	// Question(getValue().equals(answer.getName()))
	// *
	// * @param conditions
	// * @return LHS of the rule
	// */
	// private String questionAnswerEqualsCondition(List<Expression> conditions)
	// {
	// String droolsConditions = "";
	// ExpressionValueTreeObjectReference expVal =
	// (ExpressionValueTreeObjectReference) conditions.get(0);
	//
	// // Check if the first object is a question
	// TreeObject treeObject = expVal.getReference();
	// if ((treeObject != null) && (treeObject instanceof Question)) {
	// // Sublist from 2 , because the expression list received is Q1 ==
	// // Answer
	// droolsConditions += this.questionAnswerEqualsCondition((Question)
	// treeObject,
	// conditions.subList(2, conditions.size()));
	// }
	// return droolsConditions;
	// }
	//
	// /**
	// * Parse conditions of type Question (==, IN, BETWEEN) SomeAnswer. <br>
	// * Create drools rule of type Question(getValue() (==, IN, BETWEEN)
	// answer)
	// *
	// * @param conditions
	// * @return LHS of the rule
	// */
	// private String questionAnswerEqualsCondition(Question question,
	// List<Expression> answerExpressions) {
	// String droolsConditions = "";
	// Expression answerExpression = answerExpressions.get(0);
	//
	// // System.out.println("ANSWER CLASSS: " + answerExpression.getClass());
	//
	// if (answerExpression instanceof ExpressionValueTreeObjectReference) {
	// droolsConditions += this.questionAnswerEqualsCondition(question,
	// (ExpressionValueTreeObjectReference) answerExpression);
	//
	// // TODO
	// } else if (answerExpression instanceof ExpressionFunction) {
	// // Check for the existence of OR between expressions
	// if (this.expressionContainsOr(answerExpressions)) {
	// // TODO
	// // Separates the expressions around the OR
	// // List<List<Expression>> separatedExpressions =
	// // this.separateORexpressions(answerExpressions);
	// // // Add the question for each expression so it can be
	// // processed separately
	// // List<String> orConditions = new ArrayList<String>();
	// // for(int i=0; i<separatedExpressions.size(); i++){
	// // List<Expression> auxList = new ArrayList<Expression>();
	// // auxList.add(new
	// // ExpressionValueTreeObjectReference(question));
	// // auxList.addAll(separatedExpressions.get(i));
	// // orConditions.add(this.parseConditions(auxList));
	// // }
	// // String orCondition =
	// // this.createDroolsOrCondition(orConditions);
	// // System.out.println("OR CONDITION AFTER: " + orCondition);
	// // droolsConditions += orCondition;
	// } else {
	// switch (((ExpressionFunction) answerExpression).getValue()) {
	// case IN:
	// droolsConditions += this.answersInQuestionCondition(question,
	// answerExpressions);
	// break;
	// case BETWEEN:
	// droolsConditions += this.questionBetweenAnswersCondition(question,
	// answerExpressions);
	// break;
	// default:
	// break;
	// }
	// }
	// } else if (answerExpression instanceof ExpressionOperatorLogic) {
	// switch (((ExpressionOperatorLogic) answerExpression).getValue()) {
	// case GREATER_EQUALS:
	// droolsConditions += this.questionValueGreaterEqualsAnswer(question,
	// answerExpressions);
	// break;
	// case GREATER_THAN:
	// droolsConditions += this.questionValueGreaterThanAnswer(question,
	// answerExpressions);
	// break;
	// case LESS_EQUALS:
	// droolsConditions += this.questionValueLessEqualsAnswer(question,
	// answerExpressions);
	// break;
	// case LESS_THAN:
	// droolsConditions += this.questionValueLessThanAnswer(question,
	// answerExpressions);
	// break;
	//
	// default:
	// break;
	// }
	// }
	// return droolsConditions;
	// }
	//
	// private String questionAnswerEqualsCondition(Question question,
	// ExpressionValueTreeObjectReference answer) {
	// String droolsConditions = "";
	// TreeObject treeObject2 = answer.getReference();
	// if ((treeObject2 != null)) {
	// // Check the parent of the question
	// TreeObject questionParent = question.getParent();
	// this.putTreeObjectName(question,
	// question.getComparationIdNoDash().toString());
	// if (questionParent instanceof Category) {
	// droolsConditions += this.simpleCategoryConditions((Category)
	// questionParent);
	// } else if (questionParent instanceof Group) {
	// droolsConditions += this.simpleGroupConditions((Group) questionParent);
	// }
	// this.putTreeObjectName(question,
	// question.getComparationIdNoDash().toString());
	// droolsConditions += "	$" + question.getComparationIdNoDash().toString() +
	// " : Question( getAnswer() == '"
	// + treeObject2.getName() + "') from $" +
	// questionParent.getComparationIdNoDash().toString()
	// + ".getQuestions()\n";
	// }
	// return droolsConditions;
	// }

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
		droolsConditions += "	$" + question.getComparationIdNoDash().toString() + " : Question( getAnswer() == '"
				+ answer.getName() + "') from $" + questionParent.getComparationIdNoDash().toString()
				+ ".getQuestions()\n";
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
		String droolsConditions = "";
		List<Expression> operatorLeft = ((ExpressionChain) conditions.get(0)).getExpressions();
		TreeObject leftReference = null;
		if ((operatorLeft.size() == 1) && (operatorLeft.get(0) instanceof ExpressionValueTreeObjectReference)) {
			leftReference = ((ExpressionValueTreeObjectReference) operatorLeft.get(0)).getReference();
		}
		if (leftReference != null) {
			List<Expression> firstExpressionValue = ((ExpressionChain) conditions.get(2)).getExpressions();
			List<Expression> secondExpressionValue = ((ExpressionChain) conditions.get(3)).getExpressions();
			if ((firstExpressionValue.size() == 1) && (firstExpressionValue.get(0) instanceof ExpressionValueNumber)
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
								String lessEqualsDate = "getAnswer() >= DateUtils.returnCurrentDateMinusYears("
										+ value2.intValue() + ")";
								droolsConditions += "	$" + leftQuestion.getComparationIdNoDash().toString()
										+ " : Question( " + instanceOfDate + ", " + greatEqualsDate + ", "
										+ lessEqualsDate + ") from $"
										+ leftReferenceParent.getComparationIdNoDash().toString() + ".getQuestions()\n";
								droolsConditions += "and\n";
							default:
								break;
							}
							break;
						default:
							droolsConditions += "	$" + leftQuestion.getComparationIdNoDash().toString()
									+ " : Question( getAnswer() >= '" + value1 + "' || <= '" + value2 + "') from $"
									+ leftReferenceParent.getComparationIdNoDash().toString() + ".getQuestions() \n";
							droolsConditions += "and\n";
							break;
						}
					}
				}
			}
		}
		return droolsConditions;
	}

	// /**
	// * Parse conditions like => Question BETWEEN(Answer1, answer2). <br>
	// * The values inside the between must be always numbers <br>
	// * Create drools rule like => Question( (getAnswer() >= answer.getValue())
	// * && (getAnswer() <= answer.getValue()))
	// *
	// * @param conditions
	// * @return LHS of the rule
	// */
	// private String questionBetweenAnswersCondition(List<Expression>
	// conditions) {
	// String droolsConditions = "";
	// ExpressionValueTreeObjectReference expVal =
	// (ExpressionValueTreeObjectReference) conditions.get(0);
	// // Check if the first object is a question
	// TreeObject treeObject = expVal.getReference();
	// if ((treeObject != null) && (treeObject instanceof Question)) {
	// // Sublist from 1 , because the expression list received is Q1
	// // BETWEEN( answer1, ... )
	// droolsConditions += this.questionBetweenAnswersCondition((Question)
	// treeObject,
	// conditions.subList(1, conditions.size()));
	// }
	// return droolsConditions;
	// }
	//
	// /**
	// * Parse conditions like => BETWEEN(Answer1, answer2). <br>
	// * The values inside the between must be always numbers <br>
	// * Create drools rule like => Question( (getAnswer() >= answer.getValue())
	// * && (getAnswer() <= answer.getValue()))
	// *
	// * @param conditions
	// * @return LHS of the rule
	// */
	// private String questionBetweenAnswersCondition(Question question,
	// List<Expression> answerExpressions) {
	// // Get the values of the between expression
	// Double value1 = ((ExpressionValueNumber)
	// answerExpressions.get(1)).getValue();
	// Double value2 = ((ExpressionValueNumber)
	// answerExpressions.get(3)).getValue();
	// String droolsConditions = "";
	// if ((value1 != null) && (value2 != null)) {
	// TreeObject questionParent = question.getParent();
	// this.putTreeObjectName(question,
	// question.getComparationIdNoDash().toString());
	// // Check the parent of the question
	// if (questionParent instanceof Category) {
	// droolsConditions += this.simpleCategoryConditions((Category)
	// questionParent);
	// } else if (questionParent instanceof Group) {
	// droolsConditions += this.simpleGroupConditions((Group) questionParent);
	// }
	// switch (question.getAnswerType()) {
	// case INPUT:
	// switch (question.getAnswerFormat()) {
	// case DATE:
	// String instanceOfDate = "getAnswer() instanceof Date";
	// String greatEqualsDate =
	// "getAnswer() <= DateUtils.returnCurrentDateMinusYears("
	// + value1.intValue() + ")";
	// String lessEqualsDate =
	// "getAnswer() >= DateUtils.returnCurrentDateMinusYears(" +
	// value2.intValue()
	// + ")";
	// droolsConditions += "	$" + question.getComparationIdNoDash().toString() +
	// " : Question( "
	// + instanceOfDate + ", " + greatEqualsDate + ", " + lessEqualsDate +
	// ") from $"
	// + questionParent.getComparationIdNoDash().toString() +
	// ".getQuestions()\n";
	// default:
	// break;
	// }
	// break;
	// default:
	// droolsConditions += "	$" + question.getComparationIdNoDash().toString()
	// + " : Question( getAnswer() >= '" + value1 + "' || <= '" + value2 +
	// "') from $"
	// + questionParent.getComparationIdNoDash().toString() +
	// ".getQuestions()\n";
	// break;
	// }
	//
	// }
	// return droolsConditions;
	// }

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
							+ " : Question( getAnswer() in( " + inValues + " )) from $"
							+ leftReferenceParent.getComparationIdNoDash().toString() + ".getQuestions()\n";
				}
			}
		}
		return droolsConditions;
	}

	// /**
	// * Parse conditions like => Question IN(answer1, ...) <br>
	// * Create drools rule like => Question(getAnswer() in (answer1, ...))
	// *
	// * @param conditions
	// * @return LHS of the rule
	// */
	// private String answersInQuestionCondition(List<Expression> conditions) {
	// String droolsConditions = "";
	// ExpressionValueTreeObjectReference expVal =
	// (ExpressionValueTreeObjectReference) conditions.get(0);
	// TreeObject treeObject = expVal.getReference();
	// if ((treeObject != null) && (treeObject instanceof Question)) {
	// // Sublist from 1 , because the expression list received is Q1 IN(
	// // answer1, ... )
	// droolsConditions += this.answersInQuestionCondition((Question)
	// treeObject,
	// conditions.subList(1, conditions.size()));
	// }
	// return droolsConditions;
	// }
	//
	// /**
	// * Parse conditions like => IN(answer1, ...) <br>
	// * Create drools rule like => Question(getAnswer() in (answer1, ...))
	// *
	// * @param conditions
	// * @return LHS of the rule
	// */
	// private String answersInQuestionCondition(Question question,
	// List<Expression> answerExpressions) {
	// String droolsConditions = "";
	// String inValues = "";
	// // Store the values inside the IN condition in a String
	// for (int i = 1; i < (answerExpressions.size() - 1); i += 2) {
	// ExpressionValueTreeObjectReference ansVal =
	// (ExpressionValueTreeObjectReference) answerExpressions.get(i);
	// if (ansVal instanceof ExpressionValueCustomVariable) {
	// // TODO
	// } else {
	// inValues += "'" + ansVal.getReference().getName() + "', ";
	// }
	// }
	// // Remove the last comma
	// inValues = inValues.substring(0, inValues.length() - 2);
	// if (!inValues.isEmpty()) {
	// // Check the parent of the question
	// TreeObject questionParent = question.getParent();
	// this.putTreeObjectName(question,
	// question.getComparationIdNoDash().toString());
	// if (questionParent instanceof Category) {
	// droolsConditions += this.simpleCategoryConditions((Category)
	// questionParent);
	// } else if (questionParent instanceof Group) {
	// droolsConditions += this.simpleGroupConditions((Group) questionParent);
	// }
	// droolsConditions += "	$" + question.getComparationIdNoDash().toString() +
	// " : Question( getAnswer() in( "
	// + inValues + " )) from $" +
	// questionParent.getComparationIdNoDash().toString()
	// + ".getQuestions()\n";
	// }
	// return droolsConditions;
	// }

	/**
	 * Parse conditions like => Score == value. <br>
	 * Create drools rule like => Category(isScoreSet('cScore'),
	 * getVariablevalue('cScore') == value )
	 *
	 * @param conditions
	 * @return LHS of the rule
	 */
	private String treeObjectScoreEqualsValueNumber(ExpressionValueCustomVariable var, ExpressionValueNumber valueNumber) {
		String ruleCore = "";

		TreeObject scope = var.getReference();
		String varName = var.getVariable().getName();

		if (scope instanceof Form) {
			this.putTreeObjectName(scope, scope.getComparationIdNoDash().toString());
			ruleCore += "	$" + scope.getComparationIdNoDash().toString() + " : SubmittedForm( isScoreSet('" + varName
					+ "'), getNumberVariableValue('" + varName + "') == " + valueNumber.getValue() + ")\n";

		} else if (scope instanceof Category) {
			TreeObject form = scope.getParent();
			this.putTreeObjectName(scope, scope.getComparationIdNoDash().toString());
			ruleCore += this.simpleFormCondition((Form) form);
			ruleCore += "	$" + scope.getComparationIdNoDash().toString() + " : Category( isScoreSet('" + varName
					+ "'), getNumberVariableValue('" + varName + "') == " + valueNumber.getValue() + ") from $"
					+ form.getComparationIdNoDash().toString() + ".getCategory('" + scope.getName() + "')\n";

		} else if (scope instanceof Group) {
			TreeObject category = scope.getParent();
			this.putTreeObjectName(scope, scope.getComparationIdNoDash().toString());
			ruleCore += this.simpleCategoryConditions((Category) category);
			ruleCore += "	$" + scope.getComparationIdNoDash().toString() + " : Group( isScoreSet('" + varName
					+ "'), getNumberVariableValue('" + varName + "') == " + valueNumber.getValue() + " ) from $"
					+ category.getComparationIdNoDash().toString() + ".getGroup('" + scope.getName() + "')\n";

		} else if (scope instanceof Question) {
			TreeObject questionParent = scope.getParent();
			this.putTreeObjectName(scope, scope.getComparationIdNoDash().toString());
			if (questionParent instanceof Category) {
				ruleCore += this.simpleCategoryConditions((Category) questionParent);
			} else if (questionParent instanceof Group) {
				ruleCore += this.simpleGroupConditions((Group) questionParent);
			}
			ruleCore += "	$" + scope.getComparationIdNoDash().toString() + " : Question( isScoreSet('" + varName
					+ "'), getNumberVariableValue('" + varName + "') == " + valueNumber.getValue() + " ) from $"
					+ questionParent.getComparationIdNoDash().toString() + ".getQuestions()\n";
		}
		return ruleCore;
	}

	// /**
	// * Parse a list of conditions like => Conditions AND Conditions AND ...
	// <br>
	// * Create drools rule like => $conditions1... \n $conditions2 ...
	// *
	// * @param conditions
	// * @return LHS of the rule
	// */
	// private String conditionsWithAnd(List<List<Expression>> conditions) {
	// String ruleCore = "";
	//
	// for (List<Expression> condition : conditions) {
	// ruleCore += this.parseConditions(condition);
	// }
	// return Utils.removeDuplicateLines(ruleCore);
	// }
	//
	// /**
	// * Parse a list of conditions like => Conditions OR Conditions OR ... <br>
	// * Create drools rule like => $value : (conditions OR conditions OR ... )
	// *
	// * @param conditions
	// * @return LHS of the rule
	// */
	// private String conditionsWithOr(List<List<Expression>> conditions) {
	// String ruleCore = "";
	//
	// for (List<Expression> condition : conditions) {
	// ruleCore += this.parseConditions(condition);
	// }
	// return Utils.removeDuplicateLines(ruleCore);
	// }

	/**
	 * Adds condition rows to the rule that manages the assignation of a
	 * variable in the action<br>
	 *
	 * @param variable
	 *            variable to be added to the LHS of the rule
	 * @return LHS of the rule modified with the new variables
	 */
	private String addConditionVariable(ExpressionValueCustomVariable variable) {
		String ruleCore = "";

		// Add the variable assignation to the rule
		TreeObject treeObject = variable.getReference();
		if (treeObject instanceof Category) {
			ruleCore += this.simpleCategoryConditions((Category) treeObject);

		} else if (treeObject instanceof Group) {
			ruleCore += this.simpleGroupConditions((Group) treeObject);

		} else if (treeObject instanceof Question) {
			ruleCore += this.simpleQuestionConditions((Question) treeObject);
		}
		return ruleCore;
	}

	/**
	 * Parse actions like => Score = stringValue || Score = numberValue <br>
	 * Create drools rule like => setVariableValue(scopeOfVariable,
	 * variableName, stringVariableValue )
	 *
	 * @param actions
	 *            the action of the rule being parsed
	 * @return the RHS of the rule
	 */
	private String assignationAction(List<Expression> actions) {
		String ruleCore = "";
		ExpressionValueCustomVariable var = (ExpressionValueCustomVariable) actions.get(0);
		// Check if the reference exists in the rule, if not, it creates a new
		// reference
		ruleCore += this.checkVariableAssignation(var);

		ruleCore += Utils.getThenRuleString();

		if (actions.get(2) instanceof ExpressionValueString) {
			ExpressionValueString valueString = (ExpressionValueString) actions.get(2);
			ruleCore += "	$" + this.getTreeObjectName(var.getReference()) + ".setVariableValue('"
					+ var.getVariable().getName() + "', '" + valueString.getValue() + "');\n";
			ruleCore += "	AbcdLogger.debug(\"DroolsRule\", \"Variable set (" + var.getReference().getName() + ", "
					+ var.getVariable().getName() + ", " + valueString.getValue() + ")\");\n";

		} else if (actions.get(2) instanceof ExpressionValueNumber) {
			ExpressionValueNumber valueNumber = (ExpressionValueNumber) actions.get(2);
			ruleCore += "	$" + this.getTreeObjectName(var.getReference()) + ".setVariableValue('"
					+ var.getVariable().getName() + "', " + valueNumber.getValue() + ");\n";
			ruleCore += "	AbcdLogger.debug(\"DroolsRule\", \"Variable set (" + var.getReference().getName() + ", "
					+ var.getVariable().getName() + ", " + valueNumber.getValue() + ")\");\n";
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

			ruleCore += Utils.getThenRuleString();
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

			ruleCore += Utils.getThenRuleString();
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
	private String assignationFunctionAction(List<Expression> actions, String extraConditions) {

		System.out.println("ACTIONS :" + actions);

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
		}

		ruleCore = Utils.removeDuplicateLines(ruleCore);
		ruleCore += Utils.getThenRuleString();

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
	 * Generic expression parser.<br>
	 * Works like the expression parser but allows generic variables <br>
	 *
	 * @param actions
	 *            the expression being parsed
	 * @param extraConditions
	 * @return the rule
	 */
	private String genericAssignationFunctionAction(List<Expression> actions, String extraConditions) {
		String ruleCore = "";
//		if (extraConditions != null) {
//			ruleCore += extraConditions;
//		}
		// LHS
//		// We will have some expression of type Category.score = (Min | Max |
//		// Avg) of some values
//		ExpressionValueGenericCustomVariable genericVarToCalculate = (ExpressionValueGenericCustomVariable) actions
//				.get(0);
		// we have to generate a set of rules defined by the generic variable
		ruleCore += this.genericRuleSet(actions, extraConditions);
		// switch (genericVarToCalculate.getType()) {
		// case CATEGORY:
		// ruleCore += this.genericCategoryRuleSet(actions, extraConditions);
		// default:
		// break;
		// }
		this.cleaningNeeded = false;
		return ruleCore;
	}

	/**
	 * Creates a set of expressions and passes it back to the expression parser
	 *
	 * @param expressions
	 * @param extraConditions
	 * @return
	 */
	// TODO
	private String genericRuleSet(List<Expression> expressions, String extraConditions) {
		String ruleCore = "";
		ExpressionValueGenericCustomVariable genericVarToCalculate = (ExpressionValueGenericCustomVariable) expressions
				.get(0);

		List<TreeObject> treeObjects = new ArrayList<TreeObject>();
		switch (genericVarToCalculate.getType()) {
		case CATEGORY:
			treeObjects.addAll(genericVarToCalculate.getVariable().getForm().getChildren());
			break;
		case GROUP:
			for(TreeObject category : genericVarToCalculate.getVariable().getForm().getChildren()){
				treeObjects.addAll(category.getChildren());
			}
			break;
		default:
			break;
		}

		// For each category, we generate the expression to create a new rule
		if(treeObjects != null) {
			for (TreeObject category : treeObjects) {
				ExpressionValueCustomVariable expValCat = new ExpressionValueCustomVariable(category,
						genericVarToCalculate.getVariable());
				// Remove the generic
				expressions.remove(0);
				// Add the specific
				expressions.add(0, expValCat);

				try {
					ruleCore += new ExpressionParser().parse(expressions, extraConditions);
					System.out.println(ruleCore);
				} catch (ExpressionInvalidException e) {
					e.printStackTrace();
				}
			}
		}
		return ruleCore;
	}

	/**
	 * Checks the existence of a binding in drools with the the reference of the
	 * variable passed If there is no binding, creates a new one (i.e. $var :
	 * Question() ...)
	 *
	 * @param expValVariable
	 */
	private String checkVariableAssignation(ExpressionValueCustomVariable expValVariable) {
		if (this.getTreeObjectName(expValVariable.getReference()) == null) {
			// The variable don't exists and can't have a value assigned
			return this.addConditionVariable(expValVariable);
		}
		return "";
	}

	private String simpleFormCondition(Form form) {
		// if(this.getTreeObjectName(form)== null){
		this.putTreeObjectName(form, form.getComparationIdNoDash().toString());
		return "	$" + form.getComparationIdNoDash().toString() + " : SubmittedForm() and\n";
		// }
		// return "";
	}

	private String simpleCategoryConditions(Category category) {
		String conditions = "";
		Form form = (Form) category.getParent();
		conditions += this.simpleFormCondition(form);

		// if(this.getTreeObjectName(category)== null){
		this.putTreeObjectName(category, category.getComparationIdNoDash().toString());
		conditions += "	$" + category.getComparationIdNoDash().toString() + " : Category() from $"
				+ form.getComparationIdNoDash().toString() + ".getCategory('" + category.getName() + "') and\n";
		// }
		return conditions;
	}

	private String simpleGroupConditions(Group group) {
		String conditions = "";
		Category category = (Category) group.getParent();
		conditions += this.simpleCategoryConditions(category);
		// if(this.getTreeObjectName(group)== null){
		this.putTreeObjectName(group, group.getComparationIdNoDash().toString());
		conditions += "	$" + group.getComparationIdNoDash().toString() + " : Group() from $"
				+ category.getComparationIdNoDash().toString() + ".getGroup('" + group.getName() + "') and\n";
		// }
		return conditions;
	}

	private String simpleQuestionConditions(Question question) {
		String conditions = "";
		TreeObject questionParent = question.getParent();
		if (questionParent instanceof Category) {
			Category category = (Category) questionParent;
			conditions += this.simpleCategoryConditions(category);
			// if(this.getTreeObjectName(question)== null){
			this.putTreeObjectName(question, question.getComparationIdNoDash().toString());
			conditions += "	$" + question.getComparationIdNoDash().toString() + " : Question( getTag() == '"
					+ question.getName() + "') from $" + category.getComparationIdNoDash().toString()
					+ ".getQuestions()\n";
			// }

		} else if (questionParent instanceof Group) {
			Group group = (Group) questionParent;
			conditions += this.simpleGroupConditions(group);
			// if(this.getTreeObjectName(question)== null){
			this.putTreeObjectName(question, question.getComparationIdNoDash().toString());
			conditions += "	$" + question.getComparationIdNoDash().toString() + " : Question( getTag() == '"
					+ question.getName() + "') from $" + group.getComparationIdNoDash().toString()
					+ ".getQuestions()\n";
			// }
		}
		return conditions;
	}

	// private boolean expressionContainsOr(List<Expression> expChain) {
	// for (Expression expression : expChain) {
	// if ((expression instanceof ExpressionOperatorLogic)
	// && ((ExpressionOperatorLogic)
	// expression).getValue().equals(AvailableOperator.OR)) {
	// return true;
	// }
	// }
	// return false;
	// }
	//
	// private List<List<Expression>> separateOrExpressions(List<Expression>
	// expChain) {
	// int startOrConditionIndex = 0;
	// int endOrConditionIndex = 0;
	// // Creation of substrings defined by the AND or the OR expressions
	// // Each subCondition list is parsed individually
	// List<List<Expression>> orSubConditions = new
	// ArrayList<List<Expression>>();
	// for (Expression expression : expChain) {
	// if (expression instanceof ExpressionOperatorLogic) {
	// switch (((ExpressionOperatorLogic) expression).getValue()) {
	// case OR:
	// orSubConditions.add(expChain.subList(startOrConditionIndex,
	// endOrConditionIndex));
	// startOrConditionIndex = endOrConditionIndex + 1;
	// break;
	// default:
	// break;
	// }
	// }
	// endOrConditionIndex++;
	// }
	// // Add the last subCondition
	// orSubConditions.add(expChain.subList(startOrConditionIndex,
	// endOrConditionIndex));
	// return orSubConditions;
	// }
	//
	// private boolean expressionContainsAnd(List<Expression> expChain) {
	// for (Expression expression : expChain) {
	// if ((expression instanceof ExpressionOperatorLogic)
	// && ((ExpressionOperatorLogic)
	// expression).getValue().equals(AvailableOperator.AND)) {
	// return true;
	// }
	// }
	// return false;
	// }
	//
	// private String createDroolsOrCondition(List<String> conditions) {
	// String conditionsWithOr = "( ";
	// for (String condition : conditions) {
	// conditionsWithOr += condition;
	// }
	// return conditionsWithOr + " )\n";
	// }

	private String questionGeGtLeLtAnswer(List<Expression> conditions, AvailableOperator operator) {
		String droolsConditions = "";
		List<Expression> operatorLeft = ((ExpressionChain) conditions.get(0)).getExpressions();
		TreeObject leftReference = null;
		if ((operatorLeft.size() == 1) && (operatorLeft.get(0) instanceof ExpressionValueTreeObjectReference)) {
			leftReference = ((ExpressionValueTreeObjectReference) operatorLeft.get(0)).getReference();
		}
		if (leftReference != null) {
			List<Expression> firstExpressionValue = ((ExpressionChain) conditions.get(2)).getExpressions();
			if ((firstExpressionValue.size() == 1) && (firstExpressionValue.get(0) instanceof ExpressionValueNumber)) {
				// Get the values of the between expression
				Double value = ((ExpressionValueNumber) firstExpressionValue.get(0)).getValue();

				if (value != null) {
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
						if (leftQuestion.getAnswerType().equals(AnswerType.INPUT)
								&& leftQuestion.getAnswerFormat().equals(AnswerFormat.DATE)) {
							switch (operator) {
							case GREATER_EQUALS:
								return this.questionValueDateGreaterEqualsAnswer(leftReferenceParent, leftQuestion,
										value);
							case GREATER_THAN:
								return this
										.questionValueDateGreaterThanAnswer(leftReferenceParent, leftQuestion, value);
							case LESS_EQUALS:
								return this.questionValueDateLessEqualsAnswer(leftReferenceParent, leftQuestion, value);
							case LESS_THAN:
								return this.questionValueDateLessThanAnswer(leftReferenceParent, leftQuestion, value);
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

	private String questionValueDateGreaterEqualsAnswer(TreeObject leftReferenceParent, TreeObject leftQuestion,
			Double value) {
		return "	$" + leftQuestion.getComparationIdNoDash().toString()
				+ " : Question(getAnswer() instanceof Date, getAnswer() <= DateUtils.returnCurrentDateMinusYears("
				+ value.intValue() + ")) from $" + leftReferenceParent.getComparationIdNoDash().toString()
				+ ".getQuestions()\n";
	}

	private String questionValueDateGreaterThanAnswer(TreeObject leftReferenceParent, TreeObject leftQuestion,
			Double value) {
		return "	$" + leftQuestion.getComparationIdNoDash().toString()
				+ " : Question(getAnswer() instanceof Date, getAnswer() < DateUtils.returnCurrentDateMinusYears("
				+ value.intValue() + ")) from $" + leftReferenceParent.getComparationIdNoDash().toString()
				+ ".getQuestions()\n";
	}

	private String questionValueDateLessEqualsAnswer(TreeObject leftReferenceParent, TreeObject leftQuestion,
			Double value) {
		return "	$" + leftQuestion.getComparationIdNoDash().toString()
				+ " : Question(getAnswer() instanceof Date, getAnswer() >= DateUtils.returnCurrentDateMinusYears("
				+ value.intValue() + ")) from $" + leftReferenceParent.getComparationIdNoDash().toString()
				+ ".getQuestions()\n";
	}

	private String questionValueDateLessThanAnswer(TreeObject leftReferenceParent, TreeObject leftQuestion, Double value) {
		return "	$" + leftQuestion.getComparationIdNoDash().toString()
				+ " : Question(getAnswer() instanceof Date, getAnswer() > DateUtils.returnCurrentDateMinusYears("
				+ value.intValue() + ")) from $" + leftReferenceParent.getComparationIdNoDash().toString()
				+ ".getQuestions()\n";
	}

	// private String genericMinFunction(List<Expression> expressions) {
	//
	// }
}

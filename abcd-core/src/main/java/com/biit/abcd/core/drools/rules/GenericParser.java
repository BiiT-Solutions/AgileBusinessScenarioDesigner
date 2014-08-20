package com.biit.abcd.core.drools.rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueNumber;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueString;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.form.TreeObject;

public class GenericParser {

	private HashMap<TreeObject, String> treeObjectDroolsname;

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
		ruleCore = Utils.removeDuplicateLines(ruleCore);
		return ruleCore;
	}

	private String parseConditions(List<Expression> conditions) {
		// System.out.println("Parse Conditions");
		// for(Expression expression: conditions){
		// System.out.println("EXPRESSION CLASS: " + expression.getClass());
		// if(expression instanceof ExpressionChain){
		// for(Expression expression2: ((ExpressionChain) expression).getExpressions()){
		// System.out.println("EXPRESSION_INSIDE_CHAIN CLASS: " + expression2.getClass() + " :: NAME: " +
		// expression2.toString());
		// }
		// }
		// }

		// Condition of type (something AND something AND ...) or (something OR something OR ...)
		// Currently no combinations of ANDs and ORs are allowed
		if (conditions.size() > 6) {
			int startAndConditionIndex = 0;
			int endAndConditionIndex = 0;
			int startOrConditionIndex = 0;
			int endOrConditionIndex = 0;
			// Creation of substrings defined by the AND or the OR expressions
			// Each subCondition list is parsed individually
			List<List<Expression>> andSubConditions = new ArrayList<List<Expression>>();
			List<List<Expression>> orSubConditions = new ArrayList<List<Expression>>();
			for (Expression condition : conditions) {
				if (condition instanceof ExpressionOperatorLogic) {
					switch (((ExpressionOperatorLogic) condition).getValue()) {
					case AND:
						andSubConditions.add(conditions.subList(startAndConditionIndex, endAndConditionIndex));
						startAndConditionIndex = endAndConditionIndex + 1;
						break;
					case OR:
						orSubConditions.add(conditions.subList(startOrConditionIndex, endOrConditionIndex));
						startOrConditionIndex = endOrConditionIndex + 1;
						break;
					default:
						break;
					}
				}
				endAndConditionIndex++;
				endOrConditionIndex++;
			}
			// Add the last subCondition
			andSubConditions.add(conditions.subList(startAndConditionIndex, endAndConditionIndex));
			// Add the last subCondition
			orSubConditions.add(conditions.subList(startOrConditionIndex, endOrConditionIndex));
			if (startAndConditionIndex != 0) {
				return this.conditionsWithAnd(andSubConditions);
			} else if (startOrConditionIndex != 0) {
				System.out.println("OR PARSING");
				return this.conditionsWithOr(orSubConditions);
			}
		}
		// Condition of type (Question | Answer)
		if ((conditions.size() == 2) && (conditions.get(0) instanceof ExpressionValueTreeObjectReference)
				&& (conditions.get(1) instanceof ExpressionChain)) {
			TreeObject questionObject = ((ExpressionValueTreeObjectReference) conditions.get(0)).getReference();
			if ((questionObject != null) && (questionObject instanceof Question)) {
				List<Expression> answerExpressions = ((ExpressionChain) conditions.get(1)).getExpressions();
				return this.questionAnswerEqualsCondition((Question) questionObject, answerExpressions);
			}
		}
		// Condition of type (Question | Answer)
		if ((conditions.size() == 2) && (conditions.get(0) instanceof ExpressionValueTreeObjectReference)
				&& (conditions.get(1) instanceof ExpressionValueTreeObjectReference)) {
			TreeObject questionObject = ((ExpressionValueTreeObjectReference) conditions.get(0)).getReference();
			if ((questionObject != null) && (questionObject instanceof Question)) {
				return this.questionAnswerEqualsCondition((Question) questionObject,
						(ExpressionValueTreeObjectReference) conditions.get(1));

				// List<Expression> answerExpressions = ((ExpressionChain)conditions.get(1)).getExpressions();
				// return this.questionAnswerEqualsCondition((Question)questionObject, answerExpressions);
			}
		}
		// Condition of type (cat.score == value)
		else if ((conditions.size() == 3) && (conditions.get(0) instanceof ExpressionValueCustomVariable)
				&& (conditions.get(1) instanceof ExpressionOperatorLogic)
				&& (((ExpressionOperatorLogic) conditions.get(1)).getValue().equals(AvailableOperator.EQUALS))
				&& (conditions.get(2) instanceof ExpressionValueNumber)) {
			return this.scoreEqualsCondition(conditions);
		}
		// Condition of type (question1 == answer11)
		else if ((conditions.size() == 3) && (conditions.get(0) instanceof ExpressionValueTreeObjectReference)
				&& (conditions.get(1) instanceof ExpressionOperatorLogic)
				&& (((ExpressionOperatorLogic) conditions.get(1)).getValue().equals(AvailableOperator.EQUALS))
				&& (conditions.get(2) instanceof ExpressionValueTreeObjectReference)) {
			return this.questionAnswerEqualsCondition(conditions);
		}
		// Condition of type (question1 BETWEEN (answer11, answer12))
		else if ((conditions.size() == 6) && (conditions.get(0) instanceof ExpressionValueTreeObjectReference)
				&& (conditions.get(1) instanceof ExpressionFunction)
				&& (((ExpressionFunction) conditions.get(1)).getValue().equals(AvailableFunction.BETWEEN))
				&& (conditions.get(2) instanceof ExpressionValueNumber)
				&& (conditions.get(4) instanceof ExpressionValueNumber)) {
			return this.questionBetweenAnswersCondition(conditions);
		}
		// Condition of type (question1 IN (answer11, answer12, ...))
		else if ((conditions.size() > 3) && (conditions.get(0) instanceof ExpressionValueTreeObjectReference)
				&& (conditions.get(1) instanceof ExpressionFunction)
				&& (((ExpressionFunction) conditions.get(1)).getValue().equals(AvailableFunction.IN))) {

			return this.answersInQuestionCondition(conditions);
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
	private String parseActions(List<Expression> actions) {
		// Action type => (cat.scoreText = "someText") || (cat.score = someValue)
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
		// A.k.a Expression
		// Action type => cat.score = min(q1.score, q2.score, ...)
		if ((actions.get(0) instanceof ExpressionValueCustomVariable)
				&& (actions.get(1) instanceof ExpressionOperatorMath)
				&& (((ExpressionOperatorMath) actions.get(1)).getValue().equals(AvailableOperator.ASSIGNATION))
				&& (actions.get(2) instanceof ExpressionFunction)) {
			return this.assignationFunctionAction(actions, extraConditions);
		}
		return "";
	}

	/**
	 * Parse conditions of type Question == answer. <br>
	 * Create drools rule of type Question(getValue().equals(answer.getName()))
	 * 
	 * @param conditions
	 * @return LHS of the rule
	 */
	private String questionAnswerEqualsCondition(List<Expression> conditions) {
		String droolsConditions = "";
		ExpressionValueTreeObjectReference expVal = (ExpressionValueTreeObjectReference) conditions.get(0);

		// Check if the first object is a question
		TreeObject treeObject = expVal.getReference();
		if ((treeObject != null) && (treeObject instanceof Question)) {
			// Sublist from 2 , because the expression list received is Q1 == Answer
			droolsConditions += this.questionAnswerEqualsCondition((Question) treeObject,
					conditions.subList(2, conditions.size()));
		}
		return droolsConditions;
	}

	/**
	 * Parse conditions of type Question (==, IN, BETWEEN) SomeAnswer. <br>
	 * Create drools rule of type Question(getValue() (==, IN, BETWEEN) answer)
	 * 
	 * @param conditions
	 * @return LHS of the rule
	 */
	private String questionAnswerEqualsCondition(Question question, List<Expression> answerExpressions) {
		String droolsConditions = "";
		Expression answerExpression = answerExpressions.get(0);

		// System.out.println("ANSWER CLASSS: " + answerExpression.getClass());

		if (answerExpression instanceof ExpressionValueTreeObjectReference) {
			droolsConditions += this.questionAnswerEqualsCondition(question,
					(ExpressionValueTreeObjectReference) answerExpression);

			// TODO
		} else if (answerExpression instanceof ExpressionFunction) {
			// Check for the existence of OR between expressions
			if (this.expressionContainsOr(answerExpressions)) {
				// TODO
				// Separates the expressions around the OR
				// List<List<Expression>> separatedExpressions = this.separateORexpressions(answerExpressions);
				// // Add the question for each expression so it can be processed separately
				// List<String> orConditions = new ArrayList<String>();
				// for(int i=0; i<separatedExpressions.size(); i++){
				// List<Expression> auxList = new ArrayList<Expression>();
				// auxList.add(new ExpressionValueTreeObjectReference(question));
				// auxList.addAll(separatedExpressions.get(i));
				// orConditions.add(this.parseConditions(auxList));
				// }
				// String orCondition = this.createDroolsOrCondition(orConditions);
				// System.out.println("OR CONDITION AFTER: " + orCondition);
				// droolsConditions += orCondition;
			} else {
				switch (((ExpressionFunction) answerExpression).getValue()) {
				case IN:
					droolsConditions += this.answersInQuestionCondition(question, answerExpressions);
					break;
				case BETWEEN:
					droolsConditions += this.questionBetweenAnswersCondition(question, answerExpressions);
					break;
				default:
					break;
				}
			}
		} else if (answerExpression instanceof ExpressionOperatorLogic) {
			switch (((ExpressionOperatorLogic) answerExpression).getValue()) {
			case GREATER_EQUALS:
				droolsConditions += this.questionValueGreaterEqualsAnswer(question, answerExpressions);
				break;
			case GREATER_THAN:
				droolsConditions += this.questionValueGreaterThanAnswer(question, answerExpressions);
				break;
			case LESS_EQUALS:
				droolsConditions += this.questionValueLessEqualsAnswer(question, answerExpressions);
				break;
			case LESS_THAN:
				droolsConditions += this.questionValueLessThanAnswer(question, answerExpressions);
				break;

			default:
				break;
			}
		}
		return droolsConditions;
	}

	private String questionAnswerEqualsCondition(Question question, ExpressionValueTreeObjectReference answer) {
		String droolsConditions = "";
		TreeObject treeObject2 = answer.getReference();
		if ((treeObject2 != null)) {
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
					+ treeObject2.getName() + "') from $" + questionParent.getComparationIdNoDash().toString()
					+ ".getQuestions()\n";
		}
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
		String droolsConditions = "";
		ExpressionValueTreeObjectReference expVal = (ExpressionValueTreeObjectReference) conditions.get(0);
		// Check if the first object is a question
		TreeObject treeObject = expVal.getReference();
		if ((treeObject != null) && (treeObject instanceof Question)) {
			// Sublist from 1 , because the expression list received is Q1 BETWEEN( answer1, ... )
			droolsConditions += this.questionBetweenAnswersCondition((Question) treeObject,
					conditions.subList(1, conditions.size()));
		}
		return droolsConditions;
	}

	/**
	 * Parse conditions like => BETWEEN(Answer1, answer2). <br>
	 * The values inside the between must be always numbers <br>
	 * Create drools rule like => Question( (getAnswer() >= answer.getValue()) && (getAnswer() <= answer.getValue()))
	 * 
	 * @param conditions
	 * @return LHS of the rule
	 */
	private String questionBetweenAnswersCondition(Question question, List<Expression> answerExpressions) {
		// Get the values of the between expression
		Double value1 = ((ExpressionValueNumber) answerExpressions.get(1)).getValue();
		Double value2 = ((ExpressionValueNumber) answerExpressions.get(3)).getValue();
		String droolsConditions = "";
		if ((value1 != null) && (value2 != null)) {
			TreeObject questionParent = question.getParent();
			this.putTreeObjectName(question, question.getComparationIdNoDash().toString());
			// Check the parent of the question
			if (questionParent instanceof Category) {
				droolsConditions += this.simpleCategoryConditions((Category) questionParent);
			} else if (questionParent instanceof Group) {
				droolsConditions += this.simpleGroupConditions((Group) questionParent);
			}
			switch (question.getAnswerType()) {
			case INPUT:
				switch (question.getAnswerFormat()) {
				case DATE:
					String instanceOfDate = "getAnswer() instanceof Date";
					String greatEqualsDate = "getAnswer() <= DateUtils.returnCurrentDateMinusYears("
							+ value1.intValue() + ")";
					String lessEqualsDate = "getAnswer() >= DateUtils.returnCurrentDateMinusYears(" + value2.intValue()
							+ ")";
					droolsConditions += "	$" + question.getComparationIdNoDash().toString() + " : Question( "
							+ instanceOfDate + ", " + greatEqualsDate + ", " + lessEqualsDate + ") from $"
							+ questionParent.getComparationIdNoDash().toString() + ".getQuestions()\n";
				default:
					break;
				}
				break;
			default:
				droolsConditions += "	$" + question.getComparationIdNoDash().toString()
						+ " : Question( getAnswer() >= '" + value1 + "' || <= '" + value2 + "') from $"
						+ questionParent.getComparationIdNoDash().toString() + ".getQuestions()\n";
				break;
			}

		}
		return droolsConditions;
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
		ExpressionValueTreeObjectReference expVal = (ExpressionValueTreeObjectReference) conditions.get(0);
		TreeObject treeObject = expVal.getReference();
		if ((treeObject != null) && (treeObject instanceof Question)) {
			// Sublist from 1 , because the expression list received is Q1 IN( answer1, ... )
			droolsConditions += this.answersInQuestionCondition((Question) treeObject,
					conditions.subList(1, conditions.size()));
		}
		return droolsConditions;
	}

	/**
	 * Parse conditions like => IN(answer1, ...) <br>
	 * Create drools rule like => Question(getAnswer() in (answer1, ...))
	 * 
	 * @param conditions
	 * @return LHS of the rule
	 */
	private String answersInQuestionCondition(Question question, List<Expression> answerExpressions) {
		String droolsConditions = "";
		String inValues = "";
		// Store the values inside the IN condition in a String
		for (int i = 1; i < (answerExpressions.size() - 1); i += 2) {
			ExpressionValueTreeObjectReference ansVal = (ExpressionValueTreeObjectReference) answerExpressions.get(i);
			if (ansVal instanceof ExpressionValueCustomVariable) {
				// TODO
			} else {
				inValues += "'" + ansVal.getReference().getName() + "', ";
			}
		}
		// Remove the last comma
		inValues = inValues.substring(0, inValues.length() - 2);
		if (!inValues.isEmpty()) {
			// Check the parent of the question
			TreeObject questionParent = question.getParent();
			this.putTreeObjectName(question, question.getComparationIdNoDash().toString());
			if (questionParent instanceof Category) {
				droolsConditions += this.simpleCategoryConditions((Category) questionParent);
			} else if (questionParent instanceof Group) {
				droolsConditions += this.simpleGroupConditions((Group) questionParent);
			}
			droolsConditions += "	$" + question.getComparationIdNoDash().toString() + " : Question( getAnswer() in( "
					+ inValues + " )) from $" + questionParent.getComparationIdNoDash().toString()
					+ ".getQuestions()\n";
		}
		return droolsConditions;
	}

	/**
	 * Parse conditions like => Score == value. <br>
	 * Create drools rule like => Category(isScoreSet('cScore'), getVariablevalue('cScore') == value )
	 * 
	 * @param conditions
	 * @return LHS of the rule
	 */
	private String scoreEqualsCondition(List<Expression> conditions) {
		String ruleCore = "";
		ExpressionValueCustomVariable var = (ExpressionValueCustomVariable) conditions.get(0);
		ExpressionValueNumber valueNumber = (ExpressionValueNumber) conditions.get(2);

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

	/**
	 * Parse a list of conditions like => Conditions AND Conditions AND ... <br>
	 * Create drools rule like => $conditions1... \n $conditions2 ...
	 * 
	 * @param conditions
	 * @return LHS of the rule
	 */
	private String conditionsWithAnd(List<List<Expression>> conditions) {
		String ruleCore = "";

		for (List<Expression> condition : conditions) {
			ruleCore += this.parseConditions(condition);
		}
		return Utils.removeDuplicateLines(ruleCore);
	}

	/**
	 * Parse a list of conditions like => Conditions OR Conditions OR ... <br>
	 * Create drools rule like => $value : (conditions OR conditions OR ... )
	 * 
	 * @param conditions
	 * @return LHS of the rule
	 */
	private String conditionsWithOr(List<List<Expression>> conditions) {
		String ruleCore = "";

		for (List<Expression> condition : conditions) {
			ruleCore += this.parseConditions(condition);
		}
		return Utils.removeDuplicateLines(ruleCore);
	}

	/**
	 * Adds condition rows to the rule that manages the assignation of a variable in the action<br>
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
	 * Create drools rule like => setVariableValue(scopeOfVariable, variableName, stringVariableValue )
	 * 
	 * @param actions
	 *            the action of the rule being parsed
	 * @return the RHS of the rule
	 */
	private String assignationAction(List<Expression> actions) {
		String ruleCore = "";
		ExpressionValueCustomVariable var = (ExpressionValueCustomVariable) actions.get(0);
		// Check if the reference exists in the rule, if not, it creates a new reference
		ruleCore += this.checkVariableAssignation(var);

		ruleCore += Utils.getThenRuleString();

		if (actions.get(2) instanceof ExpressionValueString) {
			ExpressionValueString valueString = (ExpressionValueString) actions.get(2);
			ruleCore += "	$" + this.getTreeObjectName(var.getReference()) + ".setVariableValue('"
					+ var.getVariable().getName() + "', '" + valueString.getValue() + "');\n";
			// ruleCore += "	System.out.println( \"Variable set (" + var.getReference().getName() + ", "
			// + var.getVariable().getName() + ", " + valueString.getValue() + ")\");\n";
			ruleCore += "   AbcdLogger.debug(\"DroolsRule\", \"Variable set (" + var.getReference().getName() + ", "
					+ var.getVariable().getName() + ", " + valueString.getValue() + ")\");\n";

		} else if (actions.get(2) instanceof ExpressionValueNumber) {
			ExpressionValueNumber valueNumber = (ExpressionValueNumber) actions.get(2);
			ruleCore += "	$" + this.getTreeObjectName(var.getReference()) + ".setVariableValue('"
					+ var.getVariable().getName() + "', " + valueNumber.getValue() + ");\n";
			// ruleCore += "	System.out.println( \"Variable set (" + var.getReference().getName() + ", "
			// + var.getVariable().getName() + ", " + valueNumber.getValue() + ")\");\n";
			ruleCore += "   AbcdLogger.debug(\"DroolsRule\", \"Variable set (" + var.getReference().getName() + ", "
					+ var.getVariable().getName() + ", " + valueNumber.getValue() + ")\");\n";
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
		// Check if the reference exists in the rule, if not, it creates a new reference
		ruleCore += this.checkVariableAssignation(var);

		if (actions.get(2) instanceof ExpressionValueCustomVariable) {
			ExpressionValueCustomVariable var2 = (ExpressionValueCustomVariable) actions.get(2);
			ruleCore += this.checkVariableAssignation(var2);
			ExpressionValueNumber valueNumber = (ExpressionValueNumber) actions.get(4);

			ruleCore += Utils.getThenRuleString();
			ruleCore += "	$" + this.getTreeObjectName(var.getReference()) + ".setVariableValue('" + customVarName
					+ "', " + "(Double)$" + this.getTreeObjectName(var2.getReference()) + ".getNumberVariableValue('"
					+ customVarName + "') " + operator.getValue() + " " + valueNumber.getValue() + ");\n";
			// ruleCore += "	System.out.println( \"Variable updated (" + var.getReference().getName() + ", "
			// + customVarName + ", " + operator.getValue() + valueNumber.getValue() + ")\");\n";
			ruleCore += "   AbcdLogger.debug(\"DroolsRule\", \"Variable updated (" + var.getReference().getName()
					+ ", " + customVarName + ", " + operator.getValue() + valueNumber.getValue() + ")\");\n";

		} else if (actions.get(2) instanceof ExpressionValueNumber) {
			ExpressionValueNumber valueNumber = (ExpressionValueNumber) actions.get(2);
			ExpressionValueCustomVariable var2 = (ExpressionValueCustomVariable) actions.get(4);
			ruleCore += this.checkVariableAssignation(var2);

			ruleCore += Utils.getThenRuleString();
			ruleCore += "	$" + this.getTreeObjectName(var.getReference()) + ".setVariableValue('" + customVarName
					+ "', " + "(Double)$" + this.getTreeObjectName(var2.getReference()) + ".getNumberVariableValue('"
					+ customVarName + "') " + operator.getValue() + " " + valueNumber.getValue() + ");\n";
//			ruleCore += "	System.out.println( \"Variable updated (" + var.getReference().getName() + ", "
//					+ customVarName + ", " + operator.getValue() + valueNumber.getValue() + ")\");\n";
			ruleCore += "   AbcdLogger.debug(\"DroolsRule\", \"Variable updated (" + var.getReference().getName() + ", "
					+ customVarName + ", " + operator.getValue() + valueNumber.getValue() + ")\");\n";

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
	 * @param extraConditions
	 * @return the rule
	 */
	private String assignationFunctionAction(List<Expression> actions, String extraConditions) {
		String ruleCore = "";
		if (extraConditions != null) {
			ruleCore += extraConditions;
		}
		// LHS
		// We will have some expression of type Category.score = (Min | Max | Avg) of some values
		ExpressionValueCustomVariable variableToCalculate = (ExpressionValueCustomVariable) actions.get(0);
		// The rule is different if the variable to assign is a Form, a Category or a Group
		TreeObject treeObject = variableToCalculate.getReference();
		if (treeObject instanceof Form) {
			ruleCore += this.simpleFormCondition((Form) treeObject);

		} else if (treeObject instanceof Category) {
			ruleCore += this.simpleCategoryConditions((Category) treeObject);

		} else if (treeObject instanceof Group) {
			ruleCore += this.simpleGroupConditions((Group) treeObject);
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
		}
		// Finish the line of the condition
		ruleCore = ruleCore.substring(0, ruleCore.length() - 3);
		if (scopeClass.equals("Question")) {
			ruleCore += ") from $" + parent.getComparationIdNoDash().toString() + ".getQuestions())\n";
		} else if (scopeClass.equals("Group")) {
			ruleCore += ") from $" + parent.getComparationIdNoDash().toString() + ".getGroups())\n";
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
//			ruleCore += "	System.out.println(\"Variable set (" + variableToCalculate.getReference().getName() + ", "
//					+ variableToCalculate.getVariable().getName() + ", \" + $sol +\")\");\n";
			ruleCore += "   AbcdLogger.debug(\"DroolsRule\", \"Variable set (" + variableToCalculate.getReference().getName() + ", "
					+ variableToCalculate.getVariable().getName() + ", \" + $sol +\")\");\n";
		}
		return ruleCore;
	}

	/**
	 * Checks the existence of a binding in drools with the the reference of the variable passed If there is no binding,
	 * creates a new one (i.e. $var : Question() ...)
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
		return "	$" + form.getComparationIdNoDash().toString() + " : SubmittedForm()\n";
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
				+ form.getComparationIdNoDash().toString() + ".getCategory('" + category.getName() + "')\n";
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
				+ category.getComparationIdNoDash().toString() + ".getGroup('" + group.getName() + "')\n";
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

	private boolean expressionContainsOr(List<Expression> expChain) {
		for (Expression expression : expChain) {
			if ((expression instanceof ExpressionOperatorLogic)
					&& ((ExpressionOperatorLogic) expression).getValue().equals(AvailableOperator.OR)) {
				return true;
			}
		}
		return false;
	}

	private List<List<Expression>> separateOrExpressions(List<Expression> expChain) {
		int startOrConditionIndex = 0;
		int endOrConditionIndex = 0;
		// Creation of substrings defined by the AND or the OR expressions
		// Each subCondition list is parsed individually
		List<List<Expression>> orSubConditions = new ArrayList<List<Expression>>();
		for (Expression expression : expChain) {
			if (expression instanceof ExpressionOperatorLogic) {
				switch (((ExpressionOperatorLogic) expression).getValue()) {
				case OR:
					orSubConditions.add(expChain.subList(startOrConditionIndex, endOrConditionIndex));
					startOrConditionIndex = endOrConditionIndex + 1;
					break;
				default:
					break;
				}
			}
			endOrConditionIndex++;
		}
		// Add the last subCondition
		orSubConditions.add(expChain.subList(startOrConditionIndex, endOrConditionIndex));
		return orSubConditions;
	}

	private boolean expressionContainsAnd(List<Expression> expChain) {
		for (Expression expression : expChain) {
			if ((expression instanceof ExpressionOperatorLogic)
					&& ((ExpressionOperatorLogic) expression).getValue().equals(AvailableOperator.AND)) {
				return true;
			}
		}
		return false;
	}

	private String createDroolsOrCondition(List<String> conditions) {
		String conditionsWithOr = "( ";
		for (String condition : conditions) {
			conditionsWithOr += condition;
		}
		return conditionsWithOr + " )\n";
	}

	private String questionValueGreaterEqualsAnswer(Question question, List<Expression> answerExpressions) {
		String droolsConditions = "";
		TreeObject questionParent = question.getParent();
		if (questionParent instanceof Category) {
			droolsConditions += this.simpleCategoryConditions((Category) questionParent);
		} else if (questionParent instanceof Group) {
			droolsConditions += this.simpleGroupConditions((Group) questionParent);
		}
		// TODO fix the condition, the diagram creates another type of expression
		if (answerExpressions.get(1) instanceof ExpressionValueNumber) {
			Double years = ((ExpressionValueNumber) answerExpressions.get(1)).getValue();
			droolsConditions += "	$" + question.getComparationIdNoDash().toString()
					+ " : Question(getAnswer() instanceof Date, getAnswer() <= DateUtils.returnCurrentDateMinusYears("
					+ years.intValue() + ")) from $" + questionParent.getComparationIdNoDash().toString()
					+ ".getQuestions()\n";
		}
		return droolsConditions;
	}

	private String questionValueGreaterThanAnswer(Question question, List<Expression> answerExpressions) {
		String droolsConditions = "";
		TreeObject questionParent = question.getParent();
		if (questionParent instanceof Category) {
			droolsConditions += this.simpleCategoryConditions((Category) questionParent);
		} else if (questionParent instanceof Group) {
			droolsConditions += this.simpleGroupConditions((Group) questionParent);
		}
		// TODO fix the condition, the diagram creates another type of expression
		if (answerExpressions.get(1) instanceof ExpressionValueNumber) {
			Double years = ((ExpressionValueNumber) answerExpressions.get(1)).getValue();
			droolsConditions += "	$" + question.getComparationIdNoDash().toString()
					+ " : Question(getAnswer() instanceof Date, getAnswer() < DateUtils.returnCurrentDateMinusYears("
					+ years.intValue() + ")) from $" + questionParent.getComparationIdNoDash().toString()
					+ ".getQuestions()\n";
		}
		return droolsConditions;
	}

	private String questionValueLessEqualsAnswer(Question question, List<Expression> answerExpressions) {
		String droolsConditions = "";
		TreeObject questionParent = question.getParent();
		if (questionParent instanceof Category) {
			droolsConditions += this.simpleCategoryConditions((Category) questionParent);
		} else if (questionParent instanceof Group) {
			droolsConditions += this.simpleGroupConditions((Group) questionParent);
		}
		// TODO fix the condition, the diagram creates another type of expression
		if (answerExpressions.get(1) instanceof ExpressionValueNumber) {
			Double years = ((ExpressionValueNumber) answerExpressions.get(1)).getValue();
			droolsConditions += "	$" + question.getComparationIdNoDash().toString()
					+ " : Question(getAnswer() instanceof Date, getAnswer() >= DateUtils.returnCurrentDateMinusYears("
					+ years.intValue() + ")) from $" + questionParent.getComparationIdNoDash().toString()
					+ ".getQuestions()\n";
		}
		return droolsConditions;
	}

	private String questionValueLessThanAnswer(Question question, List<Expression> answerExpressions) {
		String droolsConditions = "";
		TreeObject questionParent = question.getParent();
		if (questionParent instanceof Category) {
			droolsConditions += this.simpleCategoryConditions((Category) questionParent);
		} else if (questionParent instanceof Group) {
			droolsConditions += this.simpleGroupConditions((Group) questionParent);
		}
		// TODO fix the condition, the diagram creates another type of expression
		if (answerExpressions.get(1) instanceof ExpressionValueNumber) {
			Double years = ((ExpressionValueNumber) answerExpressions.get(1)).getValue();
			droolsConditions += "	$" + question.getComparationIdNoDash().toString()
					+ " : Question(getAnswer() instanceof Date, getAnswer() > DateUtils.returnCurrentDateMinusYears("
					+ years.intValue() + ")) from $" + questionParent.getComparationIdNoDash().toString()
					+ ".getQuestions()\n";
		}
		return droolsConditions;
	}

}

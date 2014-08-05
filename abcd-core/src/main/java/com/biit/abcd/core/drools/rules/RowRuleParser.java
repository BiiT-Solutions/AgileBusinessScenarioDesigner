package com.biit.abcd.core.drools.rules;

import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.persistence.entity.CustomVariableScope;
import com.biit.abcd.persistence.entity.TreeObject;
import com.biit.abcd.persistence.entity.expressions.AvailableFunction;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionFunction;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorMath;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueNumber;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;

public class RowRuleParser {

	public static String parse(List<Expression> conditions, ExpressionChain action){
		String ruleCore = "";
		List<List<Object>> conditionObjects = new ArrayList<List<Object>>();

		for (Expression condition : conditions) {
			// Question
			if (condition instanceof ExpressionValueTreeObjectReference) {
				List<Object> auxList = new ArrayList<Object>();
				auxList.add(((ExpressionValueTreeObjectReference) condition).getReference());
				conditionObjects.add(auxList);
			}
			// Answer
			else if (condition instanceof ExpressionChain) {
				List<Expression> answerExpressions = ((ExpressionChain) condition).getExpressions();
				// First element of the expression, it will determine the
				// expression to transform
				Expression answerExpression = answerExpressions.get(0);

				// Simple answer (radio button | checkbox)
				if (answerExpression instanceof ExpressionValueTreeObjectReference) {
					List<Object> auxList = new ArrayList<Object>();
					auxList.add(((ExpressionValueTreeObjectReference) answerExpression).getReference());
					conditionObjects.add(auxList);
				}
				// Answer containing logic expression
				else if (answerExpression instanceof ExpressionFunction) {
					ExpressionFunction expFunction = (ExpressionFunction) answerExpression;
					if (expFunction.getValue().equals(AvailableFunction.BETWEEN)) {
						// Get the values of the between expression
						Double val1 = ((ExpressionValueNumber) answerExpressions.get(1)).getValue();
						Double val2 = ((ExpressionValueNumber) answerExpressions.get(3)).getValue();
						// Add the elements to be transformed later
						List<Object> auxList = new ArrayList<Object>();
						auxList.add(expFunction);
						auxList.add(val1);
						auxList.add(val2);
						conditionObjects.add(auxList);

					} else if (expFunction.getValue().equals(AvailableFunction.IN)) {
						int valueIndex = 0;
						List<Object> auxList = new ArrayList<Object>();
						auxList.add(expFunction);
						String inValues = "";
						for (Expression auxExp : answerExpressions) {
							// Values of the between expression
							if ((valueIndex % 2) == 1) {
								inValues += auxExp.toString() + ", ";
							}
							valueIndex++;
						}
						// Remove the last comma
						inValues = inValues.substring(0, inValues.length()-2);
						auxList.add(inValues);
						conditionObjects.add(auxList);
					}
				}
			}
		}
		// Drools rule creation
		// It is assumed that the questions in the same row always belong to the
		// same category and the same group
		if (!conditionObjects.isEmpty()) {
			String categoryName = ((TreeObject) conditionObjects.get(0).get(0)).getCategory().getName();
			ruleCore += "	$form : SubmittedForm()\n";
			ruleCore += "	$category : Category() from $form.getCategory(\"" + categoryName + "\")\n";
			// Not all the questions are in groups
			boolean questionInCategory = true;
			if(((TreeObject) conditionObjects.get(0).get(0)).getGroup() != null){
				String groupName = ((TreeObject) conditionObjects.get(0).get(0)).getGroup().getName();
				ruleCore += "	$group : Group() from $category.getGroup(\"" + groupName + "\")\n";
				questionInCategory = false;
			}

			// Flag to know the number of the question
			int questionIndex = 1;
			for (int index = 0; index < (conditionObjects.size() - 1); index += 2) {

				TreeObject question = (TreeObject) conditionObjects.get(0).get(0);
				// If the answer has more than one value, it will be a logic answer
				if (conditionObjects.get(index + 1).size() > 1) {
					Object aux = conditionObjects.get(index + 1).get(0);
					if (aux instanceof ExpressionFunction) {
						ExpressionFunction expFunction = (ExpressionFunction) aux;
						// Between expression
						if (expFunction.getValue().equals(AvailableFunction.BETWEEN)) {
							Double val1 = (Double) conditionObjects.get(index + 1).get(1);
							Double val2 = (Double) conditionObjects.get(index + 1).get(2);
							if(questionInCategory){
								ruleCore += "	$question" + questionIndex + " : Question(isScoreNotSet(), getTag() == \""
										+ question.getName() + "\", getValue() > " + val1 + " && getValue() < " + val2
										+ ") from $category.getQuestions()\n";
							}else{
								ruleCore += "	$question" + questionIndex + " : Question(isScoreNotSet(), getTag() == \""
										+ question.getName() + "\", getValue() > " + val1 + " && getValue() < " + val2
										+ ") from $group.getQuestions()\n";
							}
						}
						// IN expression
						else if (expFunction.getValue().equals(AvailableFunction.IN)) {
							// Previously parsed values
							String inValues = (String) conditionObjects.get(index + 1).get(1);
							if(questionInCategory){
								ruleCore += "	$question" + questionIndex + " : Question(isScoreNotSet(), getTag() == \""
										+ question.getName() + "\", getValue() in (" + inValues
										+ ")) from $category.getQuestions()\n";
							}else{
								ruleCore += "	$question" + questionIndex + " : Question(isScoreNotSet(), getTag() == \""
										+ question.getName() + "\", getValue() in (" + inValues
										+ ")) from $group.getQuestions()\n";
							}
						}
					}
				} else {
					TreeObject answer = (TreeObject) conditionObjects.get(index + 1).get(0);
					if(questionInCategory){
						ruleCore += "	$question" + questionIndex + " : Question(isScoreNotSet(), getTag() == \""
								+ question.getName() + "\", getValue() == \"" + answer.getName()
								+ "\") from $category.getQuestions()\n";
					}else{
						ruleCore += "	$question" + questionIndex + " : Question(isScoreNotSet(), getTag() == \""
								+ question.getName() + "\", getValue() == \"" + answer.getName()
								+ "\") from $group.getQuestions()\n";
					}
				}
				questionIndex++;
			}
		}

		ruleCore += "then\n";

		// Actions
		List<Expression> actionExpressions = action.getExpressions();
		if (actionExpressions.size() == 3) {
			if ((actionExpressions.get(0) instanceof ExpressionValueCustomVariable)
					&& (actionExpressions.get(1) instanceof ExpressionOperatorMath)
					&& (actionExpressions.get(2) instanceof ExpressionValueNumber)) {
				// Assume the expression is: CustomVariable = Value
				ExpressionValueCustomVariable var = (ExpressionValueCustomVariable) actionExpressions.get(0);
				ExpressionValueNumber val = (ExpressionValueNumber) actionExpressions.get(2);
				// If there is more than one question, the action could be
				// linked to any of the questions and we need to know what it is
				int questionIndex = 0;
				for (int i = 0; i < (conditionObjects.size() - 1); i += 2) {
					if (var.getReference().equals(conditionObjects.get(i).get(0))) {
						break;
					}
					questionIndex++;
				}

				ruleCore += "	$form.setVariableValue("
						+ getVariableScope(var.getVariable().getScope(), questionIndex + 1)
						+ ", '" + var.getVariable().getName()
						+ "', " + val.getValue()
						+ ");\n";
				ruleCore += "	out.println( \"Variable modified (" + var.getReference().getName() + ", "  + var.getVariable().getName() + ", "
						+ val.getValue() + ")\");\n";
			}
		}
		if (actionExpressions.size() == 5) {
			if ((actionExpressions.get(0) instanceof ExpressionValueCustomVariable)
					&& (actionExpressions.get(1) instanceof ExpressionOperatorMath)
					&& (actionExpressions.get(2) instanceof ExpressionValueCustomVariable)
					&& (actionExpressions.get(3) instanceof ExpressionOperatorMath)
					&& (actionExpressions.get(4) instanceof ExpressionValueNumber)) {
				// Assume the expression is: CustomVariable = CustomVariable (+ | - | * | /) Value
				ExpressionValueCustomVariable var1 = (ExpressionValueCustomVariable) actionExpressions.get(0);
				ExpressionValueCustomVariable var2 = (ExpressionValueCustomVariable) actionExpressions.get(2);
				ExpressionOperatorMath operator = (ExpressionOperatorMath) actionExpressions.get(3);
				ExpressionValueNumber value = (ExpressionValueNumber) actionExpressions.get(4);
				// If there is more than one question, the action could be
				// linked to any of the questions and we need to know what it is
				int questionIndex1 = 0;
				for (int i = 0; i < (conditionObjects.size() - 1); i += 2) {
					if (var1.getReference().equals(conditionObjects.get(i).get(0))) {
						break;
					}
					questionIndex1++;
				}

				int questionIndex2 = 0;
				for (int i = 0; i < (conditionObjects.size() - 1); i += 2) {
					if (var1.getReference().equals(conditionObjects.get(i).get(0))) {
						break;
					}
					questionIndex2++;
				}

				ruleCore += "	$form.setVariableValue("
						+ getVariableScope(var1.getVariable().getScope(), questionIndex1 + 1)
						+ ", '" + var1.getVariable().getName() + "', "
						+ "$form.getVariableValue(" + getVariableScope(var2.getVariable().getScope(), questionIndex2 + 1)
						+ ", '" + var2.getVariable().getName()+ "') " + operator.getValue() + " " + value.getValue() + ");\n";
				ruleCore += "	out.println( \"Variable modified: " + var1.getVariable().getName() + "\");\n";
			}
		}

		return ruleCore;
	}

	private static String getVariableScope(CustomVariableScope var, int questionIndex) {
		if (var.equals(CustomVariableScope.FORM)) {
			return "$form";
		} else if (var.equals(CustomVariableScope.CATEGORY)) {
			return "$category";
		} else if (var.equals(CustomVariableScope.GROUP)) {
			return "$group";
		} else if (var.equals(CustomVariableScope.QUESTION)) {
			return "$question" + questionIndex;
		}
		return "";
	}

}

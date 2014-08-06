package com.biit.abcd.core.drools.rules;

import java.util.List;

import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.CustomVariableScope;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.TreeObject;
import com.biit.abcd.persistence.entity.expressions.AvailableFunction;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionFunction;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorMath;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;

/**
 * Known restrictions: - The questions'/groups'/categories' score to set and
 * calculate can only be separated by one level (You can't calculate the
 * minimum score of the questions belonging to a group and set it to a category)
 *
 */
public class ExpressionParser {

	public static String parse(ExpressionChain expressionChain) throws ExpressionInvalidException {
		String newRule = "";
		String expressionName = expressionChain.getName();

//		RuleChecker.checkExpressionValid(expressionChain);
		newRule += Utils.getStartRuleString(expressionName);
		newRule += Utils.getAttributes();
		newRule += Utils.getWhenRuleString();
		newRule += createRule(expressionChain);
		newRule += Utils.getEndRuleString();
		return newRule;
	}

	private static String createRule(ExpressionChain expressionChain) {
		String ruleCore = "";
		List<Expression> expressionList = expressionChain.getExpressions();
		//		for (Expression expression : expressionChain.getExpressions()) {
		//			System.out.println(expression.getClass());
		//		}


		// LHS
		ExpressionValueCustomVariable variableToCalculate = null;
		if ((expressionList.get(0) instanceof ExpressionValueCustomVariable)
				&& (expressionList.get(1) instanceof ExpressionOperatorMath)
				&& (expressionList.get(2) instanceof ExpressionFunction)) {
			// We will have some expression of type Category.score = (Min | Max | Avg) of some values
			ruleCore += "	$form : SubmittedForm()\n";

			variableToCalculate = (ExpressionValueCustomVariable) expressionList.get(0);
			// The rule is different if the variable to assign is a Form, a
			// Category or a Group
			if (variableToCalculate.getReference() instanceof Form) {
			} else if (variableToCalculate.getReference() instanceof Category) {
				Category category = (Category) variableToCalculate.getReference();
				ruleCore += "	$category : Category() from $form.getCategory('" + category.getName() + "')\n";

			} else if (variableToCalculate.getReference() instanceof Group) {
				Group group = (Group) variableToCalculate.getReference();
				Category category = (Category) group.getParent();
				ruleCore += "	$category : Category() from $form.getCategory('" + category.getName() + "')\n";
				ruleCore += "	$group : Group() from $category.getGroup('" + group.getName() + "')\n";
			}

			int varIndex = 1;
			String scopeClass = "";
			TreeObject questionParent = null;
			CustomVariable cVar = null;
			for (int i = 3; i < expressionList.size(); i++) {
				Expression expression = expressionList.get(i);
				if (expression instanceof ExpressionValueCustomVariable) {
					ExpressionValueCustomVariable aux = (ExpressionValueCustomVariable) expression;
					TreeObject to = aux.getReference();
					cVar = aux.getVariable();
					if (to instanceof Question) {
						scopeClass = "Question";
						questionParent = to.getParent();
					} else if (to instanceof Group) {
						scopeClass = "Group";
					} else if (to instanceof Category) {
						scopeClass = "Category";
					}
					if (varIndex == 1) {
						ruleCore += "	$var : List() from collect( " + scopeClass + "(isScoreSet('" + cVar.getName() + "'), getTag() == '" + to.getName() + "' || ";
					} else {
						ruleCore += "== '" + to.getName() + "' || ";
					}
					varIndex++;
				}
			}
			// Finish the line of the condition
			ruleCore = ruleCore.substring(0, ruleCore.length() - 3);
			if (scopeClass.equals("Question")) {
				if ((questionParent != null) && (questionParent instanceof Category)) {
					ruleCore += ") from $category.getQuestions())\n";
				} else if ((questionParent != null) && (questionParent instanceof Group)) {
					ruleCore += ") from $group.getQuestions())\n";
				}
			} else if (scopeClass.equals("Group")) {
				ruleCore += ") from $category.getGroups())\n";
			} else if (scopeClass.equals("Category")) {
				ruleCore += ") from $form.getCategories())\n";
			}

			String getVarValue = "getVariableValue('" + cVar.getName() + "')";
			ExpressionFunction function = (ExpressionFunction) expressionList.get(2);
			if (function.getValue().equals(AvailableFunction.MAX)) {
				ruleCore += "	accumulate( " + scopeClass + "($value : " +getVarValue+ ") from $var; $sol : max($value)) \n";
			} else if (function.getValue().equals(AvailableFunction.MIN)) {
				ruleCore += "	accumulate( " + scopeClass + "($value : " +getVarValue+ ") from $var; $sol : min($value)) \n";
			} else if (function.getValue().equals(AvailableFunction.AVG)) {
				ruleCore += "	accumulate( " + scopeClass + "($value : " +getVarValue+ ") from $var; $sol : average($value)) \n";
			}
		}

		ruleCore += Utils.getThenRuleString();

		// RHS
		if(variableToCalculate != null){
			ruleCore += "	$form.setVariableValue("
					+ getVariableScope(variableToCalculate.getVariable().getScope())
					+ ", '" + variableToCalculate.getVariable().getName()
					+ "', $sol);\n";

			ruleCore += "	System.out.println(\"Result: \" + $sol);\n";
		}
		return ruleCore;
	}

	private static String getVariableScope(CustomVariableScope var) {
		if (var.equals(CustomVariableScope.FORM)) {
			return "$form";
		} else if (var.equals(CustomVariableScope.CATEGORY)) {
			return "$category";
		} else if (var.equals(CustomVariableScope.GROUP)) {
			return "$group";
		} else if (var.equals(CustomVariableScope.QUESTION)) {
			return "$question";
		}
		return "";
	}

}

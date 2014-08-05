package com.biit.abcd.core.drools.rules;

import java.util.List;

import com.biit.abcd.core.drools.rules.exceptions.RuleInvalidException;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.CustomVariableScope;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.TreeObject;
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorLogic;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorMath;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueNumber;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueString;
import com.biit.abcd.persistence.entity.expressions.Rule;

public class RuleParser {

	public static String parse(Rule rule) throws RuleInvalidException {
		String newRule = "";
		String ruleName = rule.getName();

//		RuleChecker.checkRuleValid(rule);
		newRule += Utils.getStartRuleString(ruleName);
		newRule += Utils.getAttributes();
		newRule += Utils.getWhenRuleString();
		newRule += createDroolsRule(rule.getConditions(), rule.getActions().getExpressions());
		newRule += Utils.getEndRuleString();
		return newRule;
	}

	public static String createDroolsRule(List<Expression> conditions, List<Expression> actions) {
		String ruleCore = "";

		// If is a simple condition of type (cat.score == value)
		if((conditions.size() == 3) &&
				(conditions.get(0) instanceof ExpressionValueCustomVariable) &&
				(conditions.get(1) instanceof ExpressionOperatorLogic) &&
				(conditions.get(2) instanceof ExpressionValueNumber)){
			ruleCore += "	$form : SubmittedForm()\n";

			ExpressionValueCustomVariable var = (ExpressionValueCustomVariable) conditions.get(0);
			ExpressionOperatorLogic operator = (ExpressionOperatorLogic) conditions.get(1);
			ExpressionValueNumber valueNumber = (ExpressionValueNumber) conditions.get(2);

			TreeObject scope = var.getReference();
			String varName = var.getVariable().getName();
			if(scope instanceof Form) {

			} else if (scope instanceof Category) {
				ruleCore += "	$auxcat : Category( isScoreSet('"+varName+"')) from $form.getCategory('" + scope.getName() + "')\n";
				ruleCore += "	$category : Category( getVariableValue('"+varName+"') == "+valueNumber.getValue()+" ) from $auxcat\n";

			} else if (scope instanceof Group) {
				TreeObject parent = scope.getParent();
				ruleCore += "	$category : Category() from $form.getCategory('" + parent.getName() + "')\n";
				ruleCore += "	$group : Group( isScoreSet('"+varName+"'), getVariableValue('"+varName+"') == "+valueNumber.getValue()+" ) from $category.getGroup('" + scope.getName() + "')\n";
			}

			if(operator.getValue().equals(AvailableOperator.EQUALS)){

			}


		}
		ruleCore += Utils.getThenRuleString();

		// If is a simple action of type (cat.scoreText = "someText")
		if ((actions.size() == 3)
				&& (actions.get(0) instanceof ExpressionValueCustomVariable)
				&& (actions.get(1) instanceof ExpressionOperatorMath)
				&& (actions.get(2) instanceof ExpressionValueString)) {

			ExpressionValueCustomVariable var = (ExpressionValueCustomVariable) actions.get(0);
			ExpressionOperatorMath operator = (ExpressionOperatorMath) actions.get(1);
			ExpressionValueString valueString = (ExpressionValueString) actions.get(2);

			if (operator.getValue().equals(AvailableOperator.ASSIGNATION)) {
				ruleCore += "	$form.setVariableValue(" + getVariableScope(var.getVariable().getScope()) + ", '"
						+ var.getVariable().getName() + "', '" + valueString.getValue() + "');\n";
				ruleCore += "	System.out.println( \"Variable modified (" + var.getReference().getName() + ", "
						+ var.getVariable().getName() + ", " + valueString.getValue() + ")\");\n";
			}
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

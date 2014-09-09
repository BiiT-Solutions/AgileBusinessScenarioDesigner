package com.biit.abcd.core.drools.rules;

import java.util.UUID;

import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleNotImplementedException;
import com.biit.abcd.core.drools.utils.RulesUtils;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueGenericCustomVariable;

/**
 * Known restrictions: - The questions'/groups'/categories' score to set and
 * calculate can only be separated by one level (You can't calculate the minimum
 * score of the questions belonging to a group and set it to a category)
 *
 */
public class ExpressionParser extends GenericParser {

	public ExpressionParser() {
		super();
	}

	public String parse(ExpressionChain expressionChain, String extraConditions) throws ExpressionInvalidException,
			RuleNotImplementedException {
		String newRule = "";
		if (expressionChain != null) {
			if (expressionChain.getExpressions().get(0) instanceof ExpressionValueGenericCustomVariable) {
				newRule += this.createDroolsRule(null, expressionChain, extraConditions);
			} else {
				String expressionName = expressionChain.getName();
				if (expressionName == null) {
					expressionName = UUID.randomUUID().toString().replaceAll("-", "");
				}
				RuleChecker.checkExpressionValid(expressionChain);
				newRule += RulesUtils.getStartRuleString(expressionName);
				newRule += RulesUtils.getAttributes();
				newRule += RulesUtils.getWhenRuleString();
				newRule += this.createDroolsRule(null, expressionChain, extraConditions);
				newRule += RulesUtils.getEndRuleString();
			}
		}
		return newRule;
	}

	// public String parse(ExpressionChain expressionChain, String
	// extraConditions) throws ExpressionInvalidException,
	// RuleNotImplementedException {
	// String newRule = "";
	// if (expressions != null) {
	// if (expressions.get(0) instanceof ExpressionValueGenericCustomVariable) {
	// newRule += this.createDroolsRule(null, expressionChain, extraConditions);
	// } else {
	// // String expressionName = expressionChain.getName();
	// // RuleChecker.checkExpressionValid(expressionChain);
	// newRule +=
	// Utils.getStartRuleString(UUID.randomUUID().toString().replaceAll("-",
	// ""));
	// newRule += Utils.getAttributes();
	// newRule += Utils.getWhenRuleString();
	// newRule += this.createDroolsRule(null, expressionChain, extraConditions);
	// newRule += Utils.getEndRuleString();
	// }
	// }
	// return newRule;
	// }
}

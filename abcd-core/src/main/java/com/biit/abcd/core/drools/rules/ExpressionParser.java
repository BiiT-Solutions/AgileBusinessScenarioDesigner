package com.biit.abcd.core.drools.rules;

import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleNotImplementedException;
import com.biit.abcd.core.drools.utils.RulesUtils;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.Rule;

/**
 * Known restrictions: - The questions'/groups'/categories' score to set and calculate can only be separated by one
 * level (You can't calculate the minimum score of the questions belonging to a group and set it to a category)
 * 
 */
public class ExpressionParser {

	private ExpressionParser() {
		super();
	}

	public static Rule parse(ExpressionChain expressionChain, ExpressionChain extraConditions)
			throws ExpressionInvalidException, RuleNotImplementedException {
		if (expressionChain != null) {
			Rule newRule = new Rule();
			newRule.setName(RulesUtils.getRuleName(newRule.getName(), extraConditions));
			newRule.setActions(expressionChain);
			newRule.addConditions(extraConditions);
			return newRule;
		}
		return null;
	}
}

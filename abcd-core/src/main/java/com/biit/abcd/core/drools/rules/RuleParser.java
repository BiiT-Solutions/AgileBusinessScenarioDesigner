package com.biit.abcd.core.drools.rules;

import com.biit.abcd.core.drools.rules.exceptions.RuleInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleNotImplementedException;
import com.biit.abcd.core.drools.utils.RulesUtils;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.Rule;

public class RuleParser {

	private RuleParser() {
	}

	public static Rule parse(Rule rule, ExpressionChain extraConditions) throws RuleInvalidException,
			RuleNotImplementedException {
		if (rule != null) {
			RuleChecker.checkRuleValid(rule);
			rule.setName(RulesUtils.getRuleName(rule.getName(), extraConditions));
			rule.addConditions(extraConditions);
		}
		return rule;
	}
}

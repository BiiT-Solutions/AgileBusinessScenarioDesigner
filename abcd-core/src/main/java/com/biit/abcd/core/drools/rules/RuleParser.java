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
		DroolsRule droolsRule = null;
		if (rule != null) {
			droolsRule = new DroolsRule(rule.generateCopy());
			RuleChecker.checkRuleValid(droolsRule);
			droolsRule.setName(RulesUtils.getRuleName(droolsRule.getName(), extraConditions));
			if (extraConditions != null) {
				droolsRule.addConditions(extraConditions.generateCopy());
			}
		}
		return droolsRule;
	}
}

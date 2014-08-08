package com.biit.abcd.core.drools.rules;

import com.biit.abcd.core.drools.rules.exceptions.RuleInvalidException;
import com.biit.abcd.persistence.entity.expressions.Rule;

public class RuleParser extends GenericParser {

	public RuleParser() {
		super();
	}

	public String parse(Rule rule, String extraConditions) throws RuleInvalidException {
		String newRule = "";
		if (rule != null) {
			String ruleName = rule.getName();
			// RuleChecker.checkRuleValid(rule);
			newRule += Utils.getStartRuleString(ruleName);
			newRule += Utils.getAttributes();
			newRule += Utils.getWhenRuleString();
			newRule += this.createDroolsRule(rule.getConditions(), rule.getActions().getExpressions(), extraConditions);
			newRule += Utils.getEndRuleString();
		}
		return newRule;
	}
}

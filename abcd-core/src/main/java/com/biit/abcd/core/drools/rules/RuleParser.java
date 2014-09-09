package com.biit.abcd.core.drools.rules;

import com.biit.abcd.core.drools.rules.exceptions.RuleInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleNotImplementedException;
import com.biit.abcd.core.drools.utils.RulesUtils;
import com.biit.abcd.persistence.entity.expressions.Rule;

public class RuleParser extends GenericParser {

	public RuleParser() {
		super();
	}

	public String parse(Rule rule, String extraConditions) throws RuleInvalidException, RuleNotImplementedException {
		String newRule = "";
		if (rule != null) {
			String ruleName = rule.getName();
			RuleChecker.checkRuleValid(rule);
			newRule += RulesUtils.getStartRuleString(ruleName);
			newRule += RulesUtils.getAttributes();
			newRule += RulesUtils.getWhenRuleString();
			newRule += this.createDroolsRule(rule.getConditionChain(), rule.getActionChain(), extraConditions);
			newRule += RulesUtils.getEndRuleString();
		}
		return newRule;
	}
}

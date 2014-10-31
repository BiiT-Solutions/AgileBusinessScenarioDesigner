package com.biit.abcd.core.drools.rules;

import com.biit.abcd.persistence.entity.expressions.Rule;

public class DroolsRule extends Rule {

	public DroolsRule() {
		super();
	}

	public DroolsRule(Rule rule) {
		super();
		setName(rule.getName());
		setConditions(rule.getConditions());
		setActions(rule.getActions());
	}
}

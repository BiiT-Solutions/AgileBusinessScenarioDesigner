package com.biit.abcd.core.drools.rules;

import com.biit.abcd.persistence.entity.expressions.Rule;

/**
 * Basic drools rule class.
 */
public class DroolsRule extends Rule {
	private static final long serialVersionUID = 387304400306278717L;

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

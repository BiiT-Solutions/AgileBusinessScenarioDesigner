package com.biit.abcd.core.drools.rules;

import com.biit.abcd.persistence.entity.expressions.Rule;

/**
 * All the complex rules like 'A and (B or C)' are divided in sets of simple
 * rules like: 'BC = B or C', 'A and BC'.<br>
 * To allow this we create groups of rules so we can know when a rule is part of
 * a more complex one.<br>
 */
public class DroolsRuleGroup extends DroolsRule {
	private static final long serialVersionUID = -1248781299336582512L;
	private String groupCondition = "";
	private String groupAction = "";
	private String conditionExpressionChainId = "";

	public DroolsRuleGroup() {
		super();
	}

	public DroolsRuleGroup(Rule rule) {
		super(rule);
	}

	public String getGroupCondition() {
		return groupCondition;
	}

	public void setGroupCondition(String groupCondition) {
		this.groupCondition = groupCondition;
	}

	public String getGroupAction() {
		return groupAction;
	}

	public void setGroupAction(String groupAction) {
		this.groupAction = groupAction;
	}

	public String getConditionExpressionChainId() {
		return conditionExpressionChainId;
	}

	public void setConditionExpressionChainId(String expressionChainId) {
		this.conditionExpressionChainId = expressionChainId;
	}
}

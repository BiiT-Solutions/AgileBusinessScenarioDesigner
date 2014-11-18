package com.biit.abcd.core.drools.rules;

import com.biit.abcd.persistence.entity.expressions.Rule;

public class DroolsRuleGroup extends DroolsRule {

	private String groupCondition =  "";
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

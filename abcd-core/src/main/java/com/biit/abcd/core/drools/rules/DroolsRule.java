package com.biit.abcd.core.drools.rules;

import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.persistence.entity.expressions.Rule;

public class DroolsRule extends Rule {

	private List<DroolsCondition> droolsConditions = null;
	private List<DroolsAction> droolsActions = null;

	public DroolsRule() {
		super();
		droolsConditions = new ArrayList<DroolsCondition>();
		droolsActions = new ArrayList<DroolsAction>();
	}

	public DroolsRule(Rule rule) {
		super();
		setName(rule.getName());
		setConditions(rule.getConditions());
		setActions(rule.getActions());
	}

	public String getDroolsRule() {
		String droolsRule = "";
		for(DroolsCondition droolsCondition : droolsConditions){
			droolsRule += droolsCondition.toString();
		}
		droolsRule += "then\n";		
		for(DroolsAction droolsAction : droolsActions){
			droolsRule += droolsAction.toString();
		}
		droolsRule += "end\n";
		return droolsRule;
	}
}

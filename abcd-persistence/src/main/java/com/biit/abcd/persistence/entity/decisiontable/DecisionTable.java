package com.biit.abcd.persistence.entity.decisiontable;

import java.util.HashSet;
import java.util.Set;

import com.biit.abcd.persistence.entity.Question;

public class DecisionTable {

	private Set<Question> conditions;
	
	private Set<DecisionRule> rules;
	
	public DecisionTable(){
		conditions = new HashSet<Question>();
		rules = new HashSet<DecisionRule>();
	}

	public Set<Question> getConditions() {
		return conditions;
	}
	
	public Set<DecisionRule> getRules() {
		return rules;
	}
	
}

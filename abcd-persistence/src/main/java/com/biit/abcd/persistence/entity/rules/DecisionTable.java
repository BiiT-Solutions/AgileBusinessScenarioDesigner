package com.biit.abcd.persistence.entity.rules;

import java.util.HashSet;
import java.util.Set;

import com.biit.abcd.persistence.entity.Question;

public class DecisionTable {

	private Set<Question> conditions;
	
	private Set<TableRule> rules;
	
	public DecisionTable(){
		conditions = new HashSet<Question>();
		rules = new HashSet<TableRule>();
	}

	public Set<Question> getConditions() {
		return conditions;
	}
	
	public Set<TableRule> getRules() {
		return rules;
	}
	
}

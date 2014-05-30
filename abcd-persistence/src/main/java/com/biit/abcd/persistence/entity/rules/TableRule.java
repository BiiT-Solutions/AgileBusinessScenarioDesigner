package com.biit.abcd.persistence.entity.rules;

import java.util.HashMap;
import java.util.Map;

import com.biit.abcd.persistence.entity.Question;

/**
 * Specific rules created for managing decision tables.
 * 
 */
public class TableRule {

	private Map<Question, Condition> conditions;

	public TableRule() {
		conditions = new HashMap<Question, Condition>();
	}

	public void putCondition(Question question, Condition condition) {
		conditions.put(question, condition);
	}

	public void removeCondition(Question question) {
		conditions.remove(question);
	}

	public Map<Question, Condition> getConditions() {
		return conditions;
	}

}

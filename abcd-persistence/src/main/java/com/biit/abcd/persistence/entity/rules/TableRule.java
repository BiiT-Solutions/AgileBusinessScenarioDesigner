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
	private Action action;

	public TableRule() {
		conditions = new HashMap<Question, Condition>();
		action = new Action();
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

	public Action getAction() {
		return action;
	}

}

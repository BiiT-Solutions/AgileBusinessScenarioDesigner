package com.biit.abcd.persistence.entity.decisiontable;

import java.util.HashMap;
import java.util.Map;

import com.biit.abcd.persistence.entity.Question;

public class DecisionRule {

	private Map<Question, String> conditions;

	public DecisionRule() {
		conditions = new HashMap<Question, String>();
	}

	public void putCondition(Question question, String expresion){
		conditions.put(question,expresion);
	}
	
	public void removeCondition(Question question){
		conditions.remove(question);
	}
	
}

package com.biit.abcd.persistence.entity.rules;

import com.biit.abcd.persistence.entity.Answer;

public class AnswerCondition extends Condition {
	private Answer answer;

	public AnswerCondition(Answer answer) {
		this.answer = answer;
	}

	@Override
	public String toString() {
		if (answer != null) {
			return answer.toString();
		}
		return "";
	}

}

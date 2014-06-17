package com.biit.abcd.persistence.entity.rules;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.Answer;

@Entity
@Table(name = "RULE_ANSWER_CONDITION")
public class AnswerCondition extends Condition {
	@ManyToOne(fetch = FetchType.EAGER)
	private Answer answer;

	public AnswerCondition() {

	}

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

	public Answer getAnswer() {
		return answer;
	}

	public void setAnswer(Answer answer) {
		this.answer = answer;
	}

}

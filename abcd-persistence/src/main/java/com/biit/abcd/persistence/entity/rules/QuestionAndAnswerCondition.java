package com.biit.abcd.persistence.entity.rules;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.StorableObject;

/**
 * Class for storing a column and answer condition. Used in the Table Rule definitions.
 */
@Entity
@Table(name = "QUESTION_AND_ANSWERS_CONDITION")
public class QuestionAndAnswerCondition extends StorableObject {

	@ManyToOne(fetch = FetchType.EAGER)
	private Question question;

	@ManyToOne(fetch = FetchType.EAGER)
	private Answer answer;

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public Answer getAnswer() {
		return answer;
	}

	public void setAnswer(Answer answer) {
		this.answer = answer;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((answer == null) ? 0 : answer.hashCode());
		result = prime * result + ((question == null) ? 0 : question.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		QuestionAndAnswerCondition other = (QuestionAndAnswerCondition) obj;
		if (answer == null) {
			if (other.answer != null)
				return false;
		} else if (!answer.equals(other.answer))
			return false;
		if (question == null) {
			if (other.question != null)
				return false;
		} else if (!question.equals(other.question))
			return false;
		return true;
	}
	
}

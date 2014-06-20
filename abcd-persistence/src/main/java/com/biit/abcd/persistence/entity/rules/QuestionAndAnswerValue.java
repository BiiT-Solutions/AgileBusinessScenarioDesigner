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
public class QuestionAndAnswerValue extends StorableObject {

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

}

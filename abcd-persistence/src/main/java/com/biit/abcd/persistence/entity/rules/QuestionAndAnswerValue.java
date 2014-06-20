package com.biit.abcd.persistence.entity.rules;

import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.Question;

public class QuestionAndAnswerValue {

	private Question question;

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

package com.biit.abcd.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.biit.form.BaseQuestion;
import com.biit.form.exceptions.FieldTooLongException;
import com.biit.form.exceptions.InvalidAnswerFormatException;

@Entity
@Table(name = "TREE_QUESTIONS")
public class Question extends BaseQuestion {
	@Enumerated(EnumType.STRING)
	private AnswerType answerType;
	@Enumerated(EnumType.STRING)
	private AnswerFormat answerFormat;

	public Question() {
	}

	public Question(String name) throws FieldTooLongException {
		super(name);
	}

	public AnswerType getAnswerType() {
		return this.answerType;
	}

	public void setAnswerType(AnswerType answerType) {
		this.answerType = answerType;
	}

	public AnswerFormat getAnswerFormat() {
		return this.answerFormat;
	}

	public void setAnswerFormat(AnswerFormat answerFormat) throws InvalidAnswerFormatException {
		if (answerType.isInputField()) {
			if (answerFormat == null) {
				throw new InvalidAnswerFormatException("Input fields must define an answer format.");
			}
		} else {
			if (answerFormat != null) {
				throw new InvalidAnswerFormatException("Non Input fields cannot define an answer format.");
			}
		}
		this.answerFormat = answerFormat;
	}
}

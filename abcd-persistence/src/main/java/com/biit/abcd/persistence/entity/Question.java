package com.biit.abcd.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.biit.abcd.persistence.utils.CheckDependencies;
import com.biit.form.entity.BaseQuestion;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.DependencyExistException;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

@Entity
@Table(name = "tree_questions")
public class Question extends BaseQuestion {
	private static final long serialVersionUID = 6352523681890504871L;
	@Enumerated(EnumType.STRING)
	private AnswerType answerType;
	@Enumerated(EnumType.STRING)
	private AnswerFormat answerFormat;

	public Question() {
	}

	public Question(String name) throws FieldTooLongException, CharacterNotAllowedException {
		super(name);
	}

	public AnswerType getAnswerType() {
		return answerType;
	}

	public void setAnswerType(AnswerType answerType) {
		this.answerType = answerType;
	}

	public AnswerFormat getAnswerFormat() {
		return answerFormat;
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

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof Question) {
			copyBasicInfo(object);
			answerType = ((Question) object).getAnswerType();
			answerFormat = ((Question) object).getAnswerFormat();
		} else {
			throw new NotValidTreeObjectException("Question can only be copied from another question.");
		}
	}

	@Override
	public void checkDependencies() throws DependencyExistException {
		CheckDependencies.checkTreeObjectDependencies(this);
	}
}

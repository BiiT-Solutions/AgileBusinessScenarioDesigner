package com.biit.abcd.persistence.entity;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.utils.CheckDependencies;
import com.biit.form.entity.BaseQuestion;
import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.DependencyExistException;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

@Entity
@Table(name = "tree_questions")
@Cacheable(true)
public class Question extends BaseQuestion {
	private static final long serialVersionUID = 6352523681890504871L;

	@Enumerated(EnumType.STRING)
	@Column(name = "answer_type")
	private AnswerType answerType;

	@Enumerated(EnumType.STRING)
	@Column(name = "answer_format")
	private AnswerFormat answerFormat;

	public Question() {
	}

	public Question(String name) throws FieldTooLongException, CharacterNotAllowedException {
		super(name);
	}

	public AnswerType getAnswerType() {
		return answerType;
	}

	/**
	 * This setter sets AnswerType and sets the answer format to the default
	 * answer format for a type.
	 * 
	 * @param answerType
	 */
	public void setAnswerType(AnswerType answerType) {
		AnswerType prevValue = this.answerType;
		this.answerType = answerType;
		try {
			// If you change to input field, select the default value.
			if (answerType != prevValue) {
				setAnswerFormat(answerType.getDefaultAnswerFormat());
				if (!answerType.isChildrenAllowed()) {
					getChildren().clear();
				}
				// Dropdown list does not allow subanswers.
				if (!answerType.isSubChildrenAllowed()) {
					for (TreeObject child : getChildren()) {
						child.getChildren().clear();
					}
				}
			}
		} catch (InvalidAnswerFormatException e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}
	}

	public AnswerFormat getAnswerFormat() {
		if (answerType != null && !answerType.isAnswerFormatEnabled()) {
			return null;
		}
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

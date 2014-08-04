package com.biit.abcd.persistence.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.exceptions.InvalidAnswerFormatException;

@Entity
@Table(name = "TREE_ANSWERS")
public class Answer extends TreeObject {
	private static final String DEFAULT_ANSWER_TECHNICAL_NAME = "Answer";
	private static final List<Class<?>> ALLOWED_PARENTS = new ArrayList<Class<?>>(Arrays.asList(Question.class));

	private AnswerType answerType;
	private AnswerFormat answerFormat;

	public Answer() {
	}

	public Answer(String name) {
		setName(name);
	}

	@Override
	protected List<Class<?>> getAllowedChilds() {
		return null;
	}

	@Override
	protected List<Class<?>> getAllowedParents() {
		return ALLOWED_PARENTS;
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
		if (answerType.equals(AnswerType.INPUT)) {
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

	/**
	 * Creates a default Answer technical name, different for each Answer of the same Question.
	 * 
	 * @param startingIndex
	 * @return
	 */
	public String getDefaultName(TreeObject parent, int startingIndex) {
		String name;
		if (parent != null) {
			name = DEFAULT_ANSWER_TECHNICAL_NAME + startingIndex;
			for (TreeObject child : parent.getChildren()) {
				if ((child instanceof Answer) && (((Answer) child).getName() != null)
						&& ((Answer) child).getName().equals(name)) {
					return getDefaultName(parent, startingIndex + 1);
				}
			}
		} else {
			name = DEFAULT_ANSWER_TECHNICAL_NAME;
		}
		return name;
	}

	@Override
	public String toString() {
		return getName();
	}

}

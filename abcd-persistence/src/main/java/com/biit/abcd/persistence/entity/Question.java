package com.biit.abcd.persistence.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "TREE_QUESTIONS")
public class Question extends TreeObject {
	private static final String DEFAULT_QUESTION_TECHNICAL_NAME = "Question";
	private static final List<Class<?>> ALLOWED_CHILDS = new ArrayList<Class<?>>(Arrays.asList(Answer.class));
	private static final List<Class<?>> ALLOWED_PARENTS = new ArrayList<Class<?>>(Arrays.asList(Category.class,
			Group.class));

	@Enumerated(EnumType.STRING)
	private AnswerType answerType;
	@Enumerated(EnumType.STRING)
	private AnswerFormat answerFormat;

	public Question() {
	}

	@Override
	protected List<Class<?>> getAllowedChilds() {
		return ALLOWED_CHILDS;
	}

	@Override
	protected List<Class<?>> getAllowedParents() {
		return ALLOWED_PARENTS;
	}

	/**
	 * Creates a default Question technical name, different for each Question of the same parent.
	 * 
	 * @param startingIndex
	 * @return
	 */
	public String getDefaultName(TreeObject parent, int startingIndex) {
		String name;
		if (parent != null) {
			name = DEFAULT_QUESTION_TECHNICAL_NAME + startingIndex;
			for (TreeObject child : parent.getChildren()) {
				if (child instanceof Question && ((Question) child).getName() != null
						&& ((Question) child).getName().equals(name)) {
					return getDefaultName(parent, startingIndex + 1);
				}
			}
		} else {
			name = DEFAULT_QUESTION_TECHNICAL_NAME;
		}
		return name;
	}

	@Override
	public String toString() {
		return getName();
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

	public void setAnswerFormat(AnswerFormat answerFormat) {
		this.answerFormat = answerFormat;
	}
}

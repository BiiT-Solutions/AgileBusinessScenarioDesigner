package com.biit.abcd.persistence.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "QUESTIONS")
public class Question extends TreeObject {
	private static final String DEFAULT_QUESTION_TECHNICAL_NAME = "Question";
	private static final List<Class<?>> ALLOWED_CHILDS = new ArrayList<Class<?>>(Arrays.asList(Answer.class));
	private static final List<Class<?>> ALLOWED_PARENTS = new ArrayList<Class<?>>(Arrays.asList(Category.class,
			Group.class));

	private String technicalName;

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

	public String getTechnicalName() {
		return technicalName;
	}

	public void setTechnicalName(String technicalName) {
		this.technicalName = technicalName;
	}

	/**
	 * Creates a default Question technical name, different for each Question of the same parent.
	 * 
	 * @param startingIndex
	 * @return
	 */
	public String getDefaultTechnicalName(TreeObject parent, int startingIndex) {
		String name;
		if (parent != null) {
			name = DEFAULT_QUESTION_TECHNICAL_NAME + startingIndex;
			for (TreeObject child : parent.getChildren()) {
				if (child instanceof Question && ((Question) child).getTechnicalName() != null
						&& ((Question) child).getTechnicalName().equals(name)) {
					return getDefaultTechnicalName(parent, startingIndex + 1);
				}
			}
		} else {
			name = DEFAULT_QUESTION_TECHNICAL_NAME;
		}
		return name;
	}

	@Override
	public String toString() {
		return getTechnicalName();
	}
}

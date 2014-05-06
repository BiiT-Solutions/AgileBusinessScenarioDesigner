package com.biit.abcd.persistence.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "QUESTIONS")
public class Question extends TreeObject {
	private static final List<Class<?>> ALLOWED_CHILDS = new ArrayList<Class<?>>(Arrays.asList(Answer.class));
	private static final List<Class<?>> ALLOWED_PARENTS = new ArrayList<Class<?>>(Arrays.asList(Category.class,
			Group.class));

	private String technicalName;

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
}

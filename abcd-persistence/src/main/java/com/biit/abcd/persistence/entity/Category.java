package com.biit.abcd.persistence.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "CATEGORIES")
public class Category extends TreeObject {
	private static final List<Class<?>> ALLOWED_CHILDS = new ArrayList<Class<?>>(Arrays.asList(Question.class,
			Group.class));
	private static final List<Class<?>> ALLOWED_PARENTS = new ArrayList<Class<?>>(Arrays.asList(Form.class));

	private String label;

	@Override
	protected List<Class<?>> getAllowedChilds() {
		return ALLOWED_CHILDS;
	}

	@Override
	protected List<Class<?>> getAllowedParents() {
		return ALLOWED_PARENTS;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}

package com.biit.abcd.core.drools.rules;

import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;

public enum DroolsVariableScope {
	
	FORM(Form.class, "Form"),

	CATEGORY(Category.class, "Category"),

	GROUP(Group.class, "Group"),

	QUESTION(Question.class, "Question");

	private Class<?> scope;
	private String name;

	private DroolsVariableScope(Class<?> scope, String name) {
		this.scope = scope;
		this.name = name;
	}

	public Class<?> getScope() {
		return scope;
	}

	public String getName() {
		return name;
	}
}

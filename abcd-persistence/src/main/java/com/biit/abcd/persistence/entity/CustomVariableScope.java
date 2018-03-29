package com.biit.abcd.persistence.entity;

import com.biit.form.entity.TreeObject;

public enum CustomVariableScope {

	FORM(Form.class, "class.Form", "Form"),

	CATEGORY(Category.class, "class.Category", "Category"),

	GROUP(Group.class, "class.Group", "Group"),

	QUESTION(Question.class, "class.Question", "Question");

	private Class<? extends TreeObject> scope;
	private String translation;
	private String name;

	private CustomVariableScope(Class<? extends TreeObject> scope, String translation, String name) {
		this.scope = scope;
		this.translation = translation;
		this.name = name;
	}

	public Class<? extends TreeObject> getScope() {
		return scope;
	}

	public String getTranslationCode() {
		return translation;
	}

	public String getName() {
		return name;
	}

}

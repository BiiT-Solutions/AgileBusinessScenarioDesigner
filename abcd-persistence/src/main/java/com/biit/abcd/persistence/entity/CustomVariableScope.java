package com.biit.abcd.persistence.entity;

public enum CustomVariableScope {

	FORM(Form.class, "class.Form", "Form"),

	CATEGORY(Category.class, "class.Category", "Category"),

	GROUP(Group.class, "class.Group", "Group"),

	QUESTION(Question.class, "class.Question", "Question");

	private Class<?> scope;
	private String translation;
	private String name;

	private CustomVariableScope(Class<?> scope, String translation, String name) {
		this.scope = scope;
		this.translation = translation;
		this.name = name;
	}

	public Class<?> getScope() {
		return scope;
	}

	public String getTranslationCode() {
		return translation;
	}

	public String getName() {
		return name;
	}

}

package com.biit.abcd.persistence.entity;


public enum CustomVariableScope {

	FORM(Form.class, "class.Form"),

	CATEGORY(Category.class, "class.Category"),

	GROUP(Group.class, "class.Group"),

	QUESTION(Question.class, "class.Question");

	private Class<?> scope;
	private String translation;

	private CustomVariableScope(Class<?> scope, String translation) {
		this.scope = scope;
		this.translation = translation;
	}

	public Class<?> getScope() {
		return scope;
	}

	public String getTranslationCode() {
		return translation;
	}

}

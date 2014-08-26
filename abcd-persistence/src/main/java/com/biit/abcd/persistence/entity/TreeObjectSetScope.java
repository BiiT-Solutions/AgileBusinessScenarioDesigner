package com.biit.abcd.persistence.entity;


public enum TreeObjectSetScope {

	CATEGORY(Category.class, "Categories"),

	GROUP(Group.class, "Groups"),

	QUESTION_CATEGORY(Question.class, "Category.Questions"),

	QUESTION_GROUP(Question.class, "Group.Questions");

	private Class<?> scope;
	private String name;

	private TreeObjectSetScope(Class<?> scope, String name) {
		this.scope = scope;
		this.name = name;
	}

	public Class<?> getScope() {
		return scope;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString(){
		return getName();
	}

}

package com.biit.abcd.persistence.entity;


public enum GenericTreeObjectType {

	CATEGORY(Category.class, "Categories", "Categories"),

	GROUP(Group.class, "Groups", "Groups"),

	QUESTION_CATEGORY(Question.class, "Questions", "Category.Questions"),

	QUESTION_GROUP(Question.class, "Questions", "Group.Questions");

	private Class<?> scope;
	private String treeTableName;
	private String expressionCaption;

	private GenericTreeObjectType(Class<?> scope, String treeTableName, String expressionCaption) {
		this.scope = scope;
		this.treeTableName = treeTableName;
		this.expressionCaption = expressionCaption;
	}

	public Class<?> getScope() {
		return scope;
	}

	public String getTableName() {
		return treeTableName;
	}

	public String getExpressionName() {
		return expressionCaption;
	}

	@Override
	public String toString(){
		return getExpressionName();
	}

}

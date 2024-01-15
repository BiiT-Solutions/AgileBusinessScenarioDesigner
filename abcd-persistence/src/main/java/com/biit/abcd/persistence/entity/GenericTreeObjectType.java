package com.biit.abcd.persistence.entity;


public enum GenericTreeObjectType {

    CATEGORY(Category.class, "Categories", "Categories"),

    GROUP(Group.class, "Groups", "Groups"),

    QUESTION_CATEGORY(Question.class, "Questions", "Category.Questions"),

    QUESTION_GROUP(Question.class, "Questions", "Group.Questions");

    private Class<?> scope;
    private String tableName;
    private String expressionName;

    private GenericTreeObjectType(Class<?> scope, String tableName, String expressionName) {
        this.scope = scope;
        this.tableName = tableName;
        this.expressionName = expressionName;
    }

    public Class<?> getScope() {
        return scope;
    }

    public String getTableName() {
        return tableName;
    }

    public String getExpressionName() {
        return expressionName;
    }

    @Override
    public String toString() {
        return getExpressionName();
    }

    public static GenericTreeObjectType get(String genericTreeObjectType) {
        for (GenericTreeObjectType type : GenericTreeObjectType.values()) {
            if (type.name().equalsIgnoreCase(genericTreeObjectType)) {
                return type;
            }
        }
        return null;
    }
}

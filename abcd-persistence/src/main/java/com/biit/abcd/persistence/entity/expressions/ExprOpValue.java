package com.biit.abcd.persistence.entity.expressions;

public enum ExprOpValue {

	NULL("NULL", ""),

	AND("AND", "AND"),

	OR("OR", "OR"),

	EQUALS("==", "=="),

	NOT_EQUALS("!=", "!="),

	GREATER_THAN(">", ">"),

	GREATER_EQUALS(">=", ">="),

	LESS_THAN("<", "<"),

	LESS_EQUALS("<=", "<="),

	ASSIGNATION("=", "="),

	PLUS("+", "+"),

	MINUS("-", "-"),

	MULTIPLICATION("*", "*"),

	DIVISION("/", "/"),

	MODULE("%", "%");

	private String value;
	private String caption;

	private ExprOpValue(String value, String caption) {
		this.value = value;
		this.caption = caption;
	}

	public String getValue() {
		return value;
	}

	public String getCaption() {
		return caption;
	}

	@Override
	public String toString() {
		return value;
	}
}

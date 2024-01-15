package com.biit.abcd.persistence.entity.expressions;

public enum AvailableOperator {

	NULL("NULL", "CLEAR"),

	AND("&&", "AND"),

	OR("||", "OR"),

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

	MODULE("%", "%"), 
	
	POW("^", "^");

	private String value;
	private String caption;

	private AvailableOperator(String value, String caption) {
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
		return caption;
	}

	public static AvailableOperator getOperator(String value) {
		for (AvailableOperator operator : AvailableOperator.values()) {
			if (operator.caption.equalsIgnoreCase(value)) {
				return operator;
			}
		}
		return null;
	}

	public static AvailableOperator get(String value) {
		for (AvailableOperator operator : AvailableOperator.values()) {
			if (operator.name().equalsIgnoreCase(value)) {
				return operator;
			}
		}
		return null;
	}
}

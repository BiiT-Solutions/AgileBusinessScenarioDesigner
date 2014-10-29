package com.biit.abcd.persistence.entity.expressions;

/**
 * Non operators symbols used for defining an expression.
 * 
 */
public enum AvailableSymbol {

	RIGHT_BRACKET(")", false),

	LEFT_BRACKET("(", true),

	// Comma is used for separating parameters of a function.
	COMMA(",", false),

	PILCROW("\u00B6", false);

	private String value;
	// Some symbols are composed as a pair. This flag indicates if the left part or the right one.
	private Boolean leftSymbol;

	private AvailableSymbol(String value, Boolean leftSymbol) {
		this.value = value;
		this.leftSymbol = leftSymbol;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return value;
	}

	public Boolean getLeftSymbol() {
		return leftSymbol;
	}
}

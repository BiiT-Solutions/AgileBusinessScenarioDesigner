package com.biit.abcd.persistence.entity.expressions;

public enum AvailableSymbols {

	RIGHT_BRACKET(")", false),

	LEFT_BRACKET("(", true),
	
	COMMA(",", false);

	private String value;
	// Some symbols are composed as a pair. This flag indicates if the left part or the right one.
	private Boolean leftSymbol;

	private AvailableSymbols(String value, Boolean leftSymbol) {
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

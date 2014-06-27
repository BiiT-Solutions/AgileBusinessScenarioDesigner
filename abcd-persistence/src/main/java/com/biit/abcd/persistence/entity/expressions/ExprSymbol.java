package com.biit.abcd.persistence.entity.expressions;

public enum ExprSymbol {

	RIGHT_BRACKET(")", ")", false),

	LEFT_BRACKET("(", "(", true);

	private String value;
	private String caption;
	// Some symbols are composed as a pair. This flag indicates if the left part or the right one.
	private Boolean leftSymbol;

	private ExprSymbol(String value, String caption, Boolean leftSymbol) {
		this.value = value;
		this.caption = caption;
		this.leftSymbol = leftSymbol;
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

	public Boolean getLeftSymbol() {
		return leftSymbol;
	}
}

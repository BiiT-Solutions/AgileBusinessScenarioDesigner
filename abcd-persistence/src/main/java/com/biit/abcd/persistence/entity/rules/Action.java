package com.biit.abcd.persistence.entity.rules;

public class Action {
	private static final int MAX_CHARACTERS_TO_SHOW =25;
	private String expression = "";

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	@Override
	public String toString() {
		return getExpression().substring(0, Math.min(MAX_CHARACTERS_TO_SHOW, getExpression().length()));
	}

}

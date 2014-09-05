package com.biit.abcd.persistence.entity.expressions.exceptions;

public class NotValidExpressionValue extends Exception {
	private static final long serialVersionUID = -6234518153226292280L;

	public NotValidExpressionValue(String text) {
		super(text);
	}
}

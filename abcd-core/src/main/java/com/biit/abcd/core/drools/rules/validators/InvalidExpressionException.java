package com.biit.abcd.core.drools.rules.validators;

import com.biit.abcd.persistence.entity.expressions.ExpressionValue;

/**
 * Exception created for the expression validator
 * 
 */
public class InvalidExpressionException extends Exception {
	private static final long serialVersionUID = -9031196357941801998L;

	private ExpressionValue<?> expressionValue = null;
	private String description = "";

	public InvalidExpressionException(String message, ExpressionValue<?> expressionValue) {
		super(message);
		this.expressionValue = expressionValue;
		this.description = message;
	}

	public InvalidExpressionException(String message) {
		this(message, (ExpressionValue<?>) null);
	}

	public InvalidExpressionException(String message, Exception e) {
		super(message, e);
		this.description = message;
	}

	public ExpressionValue<?> getExpressionValue() {
		return expressionValue;
	}

	public String getDescription() {
		return description;
	}
}

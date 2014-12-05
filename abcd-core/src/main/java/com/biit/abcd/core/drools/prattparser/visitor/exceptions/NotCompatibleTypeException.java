package com.biit.abcd.core.drools.prattparser.visitor.exceptions;

import com.biit.abcd.persistence.entity.expressions.ExpressionValue;

public class NotCompatibleTypeException extends Exception {
	private static final long serialVersionUID = 3677451564011626622L;
	private ExpressionValue<?> expressionValue = null;

	public NotCompatibleTypeException(String message, ExpressionValue<?> expressionValue) {
		super(message);
		this.expressionValue = expressionValue;
	}

	public ExpressionValue<?> getExpressionValue() {
		return expressionValue;
	}

}

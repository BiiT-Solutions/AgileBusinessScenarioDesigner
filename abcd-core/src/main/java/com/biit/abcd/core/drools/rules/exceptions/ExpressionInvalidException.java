package com.biit.abcd.core.drools.rules.exceptions;

import com.biit.abcd.persistence.entity.expressions.ExpressionChain;

public class ExpressionInvalidException extends Exception {
	private static final long serialVersionUID = -8070327939271645446L;
	ExpressionChain expressionChain = null;

	public ExpressionInvalidException(String message, ExpressionChain expressionChain) {
		super(message);
		this.expressionChain = expressionChain;
	}

	public ExpressionChain getExpressionChain() {
		return this.expressionChain;
	}

}

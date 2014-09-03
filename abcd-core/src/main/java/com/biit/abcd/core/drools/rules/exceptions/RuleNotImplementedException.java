package com.biit.abcd.core.drools.rules.exceptions;

import com.biit.abcd.persistence.entity.expressions.ExpressionChain;

public class RuleNotImplementedException extends Exception {
	private static final long serialVersionUID = 6476568769563510111L;
	private ExpressionChain expressionChain = null;

	public RuleNotImplementedException(String message, ExpressionChain expressionChain) {
		super(message);
		this.expressionChain = expressionChain;
	}

	public ExpressionChain getExpressionChain() {
		return this.expressionChain;
	}
}

package com.biit.abcd.core.drools.rules.exceptions;

import com.biit.abcd.persistence.entity.expressions.ExpressionChain;

public class ActionNotImplementedException extends Exception {
	private static final long serialVersionUID = -7456040041407153612L;
	private ExpressionChain expressionChain = null;

	public ActionNotImplementedException(String message, ExpressionChain expressionChain) {
		super(message);
		this.expressionChain = expressionChain;
	}

	public ExpressionChain getExpressionChain() {
		return this.expressionChain;
	}
}

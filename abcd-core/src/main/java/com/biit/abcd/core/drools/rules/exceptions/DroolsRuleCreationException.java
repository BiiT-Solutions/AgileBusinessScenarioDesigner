package com.biit.abcd.core.drools.rules.exceptions;

import com.biit.abcd.persistence.entity.expressions.ExpressionChain;

public class DroolsRuleCreationException extends Exception{
	private static final long serialVersionUID = -8041590784174454854L;
	ExpressionChain expressionChain = null;

	public DroolsRuleCreationException(String message, ExpressionChain expressionChain) {
		super(message);
		this.expressionChain = expressionChain;
	}

	public ExpressionChain getExpressionChain() {
		return this.expressionChain;
	}
	
}

package com.biit.abcd.core.drools.rules.exceptions;

public class DroolsRuleExecutionException extends Exception {
	private static final long serialVersionUID = 4083630225112950394L;
	private Exception generatedException;

	public DroolsRuleExecutionException(String message, Exception generatedException) {
		super(message);
		this.generatedException = generatedException;
	}

	public Exception getGeneratedException() {
		return generatedException;
	}
}
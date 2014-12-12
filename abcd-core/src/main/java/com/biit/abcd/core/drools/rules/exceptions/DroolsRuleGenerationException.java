package com.biit.abcd.core.drools.rules.exceptions;

public class DroolsRuleGenerationException extends Exception {
	private static final long serialVersionUID = 2547111719259690369L;
	private Exception generatedException;

	public DroolsRuleGenerationException(String message, Exception generatedException) {
		super(message);
		this.generatedException = generatedException;
	}

	public Exception getGeneratedException() {
		return generatedException;
	}
}
package com.biit.abcd.core.drools.rules.exceptions;

public class InvalidRuleException extends Exception {
	private static final long serialVersionUID = 7401211046559148275L;
	String ruleName;
	
	public InvalidRuleException(String message, String ruleName) {
		super(message);
		this.ruleName = ruleName;
	}
	
	public String getRuleName(){
		return ruleName;
	}
}

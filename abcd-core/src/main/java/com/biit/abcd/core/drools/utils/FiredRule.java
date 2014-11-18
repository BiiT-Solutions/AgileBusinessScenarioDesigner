package com.biit.abcd.core.drools.utils;

public class FiredRule {
	
	private String ruleName;

	public FiredRule() {
	}

	public FiredRule(String ruleName) {
		super();
		this.ruleName = ruleName;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
}

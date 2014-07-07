package com.biit.abcd.persistence.entity.expressions;

/**
 * Already defined functions. Used in JExVal. 
 *
 */
public enum AvailableFunction {

	MAX("MAX("),

	MIN("MIN("),

	ABS("ABS("),

	SQRT("SQRT("),
	
	IN("IN("),
	
	BETWEEN("BETWEEN("),

	ROUND("ROUND(");

	private String value;

	private AvailableFunction(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return value;
	}

}

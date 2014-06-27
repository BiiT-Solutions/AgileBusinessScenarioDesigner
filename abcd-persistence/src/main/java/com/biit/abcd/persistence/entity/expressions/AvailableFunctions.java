package com.biit.abcd.persistence.entity.expressions;

public enum AvailableFunctions {

	MAX("MAX("),

	MIN("MIN("),

	ABS("ABS("),

	SQRT("SQRT("),

	ROUND("ROUND(");

	private String value;

	private AvailableFunctions(String value) {
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

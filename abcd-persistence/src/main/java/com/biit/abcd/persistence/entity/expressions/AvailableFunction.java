package com.biit.abcd.persistence.entity.expressions;

/**
 * Already defined functions. Used in JExVal.
 *
 */
public enum AvailableFunction {

	NOT("NOT("),

	MAX("MAX("),

	MIN("MIN("),

	ABS("ABS("),

	SQRT("SQRT("),

	IN("IN("),

	BETWEEN("BETWEEN("),

	ROUND("ROUND("),

	AVG("AVG("),

	IF("IF("),

	PMT("PMT("),
	
	LOG("LOG("),

	SUM("SUM("),
	
	CONCAT("CONCAT(");

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

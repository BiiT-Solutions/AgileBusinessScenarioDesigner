package com.biit.abcd.persistence.entity.expressions;

import com.biit.abcd.persistence.entity.AnswerFormat;

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
	
	CONCAT("CONCAT("),
	
	CONCAT_SEPARATOR("CONCAT_SEP("),
	
	ELEMENT_XPATH("ELEMENT_XPATH("),
	
	ELEMENT_PATH("ELEMENT_PATH("),
	
	ELEMENT_NAME("ELEMENT_NAME("),
	
	ELEMENT_ID("ELEMENT_ID(");

	private String value;

	private AvailableFunction(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static AvailableFunction get(String availableFunction) {
		for (AvailableFunction function : AvailableFunction.values()) {
			if (function.name().equalsIgnoreCase(availableFunction)) {
				return function;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return value;
	}

}

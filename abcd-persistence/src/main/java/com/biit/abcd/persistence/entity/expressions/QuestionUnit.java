package com.biit.abcd.persistence.entity.expressions;

public enum QuestionUnit {
	DAYS("D"),

	MONTHS("M"),

	YEARS("Y");

	String abbreviature;

	QuestionUnit(String abbreviature) {
		this.abbreviature = abbreviature;
	}

	public String getAbbreviature() {
		return abbreviature;
	}
}
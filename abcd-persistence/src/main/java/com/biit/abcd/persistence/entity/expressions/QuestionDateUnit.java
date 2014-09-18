package com.biit.abcd.persistence.entity.expressions;

public enum QuestionDateUnit {
	DAYS("D"),

	MONTHS("M"),

	YEARS("Y"),

	DATE("DT");

	String abbreviature;

	QuestionDateUnit(String abbreviature) {
		this.abbreviature = abbreviature;
	}

	public String getAbbreviature() {
		return abbreviature;
	}
}
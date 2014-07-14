package com.biit.abcd.persistence.entity.expressions;

public enum DateUnit {
	DAYS("D"),

	MONTHS("M"),

	YEARS("Y");

	String abbreviature;

	DateUnit(String abbreviature) {
		this.abbreviature = abbreviature;
	}

	public String getAbbreviature() {
		return abbreviature;
	}
}
package com.biit.abcd.persistence.entity.expressions;

public enum QuestionDateUnit {
	DAYS("D", "Days"),

	MONTHS("M", "Months"),

	YEARS("Y", "Years"),

	DATE("DT", "Date");

	String abbreviature;
	String unitName;

	QuestionDateUnit(String abbreviature, String unitName) {
		this.abbreviature = abbreviature;
		this.unitName = unitName;
	}

	public String getAbbreviature() {
		return abbreviature;
	}
	
	public String getUnitName(){
		return unitName;
	}
}
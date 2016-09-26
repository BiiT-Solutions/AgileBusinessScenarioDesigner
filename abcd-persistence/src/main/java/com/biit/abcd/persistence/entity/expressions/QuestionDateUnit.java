package com.biit.abcd.persistence.entity.expressions;

public enum QuestionDateUnit {
	// Days from now.
	DAYS("D*", "Days from now"),

	// Months from now.
	MONTHS("M*", "Months from now"),

	// Years from now.
	YEARS("Y*", "Years from now"),

	//Pure data format.
	DATE("DT", "Date"),
	
	//Date from 1970
	ABSOLUTE_DAYS("D", "Days"),

	//Date from 1970
	ABSOLUTE_MONTHS("M", "Months"),
	
	//Date from 1970
	ABSOLUTE_YEARS("Y", "Years");

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
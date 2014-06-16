package com.biit.abcd.persistence.entity;


public enum CustomVariableType {
	STRING("class.String"),

	INTEGER("class.Integer"),

	DATE("class.Date");

	private String translation;

	private CustomVariableType(String translation) {
		this.translation = translation;
	}

	public String getTranslationCode() {
		return translation;
	}
}

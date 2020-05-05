package com.biit.abcd.persistence.entity;

public enum CustomVariableType {
	STRING("class.String", " "),

	NUMBER("class.Number", "0"),

	DATE("class.Date", "01/01/1970");

	private final String translation;

	private final String defaultValue;

	public String getDefaultValue() {
		return defaultValue;
	}

	private CustomVariableType(String translation, String defaultValue) {
		this.translation = translation;
		this.defaultValue = defaultValue;
	}

	public String getTranslationCode() {
		return translation;
	}
}

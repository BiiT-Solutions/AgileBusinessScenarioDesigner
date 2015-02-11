package com.biit.abcd.language;

import com.biit.abcd.persistence.entity.CustomVariableType;

public enum CustomVariableTypeUi {

	STRING(CustomVariableType.STRING, LanguageCodes.STRING),

	NUMBER(CustomVariableType.NUMBER, LanguageCodes.NUMBER),

	DATE(CustomVariableType.DATE, LanguageCodes.DATE),
	;
	
	private final CustomVariableType customvariable;
	private final LanguageCodes languageCode;
	
	CustomVariableTypeUi(CustomVariableType customVariable, LanguageCodes languageCode) {
		this.customvariable = customVariable;
		this.languageCode = languageCode;
	}

	public CustomVariableType getCustomvariable() {
		return customvariable;
	}

	public LanguageCodes getLanguageCode() {
		return languageCode;
	}
	
}

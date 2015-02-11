package com.biit.abcd.language;

import com.biit.abcd.persistence.entity.CustomVariableScope;


public enum CustomVariableScopeUi {

	FORM(CustomVariableScope.FORM, LanguageCodes.FORM),

	CATEGORY(CustomVariableScope.CATEGORY, LanguageCodes.CATEGORY),

	GROUP(CustomVariableScope.GROUP, LanguageCodes.GROUP),

	QUESTION(CustomVariableScope.QUESTION, LanguageCodes.QUESTION),
	;
	
	private final CustomVariableScope variableScope;
	private final LanguageCodes languageCode;
	
	CustomVariableScopeUi(CustomVariableScope variableScope, LanguageCodes languageCode) {
		this.variableScope = variableScope;
		this.languageCode = languageCode;
	}

	public LanguageCodes getLanguageCode() {
		return languageCode;
	}

	public CustomVariableScope getVariableScope() {
		return variableScope;
	}
}

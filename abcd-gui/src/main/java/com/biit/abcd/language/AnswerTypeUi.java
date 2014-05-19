package com.biit.abcd.language;

import com.biit.abcd.persistence.entity.AnswerType;

public enum AnswerTypeUi {
	INPUT(AnswerType.INPUT,LanguageCodes.ANSWER_TYPE_INPUT), // Uses AnswerFormat.

	RADIO(AnswerType.RADIO,LanguageCodes.ANSWER_TYPE_RADIO),

	MULTI_CHECKBOX(AnswerType.MULTI_CHECKBOX,LanguageCodes.MULTI_CHECKBOX);
	
	private AnswerType answerType;
	private LanguageCodes languageCode;
	
	private AnswerTypeUi(AnswerType answerType, LanguageCodes languageCode) {
		this.answerType = answerType;
		this.languageCode = languageCode;
	}
	
	public AnswerType getAnswerType(){
		return answerType;
	}
	
	public LanguageCodes getLanguageCode(){
		return languageCode;
	}

	public static AnswerTypeUi getFromAnswerType(AnswerType answerType){
		for(AnswerTypeUi answerTypeUi: values()){
			if(answerTypeUi.getAnswerType().equals(answerType)){
				return answerTypeUi;
			}
		}
		return null;
	}
}

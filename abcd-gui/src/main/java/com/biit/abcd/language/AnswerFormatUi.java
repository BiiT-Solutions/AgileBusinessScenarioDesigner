package com.biit.abcd.language;

import com.biit.abcd.persistence.entity.AnswerFormat;

public enum AnswerFormatUi {
	TEXT(AnswerFormat.TEXT, LanguageCodes.ANSWER_FORMAT_TEXT),

	DATE(AnswerFormat.DATE, LanguageCodes.ANSWER_FORMAT_DATE),

	NUMBER(AnswerFormat.NUMBER, LanguageCodes.ANSWER_FORMAT_NUMBER);

	private AnswerFormat answerFormat;
	private LanguageCodes languageCode;

	private AnswerFormatUi(AnswerFormat answerFormat, LanguageCodes languageCode) {
		this.answerFormat = answerFormat;
		this.languageCode = languageCode;
	}

	public AnswerFormat getAnswerFormat() {
		return answerFormat;
	}

	public LanguageCodes getLanguageCode() {
		return languageCode;
	}

	public static AnswerFormatUi getFromAnswerFormat(AnswerFormat answerFormat) {
		for (AnswerFormatUi answerFormatUi : values()) {
			if (answerFormatUi.getAnswerFormat().equals(answerFormat)) {
				return answerFormatUi;
			}
		}
		return null;
	}
}

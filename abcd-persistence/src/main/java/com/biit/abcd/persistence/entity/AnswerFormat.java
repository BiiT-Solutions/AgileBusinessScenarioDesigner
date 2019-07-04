package com.biit.abcd.persistence.entity;

import com.biit.drools.form.DroolsQuestionFormat;

/**
 * Used only for text inputs.
 */
public enum AnswerFormat {

	TEXT(DroolsQuestionFormat.TEXT),

	MULTI_TEXT(DroolsQuestionFormat.MULTI_TEXT),

	NUMBER(DroolsQuestionFormat.NUMBER),

	DATE(DroolsQuestionFormat.DATE),

	POSTAL_CODE(DroolsQuestionFormat.POSTAL_CODE);

	private DroolsQuestionFormat droolsFormat;

	private AnswerFormat(DroolsQuestionFormat droolsFormat) {
		this.droolsFormat = droolsFormat;
	}

	public DroolsQuestionFormat getDroolsFormat() {
		return droolsFormat;
	}

	/**
	 * Used in drools to decide if a answerformat is treated as string.
	 * 
	 * @return true is represents a string.
	 */
	public boolean isStringData() {
		return this.equals(AnswerFormat.TEXT) || this.equals(AnswerFormat.MULTI_TEXT)
				|| this.equals(AnswerFormat.POSTAL_CODE);
	}
}

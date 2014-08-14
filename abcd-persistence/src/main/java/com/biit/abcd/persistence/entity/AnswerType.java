package com.biit.abcd.persistence.entity;

public enum AnswerType {
	RADIO,

	MULTI_CHECKBOX,

	// Uses answer format.
	INPUT;

	public boolean isInputField() {
		return this.equals(AnswerType.INPUT);
	}
}

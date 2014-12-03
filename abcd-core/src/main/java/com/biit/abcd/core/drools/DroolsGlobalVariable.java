package com.biit.abcd.core.drools;

import com.biit.abcd.persistence.entity.AnswerFormat;

public class DroolsGlobalVariable {

	private String name;
	private AnswerFormat format;
	private Object value;

	public DroolsGlobalVariable(String name, Object value) {
		this.name = name;
		this.value = value;
	}

	public DroolsGlobalVariable(String name, AnswerFormat format, Object value) {
		this.name = name;
		this.format = format;
		this.value = value;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return this.value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public AnswerFormat getFormat() {
		return format;
	}

	public void setFormat(AnswerFormat format) {
		this.format = format;
	}
	
	@Override
	public String toString() {
		return name + " (" + format + ") " + value;
	}
}

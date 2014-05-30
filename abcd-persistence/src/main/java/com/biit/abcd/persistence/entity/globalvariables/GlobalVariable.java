package com.biit.abcd.persistence.entity.globalvariables;

import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.persistence.entity.AnswerFormat;

public class GlobalVariable {

	private String name;
	
	private AnswerFormat format;

	private List<VariableData> data;
	
	public GlobalVariable(){
		data = new ArrayList<VariableData>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<VariableData> getData() {
		return data;
	}

	public AnswerFormat getFormat() {
		return format;
	}

	public void setFormat(AnswerFormat format) {
		this.format = format;
	}
}

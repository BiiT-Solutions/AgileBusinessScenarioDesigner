package com.biit.abcd.persistence.entity.globalvariables;

import java.util.ArrayList;
import java.util.List;

public class GlobalVariable {

	private String name;

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
}

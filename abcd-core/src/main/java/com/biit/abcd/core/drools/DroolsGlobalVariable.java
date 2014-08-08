package com.biit.abcd.core.drools;

public class DroolsGlobalVariable {

	private String name;
	private Object value;

	public DroolsGlobalVariable(String name, Object value) {
		this.name = name;
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

}

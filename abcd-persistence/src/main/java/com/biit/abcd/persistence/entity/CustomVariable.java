package com.biit.abcd.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Sets all user defined custom variables that will be used in drools conditions and action.
 * 
 */
@Entity
@Table(name = "FORM_CUSTOM_VARIABLES")
public class CustomVariable extends StorableObject {

	private String name;
	private CustomVariableScope scope;
	private CustomVariableType type;

	public CustomVariable() {

	}

	public CustomVariable(String name, CustomVariableType type, CustomVariableScope scope) {
		this.name = name;
		this.scope = scope;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CustomVariableScope getScope() {
		return scope;
	}

	public void setScope(CustomVariableScope scope) {
		this.scope = scope;
	}

	public CustomVariableType getType() {
		return type;
	}

	public void setType(CustomVariableType type) {
		this.type = type;
	}

}

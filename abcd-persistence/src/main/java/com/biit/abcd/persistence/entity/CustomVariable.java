package com.biit.abcd.persistence.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.biit.persistence.entity.StorableObject;

/**
 * Sets all user defined custom variables that will be used in drools conditions and action.
 */
@Entity
@Table(name = "form_custom_variables", uniqueConstraints = { @UniqueConstraint(columnNames = { "form", "name", "scope" }) })
public class CustomVariable extends StorableObject {

	// Used mainly for unique constraint.
	@ManyToOne
	@JoinColumn(name = "form")
	private Form form;

	// MySQL unique keys are limited to 767 bytes that in utf8mb4 are ~190.
	@Column(length = MAX_UNIQUE_COLUMN_LENGTH)
	private String name;
	@Enumerated(EnumType.STRING)
	// MySQL unique keys are limited to 767 bytes that in utf8mb4 are ~190.
	@Column(length = MAX_UNIQUE_COLUMN_LENGTH)
	private CustomVariableScope scope;
	@Enumerated(EnumType.STRING)
	private CustomVariableType type;

	public CustomVariable() {
	}

	public CustomVariable(Form form, String name, CustomVariableType type, CustomVariableScope scope) {
		this.form = form;
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

	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
	}

	/**
	 * Returns true if the custom variable compared has the same name and scope
	 * 
	 * @param otherVariable
	 * @return
	 */
	public boolean hasSameNameAndScope(CustomVariable otherVariable) {
		if (getName().equals(otherVariable.getName()) && getScope().equals(otherVariable.getScope())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Has no inner elements. Returns an empty set.
	 */
	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		Set<StorableObject> innerStorableObjects = new HashSet<>();
		return innerStorableObjects;
	}

}

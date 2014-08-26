package com.biit.abcd.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.biit.persistence.entity.StorableObject;


@Entity
@Table(name = "generic_tree_objec_variable")
public class GenericTreeObjectVariable extends StorableObject {

	@Enumerated(EnumType.STRING)
	private GenericTreeObjectScope scope;

	public GenericTreeObjectVariable() {
	}

	public GenericTreeObjectVariable(GenericTreeObjectScope scope) {
		this.scope = scope;
	}

	public GenericTreeObjectScope getScope() {
		return scope;
	}

	public void setScope(GenericTreeObjectScope scope) {
		this.scope = scope;
	}
}

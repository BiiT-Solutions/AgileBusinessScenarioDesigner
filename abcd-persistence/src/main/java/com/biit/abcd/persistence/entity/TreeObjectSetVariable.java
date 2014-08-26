package com.biit.abcd.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.biit.persistence.entity.StorableObject;


@Entity
@Table(name = "tree_object_set_variable")
public class TreeObjectSetVariable extends StorableObject {

	@Enumerated(EnumType.STRING)
	private TreeObjectSetScope scope;

	public TreeObjectSetVariable() {
	}

	public TreeObjectSetVariable(TreeObjectSetScope scope) {
		this.scope = scope;
	}

	public TreeObjectSetScope getScope() {
		return scope;
	}

	public void setScope(TreeObjectSetScope scope) {
		this.scope = scope;
	}
}

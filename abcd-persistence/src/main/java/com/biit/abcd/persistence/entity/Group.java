package com.biit.abcd.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.form.BaseGroup;
import com.biit.form.exceptions.FieldTooLongException;

@Entity
@Table(name = "TREE_GROUPS")
public class Group extends BaseGroup {

	public Group() {
		super();
	}

	public Group(String name) throws FieldTooLongException {
		super(name);
	}
}

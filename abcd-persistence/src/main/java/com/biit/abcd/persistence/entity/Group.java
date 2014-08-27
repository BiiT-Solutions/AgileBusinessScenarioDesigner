package com.biit.abcd.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.form.BaseRepeatableGroup;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.FieldTooLongException;

@Entity
@Table(name = "tree_groups")
public class Group extends BaseRepeatableGroup {

	public Group() {
		super();
	}

	public Group(String name) throws FieldTooLongException {
		super(name);
	}

	@Override
	protected void copyData(TreeObject object) {
	}
}

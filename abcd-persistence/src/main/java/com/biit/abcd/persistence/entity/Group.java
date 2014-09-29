package com.biit.abcd.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.abcd.persistence.utils.CheckDependencies;
import com.biit.form.BaseRepeatableGroup;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.DependencyExistException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

@Entity
@Table(name = "tree_groups")
public class Group extends BaseRepeatableGroup {

	public Group() {
		super();
	}

	public Group(String name) throws FieldTooLongException, CharacterNotAllowedException {
		super(name);
	}

	@Override
	protected void copyData(TreeObject object) {
	}

	@Override
	public void checkDependencies() throws DependencyExistException {
		CheckDependencies.checkTreeObjectDependencies(this);
	}
}

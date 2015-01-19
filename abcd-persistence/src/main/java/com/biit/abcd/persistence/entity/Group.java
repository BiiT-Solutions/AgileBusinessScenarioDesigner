package com.biit.abcd.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.abcd.persistence.utils.CheckDependencies;
import com.biit.form.BaseRepeatableGroup;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.DependencyExistException;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

@Entity
@Table(name = "tree_groups")
public class Group extends BaseRepeatableGroup {
	private static final long serialVersionUID = -7455213859023593111L;

	public Group() {
		super();
	}

	public Group(String name) throws FieldTooLongException, CharacterNotAllowedException {
		super(name);
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		super.copyData(object);
	}

	@Override
	public void checkDependencies() throws DependencyExistException {
		CheckDependencies.checkTreeObjectDependencies(this);
	}
}

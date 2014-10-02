package com.biit.abcd.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.abcd.persistence.utils.CheckDependencies;
import com.biit.form.BaseAnswer;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.DependencyExistException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

@Entity
@Table(name = "tree_answers")
public class Answer extends BaseAnswer {

	public Answer() {
	}

	public Answer(String name) throws FieldTooLongException, CharacterNotAllowedException {
		super();
		setName(name);
	}

	@Override
	protected void copyData(TreeObject object) {
	}

	@Override
	public void checkDependencies() throws DependencyExistException {
		CheckDependencies.checkTreeObjectDependencies(this);
	}

	@Override
	public String getSimpleAsciiName() {
		return getName().replaceAll("[^a-zA-Z0-9.]", "");
	}
}

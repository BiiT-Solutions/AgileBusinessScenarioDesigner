package com.biit.abcd.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.form.BaseAnswer;
import com.biit.form.exceptions.FieldTooLongException;

@Entity
@Table(name = "TREE_ANSWERS")
public class Answer extends BaseAnswer {

	public Answer() {
	}

	public Answer(String name) throws FieldTooLongException {
		super(name);
	}

}

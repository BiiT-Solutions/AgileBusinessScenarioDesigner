package com.biit.abcd.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.form.BaseCategory;
import com.biit.form.exceptions.FieldTooLongException;

@Entity
@Table(name = "tree_categories")
public class Category extends BaseCategory {

	public Category() {
		super();
	}

	public Category(String name) throws FieldTooLongException {
		super(name);
	}
}

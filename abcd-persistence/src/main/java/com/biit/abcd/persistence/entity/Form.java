package com.biit.abcd.persistence.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.exceptions.NotValidParentException;

@Entity
@Table(name = "FORMS")
public class Form extends TreeObject {
	private static final List<Class<?>> ALLOWED_CHILDS = new ArrayList<Class<?>>(Arrays.asList(Category.class));

	private String name;

	@Override
	protected List<Class<?>> getAllowedChilds() {
		return ALLOWED_CHILDS;
	}

	@Override
	protected List<Class<?>> getAllowedParents() {
		return null;
	}

	@Override
	public void setParent(ITreeObject parent) throws NotValidParentException {
		throw new NotValidParentException("Categories cannot have a parent.");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

package com.biit.abcd.persistence.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.biit.abcd.persistence.entity.exceptions.NotValidParentException;

public class Category extends TreeObject {
	private static final List<Class<?>> ALLOWED_CHILDS = new ArrayList<Class<?>>(Arrays.asList(Question.class, Group.class));

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

}

package com.biit.abcd.persistence.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.biit.abcd.persistence.entity.exceptions.NotValidParentException;

public class Category extends TreeObject {
	private static List<Class> allowedChilds = new ArrayList<Class>(Arrays.asList(Question.class, Group.class));

	@Override
	protected List<Class> getAllowedChilds() {
		return allowedChilds;
	}

	@Override
	public void setParent(ITreeObject parent) throws NotValidParentException {
		throw new NotValidParentException("Categories cannot have a parent.");
	}
}

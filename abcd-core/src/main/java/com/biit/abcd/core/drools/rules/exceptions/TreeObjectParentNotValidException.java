package com.biit.abcd.core.drools.rules.exceptions;

import com.biit.form.entity.TreeObject;

public class TreeObjectParentNotValidException extends Exception {
	private static final long serialVersionUID = 2030465104155865686L;
	private TreeObject treeObject = null;

	public TreeObjectParentNotValidException(TreeObject treeObject) {
		super();
		this.treeObject = treeObject;
	}

	public TreeObject getTreeObject() {
		return treeObject;
	}
}

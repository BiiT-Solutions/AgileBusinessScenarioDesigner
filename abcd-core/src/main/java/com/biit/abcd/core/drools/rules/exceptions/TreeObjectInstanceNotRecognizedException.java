package com.biit.abcd.core.drools.rules.exceptions;

import com.biit.form.TreeObject;

public class TreeObjectInstanceNotRecognizedException extends Exception {
	private static final long serialVersionUID = -7864776882863456211L;
	private TreeObject treeObject = null;

	public TreeObjectInstanceNotRecognizedException(TreeObject treeObject) {
		super();
		this.treeObject = treeObject;
	}

	public TreeObject getTreeObject() {
		return treeObject;
	}
}

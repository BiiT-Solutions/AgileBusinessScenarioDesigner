package com.biit.abcd.webpages.components;

import com.biit.abcd.persistence.entity.TreeObject;
import com.biit.abcd.webpages.elements.decisiontable.FormQuestionTable;
import com.vaadin.ui.Component;

public abstract class SelectionTableWindow extends AcceptCancelWindow {
	private static final long serialVersionUID = 6781910083959136654L;
	private FormQuestionTable formQuestionTable;

	public SelectionTableWindow(TreeObject treeObject) {
		super();
		setContent(generateContent(treeObject));
		setWidth("50%");
		setHeight("75%");
		setResizable(false);
	}

	public abstract Component generateContent(TreeObject treeObject);

	public TreeObjectTable getTable() {
		return formQuestionTable;
	}
}

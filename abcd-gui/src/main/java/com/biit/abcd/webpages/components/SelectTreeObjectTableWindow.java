package com.biit.abcd.webpages.components;

import java.util.Set;

import com.biit.form.TreeObject;
import com.vaadin.ui.Component;

public abstract class SelectTreeObjectTableWindow extends AcceptCancelWindow {
	private static final long serialVersionUID = 6781910083959136654L;
	private TreeObjectTable table;

	private boolean multiselect;

	public SelectTreeObjectTableWindow(TreeObject treeObject, boolean multiselect) {
		super();
		this.multiselect = multiselect;
		setContent(generateContent(treeObject));
		setWidth("50%");
		setHeight("75%");
		setResizable(false);
	}

	public abstract Component generateContent(TreeObject treeObject);

	public TreeObjectTable getTable() {
		return table;
	}

	public void setTable(TreeObjectTable table) {
		this.table = table;
	}

	public boolean isMultiselect() {
		return multiselect;
	}

	public void setTreeObjectsSelected(Set<TreeObject> selected) {
		table.setTreeObjectsSelected(selected);
	}

	public void setTreeObjectSelected(TreeObject selected) {
		if (table != null) {
			table.setTreeObjectSelected(selected);
		}
	}
}

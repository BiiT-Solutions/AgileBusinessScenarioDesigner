package com.biit.abcd.webpages.components;

import java.util.List;

import com.biit.abcd.persistence.entity.Answer;
import com.biit.form.TreeObject;

/**
 * TreeObjectTable component
 * 
 * This is a customized component to represent a TreeObject in a tree table.
 * 
 */
public class DroolsTreeObjectTable extends TreeObjectTable {
	private static final long serialVersionUID = 2056310678127658383L;

	public void loadTreeObject(TreeObject element, TreeObject parent) {
		if (!(element instanceof Answer)) {
			addItem(element, parent);
		}

		List<TreeObject> children = element.getChildren();
		for (TreeObject child : children) {
			loadTreeObject(child, element);
		}
	}

	@Override
	public void setRootElement(TreeObject root) {
		this.removeAllItems();
		select(null);
		if (root != null) {
			loadTreeObject(root, null);
			try {
				setCollapsed(root, false);
			} catch (Exception e) {
				// Root is not inserted. Ignore error.
			}
		}
	}

}

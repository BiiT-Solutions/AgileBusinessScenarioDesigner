package com.biit.abcd.webpages.elements.droolsresults;

import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.webpages.components.TreeObjectTable;
import com.biit.form.TreeObject;

/**
 * TreeObjectTable component
 * 
 * This is a customized component to represent a TreeObject in a tree table.
 * 
 */
public class DroolsTreeObjectTable extends TreeObjectTable {
	private static final long serialVersionUID = 2056310678127658383L;

	public void addItem(TreeObject element, TreeObject parent) {
		// Not representing the answers
		if (element != null && !(element instanceof Answer)) {
			super.addItem(element, parent);
		}
	}
}

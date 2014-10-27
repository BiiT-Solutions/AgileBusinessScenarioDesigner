package com.biit.abcd.webpages.elements.testscenario;

import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.webpages.components.TreeObjectTable;
import com.biit.form.TreeObject;

/**
 * TreeObjectTable component
 * 
 * This is a customized component to represent a TreeObject in a tree table.
 * 
 */
public class TestScenarioTable extends TreeObjectTable {

	private static final long serialVersionUID = 4172158838292855446L;
	private TreeObject rootElement;

	public void setRootElement(TreeObject root) {
		rootElement = root;
		super.setRootElement(root);
	}

	public void addItem(TreeObject element, TreeObject parent) {
		// Not representing the groups, questions and answers
		if (element != null && !(element instanceof Group) && !(element instanceof Question)
				&& !(element instanceof Answer)) {
			super.addItem(element, parent);
		}
	}

	public TreeObject getRootElement() {
		return rootElement;
	}

}

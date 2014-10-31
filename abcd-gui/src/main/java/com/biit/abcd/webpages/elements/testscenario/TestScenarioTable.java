package com.biit.abcd.webpages.elements.testscenario;

import com.biit.abcd.persistence.entity.testscenarios.TestScenarioForm;
import com.biit.abcd.webpages.components.TreeObjectTable;
import com.biit.form.BaseQuestion;
import com.biit.form.BaseRepeatableGroup;
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
		if (element != null && !(element instanceof BaseRepeatableGroup) && !(element instanceof BaseQuestion)) {
			super.addItem(element, parent);
		}
	}

	public TreeObject getRootElement() {
		return rootElement;
	}
	
	public static String getItemName(TreeObject element) {
		String name = null;
		if (element instanceof TestScenarioForm) {
			name = element.getLabel();
		} else {
			name = element.getName();
		}
		if (name == null) {
			throw new UnsupportedOperationException(TreeObject.class.getName() + " subtype unknown.");
		}
		return name;
	}

}

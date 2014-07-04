package com.biit.abcd.webpages.components;

import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.TreeObject;
import com.vaadin.ui.TreeTable;

public class SelectFormAnswerTable extends TreeObjectTable {
	private static final long serialVersionUID = 6558723176678770970L;

	public SelectFormAnswerTable() {
		super();
	}

	@Override
	public Answer getValue() {
		if (!(super.getValue() instanceof Answer)) {
			return null;
		}
		return (Answer) super.getValue();
	}

	/**
	 * Adds item to table. This function is a specialization of {@link TreeTable#addItem(Object)} for form members. in
	 * this table only answers are shown.
	 * 
	 * @param element
	 */
	@Override
	public void addItem(TreeObject element, TreeObject parent) {
		// Only add answers.
		if ((element instanceof Answer)) {
			super.addItem(element, parent);
		}
	}
}

package com.biit.abcd.webpages.elements.decisiontable;

import com.biit.abcd.persistence.entity.Question;
import com.vaadin.ui.Table;

public class ConditionTable extends Table {

	private static final long serialVersionUID = -8109315235459994799L;

	public ConditionTable() {
		setImmediate(true);
		setSizeFull();
	}

	public void addColumn(Question question) {
		addContainerProperty(question, String.class, "", question.getName(), null, Align.CENTER);
		//TODO - fix other items?
	}
}

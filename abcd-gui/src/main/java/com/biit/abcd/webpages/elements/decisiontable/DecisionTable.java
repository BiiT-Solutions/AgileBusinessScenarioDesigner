package com.biit.abcd.webpages.elements.decisiontable;

import java.util.Collection;

import com.biit.abcd.persistence.entity.Question;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;

public class DecisionTable extends CustomComponent {
	private static final long serialVersionUID = 2314989763962134814L;

	private HorizontalLayout rootLayout;
	private ConditionTable conditionTable;
	private ActionTable actionTable;

	public DecisionTable() {
		rootLayout = new HorizontalLayout();
		rootLayout.setSizeFull();
		rootLayout.setImmediate(true);
		rootLayout.setSpacing(true);
		setCompositionRoot(rootLayout);

		conditionTable = new ConditionTable();
		conditionTable.setSizeFull();
		actionTable = new ActionTable();
		actionTable.setSizeFull();

		rootLayout.addComponent(conditionTable);
		rootLayout.addComponent(actionTable);
		rootLayout.setExpandRatio(conditionTable, 0.75f);
		rootLayout.setExpandRatio(actionTable, 0.25f);

		setSizeFull();
		setImmediate(true);
	}

	public void addColumn(Question question) {
		if (question != null) {
			conditionTable.addColumn(question);
		}
	}

	@SuppressWarnings("unchecked")
	public Collection<Question> getColumns() {
		return (Collection<Question>) conditionTable.getContainerPropertyIds();
	}

	public void addRow() {
		
	}
}

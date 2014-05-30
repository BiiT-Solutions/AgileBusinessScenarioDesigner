package com.biit.abcd.webpages.elements.decisiontable;

import java.util.Collection;

import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.rules.DecisionTable;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;

public class DecisionTableComponent extends CustomComponent {
	private static final long serialVersionUID = 2314989763962134814L;

	private HorizontalLayout rootLayout;
	private ConditionTable conditionTable;
	private ActionTable actionTable;
	private DecisionTable decisionTable;

	public DecisionTableComponent() {
		decisionTable = new DecisionTable();

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
			decisionTable.getConditions().add(question);
		}
	}

	@SuppressWarnings("unchecked")
	public Collection<Question> getColumns() {
		return (Collection<Question>) conditionTable.getContainerPropertyIds();
	}

	public void addRow() {
		TableRule decisionRule = new TableRule();
		decisionTable.getRules().add(decisionRule);

		// Add decision Rule to both tables.
		conditionTable.addItem(decisionRule);
		actionTable.addItem(decisionRule);

		// Select first row if only exists this.
//		if (decisionTable.getRules().size() == 1) {
//			conditionTable.select(decisionRule);
//			actionTable.select(decisionRule);
//		}
	}

	public void removeSelectedRows() {
		for (TableRule rule : conditionTable.getSelectedRules()) {
			conditionTable.removeItem(rule);
			actionTable.removeItem(rule);
		}
	}

	public void removeSelectedColumns() {
		for (Question question : conditionTable.getSelectedQuestions()) {
			conditionTable.removeContainerProperty(question);
		}
	}

	public int getNumberOfRules() {
		return decisionTable.getRules().size();
	}
}

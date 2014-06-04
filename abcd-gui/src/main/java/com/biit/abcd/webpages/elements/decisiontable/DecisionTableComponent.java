package com.biit.abcd.webpages.elements.decisiontable;

import java.util.Collection;

import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.rules.DecisionTable;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.JavaScript;

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

		conditionTable = new ConditionTable();
		conditionTable.setSizeFull();

		actionTable = new ActionTable();
		actionTable.setSizeFull();

		conditionTable.setId("main-table");
		actionTable.setId("freeze-pane");
		JavaScript
				.getCurrent()
				.execute(
						"var t=document.getElementById('main-table').children[1]; var fp=document.getElementById('freeze-pane').children[1]; fp.addEventListener('scroll', function() {t.scrollTop=fp.scrollTop;}, false);");

		rootLayout.addComponent(conditionTable);
		rootLayout.addComponent(actionTable);
		rootLayout.setExpandRatio(conditionTable, 0.75f);
		rootLayout.setExpandRatio(actionTable, 0.25f);

		setSizeFull();
		setImmediate(true);
		setCompositionRoot(rootLayout);
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

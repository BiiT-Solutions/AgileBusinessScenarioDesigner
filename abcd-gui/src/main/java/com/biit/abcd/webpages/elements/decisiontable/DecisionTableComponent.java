package com.biit.abcd.webpages.elements.decisiontable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.rules.AnswerCondition;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.JavaScript;

public class DecisionTableComponent extends CustomComponent {
	private static final long serialVersionUID = 2314989763962134814L;

	private HorizontalLayout rootLayout;
	private ConditionTable conditionTable;
	private ActionTable actionTable;
	private List<TableRule> decisionTableRules;

	public DecisionTableComponent() {
		decisionTableRules = new ArrayList<>();

		rootLayout = new HorizontalLayout();
		rootLayout.setSizeFull();
		rootLayout.setImmediate(true);
		rootLayout.setSpacing(true);

		conditionTable = new ConditionTable();
		conditionTable.setSizeFull();
		conditionTable.addCellSelectionListener(new CellSelectionListener() {

			@Override
			public void cellSelectionChanged(CellRowSelector selector) {
				actionTable.selectRows(selector.getSelectedRows(), false);
			}
		});

		actionTable = new ActionTable();
		actionTable.setSizeFull();
		actionTable.addCellSelectionListener(new CellSelectionListener() {

			@Override
			public void cellSelectionChanged(CellRowSelector selector) {
				conditionTable.selectRows(selector.getSelectedRows(), false);
			}
		});

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
			for (TableRule tableRule : decisionTableRules) {
				tableRule.getConditions().put(question, new AnswerCondition(null));
			}
		}
	}

	@SuppressWarnings("unchecked")
	public Collection<Question> getColumns() {
		return (Collection<Question>) conditionTable.getContainerPropertyIds();
	}

	public void addRow() {
		addRow(new TableRule());
	}

	public void addRow(TableRule decisionRule) {
		decisionTableRules.add(decisionRule);

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

	public List<TableRule> getTableRules() {
		return decisionTableRules;
	}
}

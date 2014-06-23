package com.biit.abcd.webpages.elements.decisiontable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.biit.abcd.persistence.entity.rules.Action;
import com.biit.abcd.persistence.entity.rules.ActionExpression;
import com.biit.abcd.persistence.entity.rules.QuestionAndAnswerCondition;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.JavaScript;

public class DecisionTableQuestionAnswerConditionComponent extends CustomComponent {
	private static final long serialVersionUID = 2314989763962134814L;

	private HorizontalLayout rootLayout;
	private QuestionAnswerPairTable conditionTable;
	private ActionTable actionTable;
	private TableRule tableRule;

	public DecisionTableQuestionAnswerConditionComponent() {

		rootLayout = new HorizontalLayout();
		rootLayout.setSizeFull();
		rootLayout.setImmediate(true);
		rootLayout.setSpacing(true);

		conditionTable = new QuestionAnswerPairTable();
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

	public void removeAll() {
		conditionTable.removeAll();
		actionTable.removeAllItems();
		// decisionTableRules = new ArrayList<>();
	}

	public void addColumnPair() {
		if (tableRule == null) {
			// Do nothing if there is no table rule.
			return;
		}
		conditionTable.addColumnPair();
		for (TableRuleRow tableRuleRow : getTableRules()) {
			// New column is filled up with empty values for all existing rows.
			tableRuleRow.getConditions().add(new QuestionAndAnswerCondition());
		}
	}

	public void addRow() {
		if (tableRule == null) {
			// Do nothing if there is no table rule.
			return;
		}
		TableRuleRow tableRuleRow = new TableRuleRow();
		// Add at least one action.
		tableRuleRow.addAction(new ActionExpression());
		addRow(tableRuleRow);
		getTableRules().add(tableRuleRow);
	}

	public void addRow(TableRuleRow decisionRule) {
		// Add decision Rule to both tables.
		conditionTable.addItem(decisionRule);
		actionTable.addItem(decisionRule);
	}

	public void removeSelectedRows() {
		for (TableRuleRow rule : conditionTable.getSelectedRules()) {
			conditionTable.removeItem(rule);
			actionTable.removeItem(rule);
			getTableRules().remove(rule);
		}
		// Always one row.
		if (conditionTable.size() == 0) {
			addRow();
		}
	}

	public void removeSelectedColumns() {
		conditionTable.removeColumns(conditionTable.getSelectedColumns());
	}

	public List<TableRuleRow> getTableRules() {
		if (tableRule != null) {
			return tableRule.getRules();
		}
		return null;
	}

	/**
	 * Returns the table rules that has been filled up by the user.
	 * 
	 * @return
	 */
	public List<TableRuleRow> getDefinedTableRules() {
		List<TableRuleRow> notEmptyRows = new ArrayList<>();
		// Row is useful if at least has one action defined.
		for (TableRuleRow row : getTableRules()) {
			for (Action action : row.getActions()) {
				if (!action.undefined()) {
					notEmptyRows.add(row);
					break;
				}
			}
		}
		return notEmptyRows;
	}

	public TableRule getTableRule() {
		return tableRule;
	}

	@SuppressWarnings("unused")
	public void setTableRule(TableRule tableRule) {
		this.tableRule = tableRule;
		// Add columns.
		if (!tableRule.getRules().isEmpty()) {
			for (QuestionAndAnswerCondition questionAndAnswerCondition : tableRule.getRules().get(0).getConditions()) {
				conditionTable.addColumnPair();
			}
			// Add rows.
			for (TableRuleRow tableRuleRow : tableRule.getRules()) {
				addRow(tableRuleRow);
			}
		}

	}

	public Collection<?> getColumns() {
		return conditionTable.getContainerPropertyIds();
	}
}
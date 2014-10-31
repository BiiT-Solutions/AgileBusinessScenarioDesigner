package com.biit.abcd.webpages.elements.decisiontable;

import java.util.Collection;
import java.util.Set;

import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.JavaScript;

public class RuleTable extends CustomComponent {

	private static final long serialVersionUID = -1214662437608420332L;
	private HorizontalLayout rootLayout;
	private NewConditionTable conditionTable;
	private NewActionTable actionTable;

	public RuleTable() {
		super();

		rootLayout = new HorizontalLayout();
		rootLayout.setSizeFull();
		rootLayout.setImmediate(true);
		rootLayout.setSpacing(true);

		conditionTable = new NewConditionTable();
		conditionTable.setSizeFull();
		conditionTable.addCellSelectionListener(new CellSelectionListener() {
			@Override
			public void cellSelectionChanged(CellRowSelector selector) {
				actionTable.selectRows(selector.getSelectedRows(), false);
			}
		});

		actionTable = new NewActionTable();
		actionTable.setSizeFull();
		actionTable.addCellSelectionListener(new CellSelectionListener() {
			@Override
			public void cellSelectionChanged(CellRowSelector selector) {
				conditionTable.selectRows(selector.getSelectedRows(), false);
			}
		});

		conditionTable.setId("main-table");
		actionTable.setId("freeze-pane");

		// Sincronize both Action Table and Question Answer.
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

	public Collection<?> getColumns() {
		return conditionTable.getContainerPropertyIds();
	}

	public void addRow(TableRuleRow decisionRule) {
		// Add decision Rule to both tables.
		conditionTable.addRow(decisionRule);
		actionTable.addRow(decisionRule);
	}

	public void removeAll() {
		conditionTable.removeAll();
		actionTable.removeAll();

	}

	public void removeSelectedRows(TableRule selectedTableRule) {
		for (TableRuleRow rule : conditionTable.getSelectedRules()) {
			conditionTable.removeItem(rule);
			actionTable.removeItem(rule);
			selectedTableRule.removeRule(rule);
		}
	}

	private void setSelectedRows(Set<Object> selectedRows) {
		conditionTable.selectRows(selectedRows, false);
		actionTable.selectRows(selectedRows, false);
	}

	public void removeSelectedColumns(TableRule selectedTableRule) {
		conditionTable.removeColumns(selectedTableRule, conditionTable.getSelectedColumns());
	}

	public void update(TableRule selectedTableRule) {
		Set<Object> selectedRows = conditionTable.getCellRowSelector().getSelectedRows();
		if (selectedTableRule != null) {
			removeAll();
			updateColumns(selectedTableRule);
			updateRows(selectedTableRule);
		}
		setSelectedRows(selectedRows);
	}

	/**
	 * Add all the necessary columns
	 * 
	 * @param selectedTableRule
	 */
	public void updateColumns(TableRule selectedTableRule) {
		if (!selectedTableRule.getRules().isEmpty()) {
			int columns = selectedTableRule.getConditionNumber();
			for (int i = 0; i < (columns / 2); i++) {
				conditionTable.addEmptyColumnPair();
			}
		}
	}

	/**
	 * Add the necessary rows and fills them
	 * 
	 * @param selectedTableRule
	 */
	public void updateRows(TableRule selectedTableRule) {
		for (TableRuleRow row : selectedTableRule.getRules()) {
			addRow(row);
		}
	}

	public int getTableSize(TableRule selectedTableRule) {
		return selectedTableRule.getRules().size();
	}

	// ********************
	// Cell functions
	// ********************

	public Expression getPreviousCellValue(TableRuleRow row, Object propertyId) {
		return conditionTable.getPreviousExpressionValue(row, propertyId);
	}

	public Expression getNextCellValue(TableRuleRow row, Object propertyId) {
		return conditionTable.getNextExpressionValue(row, propertyId);
	}

	public Expression getCellValue(TableRuleRow row, Object propertyId) {
		return conditionTable.getExpressionValue(row, propertyId);
	}

	public void setCellValue(TableRuleRow row, Object propertyId, Expression expression) {
		conditionTable.setExpressionValue(row, propertyId, expression);
	}

	// *************************************
	// Condition and Action tables listeners
	// *************************************

	public void addEditExpressionListener(EditExpressionListener listener) {
		conditionTable.addEditExpressionListener(listener);
	}

	public void removeEditExpressionListener(EditExpressionListener listener) {
		conditionTable.removeEditExpressionListener(listener);
	}

	public void addClearExpressionListener(ClearExpressionListener listener) {
		conditionTable.addClearExpressionListener(listener);
	}

	public void removeClearExpressionListener(ClearExpressionListener listener) {
		conditionTable.removeClearExpressionListener(listener);
	}

	public void addEditActionListener(EditActionListener listener) {
		actionTable.addEditActionListener(listener);
	}

	public void removeEditActionListener(EditActionListener listener) {
		actionTable.removeEditActionListener(listener);
	}

	public void addClearActionListener(ClearActionListener listener) {
		actionTable.addClearActionListener(listener);
	}

	public void removeClearActionListener(ClearActionListener listener) {
		actionTable.removeClearActionListener(listener);
	}

	public Collection<TableRuleRow> getSelectedRules() {
		return conditionTable.getSelectedRules();
	}
}

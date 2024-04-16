package com.biit.abcd.webpages.elements.decisiontable;

import java.util.Collection;
import java.util.Set;

import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.JavaScript;

public class RuleTable extends CustomComponent {

	private static final long serialVersionUID = -1214662437608420332L;
	private HorizontalSplitPanel rootLayout;
	private ConditionTable conditionTable;
	private ActionTable actionTable;

	public RuleTable() {
		super();

		rootLayout = new HorizontalSplitPanel();
		rootLayout.setSizeFull();
		rootLayout.setImmediate(true);
		// rootLayout.setSpacing(true);

		conditionTable = new ConditionTable();
		conditionTable.setSizeFull();
		conditionTable.addCellSelectionListener(selector
				-> actionTable.selectRows(selector.getSelectedRows(), false));

		actionTable = new ActionTable();
		actionTable.setSizeFull();
		actionTable.addCellSelectionListener(selector
				-> conditionTable.selectRows(selector.getSelectedRows(), false));

		conditionTable.setId("main-table");
		actionTable.setId("freeze-pane");

		// Synchronize both Action Table and Question Answer.
		JavaScript
				.getCurrent()
				.execute(
						"var t=document.getElementById('main-table').children[1]; var fp=document.getElementById('freeze-pane').children[1]; fp.addEventListener('scroll', function() {t.scrollTop=fp.scrollTop;}, false);");

		rootLayout.setFirstComponent(conditionTable);
		rootLayout.setSecondComponent(actionTable);
		rootLayout.setSplitPosition(75, Unit.PERCENTAGE);

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
		// Selected by conditions.
		if (!conditionTable.getSelectedRules().isEmpty()) {
			for (TableRuleRow rule : conditionTable.getSelectedRules()) {
				conditionTable.removeItem(rule);
				actionTable.removeItem(rule);
				selectedTableRule.removeRule(rule);
			}
		} else {
			// No conditions defined, only actions. Remove by selected action. 
			for (TableRuleRow rule : actionTable.getSelectedRules()) {
				conditionTable.removeItem(rule);
				actionTable.removeItem(rule);
				selectedTableRule.removeRule(rule);
			}
		}
	}

	public void setSelectedRows(Set<Object> selectedRows) {
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

package com.biit.abcd.webpages.elements.decisiontable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table;

public class ConditionTable extends Table {

	private static final long serialVersionUID = 2760300775584165320L;
	private static final int rowHeaderWidth = 32;
	private CellRowSelector cellRowSelector;
	private List<EditExpressionListener> editExpressionListeners;
	private List<ClearExpressionListener> clearExpressionListeners;

	public ConditionTable() {
		super();

		editExpressionListeners = new ArrayList<EditExpressionListener>();
		clearExpressionListeners = new ArrayList<ClearExpressionListener>();

		setRowHeaderMode(RowHeaderMode.INDEX);
		setColumnWidth(null, rowHeaderWidth);
		setSortEnabled(false);

		setImmediate(true);
		setSizeFull();

		cellRowSelector = new CellRowSelector();
		addItemClickListener(cellRowSelector);
		setCellStyleGenerator(cellRowSelector);
		addActionHandler(cellRowSelector);

		setSelectable(false);
	}

	/**
	 * Removes all columns and rows.
	 */
	public void removeAll() {
		removeAllItems();
		removeAllColumns();
	}

	private void removeAllColumns() {
		List<Object> objectList = new ArrayList<>(getContainerPropertyIds());
		for (Object propertyId : objectList) {
			removeContainerProperty(propertyId);
		}
	}

	public void addEmptyColumnPair() {
		int columnId = getContainerPropertyIds().size();
		addContainerProperty(columnId, ExpressionEditCell.class, null,
				ServerTranslate.translate(LanguageCodes.CONDITION_TABLE_HEADER_QUESTION_CONDITION), null, Align.CENTER);
		addContainerProperty(columnId + 1, ExpressionEditCell.class, null,
				ServerTranslate.translate(LanguageCodes.CONDITION_TABLE_HEADER_ANSWER_CONDITION), null, Align.CENTER);
		if (getContainerPropertyIds().size() >= 2) {
			setSortContainerPropertyId(0);
			sort();
		}
	}

	private class RowDoubleClickedListener implements CellDoubleClickedListener {
		private Object row;
		private Object propertyId;

		public RowDoubleClickedListener(Object itemId, Object propertyId) {
			row = itemId;
			this.propertyId = propertyId;
		}

		@Override
		public void isDoubleClick() {
			fireEditExpressionListeners((TableRuleRow) row, propertyId);
		}
	}

	private class CellEditButtonClickListener implements ClickListener {

		private static final long serialVersionUID = 8366131692308964272L;
		private Object row;
		private Object propertyId;

		public CellEditButtonClickListener(Object itemId, Object propertyId) {
			row = itemId;
			this.propertyId = propertyId;
		}

		@Override
		public void buttonClick(ClickEvent event) {
			fireEditExpressionListeners((TableRuleRow) row, propertyId);
		}
	}

	private class CellClearButtonClickListener implements ClickListener {

		private static final long serialVersionUID = 2915483408521823465L;
		private Object row;
		private Object propertyId;

		public CellClearButtonClickListener(Object row, Object propertyId) {
			this.row = row;
			this.propertyId = propertyId;
		}

		@Override
		public void buttonClick(ClickEvent event) {
			fireClearExpressionListeners((TableRuleRow) row, propertyId);
		}
	}

	private class LayoutClickPropagator implements LayoutClickListener {
		private static final long serialVersionUID = 5504698883691497113L;
		private Table conditionTable;
		private Item item;
		private Object itemId;
		private Object propertyId;

		public LayoutClickPropagator(Table conditiontable, Item item, Object itemId, Object propertyId) {
			conditionTable = conditiontable;
			this.item = item;
			this.itemId = itemId;
			this.propertyId = propertyId;
		}

		@Override
		public void layoutClick(LayoutClickEvent event) {
			MouseEventDetails mouseEvent = new MouseEventDetails();
			mouseEvent.setAltKey(event.isAltKey());
			mouseEvent.setButton(event.getButton());
			mouseEvent.setClientX(event.getClientX());
			mouseEvent.setClientY(event.getClientY());
			mouseEvent.setCtrlKey(event.isCtrlKey());
			mouseEvent.setMetaKey(event.isMetaKey());
			mouseEvent.setRelativeX(event.getRelativeX());
			mouseEvent.setRelativeY(event.getRelativeY());
			mouseEvent.setShiftKey(event.isShiftKey());
			if (event.isDoubleClick()) {
				// Double click
				mouseEvent.setType(0x00002);
			} else {
				mouseEvent.setType(0x00001);
			}
			cellRowSelector.itemClick(new ItemClickEvent(conditionTable, item, itemId, propertyId, mouseEvent));
		}
	}

	public void addCellSelectionListener(CellSelectionListener listener) {
		cellRowSelector.addCellSelectionListener(listener);
	}

	public void removeCellSelectionListener(CellSelectionListener listener) {
		cellRowSelector.removeCellSelectionListener(listener);
	}

	public void selectRows(Set<Object> rowIds, boolean propagate) {
		Set<Cell> rows = new HashSet<Cell>();
		for (Object rowId : rowIds) {
			for (Object colId : getContainerPropertyIds()) {
				Cell tempCell = new Cell(rowId, colId);
				rows.add(tempCell);
			}
		}
		setCurrentSelectedCells(rows, null, propagate);
	}

	public void setCurrentSelectedCells(Set<Cell> cells, Cell cursorCell, boolean propagate) {
		cellRowSelector.setCurrentSelectedCells(this, cells, cursorCell, propagate);
	}

	public void addRow(TableRuleRow decisionRule) {
		Item item = addItem(decisionRule);
		setDefaultNewItemPropertyValues(decisionRule, item);
		updateRow(decisionRule);
	}

	@SuppressWarnings("unchecked")
	private void setDefaultNewItemPropertyValues(final TableRuleRow itemId, final Item item) {
		if (item != null) {
			for (final Object propertyId : getContainerPropertyIds()) {
				if (item.getItemProperty(propertyId).getValue() == null) {
					ExpressionEditCell editCellComponent = new ExpressionEditCell();
					editCellComponent.setExpression(itemId.getConditions());
					editCellComponent.addEditButtonClickListener(new CellEditButtonClickListener(itemId, propertyId));
					editCellComponent
							.addRemoveButtonClickListener(new CellClearButtonClickListener(itemId, propertyId));
					editCellComponent.addDoubleClickListener(new RowDoubleClickedListener(itemId, propertyId));
					// Propagate element click.
					editCellComponent.addLayoutClickListener(new LayoutClickPropagator(this, item, itemId, propertyId));
					item.getItemProperty(propertyId).setValue(editCellComponent);
				}
			}
		}
	}

	public void updateRow(TableRuleRow decisionRule) {
		Item item = getItem(decisionRule);
		for (final Object propertyId : getContainerPropertyIds()) {
			if (decisionRule.getConditions().getExpressions().size() > 0) {
				Expression expression = decisionRule.getConditions().getExpressions().get((Integer) propertyId);
				((ExpressionEditCell) item.getItemProperty(propertyId).getValue()).setLabel(expression
						.getRepresentation());
			}
		}
		sort();
	}

	public Collection<TableRuleRow> getSelectedRules() {
		Set<TableRuleRow> rules = new HashSet<TableRuleRow>();
		for (Cell cell : cellRowSelector.getSelectedCells()) {
			rules.add((TableRuleRow) cell.getRow());
		}
		return rules;
	}

	public CellRowSelector getCellRowSelector() {
		return cellRowSelector;
	}

	public Collection<Integer> getSelectedColumns() {
		Set<Integer> columns = new HashSet<Integer>();
		for (Cell cell : cellRowSelector.getSelectedCells()) {
			columns.add((Integer) cell.getCol());
		}
		return columns;
	}

	public void removeColumns(TableRule selectedTableRule, Collection<Integer> columnIds) {
		// To remove the expression related
		if ((columnIds.size() == 1)) {
			if ((columnIds.iterator().next() % 2) == 0) {
				columnIds.add(columnIds.iterator().next() + 1);
			} else {
				columnIds.add(columnIds.iterator().next() - 1);
			}
		}
		for (Object object : getItemIds()) {
			TableRuleRow row = (TableRuleRow) object;
			List<Expression> values = new ArrayList<Expression>();
			for (Integer columnId : columnIds) {
				values.add(getExpressionValue(row, columnId));
			}
			selectedTableRule.removeConditions(row, values);
		}

		Set<Integer> filteredColumnIds = new HashSet<>();
		for (Integer columnId : columnIds) {
			if ((columnId % 2) == 0) {
				filteredColumnIds.add(columnId);
			} else {
				filteredColumnIds.add(columnId - 1);
			}
		}
		for (int i = 0; i < filteredColumnIds.size(); i++) {
			removeContainerProperty(getContainerPropertyIds().size() - 1);
			removeContainerProperty(getContainerPropertyIds().size() - 1);
		}
	}

	// ********************
	// Expression functions
	// ********************

	public Expression getExpressionValue(TableRuleRow row, Object propertyId) {
		return row.getConditions().getExpressions().get((Integer) propertyId);
	}

	public void setExpressionValue(TableRuleRow row, Object propertyId, Expression expression) {
		row.getConditions().getExpressions().set(((Integer) propertyId), expression);
	}

	public Expression getNextExpressionValue(TableRuleRow row, Object propertyId) {
		int index = ((Integer) propertyId) + 1;
		if (index >= row.getConditions().getExpressions().size()) {
			return null;
		} else {
			return row.getConditions().getExpressions().get(index);
		}
	}

	public Expression getPreviousExpressionValue(TableRuleRow row, Object propertyId) {
		return row.getConditions().getExpressions().get(((Integer) propertyId) - 1);
	}

	// ******************
	// Listeners managers
	// ******************

	public void addEditExpressionListener(EditExpressionListener listener) {
		editExpressionListeners.add(listener);
	}

	public void removeEditExpressionListener(EditExpressionListener listener) {
		editExpressionListeners.remove(listener);
	}

	private void fireEditExpressionListeners(TableRuleRow row, Object propertyId) {
		for (EditExpressionListener listener : editExpressionListeners) {
			listener.editExpression(row, propertyId);
		}
	}

	public void addClearExpressionListener(ClearExpressionListener listener) {
		clearExpressionListeners.add(listener);
	}

	public void removeClearExpressionListener(ClearExpressionListener listener) {
		clearExpressionListeners.remove(listener);
	}

	private void fireClearExpressionListeners(TableRuleRow row, Object propertyId) {
		for (ClearExpressionListener listener : clearExpressionListeners) {
			listener.clearExpression(row, propertyId);
		}
	}
}

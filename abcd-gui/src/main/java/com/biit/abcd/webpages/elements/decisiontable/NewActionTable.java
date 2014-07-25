package com.biit.abcd.webpages.elements.decisiontable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;

public class NewActionTable extends Table {

	private static final long serialVersionUID = -5400952496906256671L;
	private CellRowSelector cellRowSelector;
	private List<EditActionListener> editActionListeners;
	private List<ClearActionListener> clearActionListeners;
	private int CHARACTER_LIMIT = 25;

	enum Columns {
		ACTION
	};

	public NewActionTable() {
		super();

		editActionListeners = new ArrayList<EditActionListener>();
		clearActionListeners = new ArrayList<ClearActionListener>();

		setImmediate(true);
		setSizeFull();
		addContainerProperty(Columns.ACTION, Component.class, null,
				ServerTranslate
				.translate(LanguageCodes.ACTION_TABLE_HEADER_ACTION),
				null, Align.CENTER);
		cellRowSelector = new CellRowSelector();
		addItemClickListener(cellRowSelector);
		setCellStyleGenerator(cellRowSelector);
		setSelectable(false);
	}

	public void addRow(TableRuleRow row) {
		if (row != null) {
			Item item = addItem(row);
			setDefaultNewItemPropertyValues(row, item);
			updateRow(row);
		}
	}

	public void updateRow(TableRuleRow row){
		Item rowItem = getItem(row);
		ActionValueEditCell actionValue = ((ActionValueEditCell) rowItem.getItemProperty(Columns.ACTION).getValue());
		if (row.getAction() != null) {
			String representation = row.getAction().getRepresentation();
			if(representation.length()>CHARACTER_LIMIT){
				representation = "..."+representation.substring(representation.length()-CHARACTER_LIMIT, representation.length());
			}
			actionValue.setLabel(representation);
		} else {
			actionValue.setLabel("null");
		}
	}

	@SuppressWarnings("unchecked")
	private void setDefaultNewItemPropertyValues(final Object itemId,
			final Item item) {
		final Table thisTable = this;
		if (item.getItemProperty(Columns.ACTION).getValue() == null) {
			EditCellComponent editCellComponent = new ActionValueEditCell();
			// Propagate element click.
			editCellComponent.addLayoutClickListener(new LayoutClickListener() {
				private static final long serialVersionUID = -8606373054437936380L;

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
					cellRowSelector.itemClick(new ItemClickEvent(thisTable,
							item, itemId, Columns.ACTION, mouseEvent));
				}
			});
			item.getItemProperty(Columns.ACTION).setValue(editCellComponent);
			editCellComponent.addEditButtonClickListener(new CellEditButtonClickListener(itemId));
			editCellComponent.addRemoveButtonClickListener(new CellDeleteButtonClickListener(itemId));
			item.getItemProperty(Columns.ACTION).setValue(editCellComponent);
		}
	}

	/**
	 * This action listener is called when the user press on the edit button of
	 * the cell component
	 * 
	 */
	private class CellEditButtonClickListener implements ClickListener {
		private static final long serialVersionUID = -4186477224806988479L;
		private Object row;

		public CellEditButtonClickListener(Object row) {
			this.row = row;
		}

		@Override
		public void buttonClick(ClickEvent event) {
			fireEditActionListeners((TableRuleRow) row);
		}
	}

	/**
	 * This action listener is called when the user press on the remove button
	 * of the cell component
	 * 
	 */
	private class CellDeleteButtonClickListener implements ClickListener {
		private static final long serialVersionUID = -7125934888135148456L;
		private Object row;

		public CellDeleteButtonClickListener(Object itemId) {
			this.row = itemId;
		}

		@Override
		public void buttonClick(ClickEvent event) {
			fireClearActionListeners((TableRuleRow) row);
		}
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

	public void addCellSelectionListener(CellSelectionListener listener) {
		cellRowSelector.addCellSelectionListener(listener);
	}

	public void removeCellSelectionListener(CellSelectionListener listener) {
		cellRowSelector.removeCellSelectionListener(listener);
	}

	public void removeAll() {
		removeAllItems();
	}

	// **********************
	// Action table listeners
	// **********************

	public void addEditActionListener(EditActionListener listener){
		editActionListeners.add(listener);
	}

	public void removeEditActionListener(EditActionListener listener){
		editActionListeners.remove(listener);
	}

	private void fireEditActionListeners(TableRuleRow row) {
		for (EditActionListener listener : editActionListeners) {
			listener.editAction(row);
		}
	}
	public void addClearActionListener(ClearActionListener listener){
		clearActionListeners.add(listener);
	}

	public void removeClearActionListener(ClearActionListener listener){
		clearActionListeners.remove(listener);
	}

	private void fireClearActionListeners(TableRuleRow row) {
		for (ClearActionListener listener : clearActionListeners) {
			listener.removeAction(row);
		}
	}
}

package com.biit.abcd.webpages.elements.decisiontable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.CellStyleGenerator;

public class CellRowSelector implements ItemClickListener, CellStyleGenerator, Handler {
	private static final long serialVersionUID = 6036202869337803510L;

	public class Cell {

		private Object row;
		private Object col;

		public Cell(Object row, Object col) {
			this.row = row;
			this.col = col;
		}

		public Object getRow() {
			return row;
		}

		public Object getCol() {
			return col;
		}

		private CellRowSelector getOuterType() {
			return CellRowSelector.this;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((col == null) ? 0 : col.hashCode());
			result = prime * result + ((row == null) ? 0 : row.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Cell other = (Cell) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (col == null) {
				if (other.col != null)
					return false;
			} else if (!col.equals(other.col))
				return false;
			if (row == null) {
				if (other.row != null)
					return false;
			} else if (!row.equals(other.row))
				return false;
			return true;
		}
	}

	private Cell startSelectionCell;
	private Cell cursorCell;
	private Set<Cell> selectedCells;

	private List<CellSelectionListener> listeners;

	public CellRowSelector() {
		cursorCell = null;
		selectedCells = new HashSet<Cell>();
		listeners = new ArrayList<CellSelectionListener>();
	}

	@Override
	public void itemClick(ItemClickEvent event) {
		Table table = (Table) event.getComponent();
		if (table != null) {
			if (event.isShiftKey()) {
				selectAreaFromCursorToNewPosition(table, event.getItemId(), event.getPropertyId());
				return;
			}
			if (event.isCtrlKey()) {
				changeSelectionStateOfCell(table, new Cell(event.getItemId(), event.getPropertyId()));
				return;
			}
			if (event.isAltKey()) {
				return;
			}

			if (!event.isDoubleClick()) {
				// Simple click (Select cell)
				setCursorTo(table, event.getItemId(), event.getPropertyId());
			} else {
				// Select row
				selectRow(table, event.getItemId(), event.getPropertyId());
			}

			table.focus();
			// UI.getCurrent().setFocusedComponent(table);
		}
	}

	public void selectAreaFromCursorToNewPosition(Table table, Object row, Object col) {
		// If there is no cursor
		if (cursorCell == null) {
			setCursorTo(table, row, col);
			return;
		}

		// Clean the selection
		cleanSelection(table, false);

		if (startSelectionCell == null) {
			startSelectionCell = cursorCell;
		}
		cursorCell = new Cell(row, col);

		Set<Object> selectionCols;
		Set<Object> selectionRows;

		// Get row and col ids in selection.
		selectionCols = getAllIdsBetweenTwoIds(table.getContainerPropertyIds(), startSelectionCell.getCol(),
				cursorCell.getCol());
		selectionRows = getAllIdsBetweenTwoIds(table.getItemIds(), startSelectionCell.getRow(), cursorCell.getRow());
		Set<Cell> selectedAreaCells = new HashSet<CellRowSelector.Cell>();

		// Create a list of elements in the area to the new position
		for (Object selectedCol : selectionCols) {
			for (Object selectedRow : selectionRows) {
				selectedAreaCells.add(new Cell(selectedRow, selectedCol));
			}
		}

		setSelection(table, selectedAreaCells);

		fireCellSelectionListener();
		paintSelection(table);
	}
	
	public void setCurrentSelectedCells(Set<Cell> cells, Cell cursorCell){
		this.selectedCells = cells;
		this.cursorCell = cursorCell;
	}

	protected void setSelection(Table table, Collection<Cell> cells) {
		for (Cell cell : cells) {
			selectedCells.add(cell);
			((EditCellComponent) table.getItem(cell.getRow()).getItemProperty(cell.getCol()).getValue()).select(false);
		}
	}

	protected void changeSelectionStateOfCell(Table table, Cell cell) {
		if (!selectedCells.contains(cell)) {
			selectedCells.add(cell);
		} else {
			((EditCellComponent) table.getItem(cell.getRow()).getItemProperty(cell.getCol()).getValue()).select(false);
			selectedCells.remove(cell);
		}
		table.refreshRowCache();
	}

	private Set<Object> getAllIdsBetweenTwoIds(Collection<?> collection, Object id1, Object id2) {
		Set<Object> elements = new HashSet<>();
		Iterator<?> itemsIds = collection.iterator();
		int found = 0;
		while (itemsIds.hasNext()) {
			Object next = itemsIds.next();
			if (next.equals(id1) || next.equals(id2)) {
				found++;
				if (id1.equals(id2)) {
					found++;
				}
			}
			if (found >= 1) {
				elements.add(next);
			}
			if (found > 1) {
				break;
			}
		}
		return elements;
	}

	public void setCursorTo(Table table, Object row, Object col) {
		startSelectionCell = null;
		cleanSelection(table, false);
		if (row != null && col != null) {
			cursorCell = new Cell(row, col);
			selectedCells.add(cursorCell);
		}
		fireCellSelectionListener();
		paintSelection(table);
	}

	public void selectRow(Table table, Object row, Object col) {
		cleanSelection(table, false);
		if (row != null && col != null) {
			cursorCell = new Cell(row, col);
			Iterator<?> colItr = table.getContainerPropertyIds().iterator();
			while (colItr.hasNext()) {
				Object colId = colItr.next();
				if (colId == null) {
					continue;
				}
				selectedCells.add(new Cell(row, colId));
			}
		}
		fireCellSelectionListener();
		paintSelection(table);
		table.focus();
	}

	protected void cleanSelection(Table table, boolean propagate) {
		for (Cell cell : selectedCells) {
			((EditCellComponent) table.getItem(cell.getRow()).getItemProperty(cell.getCol()).getValue()).select(false);
		}
		selectedCells.clear();
		if (propagate) {
			fireCellSelectionListener();
		}
	}

	public void cleanSelection(Table table) {
		cleanSelection(table, true);
	}

	protected void paintSelection(Table table) {
		for (Cell cell : selectedCells) {
			((EditCellComponent) table.getItem(cell.getRow()).getItemProperty(cell.getCol()).getValue()).select(true);
		}
		table.refreshRowCache();
	}

	@Override
	public String getStyle(Table source, Object itemId, Object propertyId) {
		if (propertyId == null) {
			return "row-kiwi";
		}

		if ((new Cell(itemId, propertyId)).equals(cursorCell)) {
			return "cursor-cell";
		}
		if (selectedCells.contains(new Cell(itemId, propertyId))) {
			return "selected";
		}

		return "";
	}

	public Set<Cell> getSelectedCells() {
		return selectedCells;
	}

	public void addCellSelectionListener(CellSelectionListener listener) {
		listeners.add(listener);
	}

	public void removeCellSelectionListener(CellSelectionListener listener) {
		listeners.remove(listener);
	}

	public void fireCellSelectionListener() {
		for (CellSelectionListener listener : listeners) {
			listener.cellSelectionChanged(this);
		}
	}

	Action tab_next = new ShortcutAction("Tab", ShortcutAction.KeyCode.TAB, null);
	Action tab_prev = new ShortcutAction("Shift+Tab", ShortcutAction.KeyCode.TAB,
			new int[] { ShortcutAction.ModifierKey.SHIFT });
	Action cur_down = new ShortcutAction("Down", ShortcutAction.KeyCode.ARROW_DOWN, null);
	Action cur_up = new ShortcutAction("Up", ShortcutAction.KeyCode.ARROW_UP, null);
	Action cur_left = new ShortcutAction("Left", ShortcutAction.KeyCode.ARROW_LEFT, null);
	Action cur_right = new ShortcutAction("Right", ShortcutAction.KeyCode.ARROW_RIGHT, null);
	Action enter = new ShortcutAction("Enter", ShortcutAction.KeyCode.ENTER, null);

	@Override
	public Action[] getActions(Object target, Object sender) {
		return new Action[] { tab_next, tab_prev, cur_down, cur_up, cur_left, cur_right, enter };
	}

	@Override
	public void handleAction(Action action, Object sender, Object target) {
		System.out.println("kiwi!");
	}
}
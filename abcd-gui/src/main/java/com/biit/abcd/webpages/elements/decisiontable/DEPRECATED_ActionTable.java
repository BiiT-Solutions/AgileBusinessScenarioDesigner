package com.biit.abcd.webpages.elements.decisiontable;

import java.util.HashSet;
import java.util.Set;

import com.biit.abcd.MessageManager;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidExpression;
import com.biit.abcd.persistence.entity.rules.ActionExpression;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;

public class DEPRECATED_ActionTable extends Table {
	private static final long serialVersionUID = -8737505874064899775L;
	private CellRowSelector cellRowSelector;

	enum Columns {
		ACTION
	};

	public DEPRECATED_ActionTable() {
		setImmediate(true);
		setSizeFull();
		addContainerProperty(Columns.ACTION, Component.class, null,
				ServerTranslate.translate(LanguageCodes.ACTION_TABLE_HEADER_ACTION), null, Align.CENTER);
		cellRowSelector = new CellRowSelector();
		addItemClickListener(cellRowSelector);
		setCellStyleGenerator(cellRowSelector);
		setSelectable(false);
	}

	public void addItem(TableRuleRow rule) {
		if (rule != null) {
			setDefaultNewItemPropertyValues(rule, super.addItem(rule));
			updateItemActionInGui(rule);
		}
	}

	@SuppressWarnings("unchecked")
	private void setDefaultNewItemPropertyValues(final Object itemId, final Item item) {
		final Table thisTable = this;
		for (final Object propertyId : getContainerPropertyIds()) {
			if (item.getItemProperty(propertyId).getValue() == null) {
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
						cellRowSelector.itemClick(new ItemClickEvent(thisTable, item, itemId, propertyId, mouseEvent));
					}
				});
				item.getItemProperty(propertyId).setValue(editCellComponent);
				editCellComponent.addEditButtonClickListener(new CellEditButtonClickListener((TableRuleRow) itemId));
				editCellComponent
				.addRemoveButtonClickListener(new CellDeleteButtonClickListener((TableRuleRow) itemId));
				item.getItemProperty(propertyId).setValue(editCellComponent);
			}
		}
	}

	/**
	 * Updates a row of the table.
	 * 
	 * @param rule
	 */
	private void updateItemActionInGui(TableRuleRow rule) {
		Item row = getItem(rule);
		ActionValueEditCell actionValue = ((ActionValueEditCell) row.getItemProperty(Columns.ACTION).getValue());
		if ((rule.getActions() != null) && !rule.getActions().isEmpty()) {
			actionValue.setLabel(rule.getActions().get(0));
		} else {
			actionValue.setLabel((ActionExpression) null);
		}
	}

	private class CellEditButtonClickListener implements ClickListener {
		private static final long serialVersionUID = -4186477224806988479L;
		private TableRuleRow rule;

		public CellEditButtonClickListener(TableRuleRow rule) {
			this.rule = rule;
		}

		@Override
		public void buttonClick(ClickEvent event) {
			try {
				final AddNewActionExpressionWindow newActionValueWindow = new AddNewActionExpressionWindow(rule
						.getActions().get(0));

				newActionValueWindow.showCentered();
				newActionValueWindow.addAcceptActionListener(new AcceptActionListener() {
					@Override
					public void acceptAction(AcceptCancelWindow window) {
						try {
							rule.getActions().get(0).setExpressionChain(newActionValueWindow.getExpressionChain());
							updateItemActionInGui(rule);
							newActionValueWindow.close();
						} catch (NotValidExpression e) {
							MessageManager.showError(e.getMessage());
						}
					}
				});
			} catch (NotValidExpression e1) {
				MessageManager.showError(e1.getMessage());
				AbcdLogger.errorMessage(DEPRECATED_ActionTable.class.getName(), e1);
			}
		}
	}

	private class CellDeleteButtonClickListener implements ClickListener {
		private static final long serialVersionUID = -7125934888135148456L;
		private TableRuleRow rule;

		public CellDeleteButtonClickListener(TableRuleRow rule) {
			this.rule = rule;
		}

		@Override
		public void buttonClick(ClickEvent event) {
			try {
				rule.getActions().get(0).setExpressionChain("");
				updateItemActionInGui(rule);
			} catch (NotValidExpression e) {
				MessageManager.showError(e.getMessage());
			}
		}
	}

	@Override
	public boolean removeItem(Object itemId) {
		setCurrentSelectedCells(new HashSet<Cell>(), null, true);
		return super.removeItem(itemId);
	}

	public void setCurrentSelectedCells(Set<Cell> cells, Cell cursorCell, boolean propagate) {
		cellRowSelector.setCurrentSelectedCells(this, cells, cursorCell, propagate);
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

	public void addCellSelectionListener(CellSelectionListener listener) {
		cellRowSelector.addCellSelectionListener(listener);
	}

	public void removeCellSelectionListener(CellSelectionListener listener) {
		cellRowSelector.removeCellSelectionListener(listener);
	}
}

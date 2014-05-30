package com.biit.abcd.webpages.elements.decisiontable;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.rules.TableRule;
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

public class ActionTable extends Table {
	private static final long serialVersionUID = -8737505874064899775L;
	private CellRowSelector cellRowSelector;

	enum Columns {
		ACTION
	};

	public ActionTable() {
		setImmediate(true);
		setSizeFull();
		addContainerProperty(Columns.ACTION, Component.class, null,
				ServerTranslate.tr(LanguageCodes.ACTION_TABLE_HEADER_ACTION), null, Align.CENTER);
		cellRowSelector = new CellRowSelector();
		addItemClickListener(cellRowSelector);
		setCellStyleGenerator(cellRowSelector);
		setSelectable(false);
	}

	public void addItem(TableRule rule) {
		if (rule != null) {
			setDefaultNewItemPropertyValues(rule, super.addItem(rule));
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
				editCellComponent.addEditButtonClickListener(new CellEditButtonClickListener((TableRule) itemId));
				editCellComponent.addRemoveButtonClickListener(new CellDeleteButtonClickListener((TableRule) itemId));
				item.getItemProperty(propertyId).setValue(editCellComponent);
			}
		}
	}

	/**
	 * Updates a row of the table.
	 * 
	 * @param rule
	 */
	private void updateItem(TableRule rule) {
		Item row = getItem(rule);
		ActionValueEditCell actionValue = ((ActionValueEditCell) row.getItemProperty(Columns.ACTION).getValue());
		actionValue.setLabel(rule.getAction().getExpression());
	}

	private class CellEditButtonClickListener implements ClickListener {
		private static final long serialVersionUID = -4186477224806988479L;
		private TableRule rule;

		public CellEditButtonClickListener(TableRule rule) {
			this.rule = rule;
		}

		@Override
		public void buttonClick(ClickEvent event) {
			final AddNewActionValue newActionValue = new AddNewActionValue(rule.getAction());
			newActionValue.showCentered();
			newActionValue.addAcceptAcctionListener(new AcceptActionListener() {
				@Override
				public void acceptAction(AcceptCancelWindow window) {
					rule.getAction().setExpression(newActionValue.getText());
					updateItem(rule);
					newActionValue.close();
				}
			});
		}
	}

	private class CellDeleteButtonClickListener implements ClickListener {
		private static final long serialVersionUID = -7125934888135148456L;
		private TableRule rule;

		public CellDeleteButtonClickListener(TableRule rule) {
			this.rule = rule;
		}

		@Override
		public void buttonClick(ClickEvent event) {
			rule.getAction().setExpression("");
			updateItem(rule);
		}
	}
}

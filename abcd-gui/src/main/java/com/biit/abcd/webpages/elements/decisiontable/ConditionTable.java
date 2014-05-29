package com.biit.abcd.webpages.elements.decisiontable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.decisiontable.DecisionRule;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;

public class ConditionTable extends Table {

	private static final long serialVersionUID = -8109315235459994799L;

	private CellRowSelector cellRowSelector;

	public ConditionTable() {
		setImmediate(true);
		setSizeFull();
		cellRowSelector = new CellRowSelector();
		addItemClickListener(cellRowSelector);
		setCellStyleGenerator(cellRowSelector);
		setSelectable(false);
	}

	public void addColumn(Question question) {
		addContainerProperty(question, Component.class, null, question.getName(), null, Align.CENTER);
		for (Object itemId : getItemIds()) {
			setDefaultNewItemPropertyValues(itemId, getItem(itemId));
		}
	}

	public class CellRowSelector implements ItemClickListener, CellStyleGenerator {
		private static final long serialVersionUID = 6036202869337803510L;

		private List<Object> selectedItems;
		private List<Object> selectedProperties;

		public CellRowSelector() {
			selectedItems = new ArrayList<Object>();
			selectedProperties = new ArrayList<Object>();
		}

		@Override
		public void itemClick(ItemClickEvent event) {
			Table table = (Table) event.getComponent();
			if (table != null) {
				if (event.isShiftKey()) {
					return;
				}
				if (event.isCtrlKey()) {
					return;
				}
				// Simple selection mode.
				// Clean selection first
				cleanSelection(table);

				if (!event.isDoubleClick()) {
					// Simple click (Select cell)
					selectedItems.add(event.getItemId());
					selectedProperties.add(event.getPropertyId());
				} else {
					// Double click (Select row)
					selectedItems.add(event.getItemId());
					selectedProperties.addAll(table.getContainerPropertyIds());
				}
				// Paint changes
				paintSelection(table);
			}
		}

		public void cleanSelection(Table table) {
			for (Object itemId : selectedItems) {
				for (Object propertyId : selectedProperties) {
					((EditCellComponent) table.getItem(itemId).getItemProperty(propertyId).getValue()).select(false);
				}
			}
			selectedItems.clear();
			selectedProperties.clear();
		}

		public void paintSelection(Table table) {
			for (Object itemId : selectedItems) {
				for (Object propertyId : selectedProperties) {
					((EditCellComponent) table.getItem(itemId).getItemProperty(propertyId).getValue()).select(true);
				}
			}
			table.refreshRowCache();
		}

		public void selectItem(Table table, Object itemId, Object propertyId) {
			cleanSelection(table);
			selectedItems.add(itemId);
			selectedProperties.add(propertyId);
			paintSelection(table);
		}

		@Override
		public String getStyle(Table source, Object itemId, Object propertyId) {
			if (propertyId == null || itemId == null) {
				// Yes this function is called with itemId and property null.
				return "";
			}

			if (selectedItems.contains(itemId) && selectedProperties.contains(propertyId)) {
				return "selected";
			}
			return "";
		}

		public Collection<Object> getSelectedPropertiesId() {
			return selectedProperties;
		}

		public Collection<Object> getSelectedItemsId() {
			return selectedItems;
		}
	}

	public Collection<Question> getSelectedQuestions() {
		Set<Question> questions = new HashSet<Question>();
		for (Object object : cellRowSelector.getSelectedPropertiesId()) {
			questions.add((Question) object);
		}
		return questions;
	}

	public Collection<DecisionRule> getSelectedRules() {
		Set<DecisionRule> rules = new HashSet<DecisionRule>();
		for (Object object : cellRowSelector.getSelectedItemsId()) {
			rules.add((DecisionRule) object);
		}
		return rules;
	}

	public void addItem(DecisionRule rule) {
		if (rule != null) {
			setDefaultNewItemPropertyValues(rule, super.addItem(rule));
		}
	}

	@SuppressWarnings("unchecked")
	private void setDefaultNewItemPropertyValues(final Object itemId, final Item item) {
		final Table thisTable = this;
		for (final Object propertyId : getContainerPropertyIds()) {
			if (item.getItemProperty(propertyId).getValue() == null) {
				EditCellComponent editCellComponent = new QuestionValueEditCell();
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
				editCellComponent.addEditButtonClickListener(new CellEditButtonClickListener());
				item.getItemProperty(propertyId).setValue(editCellComponent);
			}
		}
	}

	public class CellEditButtonClickListener implements ClickListener {
		private static final long serialVersionUID = -4186477224806988479L;

		@Override
		public void buttonClick(ClickEvent event) {

			UI.getCurrent().addWindow(new AcceptCancelWindow(new Label("kiei")));

		}
	}

	//
	// public void setValue(Object itemId){
	//
	// }
}

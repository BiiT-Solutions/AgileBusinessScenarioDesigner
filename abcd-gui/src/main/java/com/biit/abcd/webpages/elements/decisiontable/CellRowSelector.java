package com.biit.abcd.webpages.elements.decisiontable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.CellStyleGenerator;

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
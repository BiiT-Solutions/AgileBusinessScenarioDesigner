package com.biit.abcd.webpages.components;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (GUI)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.webpages.elements.decisiontable.Cell;
import com.biit.abcd.webpages.elements.decisiontable.CellRowSelector;
import com.biit.persistence.entity.StorableObject;
import com.biit.utils.date.DateManager;
import com.vaadin.data.Item;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.Table;

public class TableCellLabelEdit extends Table {
	private static final long serialVersionUID = 6868344288061962557L;
	private CellRowSelector cellRowSelector;

	enum MenuProperties {
		TABLE_NAME, UPDATE_TIME;
	};

	public TableCellLabelEdit(LanguageCodes header1, LanguageCodes header2) {
		initContainerProperties(header1, header2);
	}

	public void update(List<Object> objects) {
		this.removeAllItems();
		for (Object value : objects) {
			addRow(value);
		}
	}

	private void initContainerProperties(LanguageCodes header1, LanguageCodes header2) {
		setSelectable(true);
		setImmediate(true);
		setMultiSelect(false);
		setNullSelectionAllowed(false);
		setSizeFull();

		addContainerProperty(MenuProperties.TABLE_NAME, EditCellComponent.class, null,
				ServerTranslate.translate(header1), null, Align.LEFT);

		addContainerProperty(MenuProperties.UPDATE_TIME, String.class, "", ServerTranslate.translate(header2), null,
				Align.LEFT);

		cellRowSelector = new CellRowSelector();
		addItemClickListener(cellRowSelector);
		setCellStyleGenerator(cellRowSelector);

		setColumnCollapsingAllowed(true);
		setColumnCollapsible(MenuProperties.TABLE_NAME, false);
		setColumnCollapsible(MenuProperties.UPDATE_TIME, true);
		setColumnCollapsed(MenuProperties.UPDATE_TIME, true);

		this.setColumnExpandRatio(MenuProperties.TABLE_NAME, 1);
		this.setColumnExpandRatio(MenuProperties.UPDATE_TIME, 1);

		setSortContainerPropertyId(MenuProperties.TABLE_NAME);
		// setSortAscending(false);
	}

	public void addRow(Object object) {
		if (object != null) {
			setDefaultNewItemPropertyValues(object, super.addItem(object));
			updateItemTableRuleInGui((StorableObject) object);
		}
		sort();
	}

	public void removeSelectedRow() {
		Object row = getValue();
		if (row != null) {
			removeItem(row);
		}
	}

	@SuppressWarnings("unchecked")
	protected EditCellComponent setDefaultNewItemPropertyValues(final Object itemId, final Item item) {
		if (item.getItemProperty(MenuProperties.TABLE_NAME).getValue() == null) {
			EditCellComponent editCellComponent = new SelectTableEditCell();
			editCellComponent.setOnlyEdit(true);
			editCellComponent.addLayoutClickListener(new LayoutClickListener() {
				private static final long serialVersionUID = -4750839674064167369L;

				@Override
				public void layoutClick(LayoutClickEvent event) {
					setValue(itemId);
				}
			});
			item.getItemProperty(MenuProperties.TABLE_NAME).setValue(editCellComponent);
			item.getItemProperty(MenuProperties.UPDATE_TIME).setValue(
					DateManager.convertDateToStringWithHours(((StorableObject) itemId).getUpdateTime()));
			return editCellComponent;
		}
		return null;
	}

	/**
	 * Updates a row of the table.
	 *
	 * @param object
	 *            a storable object.
	 */
	@SuppressWarnings("unchecked")
	protected void updateItemTableRuleInGui(StorableObject object) {
		Item row = getItem(object);
		SelectTableEditCell tableCell = ((SelectTableEditCell) row.getItemProperty(MenuProperties.TABLE_NAME)
				.getValue());
		row.getItemProperty(MenuProperties.UPDATE_TIME).setValue(
				DateManager.convertDateToStringWithHours(object.getUpdateTime()));
		tableCell.setLabel(object);
	}

	@Override
	public void setValue(Object itemId) {
		if (itemId != null) {
			Set<Cell> cells = new HashSet<Cell>();
			for (Object colId : getContainerPropertyIds()) {
				Cell tempCell = new Cell(itemId, colId);
				cells.add(tempCell);
			}
			cellRowSelector.setCurrentSelectedCells(this, cells, null, false);
		}
		super.setValue(itemId);
	}

}

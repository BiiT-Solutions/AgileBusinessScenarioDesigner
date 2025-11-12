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

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.utils.date.DateManager;
import com.vaadin.data.Item;
import com.vaadin.ui.Table;

public class SelectTableRuleTable extends Table {
	private static final long serialVersionUID = -5723571725991709050L;

	enum MenuProperties {		TABLE_NAME, UPDATE_TIME;
	};

	public SelectTableRuleTable() {
		initContainerProperties();
	}

	private void initContainerProperties() {
		setSelectable(true);
		setImmediate(true);
		setMultiSelect(false);
		setSizeFull();
		
		addContainerProperty(MenuProperties.TABLE_NAME, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_VARIABLE_TABLE_COLUMN_NAME), null, Align.LEFT);

		addContainerProperty(MenuProperties.UPDATE_TIME, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_VARIABLE_TABLE_COLUMN_UPDATE), null, Align.LEFT);
		
		setColumnCollapsingAllowed(true);
		setColumnCollapsible(MenuProperties.TABLE_NAME, false);
		setColumnCollapsible(MenuProperties.UPDATE_TIME, true);
		setColumnCollapsed(MenuProperties.UPDATE_TIME, true);

		this.setColumnExpandRatio(MenuProperties.TABLE_NAME, 1);
		this.setColumnExpandRatio(MenuProperties.UPDATE_TIME, 1);
		
		setSortContainerPropertyId(MenuProperties.TABLE_NAME);
		setSortAscending(true);
		sort();
	}

	@SuppressWarnings({ "unchecked" })
	public void addRow(TableRule tableRule) {
		Item item = addItem(tableRule);
		item.getItemProperty(MenuProperties.TABLE_NAME).setValue(tableRule.getName());
		item.getItemProperty(MenuProperties.UPDATE_TIME).setValue(DateManager.convertDateToStringWithHours(tableRule.getUpdateTime()));
	}

	public void removeSelectedRow() {
		TableRule tableRule = (TableRule) getValue();
		if (tableRule != null) {
			removeItem(tableRule);
		}
	}

	public void update(Form form) {
		this.removeAllItems();
		for (TableRule tableRule : form.getTableRules()) {
			addRow(tableRule);
		}
		this.sort();
	}

	public TableRule getSelectedTableRule() {
		return (TableRule) getValue();
	}

	public void setSelectedTableRule(TableRule tableRule) {
		setValue(tableRule);
	}

}

package com.biit.abcd.webpages.elements.global.variables;

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

import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.language.AnswerFormatUi;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.vaadin.data.Item;
import com.vaadin.ui.Table;

public class GlobalVariablesTable extends Table {
	private static final long serialVersionUID = 7897917138494222638L;

	public enum Properties {
		VARIABLE_NAME, VARIABLE_TYPE
	};

	public GlobalVariablesTable() {
		super();
		setImmediate(true);
		setSelectable(true);
		addContainerProperty(Properties.VARIABLE_NAME, String.class, "",
				ServerTranslate.translate(LanguageCodes.GLOBAL_VARIABLE_NAME), null, Align.CENTER);
		addContainerProperty(Properties.VARIABLE_TYPE, String.class, "",
				ServerTranslate.translate(LanguageCodes.GLOBAL_VARIABLE_TYPE), null, Align.CENTER);

		setSortContainerPropertyId(Properties.VARIABLE_NAME);
	}

	@SuppressWarnings("unchecked")
	public Item addItem(GlobalVariable globalVariable) {
		Item item = super.addItem(globalVariable);
		item.getItemProperty(Properties.VARIABLE_NAME).setValue(globalVariable.getName());
		try {
			item.getItemProperty(Properties.VARIABLE_TYPE).setValue(ServerTranslate
					.translate(AnswerFormatUi.getFromAnswerFormat(globalVariable.getFormat()).getLanguageCode()));
		} catch (NullPointerException npe) {

		}
		setValue(globalVariable);
		return item;
	}

	@Override
	public Item addItem(Object itemId) {
		if (itemId instanceof GlobalVariable) {
			return addItem((GlobalVariable) itemId);
		}
		return null;
	}

	public GlobalVariable getSelectedGlobalVariable() {
		if (getValue() != null) {
			return (GlobalVariable) getValue();
		}
		return null;
	}

	public List<GlobalVariable> getGlobalVariables() {
		List<GlobalVariable> globalVariables = new ArrayList<>();
		for (Object item : getItemIds()) {
			globalVariables.add((GlobalVariable) item);
		}
		return globalVariables;
	}

	@SuppressWarnings("unchecked")
	public void updateItem(GlobalVariable itemEdited) {
		Item item = getItem(itemEdited);
		if (item != null) {
			item.getItemProperty(Properties.VARIABLE_NAME).setValue(itemEdited.getName());
			item.getItemProperty(Properties.VARIABLE_TYPE).setValue(ServerTranslate
					.translate(AnswerFormatUi.getFromAnswerFormat(itemEdited.getFormat()).getLanguageCode()));
		}
	}
}

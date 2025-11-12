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
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.utils.date.DateManager;
import com.vaadin.data.Item;
import com.vaadin.ui.Table;

public class SelectExpressionTable extends Table {
	private static final long serialVersionUID = 3348987098295904893L;

	enum MenuProperties {
		EXPRESSION_NAME, UPDATE_TIME;
	};

	public SelectExpressionTable() {
		initContainerProperties();
	}

	private void initContainerProperties() {
		setSelectable(true);
		setImmediate(true);
		setMultiSelect(false);
		setSizeFull();

		addContainerProperty(MenuProperties.EXPRESSION_NAME, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_EXPRESSIONS_TABLE_COLUMN_NAME), null, Align.LEFT);

		addContainerProperty(MenuProperties.UPDATE_TIME, String.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_EXPRESSIONS_TABLE_COLUMN_UPDATE), null, Align.LEFT);

		setColumnCollapsingAllowed(true);
		setColumnCollapsible(MenuProperties.EXPRESSION_NAME, false);
		setColumnCollapsible(MenuProperties.UPDATE_TIME, true);
		setColumnCollapsed(MenuProperties.UPDATE_TIME, true);

		this.setColumnExpandRatio(MenuProperties.EXPRESSION_NAME, 1);
		this.setColumnExpandRatio(MenuProperties.UPDATE_TIME, 1);

		setSortContainerPropertyId(MenuProperties.EXPRESSION_NAME);
		setSortAscending(true);
	}

	@SuppressWarnings({ "unchecked" })
	public void addRow(ExpressionChain expression) {
		Item item = addItem(expression);
		item.getItemProperty(MenuProperties.EXPRESSION_NAME).setValue(expression.getName());
		item.getItemProperty(MenuProperties.UPDATE_TIME).setValue(
				DateManager.convertDateToStringWithHours(expression.getUpdateTime()));
	}

	public void removeSelectedRow() {
		Expression expression = (Expression) getValue();
		if (expression != null) {
			removeItem(expression);
		}
	}

	public void update(Form form) {
		this.removeAllItems();
		for (ExpressionChain expression : form.getExpressionChains()) {
			addRow(expression);
		}
	}

	public ExpressionChain getSelectedExpression() {
		return (ExpressionChain) getValue();
	}

	public void setSelectedExpression(ExpressionChain expression) {
		setValue(expression);
	}
}

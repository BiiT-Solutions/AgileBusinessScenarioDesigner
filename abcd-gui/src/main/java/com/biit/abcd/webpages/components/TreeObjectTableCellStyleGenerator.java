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

import com.biit.abcd.webpages.components.TreeObjectTable.TreeObjectTableProperties;
import com.biit.form.entity.BaseAnswer;
import com.biit.form.entity.BaseCategory;
import com.biit.form.entity.BaseForm;
import com.biit.form.entity.BaseQuestion;
import com.biit.form.entity.BaseRepeatableGroup;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.CellStyleGenerator;

/**
 * Cell Style generator for FormTreeTable. Adds style for column name in each
 * row depending on the itemId.
 *
 */
public class TreeObjectTableCellStyleGenerator implements CellStyleGenerator {
	private static final long serialVersionUID = 2280270027613134315L;

	@Override
	public String getStyle(Table source, Object itemId, Object propertyId) {
		if (propertyId == TreeObjectTableProperties.ELEMENT_NAME) {
			if (itemId instanceof BaseForm) {
				return "tree-cell-form";
			}
			if (itemId instanceof BaseCategory) {
				return "tree-cell-category";
			}
			if (itemId instanceof BaseRepeatableGroup) {
				return "tree-cell-group";
			}
			if (itemId instanceof BaseQuestion) {
				return "tree-cell-question";
			}
			if (itemId instanceof BaseAnswer) {
				return "tree-cell-answer";
			}
		}
		return "";
	}
}

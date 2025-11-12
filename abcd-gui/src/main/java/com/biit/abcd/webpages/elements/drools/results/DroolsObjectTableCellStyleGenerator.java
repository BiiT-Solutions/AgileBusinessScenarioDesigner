package com.biit.abcd.webpages.elements.drools.results;

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

import com.biit.abcd.webpages.elements.drools.results.DroolsTreeObjectTable.DroolsTreeObjectTableProperties;
import com.biit.drools.form.DroolsSubmittedCategory;
import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.drools.form.DroolsSubmittedGroup;
import com.biit.drools.form.DroolsSubmittedQuestion;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.CellStyleGenerator;

/**
 * Cell Style generator for FormTreeTable. Adds style for column name in each
 * row depending on the itemId.
 *
 */
public class DroolsObjectTableCellStyleGenerator implements CellStyleGenerator {
	private static final long serialVersionUID = 2280270027613134315L;

	@Override
	public String getStyle(Table source, Object itemId, Object propertyId) {
		if (propertyId == DroolsTreeObjectTableProperties.ELEMENT_NAME) {
			if (itemId instanceof DroolsSubmittedForm) {
				return "tree-cell-form";
			}
			if (itemId instanceof DroolsSubmittedCategory) {
				return "tree-cell-category";
			}
			if (itemId instanceof DroolsSubmittedGroup) {
				return "tree-cell-group";
			}
			if (itemId instanceof DroolsSubmittedQuestion) {
				return "tree-cell-question";
			}
		}
		return "";
	}
}

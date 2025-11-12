package com.biit.abcd.webpages.elements.decisiontable;

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

import java.util.List;

import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.webpages.components.TreeObjectTableCellStyleGenerator;
import com.vaadin.ui.Table;

public class FormQuestionTableCellGenerator extends TreeObjectTableCellStyleGenerator {
	private static final long serialVersionUID = -5325256862646100074L;

	@Override
	public String getStyle(Table source, Object itemId, Object propertyId) {
		FormQuestionTable formQuestionTable = (FormQuestionTable) source;
		List<Question> disabledQuestions = formQuestionTable.getDisabledQuestions();

		if (formQuestionTable.isElementFiltered(itemId)) {
			return super.getStyle(source, itemId, propertyId)+" filtered";
		} else {
			if (disabledQuestions.contains(itemId)) {
				return "cell-disabled";
			} else {
				return super.getStyle(source, itemId, propertyId);
			}
		}
	}
}

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
import com.biit.form.entity.TreeObject;

/**
 * This object is a variation of TreeObjectTable that can use multiselect. The icon is removed due to is not possible to
 * use component inside the table and multiselect with shift key together.
 */
public class TreeObjectTableMultiSelect extends TreeObjectTable {
	private static final long serialVersionUID = -820060659335684751L;

	public TreeObjectTableMultiSelect() {
		super();
		this.setMultiSelect(true);
	}

	@Override
	protected void initContainerProperties() {
		addContainerProperty(TreeObjectTableProperties.ELEMENT_NAME, String.class, null,
				ServerTranslate.translate(LanguageCodes.FORM_TREE_PROPERTY_NAME), null, Align.LEFT);
		setCellStyleGenerator(new TreeObjectTableCellStyleGenerator());
	}

	/**
	 * Return a simple string with the element name.
	 */
	@Override
	protected Object createElementWithIcon(final TreeObject element) {
		return element.toString();
	}
}

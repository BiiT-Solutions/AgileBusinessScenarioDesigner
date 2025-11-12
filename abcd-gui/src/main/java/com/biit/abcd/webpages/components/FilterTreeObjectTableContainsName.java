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

import com.biit.form.entity.TreeObject;
import com.vaadin.data.Item;

/**
 * Filter for TableTreeObject. Does a contains of the ElementName.
 *
 */
public class FilterTreeObjectTableContainsName implements IFilterContainsText {
	private static final long serialVersionUID = 852831365303680293L;
	private String text;

	@Override
	public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
		return checkIfTreeObjectPasses((TreeObject) itemId);
	}

	@Override
	public boolean appliesToProperty(Object propertyId) {
		if (text == null || text.isEmpty()) {
			return true;
		}
		if (TableTreeObjectLabel.TreeObjectTableProperties.ELEMENT_NAME.equals(propertyId)) {
			return true;
		}
		return false;
	}

	protected boolean checkNameWithFilter(TreeObject element) {
		String name = element.getName().toLowerCase();

		if (name.contains(getFilterText())) {
			return true;
		}
		return false;
	}

	public boolean checkIfTreeObjectPasses(TreeObject element) {
		if (checkNameWithFilter(element)) {
			return true;
		}

		if (element.getParent() == null) {
			return false;
		} else {
			return checkIfTreeObjectPasses(element.getParent());
		}
	}

	@Override
	public void setFilterText(String text) {
		this.text = text.toLowerCase();
	}

	public String getFilterText() {
		return text;
	}

}

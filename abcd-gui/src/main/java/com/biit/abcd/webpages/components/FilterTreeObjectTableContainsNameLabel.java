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
 * Filter for TreeObjectTable. Does a contains of the Name (super) and the label
 * properties.
 *
 */
public class FilterTreeObjectTableContainsNameLabel extends FilterTreeObjectTableContainsName {
	private static final long serialVersionUID = 3867634034792955643L;

	@Override
	public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
		return checkIfTreeObjectPasses((TreeObject) itemId);
	}

	@Override
	public boolean appliesToProperty(Object propertyId) {
		if (TableTreeObjectLabel.TreeObjectTableDesignerProperties.ELEMENT_LABEL.equals(propertyId)) {
			return true;
		}
		return super.appliesToProperty(propertyId);
	}

	@Override
	public boolean checkIfTreeObjectPasses(TreeObject element) {
		if (checkNameWithFilter(element)) {
			return true;
		}

		String label = element.getLabel().toLowerCase();
		if (label.contains(getFilterText())) {
			return true;
		}

		if (element.getParent() == null) {
			return false;
		} else {
			return checkIfTreeObjectPasses(element.getParent());
		}
	}
}

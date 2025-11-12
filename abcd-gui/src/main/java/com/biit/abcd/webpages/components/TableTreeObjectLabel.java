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
 * Table tree object with a label property added.
 *
 */
public class TableTreeObjectLabel extends TreeObjectTable {
	private static final long serialVersionUID = 2882643672843469056L;
	private static final int LABEL_MAX_LENGTH = 50;

	public enum TreeObjectTableDesignerProperties {
		ELEMENT_LABEL
	};

	@Override
	protected void initContainerProperties() {
		super.initContainerProperties();
		addContainerProperty(TreeObjectTableDesignerProperties.ELEMENT_LABEL, String.class, null, "", null, Align.LEFT);
	}

	@Override
	protected void setValuesToItem(Item item, TreeObject element) {
		super.setValuesToItem(item, element);
		String label = element.getLabel();
		if (label == null) {
			label = new String();
		}
		setLabelToItem(item, label);
	}

	/**
	 * Sets label to item and cuts label to max length.
	 *
	 * @param item
	 * @param label
	 */
	@SuppressWarnings("unchecked")
	protected void setLabelToItem(Item item, String label) {
		if (label.length() > LABEL_MAX_LENGTH) {
			label = label.substring(0, LABEL_MAX_LENGTH - 1);
			label += "...";
		}
		item.getItemProperty(TreeObjectTableDesignerProperties.ELEMENT_LABEL).setValue(label);
	}

	/**
	 * Expands the tree until treeObject
	 *
	 * @param treeObject
	 *            the tree object.
	 */
	public void expand(TreeObject treeObject) {
		if (treeObject.getParent() != null) {
			expand(treeObject.getParent());
		}
		setCollapsed(treeObject, false);
	}
}

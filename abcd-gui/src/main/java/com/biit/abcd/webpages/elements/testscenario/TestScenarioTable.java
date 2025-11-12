package com.biit.abcd.webpages.elements.testscenario;

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

import com.biit.abcd.persistence.entity.testscenarios.TestScenarioForm;
import com.biit.abcd.webpages.components.TreeObjectTable;
import com.biit.form.entity.BaseQuestion;
import com.biit.form.entity.BaseRepeatableGroup;
import com.biit.form.entity.TreeObject;

/**
 * TreeObjectTable component
 *
 * This is a customized component to represent a TreeObject in a tree table.
 *
 */
public class TestScenarioTable extends TreeObjectTable {

	private static final long serialVersionUID = 4172158838292855446L;
	private TreeObject rootElement;

	@Override
	public void setRootElement(TreeObject root) {
		rootElement = root;
		super.setRootElement(root);
	}

	@Override
	public void addItem(TreeObject element, TreeObject parent, boolean selectRow) {
		// Not representing the groups, questions and answers
		if (element != null && !(element instanceof BaseRepeatableGroup) && !(element instanceof BaseQuestion)) {
			super.addItem(element, parent, false);
		}
	}

	public TreeObject getRootElement() {
		return rootElement;
	}
	
	public static String getItemName(TreeObject element) {
		String name = null;
		if (element instanceof TestScenarioForm) {
			name = element.getLabel();
		} else {
			name = element.getName();
		}
		if (name == null) {
			throw new UnsupportedOperationException(TreeObject.class.getName() + " subtype unknown.");
		}
		return name;
	}

}

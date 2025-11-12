package com.biit.abcd.gui.test.window;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (Test)
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

import com.vaadin.testbench.elements.ListSelectElement;
import com.vaadin.testbench.elements.TreeTableElement;
import com.vaadin.testbench.elements.WindowElement;

public class SelectAVariable extends AcceptCancelWindow {

	private static final String WINDOW_CAPTION = "Select Form Variables";
	private static final String SELECTION_CAPTION = "Select a Variable:";
	private static final String ELEMENT_TABLE = "Select an Element:";
	private static final String CLASS_NAME = "com.biit.abcd.webpages.elements.expression.viewer.SelectFormElementVariableWindow";

	public void selectVariableAndAcceptElement(String name) {
		waitToShow();
		getSelectionList().selectByText(name);
		clickAccept();
	}

	private ListSelectElement getSelectionList() {
		return $$(WindowElement.class).caption(WINDOW_CAPTION).$(ListSelectElement.class).caption(SELECTION_CAPTION)
				.first();
	}

	public void selectElement(int row) {
		getTable().getCell(row, 0).click();
	}

	private TreeTableElement getTable() {
		return $$(WindowElement.class).caption(WINDOW_CAPTION).$(TreeTableElement.class).caption(ELEMENT_TABLE).first();
	}

	public void toggleElement(int row) {
		getTable().getRow(row).toggleExpanded();
	}

	@Override
	protected String getWindowId() {
		return CLASS_NAME;
	}

}

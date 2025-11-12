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

public class SelectGenericVariable extends AcceptCancelWindow {

	private static final String GENERIC_ELEMENT_TABLE = "Select a Generic Element:";
	private static final String SELECT_GENERIC_VARIABLE = "Select a Generic Variable:";
	private static final String WINDOW_ID = "com.biit.abcd.webpages.elements.expression.viewer.SelectFormGenericVariablesWindow";

	public void selectAndAccept(int row, String variable) {
		selectGenericElement(row);
		selectGenericVariable(variable);
		clickAccept();
	}

	private void selectGenericVariable(String variable) {
		getGenericVariableTable().selectByText(variable);
		getGenericVariableTable().waitForVaadin();
	}

	private ListSelectElement getGenericVariableTable() {
		return getWindow().$(ListSelectElement.class).caption(SELECT_GENERIC_VARIABLE).first();
	}

	private void selectGenericElement(int row) {
		getGenericElementTable().getCell(row, 0);
		getGenericElementTable().waitForVaadin();
	}

	private TreeTableElement getGenericElementTable() {
		return getWindow().$(TreeTableElement.class).caption(GENERIC_ELEMENT_TABLE).first();
	}

	@Override
	protected String getWindowId() {
		return WINDOW_ID;
	}

}

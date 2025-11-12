package com.biit.abcd.gui.test.webpage;

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

import com.vaadin.testbench.ElementQuery;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.TableElement;

public abstract class LeftTreeTableWebpage extends AbcdCommonWebpage {

	private static final String BUTTON_NEW = "New";
	private static final String BUTTON_REMOVE = "Remove";

	public LeftTreeTableWebpage() {
		super();
	}

	public ButtonElement getNewButton() {
		ElementQuery<ButtonElement> button = $(ButtonElement.class).caption(BUTTON_NEW);
		if (button.exists()) {
			return button.first();
		} else {
			return null;
		}
	}
	
	public ButtonElement getRemoveButton() {
		ElementQuery<ButtonElement> button = $(ButtonElement.class).caption(BUTTON_REMOVE);
		if (button.exists()) {
			return button.first();
		} else {
			return null;
		}
	}

	public abstract TableElement getTable();

	protected abstract String getTableId();

	public void selectRow(int row) {
		TableElement diagramTable = getTable();
		diagramTable.getCell(row, 0).click();
		diagramTable.getCell(row, 0).waitForVaadin();
	}

	public boolean isRowSelected(int diagramRow) {
		return getTable().getRow(diagramRow).getAttribute(CSS_CLASS).contains(TABLE_ROW_SELECTED);
	}

}

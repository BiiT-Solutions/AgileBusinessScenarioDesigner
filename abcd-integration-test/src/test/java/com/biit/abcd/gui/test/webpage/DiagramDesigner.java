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

import com.biit.abcd.gui.test.window.NewDiagramWindow;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.TableElement;
import com.vaadin.testbench.elements.TreeTableElement;

public class DiagramDesigner extends LeftTreeTableWebpage {

	private static final String DIAGRAM_TABLE_ID = "diagram-table";
	private NewDiagramWindow newDiagramWindow;
	private static final String SAVE_BUTTON = "Save";

	public DiagramDesigner() {
		super();
		newDiagramWindow = new NewDiagramWindow();
		addWindow(newDiagramWindow);
	}
	
	public void newDiagram(String name){
		getNewButton().click();
		newDiagramWindow.setNameAndAccept(name);
	}
	
	public void removeDiagram(int row){
		selectRow(row);
		getRemoveButton().click();
		getProceedWindow().clickAccept();
	}

	@Override
	protected String getTableId() {
		return DIAGRAM_TABLE_ID;
	}

	@Override
	public TableElement getTable() {
		TreeTableElement query = $(TreeTableElement.class).id(getTableId());
		return query;
	}

	public void save() {
		$(ButtonElement.class).caption(SAVE_BUTTON).first().click();
	}
}

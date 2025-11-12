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

public class AddNewConditionWindow extends AcceptCancelWindow{
	
	private static final String CLASSNAME = "com.biit.abcd.webpages.elements.decisiontable.AddNewQuestionEditorWindow";
	private static final String SELECT_AN_ELEMENT_CAPTION = "Select an Element:";
	private static final String SELECT_A_VARIABLE_CAPTION = "Select a Variable:";

	@Override
	protected String getWindowId() {
		return CLASSNAME;
	}
	
	public void selectAnElement(int row){
		getSelectAnElement().getCell(row, 0).click();
		getSelectAnElement().getCell(row, 0).waitForVaadin();
		clickAccept();
	}
	
	public void selectAVariable(int row, String name){
		getSelectAnElement().getCell(row, 0).click();
		getSelectAnElement().getCell(row, 0).waitForVaadin();
		getSelectAVariable().selectByText(name);
		getSelectAVariable().waitForVaadin();
		clickAccept();
	}
	
	public TreeTableElement getSelectAnElement(){
		 return getWindow().$(TreeTableElement.class).caption(SELECT_AN_ELEMENT_CAPTION).first();
	}
	
	public ListSelectElement getSelectAVariable(){
		return getWindow().$(ListSelectElement.class).caption(SELECT_A_VARIABLE_CAPTION).first();
	}

	public void expandElement(int row) {
		getSelectAnElement().getRow(row).toggleExpanded();
	}

}

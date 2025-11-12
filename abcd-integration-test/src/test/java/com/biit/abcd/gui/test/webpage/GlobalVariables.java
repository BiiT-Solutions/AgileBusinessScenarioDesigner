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

import com.biit.abcd.gui.test.webpage.FormDesigner.AnswerFormat;
import com.biit.abcd.gui.test.window.AddNewValueWindow;
import com.biit.abcd.gui.test.window.NewVariableWindow;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.TableElement;

public class GlobalVariables extends AbcdCommonWebpage {

	private static final String EDIT_VALUE_BUTTON = "Edit ";
	private static final String ADD_VALUE_BUTTON = "Add ";
	private static final String ADD_VARIABLE_BUTTON = "Add ";
	private static final String REMOVE_VARIABLE_BUTTON = "Remove";
	private static final String EDIT_VARIABLE_BUTTON = "Edit ";
	private static final String REMOVE_VALUE_BUTTON = "Remove ";
	private final NewVariableWindow newVariableWindow;
	private final AddNewValueWindow addNewValueWindow;

	public GlobalVariables() {
		super();
		newVariableWindow = new NewVariableWindow();
		addNewValueWindow = new AddNewValueWindow();
		addWindow(newVariableWindow);
		addWindow(addNewValueWindow);
	}

	public void createGlobalVariable(String name, AnswerFormat format,String value,String validFrom, String validTo) {
		clickAddVariableButton();
		getNewVariableWindow().setName(name);
		getNewVariableWindow().setType(format);
		getNewVariableWindow().clickAccept();
		
		getAddNewValueWindow().waitToShow();
		getAddNewValueWindow().setValue(value);
		getAddNewValueWindow().setValidFrom(validFrom);
		if(validTo!=null){
			getAddNewValueWindow().setValidTo(validTo);
		}
		getAddNewValueWindow().clickAccept();
	}
	
	public void clickAddNewValue() {
		getAddValueButton().click();
		getAddValueButton().waitForVaadin();
	}
	
	public void clickRow(int row){
		$(TableElement.class).first().getCell(row, 0).click();
	}
	
	public void editVariable(){
		getEditVariableButton().click();
		getEditVariableButton().waitForVaadin();
	}
	
	public ButtonElement getRemoveVariableButton(){
		return $(ButtonElement.class).caption(REMOVE_VARIABLE_BUTTON).first();
	}

	private ButtonElement getAddVariableButton() {
		return $(ButtonElement.class).caption(ADD_VARIABLE_BUTTON).first();
	}
	
	private ButtonElement getAddValueButton(){
		return $(ButtonElement.class).caption(ADD_VALUE_BUTTON).get(1);
	}

	public ButtonElement getEditValueButton() {
		return $(ButtonElement.class).caption(EDIT_VALUE_BUTTON).get(1);
	}
	
	private ButtonElement getEditVariableButton(){
		return $(ButtonElement.class).caption(EDIT_VARIABLE_BUTTON).first();
	}

	private void clickAddVariableButton() {
		getAddVariableButton().click();
		getAddVariableButton().waitForVaadin();
	}
	
	public void addNewValue(String value, String validFrom, String validTo){
		clickAddNewValue();	
		getAddNewValueWindow().waitToShow();
		getAddNewValueWindow().setValue(value);
		if(validFrom!=null){
			getAddNewValueWindow().setValidFrom(validFrom);
		}
		if(validTo!=null){
			getAddNewValueWindow().setValidTo(validTo);
		}
		getAddNewValueWindow().clickAccept();
	}

	public NewVariableWindow getNewVariableWindow() {
		return newVariableWindow;
	}
	
	public AddNewValueWindow getAddNewValueWindow() {
		return addNewValueWindow;
	}

	public void removeVariable() {
		getRemoveVariableButton().click();
		getRemoveVariableButton().waitForVaadin();
	}

	public void clickEditValue() {
		getEditValueButton().click();
		getEditValueButton().waitForVaadin();
	}
	
	public TableElement getValueTable(){
		return $(TableElement.class).get(1);
	}

	public void selectValue(int row) {
		getValueTable().getCell(row, 0).click();
	}
	
	public ButtonElement getRemoveValueButton(){
		return $(ButtonElement.class).caption(REMOVE_VALUE_BUTTON).first();
	}

	public void clickRemoveValue() {
		 getRemoveValueButton().click();
		 getRemoveValueButton().waitForVaadin();
	}
}

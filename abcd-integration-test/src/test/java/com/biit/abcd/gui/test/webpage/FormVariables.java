package com.biit.abcd.gui.test.webpage;

import org.openqa.selenium.By;

import com.biit.abcd.gui.test.StatusPreservingTests.Scope;
import com.biit.abcd.gui.test.webpage.FormDesigner.AnswerFormat;
import com.vaadin.testbench.ElementQuery;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.ComboBoxElement;
import com.vaadin.testbench.elements.TableElement;
import com.vaadin.testbench.elements.TableRowElement;
import com.vaadin.testbench.elements.TextFieldElement;

public class FormVariables extends AbcdCommonWebpage{

	private static final String ADD_VARIABLE_CAPTION = "Add";	
	private static final String BUTTON_REMOVE = "Remove";
	private static final String SAVE_BUTTON = "Save";

	public void clickAddVariable(){
		 ButtonElement button = $(ButtonElement.class).caption(ADD_VARIABLE_CAPTION).first();
		 button.click();
		 button.waitForVaadin();
	}
	
	public TextFieldElement getTextField(int row, int pos){
		TableElement table = $(TableElement.class).first();
		TableRowElement rowElement = table.getRow(row);
		TestBenchElement cellElement = rowElement.getCell(pos);
		TextFieldElement textField = ((TestBenchElement)cellElement.findElement(By.tagName("input"))).wrap(TextFieldElement.class);
		return textField;
	}
	
	public ComboBoxElement getComboBoxElement(int row, int pos){
		TableElement table = $(TableElement.class).first();
		ComboBoxElement comboBox = ((TestBenchElement)table.getCell(row, pos).findElement(By.className("v-widget"))).wrap(ComboBoxElement.class);
		return comboBox;
	}
	
	public void addVariable(){
		clickAddVariable();
	}
	
	public void addVariable(int pos, String name, AnswerFormat format, Scope scope, String value){
		addVariable();
		getTextField(pos, 0).setValue(name);
		getComboBoxElement(pos, 1).selectByText(format.getValue());
		getComboBoxElement(pos, 2).selectByText(scope.getValue());
		if(value!=null){
			getTextField(pos, 3).setValue(value);
		}
		
	}
	
	public void save() {
		$(ButtonElement.class).caption(SAVE_BUTTON).first().click();
	}

	public void removeVariable(int row) {
		$(TableElement.class).first().getRow(row).click();
//		$(TableElement.class).first().getCell(row, 0).click();
		$(TableElement.class).first().waitForVaadin();
		getRemoveButton().click();
		getRemoveButton().waitForVaadin();
	}
	
	public ButtonElement getRemoveButton() {
		ElementQuery<ButtonElement> button = $(ButtonElement.class).caption(BUTTON_REMOVE);
		if (button.exists()) {
			return button.first();
		} else {
			return null;
		}
	}
}

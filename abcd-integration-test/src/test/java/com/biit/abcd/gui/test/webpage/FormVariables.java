package com.biit.abcd.gui.test.webpage;

import org.openqa.selenium.By;

import com.vaadin.testbench.ElementQuery;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.ComboBoxElement;
import com.vaadin.testbench.elements.TableElement;
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
		TextFieldElement textField = ((TestBenchElement)table.getCell(row, pos).findElement(By.tagName("input"))).wrap(TextFieldElement.class);
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
	
	public void save() {
		$(ButtonElement.class).caption(SAVE_BUTTON).first().click();
	}

	public void removeVariable(int row) {
		$(TableElement.class).first().getCell(row, 0).click();
		$(TableElement.class).first().waitForVaadin();
		getRemoveButton().click();
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

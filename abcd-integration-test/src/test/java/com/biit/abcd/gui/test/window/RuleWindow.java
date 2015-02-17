package com.biit.abcd.gui.test.window;

import com.biit.abcd.gui.test.webpage.FormDesigner.AnswerFormat;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.ListSelectElement;
import com.vaadin.testbench.elements.TabSheetElement;
import com.vaadin.testbench.elements.TreeTableElement;

public class RuleWindow extends AcceptCancelWindow {

	private static final String CLASSNAME = "com.biit.abcd.webpages.elements.decisiontable.AddNewActionExpressionWindow";
	private static final String SELECT_AN_ELEMENT_CAPTION = "Select an Element:";
	private static final String SELECT_VARIABLE = "Select a Variable:";
	private static final String ADD_VARIABLE_BUTTON_CAPTION = "Add Variable";
	private static final String ADD_ELEMENT_BUTTON = "Add Element";
	private static final String ASSIGN_CAPTION = "=";
	private static final String INPUT_VALUE_CAPTION = "Input Value";
	
	private final StringInputWindow stringInputWindow;
	
	public RuleWindow() {
		super();
		stringInputWindow = new StringInputWindow();
		addWindow(stringInputWindow);
	}

	@Override
	protected String getWindowId() {
		return CLASSNAME;
	}

	public TreeTableElement getSelectAnElement() {
		return getWindow().$(TreeTableElement.class).caption(SELECT_AN_ELEMENT_CAPTION).first();
	}

	public void selectAnElement(int row) {
		getSelectAnElement().getCell(row, 0).click();
		getSelectAnElement().getCell(row, 0).waitForVaadin();
	}

	public void expandAnElement(int row) {
		getSelectAnElement().getRow(row).toggleExpanded();
		getSelectAnElement().getRow(row).waitForVaadin();
	}

	public void addVariable(int row, String name) {
		selectAnElement(row);
		getSelectionVariable().selectByText(name);
		getSelectionVariable().waitForVaadin();
		getAddVariableButton().click();
		getAddVariableButton().waitForVaadin();
	}

	private TestBenchElement getAddVariableButton() {
		return getWindow().$(ButtonElement.class).caption(ADD_VARIABLE_BUTTON_CAPTION).first();
	}

	private ListSelectElement getSelectionVariable() {
		return getWindow().$(ListSelectElement.class).caption(SELECT_VARIABLE).first();
	}

	public void addElement(int row) {
		selectAnElement(row);
		getAddElementButton().click();
		getAddElementButton().waitForVaadin();
	}

	private TestBenchElement getAddElementButton() {
		return $(ButtonElement.class).caption(ADD_ELEMENT_BUTTON).first();
	}

	public void changeTab(int tab) {
		getWindow().$(TabSheetElement.class).first().openTab(tab);
		getWindow().$(TabSheetElement.class).first().waitForVaadin();
	}

	public void clickAssign() {
		clickButtonOnWindow(ASSIGN_CAPTION);
	}

	public void clickInputValue() {
		clickButtonOnWindow(INPUT_VALUE_CAPTION);
	}
	
	public void addInputValue(AnswerFormat answerFormat, String value){
		clickInputValue();
		getStringInputWindow().setType(answerFormat);
		getStringInputWindow().setValue(value);
		getStringInputWindow().clickAccept();
	}

	public void clickButtonOnWindow(String caption) {
		getWindow().$(ButtonElement.class).caption(caption).first().click();
		getWindow().$(ButtonElement.class).caption(caption).first().waitForVaadin();
	}
	
	public StringInputWindow getStringInputWindow() {
		return stringInputWindow;
	}

}

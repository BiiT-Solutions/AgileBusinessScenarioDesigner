package com.biit.abcd.gui.test.window;

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

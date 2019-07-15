package com.biit.abcd.gui.test.window;

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

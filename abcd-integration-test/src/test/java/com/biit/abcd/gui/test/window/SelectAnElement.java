package com.biit.abcd.gui.test.window;

import com.vaadin.testbench.elements.TreeTableElement;

public class SelectAnElement extends AcceptCancelWindow {

	private static final String CLASS_NAME = "com.biit.abcd.webpages.components.SelectTreeObjectWindow";

	public TreeTableElement getTable() {
		return getWindow().$(TreeTableElement.class).first();
	}

	public void selectElement(int row) {
		getTable().getCell(row, 0).click();
	}

	public void selectAndAcceptElement(int row) {
		waitToShow();
		selectElement(row);
		clickAccept();
	}

	@Override
	protected String getWindowId() {
		return CLASS_NAME;
	}

}

package com.biit.abcd.gui.test.window;

import com.vaadin.testbench.elements.TreeTableElement;
import com.vaadin.testbench.elements.WindowElement;

public class SelectAnElement extends AcceptCancelWindow {

	private static final String WINDOW_CAPTION = "Select an Element.";

	public void waitWindowOpen() {
		while (true) {
			try {
				if ($(WindowElement.class).caption(WINDOW_CAPTION).exists()) {
					return;
				}
			} catch (Exception e) {

			}
		}
	}

	public TreeTableElement getTable() {
		return $(WindowElement.class).caption(WINDOW_CAPTION).$(TreeTableElement.class).first();
	}

	public void selectElement(int row) {
		getTable().getCell(row, 0).click();
	}

	public void selectAndAcceptElement(int row) {
		waitWindowOpen();
		selectElement(row);
		clickAccept();
	}

}

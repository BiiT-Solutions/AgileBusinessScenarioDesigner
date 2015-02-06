package com.biit.abcd.gui.test.window;

import com.vaadin.testbench.elements.ListSelectElement;
import com.vaadin.testbench.elements.TreeTableElement;
import com.vaadin.testbench.elements.WindowElement;

public class SelectAVariable extends AcceptCancelWindow {

	private static final String WINDOW_CAPTION = "Select Form Variables";
	private static final String SELECTION_CAPTION = "Select a Variable:";
	private static final String ELEMENT_TABLE = "Select an Element:";

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

	public void selectVariableAndAcceptElement(String name) {
		waitWindowOpen();
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

}

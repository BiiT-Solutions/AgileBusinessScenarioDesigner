package com.biit.abcd.gui.test.window;

import com.vaadin.testbench.ElementQuery;
import com.vaadin.testbench.elements.LabelElement;
import com.vaadin.testbench.elements.WindowElement;

public class WarningUnsavedData extends AcceptCancelWindow {

	private static final Object WARNING_LABEL = "Warning! Any unsaved data will be lost.";

	public boolean isVisible() {
		ElementQuery<LabelElement> label = $$(WindowElement.class).$(LabelElement.class);
		if (label.exists()) {
			LabelElement labelElement = label.first();
			if (labelElement.getText().equals(WARNING_LABEL)) {
				return true;
			}
		}
		return false;
	}

}

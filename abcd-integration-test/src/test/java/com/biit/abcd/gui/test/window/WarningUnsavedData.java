package com.biit.abcd.gui.test.window;

import com.vaadin.testbench.ElementQuery;
import com.vaadin.testbench.elements.LabelElement;

public class WarningUnsavedData extends AcceptCancelWindow {

	private static final Object WARNING_LABEL = "Warning! Any unsaved data will be lost.";
	private static final String CLASS_NAME = "com.biit.abcd.webpages.components.AlertMessageWindow";

	public boolean isVisible() {
		if(getWindow()==null){
			return false;
		}
		
		ElementQuery<LabelElement> label = getWindow().$(LabelElement.class);
		if (label.exists()) {
			LabelElement labelElement = label.first();
			if (labelElement.getText().equals(WARNING_LABEL)) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected String getWindowId() {
		return CLASS_NAME;
	}

}

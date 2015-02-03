package com.biit.abcd.gui.test.window;

import com.biit.gui.tester.VaadinGuiWindow;
import com.vaadin.testbench.ElementQuery;
import com.vaadin.testbench.elements.ButtonElement;

public class AcceptCancelWindow extends VaadinGuiWindow {

	private static final String ACCEPT_BUTTON_CAPTION = "Accept";
	
	public String getAcceptCaption(){
		return ACCEPT_BUTTON_CAPTION;
	}

	public ButtonElement getAcceptButton() {
		ElementQuery<ButtonElement> accept = $(ButtonElement.class).caption(getAcceptCaption());
		if (accept.exists()) {
			return accept.first();
		}
		return null;
	}

	public void clickAccept() {
		getAcceptButton().click();
	}
}

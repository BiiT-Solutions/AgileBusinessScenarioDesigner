package com.biit.abcd.gui.test.window;

import org.openqa.selenium.NoSuchElementException;

import com.biit.gui.tester.VaadinGuiWindow;
import com.vaadin.testbench.ElementQuery;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.WindowElement;

public abstract class AcceptCancelWindow extends VaadinGuiWindow {

	private static final String ACCEPT_BUTTON_CAPTION = "Accept";
	private static final String CANCEL_BUTTON_CAPTION = "Cancel";

	public String getAcceptCaption() {
		return ACCEPT_BUTTON_CAPTION;
	}

	public String getCancelCaption() {
		return CANCEL_BUTTON_CAPTION;
	}

	public ButtonElement getCancelButton() {
		ElementQuery<ButtonElement> cancel = $(ButtonElement.class).caption(getCancelCaption());
		if (cancel.exists()) {
			return cancel.first();
		}
		return null;
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
		if (getAcceptButton() != null) {
			getAcceptButton().waitForVaadin();
		}
	}

	public void clickCancel() {
		getCancelButton().click();
	}

	public WindowElement getWindow() {
		try {
			return $$(WindowElement.class).id(getWindowId());
		} catch (NoSuchElementException e) {
			return null;
		}
	}

	protected abstract String getWindowId();

	public void waitToShow() {
		while (true) {
			try {
				if (getWindow() != null) {
					return;
				}
			} catch (Exception e) {
				// ignore
			}
		}
	}
}

package com.biit.abcd.gui.test.window;

import com.vaadin.testbench.elements.DateFieldElement;
import com.vaadin.testbench.elements.TextFieldElement;
import com.vaadin.testbench.elements.WindowElement;

public class AddNewValueWindow extends AcceptCancelWindow {

	private static final String VALUE_CAPTION = "Value";
	private static final String VALID_FROM = "Valid from";
	private static final String VALID_TO = "Valid to";

	public void setValue(String value) {
		try {
			$(TextFieldElement.class).caption(VALUE_CAPTION).first().setValue(value);
			$(TextFieldElement.class).caption(VALUE_CAPTION).first().waitForVaadin();
		} catch (Exception e) {
			$(DateFieldElement.class).caption(VALUE_CAPTION).first().setValue(value);
			$(DateFieldElement.class).caption(VALUE_CAPTION).first().waitForVaadin();
		}
	}

	public void setValidFrom(String value) {
		$(DateFieldElement.class).caption(VALID_FROM).first().setValue(value);
	}

	public void setValidTo(String value) {
		$(DateFieldElement.class).caption(VALID_TO).first().setValue(value);
	}

	public void waitToShow() {
		while (true) {
			try {
				$$(WindowElement.class).caption("Add a new value").first().waitForVaadin();
				return;
			} catch (Exception e) {
				// ignore
			}
		}
	}

}

package com.biit.abcd.gui.test.window;

import com.biit.gui.tester.VaadinGuiWindow;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.TextFieldElement;

public class NewTestScenarioWindow extends VaadinGuiWindow {

	public static final String TEXT_FIELD_CAPTION = "Test Scenario Name";
	public static final String ACCEPT_BUTTON_CAPTION = "Accept";

	private TextFieldElement getNewTestScenarioTextField() {
		return $(TextFieldElement.class).caption(TEXT_FIELD_CAPTION).first();
	}

	private ButtonElement getAcceptButton() {
		return $(ButtonElement.class).caption(ACCEPT_BUTTON_CAPTION).first();
	}

	public void createNewTestScenario(String testScenarioName) {
		getNewTestScenarioTextField().setValue(testScenarioName);
		getAcceptButton().click();
	}

}

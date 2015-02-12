package com.biit.abcd.gui.test.window;

import com.biit.abcd.gui.test.webpage.FormDesigner.AnswerFormat;
import com.vaadin.testbench.elements.ComboBoxElement;
import com.vaadin.testbench.elements.TextFieldElement;

public class StringInputWindow extends AcceptCancelWindow {

	private static final String CLASSNAME = "com.biit.abcd.webpages.components.StringInputWindow";

	@Override
	protected String getWindowId() {
		return CLASSNAME;
	}

	public void setType(AnswerFormat answerFormat) {
		getType().selectByText(answerFormat.getValue());
	}

	private ComboBoxElement getType() {
		return getWindow().$(ComboBoxElement.class).first();
	}

	public void setValue(String value) {
		getValue().setValue(value);
	}

	private TextFieldElement getValue() {
		return getWindow().$(TextFieldElement.class).first();
	}

}

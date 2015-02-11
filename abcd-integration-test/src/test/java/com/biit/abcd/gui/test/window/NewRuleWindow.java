package com.biit.abcd.gui.test.window;

public class NewRuleWindow extends TextFieldAcceptCancelWindow{

	private static final String TEXT_FIELD_CAPTION = "Rule Name";

	@Override
	public String getTextFieldCaption() {
		return TEXT_FIELD_CAPTION;
	}

	@Override
	protected String getWindowId() {
		// This window doesn't use id for now.
		return null;
	}

}

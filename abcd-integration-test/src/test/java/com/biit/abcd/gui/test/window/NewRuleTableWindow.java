package com.biit.abcd.gui.test.window;

public class NewRuleTableWindow extends TextFieldAcceptCancelWindow{

	private static final String RULE_TABLE_NAME = "Table Name";

	@Override
	public String getTextFieldCaption() {
		return RULE_TABLE_NAME;
	}

	@Override
	protected String getWindowId() {
		// This window doesn't use id for now.
		return null;
	}

}

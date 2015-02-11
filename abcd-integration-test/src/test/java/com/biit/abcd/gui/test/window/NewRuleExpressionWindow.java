package com.biit.abcd.gui.test.window;

public class NewRuleExpressionWindow extends TextFieldAcceptCancelWindow{

	private static final String EXPRESSION_NAME = "Expression Name";

	@Override
	public String getTextFieldCaption() {
		return EXPRESSION_NAME;
	}

	@Override
	protected String getWindowId() {
		// This window doesn't use id for now.
		return null;
	}

}

package com.biit.abcd.gui.test.window;

public class NewRuleExpressionWindow extends TextFieldAcceptCancelWindow{

	private static final String EXPRESSION_NAME = "Expression Name";
	private static final String CLASSNAME = "com.biit.abcd.webpages.elements.expression.viewer.WindowNewExpression";

	@Override
	public String getTextFieldCaption() {
		return EXPRESSION_NAME;
	}

	@Override
	protected String getWindowId() {
		return CLASSNAME;
	}

}

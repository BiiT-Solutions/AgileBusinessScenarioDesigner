package com.biit.abcd.gui.test.window;

public class NewRuleTableWindow extends TextFieldAcceptCancelWindow{

	private static final String RULE_TABLE_NAME = "Table Name";
	private static final String CLASSNAME = "com.biit.abcd.webpages.elements.decisiontable.WindoNewTable";
	private static final String CREATE_BUTTON_CAPTION = "Accept";
		
	public String getAcceptCaption(){
		return CREATE_BUTTON_CAPTION;
	}

	@Override
	public String getTextFieldCaption() {
		return RULE_TABLE_NAME;
	}

	@Override
	protected String getWindowId() {
		return CLASSNAME;
	}

}

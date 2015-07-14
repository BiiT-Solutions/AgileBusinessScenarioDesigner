package com.biit.abcd.gui.test.window;

public class NewDiagramWindow extends TextFieldAcceptCancelWindow {

	private static final String DIAGRAM_NAME_FIELD_CAPTION = "New Diagram Name:";

	@Override
	public String getTextFieldCaption() {
		return DIAGRAM_NAME_FIELD_CAPTION;
	}

	@Override
	protected String getWindowId() {
		// This window doesn't use accept/cancel id for now.
		return null;
	}
}

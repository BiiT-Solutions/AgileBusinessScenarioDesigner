package com.biit.abcd.gui.test.window;

import com.vaadin.testbench.elements.ListSelectElement;

public class SelectGlobalConstantWindow extends AcceptCancelWindow{

	private static final String CLASSNAME = "com.biit.abcd.webpages.components.SelectGlobalConstantsWindow";

	@Override
	protected String getWindowId() {
		return CLASSNAME;
	}
	
	public void selectAndAcceptGlobalConstant(String name){
		getWindow().$(ListSelectElement.class).first().selectByText(name);
		clickAccept();
	}

}

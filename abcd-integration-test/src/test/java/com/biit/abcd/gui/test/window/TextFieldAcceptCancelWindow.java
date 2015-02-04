package com.biit.abcd.gui.test.window;

import com.vaadin.testbench.ElementQuery;
import com.vaadin.testbench.elements.TextFieldElement;

public abstract class TextFieldAcceptCancelWindow extends AcceptCancelWindow{
	
	private static final String CREATE_BUTTON_CAPTION = "Create";
	
	public abstract String getTextFieldCaption();
	
	public String getAcceptCaption(){
		return CREATE_BUTTON_CAPTION;
	}

	public TextFieldElement getTextField(){
		ElementQuery<TextFieldElement> textField = $(TextFieldElement.class).caption(getTextFieldCaption());
		if(	textField.exists()){
			return textField.first();
		}
		return null;
	}
	
	public TextFieldElement setName(String name){
		takeScreenshot("ABCD_TextFieldAcceptCancelWindowGetName");
		getTextField().setValue(name);
		getTextField().waitForVaadin();
		return getTextField();
	}
	
	public void setNameAndAccept(String name){
		setName(name);
		getAcceptButton().focus();
		clickAccept();
	}
}
package com.biit.abcd.gui.test.window;

import com.vaadin.testbench.ElementQuery;
import com.vaadin.testbench.elements.TextFieldElement;

public abstract class TextFieldAcceptCancelWindow extends AcceptCancelWindow{
	
	public abstract String getTextFieldCaption();
	
	public TextFieldElement getTextField(){
		ElementQuery<TextFieldElement> textField = getWindow().$(TextFieldElement.class).caption(getTextFieldCaption());
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
		clickAccept();
	}
}
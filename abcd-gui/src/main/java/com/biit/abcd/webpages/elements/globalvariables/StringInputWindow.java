package com.biit.abcd.webpages.elements.globalvariables;

import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.vaadin.ui.TextField;

public class StringInputWindow extends AcceptCancelWindow{
	private static final long serialVersionUID = 361486551550136464L;
	
	private TextField textField;

	public StringInputWindow(){
		
		
		
	}
	
	public String getValue(){
		return textField.getValue();
	}
}

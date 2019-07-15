package com.biit.abcd.gui.test.window;

import com.biit.abcd.gui.test.webpage.FormDesigner.AnswerFormat;
import com.vaadin.testbench.elements.ComboBoxElement;
import com.vaadin.testbench.elements.TextFieldElement;

public class NewVariableWindow extends AcceptCancelWindow{

	private static final String NAME_CAPTION = "Name";
	private static final String NAME_TYPE = "Type";
	private static final String CLASS_NAME = "com.biit.abcd.webpages.elements.global.variables.VariableWindow";

	public void setName(String name){
		$(TextFieldElement.class).caption(NAME_CAPTION).first().setValue(name);
		$(TextFieldElement.class).caption(NAME_CAPTION).first().waitForVaadin();
	}
	
	public void setType(AnswerFormat format){
		$(ComboBoxElement.class).caption(NAME_TYPE).first().selectByText(format.getValue());
		$(ComboBoxElement.class).caption(NAME_TYPE).first().waitForVaadin();
	}
	
	public boolean isEnabledType(){
		return $(ComboBoxElement.class).caption(NAME_TYPE).first().isEnabled();
	}

	@Override
	protected String getWindowId() {
		return CLASS_NAME;
	}
	
}

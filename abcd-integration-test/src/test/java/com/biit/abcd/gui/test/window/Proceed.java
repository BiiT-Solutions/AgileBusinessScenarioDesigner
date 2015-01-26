package com.biit.abcd.gui.test.window;

import com.biit.gui.tester.VaadinGuiWindow;
import com.vaadin.testbench.elements.ButtonElement;

public class Proceed extends VaadinGuiWindow{

	public void clickAccept(){
		$(ButtonElement.class).caption("Accept").first().click();
	}
	
}

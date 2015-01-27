package com.biit.abcd.gui.test.window;

import com.biit.gui.tester.VaadinGuiWindow;
import com.vaadin.testbench.elements.ButtonElement;

public class InfoWindow extends VaadinGuiWindow{

	public void clickCloseButton(){
		$(ButtonElement.class).caption("Close").first().click();
	}
	
}

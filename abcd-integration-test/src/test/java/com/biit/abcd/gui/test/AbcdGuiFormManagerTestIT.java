package com.biit.abcd.gui.test;

import org.testng.annotations.Test;

import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.ComboBoxElement;
import com.vaadin.testbench.elements.TextFieldElement;
import com.vaadin.testbench.elements.TreeTableElement;

@Test(groups = "formManager")
public class AbcdGuiFormManagerTestIT extends AbcdGuiTestIT {

	public void createNewForm() {
		// Login into the application
		autoLogin();
		// Create form
		$(ButtonElement.class).caption("New").first().click();
		$(ButtonElement.class).caption("Form").first().click();
		$(TextFieldElement.class).caption("New form name:").first().setValue("testbenchForm");
		$(ComboBoxElement.class).caption("Group:").first().selectByText("Bii1");
		$(ButtonElement.class).caption("Accept").first().click();
		// Remove form
		$(TreeTableElement.class).first().getCell(1, 0).click();
		$(ButtonElement.class).caption("Remove Form").first().click();
		$(ButtonElement.class).caption("Accept").first().click();
		
		logOut();
	}
}

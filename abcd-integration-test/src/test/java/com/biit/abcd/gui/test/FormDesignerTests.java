package com.biit.abcd.gui.test;

import java.util.Date;

import org.testng.annotations.Test;

@Test(groups = "formDesigner")
public class FormDesignerTests extends AbcdTester {

	private static final String NEW_FORM_NAME = "new_form";

	@Test
	public void generateCompleteFormWithDesigner() {
		String formName = NEW_FORM_NAME + "_" + (new Date()).getTime();

		createForm(formName, ABCD_FORM_ADMIN_BIIT1);
		
		generateTestForm();
	
		getFormDesigner().goToFormManager();
		getFormManager().deleteForm();
	}

}

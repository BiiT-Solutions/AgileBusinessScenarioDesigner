package com.biit.abcd.gui.test;

import org.testng.annotations.Test;

@Test(groups = "formDesigner")
public class FormDesignerTests extends AbcdTester {

	private static final String NEW_FORM_NAME = "new_form";
	private static final String NAME_CAT_1 = "category1";
	private static final String NAME_QUESTION = "question";

	@Test
	public void generateCompleteFormWithDesigner() {
		createForm(NEW_FORM_NAME, ABCD_FORM_ADMIN_BIIT1);
		getFormManager().goToDesigner(1);
		// Click on first row to deselect
		getFormDesigner().clickInTableRow(0);
		getFormDesigner().createCategory(0, NAME_CAT_1);
		getFormDesigner().createBlockOfAllKindOfQuestions(1, NAME_QUESTION);
		
		getFormDesigner().save();
	}

}

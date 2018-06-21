package com.biit.abcd.gui.test;

import org.junit.Assert;
import org.testng.annotations.Test;

import com.vaadin.testbench.elements.ComboBoxElement;

@Test(groups = "basicFunctionalityTests")
public class BasicFunctionalityTests extends AbcdTester {

	private final static String FORM_NAME = "test_form";

	@Test
	public void openSettingsInfoScreen() {
		loginRead1();
		getFormManager().openAndCloseInfoScreen();
		getFormManager().logOut();
	}

	@Test
	public void createForm() {
		createForm(FORM_NAME + "_1", ABCD_FORM_EDIT_BIIT1);
		getFormManager().logOut();
		deleteForm(1, ABCD_FORM_ADMIN_BIIT1);
	}

	@Test
	public void attemptToEditFormOfAnotherOrganization() {
		createForm(FORM_NAME+ "_2", ABCD_FORM_EDIT_BIIT1);
		getFormManager().logOut();
		login(ABCD_FORM_EDIT_BIIT2);
		Assert.assertFalse(getFormManager().checkIfRowExists(1));
		getFormManager().logOut();
		deleteForm(1, ABCD_FORM_ADMIN_BIIT1);
		getFormManager().logOut();
	}

	@Test
	public void attemtToEditFormWithReadOnlyPermission() {
		createForm(FORM_NAME+ "_3", ABCD_FORM_EDIT_BIIT1);
		getFormManager().logOut();
		openForm(1, ABCD_READ_BIIT1);
		checkNotificationIsWarning(getNotification());
		getFormManager().logOut();
		deleteForm(1, ABCD_FORM_ADMIN_BIIT1);
		getFormManager().logOut();
	}

	@Test
	public void attemtToEditForm() {
		createForm(FORM_NAME+ "_4", ABCD_FORM_EDIT_BIIT1);
		getFormManager().logOut();
		openForm(1, ABCD_FORM_EDIT_BIIT1);
		// No notification.
		Assert.assertNull(getNotification());
		getFormManager().logOut();
		deleteForm(1, ABCD_FORM_ADMIN_BIIT1);
		getFormManager().logOut();
	}

	@Test
	public void finishForm() {
		createForm(FORM_NAME+ "_5", ABCD_FORM_EDIT_BIIT1);
		getFormManager().goToDesigner(1);
		getFormDesigner().finishDesign();
		$(ComboBoxElement.class).first().getValue().equals("Final Design");
		getFormManager().logOut();
		// clean
		deleteForm(1, ABCD_FORM_ADMIN_BIIT1);
		getFormManager().logOut();
	}

	@Test
	public void changeFormStatus() {
		createForm(FORM_NAME+ "_6", ABCD_FORM_ADMIN_BIIT1);
		getFormManager().goToDesigner(1);
		getFormDesigner().finishDesign();
		$(ComboBoxElement.class).first().selectByText("Design");
		getFormManager().clickAcceptProceed();
		Assert.assertTrue($(ComboBoxElement.class).first().getValue().equals("Design"));
		getFormManager().logOut();
		// clean
		deleteForm(1, ABCD_FORM_ADMIN_BIIT1);
		getFormManager().logOut();
	}

	@Test
	public void attemptToChangeFormStatus() {
		createForm(FORM_NAME+ "_7", ABCD_FORM_EDIT_BIIT1);
		getFormManager().goToDesigner(1);
		getFormDesigner().finishDesign();
		Assert.assertFalse($(ComboBoxElement.class).first().isEnabled());
		getFormManager().logOut();
		// clean
		deleteForm(1, ABCD_FORM_ADMIN_BIIT1);
		getFormManager().logOut();
	}

	@Test
	public void attemptToClearCacheWithRights() {
		login(ABCD_APP_ADMIN_BIIT1);
		getFormManager().clickClearCache();
		getFormManager().logOut();
	}

	@Test()
	public void attemtToClearCacheWithoutRights() {
		login(ABCD_READ_BIIT1);
		Assert.assertNull(getFormManager().getClearCache());
	}
}

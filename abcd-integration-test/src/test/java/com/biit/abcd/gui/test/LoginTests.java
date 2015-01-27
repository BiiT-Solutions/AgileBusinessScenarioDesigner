package com.biit.abcd.gui.test;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = "login")
public class LoginTests extends AbcdTester {

	private static final String BADUSERNAME = "badUserName@bad.com";
	private static final String BAPSSWORD = "badPassword";
	private static final String NEW_FORM_NAME = "new_form_1";

	@Test
	public void testLoginLogout() {
		loginRead1();
		getFormManager().logOut();
	}

	@Test
	public void testLoginFail() {
		login(BADUSERNAME, BAPSSWORD);
		checkNotificationIsError(getNotification());
	}

	@Test
	public void testLoginWithRightsToManageForm(){
		loginFormAdmin1();
		getFormManager().createNewForm(NEW_FORM_NAME);
		getFormManager().deleteForm(1);
		getFormManager().logOut();
	}
	
	@Test
	public void testLoginWithRightsToManageButNotDeleteForm(){
		loginFormEdit1();
		Assert.assertTrue(getFormManager().getNewForm().isEnabled());
		//close New menu
		getFormManager().getNewMenu().click();
		Assert.assertNull(getFormManager().getRemoveForm());
		getFormManager().logOut();
	}

	@Test
	public void testLoginWithoutRightsToManageForm(){
		loginRead1();
		Assert.assertFalse(getFormManager().getNewForm().isEnabled());
		//close New menu
		getFormManager().getNewMenu().click();
		Assert.assertNull(getFormManager().getRemoveForm());
		getFormManager().logOut();
	}
}

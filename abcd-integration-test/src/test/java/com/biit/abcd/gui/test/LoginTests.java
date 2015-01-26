package com.biit.abcd.gui.test;

import org.testng.annotations.Test;

@Test(groups = "login")
public class LoginTests extends AbcdTester {

	private static final String BADUSERNAME = "badUserName@bad.com";
	private static final String BAPSSWORD = "badPassword";
	private static final String NEW_FORM_NAME = "new_form_1";

	@Test
	public void testLoginLogout() {
		mainPage();
		getLoginPage().login(ABCD_READ_BIIT1, USER_PASSWORD);
		getFormManager().logOut();
	}

	@Test
	public void testLoginFail() {
		mainPage();
		getLoginPage().login(BADUSERNAME, BAPSSWORD);
		checkNotificationIsError(getNotification());
	}
	
	@Test
	public void testLoginWithRightsToManageForm(){
		mainPage();
		getLoginPage().login(ABCD_FORM_ADMIN_BIIT1, USER_PASSWORD);
		getFormManager().createNewForm(NEW_FORM_NAME);
		getFormManager().deleteForm(1);
	}

}

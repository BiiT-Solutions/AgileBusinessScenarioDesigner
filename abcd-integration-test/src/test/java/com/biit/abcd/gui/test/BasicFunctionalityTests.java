package com.biit.abcd.gui.test;

import org.testng.annotations.Test;

@Test(groups = "basicFunctionality")
public class BasicFunctionalityTests extends AbcdTester {

	@Test
	public void openSettingsInfoScreen() {
		mainPage();
		getLoginPage().login(ABCD_READ_BIIT1, USER_PASSWORD);
		getFormManager().logOut();
	}
	
}

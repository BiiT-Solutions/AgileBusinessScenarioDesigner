package com.biit.abcd.gui.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.NotificationElement;
import com.vaadin.testbench.elements.PasswordFieldElement;
import com.vaadin.testbench.elements.TextFieldElement;

@Test(groups = "login")
public class AbcdGuiLoginTestIT extends AbcdGuiTestIT {

	@Test
	public void testAbcdLogin() {
		autoLogin();
		logOut();
	}

	@Test
	public void testAbcdLoginError() {
		// Get the page and log in with a fake user
		getDriver().get(DRIVER_URL);
		$(TextFieldElement.class).id("userNameLoginForm").setValue("fake-user@biit-solutions.com");
		$(PasswordFieldElement.class).id("userPassLoginForm").setValue("tacosAreWonderful");
		$(ButtonElement.class).id("loginButton").click();
		// Look up for the error notification
		NotificationElement notification = $(NotificationElement.class).first();
		Assert.assertEquals("Either username or password was wrong.", notification.getCaption());
		Assert.assertEquals("Try again.", notification.getDescription());
		Assert.assertEquals("error", notification.getType());
		notification.close();
	}
}

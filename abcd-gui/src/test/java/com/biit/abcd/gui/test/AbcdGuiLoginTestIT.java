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
		// Get the page and log in
		getDriver().get("http://localhost:9081");
		if (testInJenkins) {
			$(TextFieldElement.class).id("userNameLoginForm").setValue("jenkins-abcd@biit-solutions.com");
			$(PasswordFieldElement.class).id("userPassLoginForm").setValue("jAqDr0r3Agrj");
		} else {
			$(TextFieldElement.class).id("userNameLoginForm").setValue("test@liferay.com");
			$(PasswordFieldElement.class).id("userPassLoginForm").setValue("test");
		}
		$(ButtonElement.class).id("loginButton").click();
		logOut();
	}

	@Test
	public void testAbcdLoginError() {
		// Get the page and log in with a fake user
		getDriver().get("http://localhost:9081");
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

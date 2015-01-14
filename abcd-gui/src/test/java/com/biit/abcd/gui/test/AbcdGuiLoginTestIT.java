package com.biit.abcd.gui.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.vaadin.testbench.TestBenchTestCase;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.PasswordFieldElement;
import com.vaadin.testbench.elements.TextFieldElement;

public class AbcdGuiLoginTestIT extends TestBenchTestCase {

	@Before
	public void setUp() throws Exception {
		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("intl.accept_languages", "en_US");
		setDriver(new FirefoxDriver(profile));
	}

	@Test
	public void testAbcdLogin() {
		// Get the page and log in
		getDriver().get("http://localhost:9081");
		$(TextFieldElement.class).id("userNameLoginForm").setValue("test@liferay.com");
		$(PasswordFieldElement.class).id("userPassLoginForm").setValue("test");
		$(ButtonElement.class).caption("Sign In").first().click();
	}

	@After
	public void tearDown() throws Exception {
		getDriver().quit();
	}
}

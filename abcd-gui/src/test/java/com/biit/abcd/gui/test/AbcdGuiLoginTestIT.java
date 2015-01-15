package com.biit.abcd.gui.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.vaadin.testbench.TestBenchTestCase;

public class AbcdGuiLoginTestIT extends TestBenchTestCase {

	@Before
	public void setUp() {
		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("intl.accept_languages", "en_US");
		setDriver(new FirefoxDriver(profile));
	}

	@Test
	public void testAbcdLogin() {
		// Get the page and log in
		getDriver().get("http://localhost:9081");
//		$(TextFieldElement.class).id("userNameLoginForm").setValue("jenkins-abcd@biit-solutions.com");
//		$(PasswordFieldElement.class).id("userPassLoginForm").setValue("jAqDr0r3Agrj");
//		$(ButtonElement.class).caption("Sign In").first().click();
	}
	
	@After
	public void tearDown() {
		getDriver().quit();
	}
}

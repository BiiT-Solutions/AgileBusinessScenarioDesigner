package com.biit.abcd.gui.test;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.vaadin.testbench.TestBenchTestCase;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.PasswordFieldElement;
import com.vaadin.testbench.elements.TableElement;
import com.vaadin.testbench.elements.TextFieldElement;

public class AbcdGuiLoginTestIT extends TestBenchTestCase {

	@Before
	public void setUp() throws Exception {
		setDriver(new FirefoxDriver());
	}

	public void testAbcdLogin() {
		// Get the page and log in
		getDriver().get("http://localhost:8081");
		$(TextFieldElement.class).id("userNameLoginForm").setValue("test@liferay.com");
		$(PasswordFieldElement.class).id("userPassLoginForm").setValue("test");
		$(ButtonElement.class).caption("Sign In").first().click();
		$(TableElement.class).first();
	}

	@After
	public void tearDown() throws Exception {
		getDriver().quit();
	}
}

package com.biit.abcd.gui.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.vaadin.testbench.Parameters;
import com.vaadin.testbench.ScreenshotOnFailureRule;
import com.vaadin.testbench.TestBench;
import com.vaadin.testbench.TestBenchTestCase;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.PasswordFieldElement;
import com.vaadin.testbench.elements.TextFieldElement;

public class AbcdGuiLoginTestIT extends TestBenchTestCase {

	@Before
	public void setUp() throws OsNotSupportedException {
		setScreenshotsParameters("/var/lib/jenkins/screenshots");

		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("intl.accept_languages", "en_US");
		profile.setPreference("focusmanager.testmode", true);
		setDriver(TestBench.createDriver(new FirefoxDriver(profile)));
	}

	@Rule
	public ScreenshotOnFailureRule screenshotOnFailureRule = new ScreenshotOnFailureRule(this, true);

	@Test
	public void testAbcdLogin() {
		// Get the page and log in
		getDriver().get("http://localhost:9081");
		$(TextFieldElement.class).id("userNameLoginForm").setValue("jenkins-abcd@biit-solutions.com");
		$(PasswordFieldElement.class).id("userPassLoginForm").setValue("jAqDr0r3Agrj");
		$(ButtonElement.class).id("loginButton").click();
	}

	@After
	public void tearDown() {
		 getDriver().quit();
	}

	public static void setScreenshotsParameters(String path) {
		Parameters.setScreenshotErrorDirectory(path + "/errors");
		Parameters.setScreenshotReferenceDirectory(path + "/reference");
		Parameters.setMaxScreenshotRetries(2);
		Parameters.setScreenshotComparisonTolerance(1.0);
		Parameters.setScreenshotRetryDelay(10);
		Parameters.setScreenshotComparisonCursorDetection(true);
	}
}

package com.biit.abcd.gui.test;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.vaadin.testbench.Parameters;
import com.vaadin.testbench.TestBench;
import com.vaadin.testbench.TestBenchTestCase;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.PasswordFieldElement;
import com.vaadin.testbench.elements.TextFieldElement;

public class AbcdGuiTestIT extends TestBenchTestCase {

	// Activates screenshots on application failure
	private boolean takeScreeenshots = false;
	protected boolean testInJenkins = false;
	private static String SCREENSHOTS_PATH = "/var/lib/jenkins/screenshots";

	@BeforeClass
	protected void setUp() {
		if (takeScreeenshots) {
			setScreenshotsParameters(SCREENSHOTS_PATH);
		}
		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("intl.accept_languages", "en_US");
		setDriver(TestBench.createDriver(new FirefoxDriver(profile)));
	}

	protected void autoLogin() {
		// Get the page and log in
		getDriver().get("http://localhost:9081");
		$(TextFieldElement.class).id("userNameLoginForm").setValue("jenkins-abcd@biit-solutions.com");
		$(PasswordFieldElement.class).id("userPassLoginForm").setValue("jAqDr0r3Agrj");
		$(ButtonElement.class).id("loginButton").click();
	}

	protected void logOut() {
		$(ButtonElement.class).id("settingsButton").click();
		$(ButtonElement.class).id("logoutButton").click();
	}

	@AfterClass
	protected void tearDown() {
		// Do not call driver.quit if you want to take screenshots when the
		// application fails
		if (!takeScreeenshots) {
			getDriver().quit();
		}
	}

	private static void setScreenshotsParameters(String path) {
		Parameters.setScreenshotErrorDirectory(path + "/errors");
		Parameters.setScreenshotReferenceDirectory(path + "/reference");
		Parameters.setMaxScreenshotRetries(2);
		Parameters.setScreenshotComparisonTolerance(1.0);
		Parameters.setScreenshotRetryDelay(10);
		Parameters.setScreenshotComparisonCursorDetection(true);
	}
}

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
	protected final static String USER_EDIT_FORM = "abcd_form_admin@biit1.com";
	protected final static String USER_EDIT_PASSWORD = "asd123";
	protected final static String DRIVER_URL = "http://localhost:9081";

	// Activates screenshots on application failure
	private boolean takeScreeenshots = false;
	protected boolean testInJenkins = false;
	private final static String SCREENSHOTS_PATH = System.getProperty("java.io.tmpdir");

	@BeforeClass(inheritGroups = true, alwaysRun = true)
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
		getDriver().get(DRIVER_URL);
		$(TextFieldElement.class).id("userNameLoginForm").setValue(USER_EDIT_FORM);
		$(PasswordFieldElement.class).id("userPassLoginForm").setValue(USER_EDIT_PASSWORD);

		$(ButtonElement.class).id("loginButton").click();
	}

	protected void logOut() {
		$(ButtonElement.class).id("settingsButton").click();
		$(ButtonElement.class).id("logoutButton").click();
	}

	@AfterClass(inheritGroups = true, alwaysRun = true)
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

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
//		FirefoxBinary binary = null;
//		if (isWindows()) {
//			binary = new FirefoxBinary(new File("C:/Program Files (x86)/Mozilla Firefox/firefox.exe"));
//		} else if (isUnix()) {
//			binary = new FirefoxBinary(new File("/usr/lib/firefox/firefox"));
//		} else {
//			throw new OsNotSupportedException("Your OS is not supported!!");
//		}

		Parameters.setScreenshotErrorDirectory("/var/lib/jenkins/screenshots/errors");
		Parameters.setScreenshotReferenceDirectory("/var/lib/jenkins/screenshots/reference");
		Parameters.setMaxScreenshotRetries(2);
		Parameters.setScreenshotComparisonTolerance(1.0);
		Parameters.setScreenshotRetryDelay(10);
		Parameters.setScreenshotComparisonCursorDetection(true);

		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("intl.accept_languages", "en_US");
		profile.setPreference("focusmanager.testmode", true);
		setDriver(TestBench.createDriver(new FirefoxDriver(profile)));
	}

	@Rule
	public ScreenshotOnFailureRule screenshotOnFailureRule = new ScreenshotOnFailureRule(this, true);
	
	@Test
	public void testAbcdLogin() throws InterruptedException {
		// Get the page and log in
		getDriver().get("http://localhost:9081");
		
		$(TextFieldElement.class).first().setValue("jenkins-abcd@biit-solutions.com");
		
		$(PasswordFieldElement.class).first().setValue("jAqDr0r3Agrj");
		
		$(ButtonElement.class).first().click();
		
//		$(TextFieldElement.class).id("userNameLoginForm").setValue("jenkins-abcd@biit-solutions.com");
//		$(PasswordFieldElement.class).id("userPassLoginForm").setValue("jAqDr0r3Agrj");
//		$(ButtonElement.class).id("loginButton").click();
	}

	@After
	public void tearDown() {
//		getDriver().quit();
	}

	public static boolean isWindows() {
		String os = System.getProperty("os.name").toLowerCase();
		// windows
		return (os.indexOf("win") >= 0);
	}

	public static boolean isUnix() {
		String os = System.getProperty("os.name").toLowerCase();
		return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") > 0);
	}
}

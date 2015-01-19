package com.biit.abcd.gui.test;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.vaadin.testbench.Parameters;
import com.vaadin.testbench.ScreenshotOnFailureRule;
import com.vaadin.testbench.TestBench;
import com.vaadin.testbench.TestBenchTestCase;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.PasswordFieldElement;
import com.vaadin.testbench.elements.TextFieldElement;

public class AbcdGuiLoginTestIT extends TestBenchTestCase {

	private WebDriverWait wait;
	private FirefoxDriver driver;

	@Before
	public void setUp() throws OsNotSupportedException {
		setScreenshotsParameters("/var/lib/jenkins/screenshots");

		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("intl.accept_languages", "en_US");
		profile.setPreference("focusmanager.testmode", true);
		driver = new FirefoxDriver(profile);
		setDriver(TestBench.createDriver(driver));

		getDriver().manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		wait = new WebDriverWait(getDriver(), 5);
	}

	@Rule
	public ScreenshotOnFailureRule screenshotOnFailureRule = new ScreenshotOnFailureRule(this, true);

	@Test
	public void testAbcdLogin() {
		// Get the page and log in
		getDriver().get("http://localhost:9081");
		// wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userNameLoginForm")));
		$(TextFieldElement.class).id("userNameLoginForm").setValue("jenkins-abcd@biit-solutions.com");
		$(PasswordFieldElement.class).id("userPassLoginForm").setValue("jAqDr0r3Agrj");
		$(ButtonElement.class).id("loginButton").click();
	}

	// @Test
	// public void testAbcdLoginSelenium() {
	// // Get the page and log in
	// getDriver().get("http://localhost:9081");
	// getDriver().findElement(By.id("userNameLoginForm")).clear();
	// getDriver().findElement(By.id("userNameLoginForm")).sendKeys("jenkins-abcd@biit-solutions.com");
	// getDriver().findElement(By.id("userPassLoginForm")).clear();
	// getDriver().findElement(By.id("userPassLoginForm")).sendKeys("jAqDr0r3Agrj");
	// getDriver().findElement(By.cssSelector("span.v-button-caption")).click();
	// }

	@After
	public void tearDown() {
		// getDriver().quit();
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

	public static void setScreenshotsParameters(String path) {
		Parameters.setScreenshotErrorDirectory(path + "/errors");
		Parameters.setScreenshotReferenceDirectory(path + "/reference");
		Parameters.setMaxScreenshotRetries(2);
		Parameters.setScreenshotComparisonTolerance(1.0);
		Parameters.setScreenshotRetryDelay(10);
		Parameters.setScreenshotComparisonCursorDetection(true);
	}
}

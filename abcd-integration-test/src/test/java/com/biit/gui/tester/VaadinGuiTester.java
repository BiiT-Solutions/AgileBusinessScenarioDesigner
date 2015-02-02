package com.biit.gui.tester;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.os.ProcessUtils.ProcessStillAliveException;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.vaadin.testbench.TestBench;
import com.vaadin.testbench.TestBenchTestCase;
import com.vaadin.testbench.elements.NotificationElement;

public class VaadinGuiTester extends TestBenchTestCase {

	private static final String FIREFOX_LANGUAGE_PROPERTY = "intl.accept_languages";
	private static final String FIREFOX_LANGUAGE_VALUE = "en_US";
	private static final String APPLICATION_URL_NEW_UI = "http://localhost:9081/?restartApplication";
	private static final String NOTIFICATION_TYPE_ERROR = "error";
	private static final String NOTIFICATION_TYPE_WARNING = "warning";
	// This parameter set to 'true' activates phantomJs driver instead of
	// firefox driver
	private boolean headlessTesting = false;

	// To debug last step on firefox
	private boolean destroyDriver = true;

	private final List<VaadinGuiWebpage> webpages;

	public VaadinGuiTester() {
		webpages = new ArrayList<VaadinGuiWebpage>();
	}

	@BeforeClass(inheritGroups = true, alwaysRun = true)
	public void createDriver() {
		if (headlessTesting) {
			DesiredCapabilities caps = new DesiredCapabilities();
			caps.setJavascriptEnabled(true);
			caps.setCapability("takesScreenshot", true);
			setDriver(TestBench.createDriver(new PhantomJSDriver(caps)));
		} else {
			FirefoxProfile profile = new FirefoxProfile();
			profile.setPreference(FIREFOX_LANGUAGE_PROPERTY, FIREFOX_LANGUAGE_VALUE);
			setDriver(TestBench.createDriver(new FirefoxDriver(profile)));
		}
		getDriver().manage().window().setSize(new Dimension(1920, 1080));
		for (VaadinGuiWebpage webpage : webpages) {
			webpage.setDriver(getDriver());
		}
	}

	@AfterClass(inheritGroups = true, alwaysRun = true)
	public void destroyDriver() {
		if (destroyDriver) {
			try {
				getDriver().quit();
			} catch (ProcessStillAliveException psae) {
				// Ignore
			}
		}
	}

	public void addWebpage(VaadinGuiWebpage webpage) {
		webpages.add(webpage);
	}

	public void mainPage() {
		getDriver().get(APPLICATION_URL_NEW_UI);
	}

	public NotificationElement getNotification() {
		if ($(NotificationElement.class).exists()) {
			return $(NotificationElement.class).first();
		}
		return null;
	}

	public static void checkNotificationIsError(NotificationElement notification) {
		Assert.assertEquals(NOTIFICATION_TYPE_ERROR, notification.getType());
	}

	public static void checkNotificationIsWarning(NotificationElement notification) {
		Assert.assertEquals(NOTIFICATION_TYPE_WARNING, notification.getType());
	}
}

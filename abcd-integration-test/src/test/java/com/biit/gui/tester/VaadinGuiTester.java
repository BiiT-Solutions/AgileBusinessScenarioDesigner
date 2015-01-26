package com.biit.gui.tester;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.vaadin.testbench.Parameters;
import com.vaadin.testbench.TestBench;
import com.vaadin.testbench.TestBenchTestCase;
import com.vaadin.testbench.elements.NotificationElement;

public class VaadinGuiTester extends TestBenchTestCase{

	// Activates screenshots on application failure
	private boolean takeScreeenshots = false;
	private final static String SCREENSHOTS_PATH = System.getProperty("java.io.tmpdir");
	private static final String FIREFOX_LANGUAGE_PROPERTY = "intl.accept_languages";
	private static final String FIREFOX_LANGUAGE_VALUE = "en_US";
	private static final String APPLICATION_URL_NEW_UI = "http://localhost:9081/?restartApplication";
	private static final String NOTIFICATION_TYPE_ERROR = "error";
	
	private final List<VaadinGuiWebpage> webpages;
	
	public VaadinGuiTester() {
		webpages = new ArrayList<VaadinGuiWebpage>();
	}
	
	@BeforeClass(inheritGroups = true, alwaysRun = true)
	public void createDriver(){
		if (takeScreeenshots) {
			setScreenshotsParameters(SCREENSHOTS_PATH);
		}
		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference(FIREFOX_LANGUAGE_PROPERTY, FIREFOX_LANGUAGE_VALUE);
		setDriver(TestBench.createDriver(new FirefoxDriver(profile)));
		for(VaadinGuiWebpage webpage:webpages){
			webpage.setDriver(getDriver());
		}
	}
	
	@AfterClass(inheritGroups = true, alwaysRun = true)
	public void destroyDriver(){
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
	
	public void addWebpage(VaadinGuiWebpage webpage){
		webpages.add(webpage);
	}
	
	public void mainPage(){
		getDriver().get(APPLICATION_URL_NEW_UI);
	}
	
	public NotificationElement getNotification(){
		return $(NotificationElement.class).first();
	}
	
	public static void checkNotificationIsError(NotificationElement notification){
		Assert.assertEquals(NOTIFICATION_TYPE_ERROR, notification.getType());
	}
}

package com.biit.gui.tester;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.os.ProcessUtils.ProcessStillAliveException;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.biit.abcd.logger.AbcdLogger;
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
	private boolean destroyDriver = false;

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

	protected void executeSqlDump(String dbUrl, String dbUser, String dbPass, String sqlFilePath,
			String outputFilePath, String errorFilePath) {
		try {
			// Create MySql Connection
			Connection con = DriverManager.getConnection(dbUrl, dbUser, dbPass);
			// Initialize object for ScripRunner
			ScriptRunner sr = new ScriptRunner(con);
			// Give the input file to Reader
			Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(sqlFilePath),
					StandardCharsets.UTF_8));

			// new FileReader(sqlFilePath));
			// Execute script
			sr.setLogWriter(new PrintWriter(new File(outputFilePath)));
			sr.setErrorLogWriter(new PrintWriter(new File(errorFilePath)));
			sr.runScript(reader);
			sr.closeConnection();

		} catch (FileNotFoundException | SQLException e) {
			AbcdLogger.errorMessage(this.getClass().getName(),
					"Failed to execute the file: " + sqlFilePath + "\n " + e.getMessage());
			Assert.fail();
		}
	}
}

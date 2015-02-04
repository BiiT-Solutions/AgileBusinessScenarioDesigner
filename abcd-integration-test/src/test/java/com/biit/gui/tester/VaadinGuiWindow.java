package com.biit.gui.tester;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.vaadin.testbench.TestBenchTestCase;

public class VaadinGuiWindow extends TestBenchTestCase{

	private static final String SCREENSHOT_TYPE = ".jpeg";
	
	/**
	 * Takes a screenshot of the webpage and stores it in the Temp folder
	 * 
	 * @param screenshotName
	 */
	public void takeScreenshot(String screenshotName) {
		File scrFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrFile, new File(System.getProperty("java.io.tmpdir") + File.separator + screenshotName
					+ SCREENSHOT_TYPE), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

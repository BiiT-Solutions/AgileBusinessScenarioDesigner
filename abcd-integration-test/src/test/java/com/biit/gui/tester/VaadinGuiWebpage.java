package com.biit.gui.tester;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.vaadin.testbench.TestBenchTestCase;


public abstract class VaadinGuiWebpage extends TestBenchTestCase{
	
	private static final String SCREENSHOT_TYPE = ".jpeg";
	
	private final List<VaadinGuiWindow> windows;
	
	public VaadinGuiWebpage() {
		windows = new ArrayList<VaadinGuiWindow>();
	}
	
	public void addWindow(VaadinGuiWindow window){
		windows.add(window);
	}
	
	@Override
	public void setDriver(WebDriver driver){
		super.setDriver(driver);
		for(VaadinGuiWindow window:windows){
			window.setDriver(getDriver());
		}
	}
	
	public void goToPage(){
		getDriver().get(getWebpageUrl());
	}

	public abstract String getWebpageUrl();
	
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

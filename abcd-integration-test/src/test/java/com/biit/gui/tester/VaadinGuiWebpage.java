package com.biit.gui.tester;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;

import com.vaadin.testbench.TestBenchTestCase;


public abstract class VaadinGuiWebpage extends TestBenchTestCase{
	
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
	
}

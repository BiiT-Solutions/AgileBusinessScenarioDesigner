package com.biit.abcd.gui.test.webpage;

import org.junit.Assert;

import com.biit.abcd.gui.test.window.InfoWindow;
import com.biit.abcd.gui.test.window.Proceed;
import com.biit.abcd.gui.test.window.WarningUnsavedData;
import com.biit.gui.tester.VaadinGuiWebpage;
import com.vaadin.testbench.ElementQuery;
import com.vaadin.testbench.elements.ButtonElement;

public class AbcdCommonWebpage extends VaadinGuiWebpage{
	
	protected static final String CSS_CLASS = "class";
	protected static final String TABLE_ROW_SELECTED = "v-selected";
	private final static String SETTINGS_BUTTON = "settingsButton";
	private final static String LOGOUT_BUTTON = "logoutButton";
	
	private final InfoWindow infoWindow;
	private final Proceed proceed;
	private final WarningUnsavedData warningUnsavedData;

	public AbcdCommonWebpage() {
		super();
		infoWindow = new InfoWindow();
		proceed = new Proceed();
		warningUnsavedData = new WarningUnsavedData();
		addWindow(infoWindow);
		addWindow(proceed);
		addWindow(warningUnsavedData);
	}
	
	@Override
	public String getWebpageUrl() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void openAndCloseInfoScreen() {
		getInfoButton().click();
		infoWindow.clickCloseButton();
	}

	private ButtonElement getInfoButton() {
		toggleSettings();
		return $(ButtonElement.class).caption("Info").first();
	}
	
	public void toggleSettings() {
		$(ButtonElement.class).id(SETTINGS_BUTTON).click();
	}

	public void logOut() {
		toggleSettings();
		$(ButtonElement.class).id(LOGOUT_BUTTON).click();
	}

	public ButtonElement getClearCache(){
		toggleSettings();
		ElementQuery<ButtonElement> button = $(ButtonElement.class).caption("Clear Cache");
		if(button.exists()){
			return button.first();
		}
		return null;
	}

	public void clickClearCache() {
		ButtonElement button = getClearCache();
		Assert.assertTrue(button.isEnabled());
		button.click();
		proceed.clickAccept(); 
	}
	
	public void clickAcceptProceed() {
		proceed.clickAccept();
	}

	public Proceed getProceed() {
		return proceed;
	}
	
	public void clickFormDesigner() {
		$(ButtonElement.class).caption("Form Designer").first().click();
	}
	
	public void clickFormVariables() {
		$(ButtonElement.class).caption("Form Variables").first().click();
	}
	
	public void clickDiagramDesigner() {
		$(ButtonElement.class).caption("Diagram Designer").first().click();
	}
	
	public void clickRuleExpressionEditor() {
		$(ButtonElement.class).caption("Rule Expression Editor").first().click();
	}
	
	public void clickRuleEditor() {
		$(ButtonElement.class).caption("Rule Editor").first().click();
	}
	
	public void clickRuleTableEditor() {
		$(ButtonElement.class).caption("Rule Table Editor").first().click();
	}

	public void goToFormManager() {
		$(ButtonElement.class).caption("Forms").first().click();
	}
	
	public WarningUnsavedData getWarningUnsavedData() {
		return warningUnsavedData;
	}
}

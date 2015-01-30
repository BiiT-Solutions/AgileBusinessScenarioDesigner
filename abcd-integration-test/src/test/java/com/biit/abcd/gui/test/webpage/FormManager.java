package com.biit.abcd.gui.test.webpage;

import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;

import com.biit.abcd.gui.test.window.InfoWindow;
import com.biit.abcd.gui.test.window.NewForm;
import com.biit.abcd.gui.test.window.Proceed;
import com.biit.gui.tester.VaadinGuiWebpage;
import com.vaadin.testbench.By;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.TreeTableElement;

public class FormManager extends VaadinGuiWebpage {

	private final static String SETTINGS_BUTTON = "settingsButton";
	private final static String LOGOUT_BUTTON = "logoutButton";

	private final NewForm newFormWindow;
	private final Proceed proceed;
	private final InfoWindow infoWindow;

	public FormManager() {
		super();
		newFormWindow = new NewForm();
		proceed = new Proceed();
		infoWindow = new InfoWindow();
		addWindow(newFormWindow);
		addWindow(proceed);
		addWindow(infoWindow);
	}

	public void toggleSettings() {
		$(ButtonElement.class).id(SETTINGS_BUTTON).click();
	}

	public void logOut() {
		toggleSettings();
		$(ButtonElement.class).id(LOGOUT_BUTTON).click();
	}

	public ButtonElement getNewMenu() {
		return $(ButtonElement.class).caption("New").first();
	}

	public ButtonElement getNewForm() {
		getNewMenu().click();
		return getNewFormButton();
	}
	
	public ButtonElement getNewFormButton() {
		return $(ButtonElement.class).caption("Form").first();
	}

	/**
	 * If the element exists return the element otherwise return null.
	 * 
	 * @return
	 */
	public ButtonElement getRemoveForm() {
		if ($(ButtonElement.class).caption("Remove Form").exists()) {
			return $(ButtonElement.class).caption("Remove Form").first();
		}
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

	private void openNewForm() {
		getNewForm().click();
	}

	public void createNewForm(String formName) {
		openNewForm();
		newFormWindow.createNewForm(formName);
	}

	public TreeTableElement getFormTable() {
		return $(TreeTableElement.class).first();
	}

	@Override
	public String getWebpageUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	public void selectForm(int row) {
		getFormTable().getCell(row, 0).click();
	}

	public void deleteForm() {
		getRemoveForm().click();
		proceed.clickAccept();
	}

	public void deleteForm(int row) {
		selectForm(row);
		deleteForm();
	}

	public void clickFormDesigner() {
		$(ButtonElement.class).caption("Form Designer").first().click();
	}

	public boolean checkIfRowExists(int row) {
		return !getFormTable().findElements(By.vaadin("#row[" + row + "]")).isEmpty();
	}

	public void goToDesigner(int i) {
		selectForm(i);
		clickFormDesigner();
	}

	public void clickAcceptProceed() {
		proceed.clickAccept();
	}

	/**
	 * Workaround to close the popover.<br>
	 * When the popover is displayed only the element inside the popover can be
	 * selected.<br>
	 * To close it, we have focus it and send the close key defined.
	 */
	public void closeNewPopover() {
		getNewFormButton().focus();
		Actions builder = new Actions(getDriver());
		builder.sendKeys(Keys.ESCAPE).perform();
	}

}

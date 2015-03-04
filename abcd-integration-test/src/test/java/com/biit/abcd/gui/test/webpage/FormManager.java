package com.biit.abcd.gui.test.webpage;

import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;

import com.biit.abcd.gui.test.window.NewForm;
import com.vaadin.testbench.By;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.TableElement;
import com.vaadin.testbench.elements.TreeTableElement;

public class FormManager extends AbcdCommonWebpage {

	private static final String CSS_CLASS = "class";
	private static final CharSequence SELECTED_ROW = "v-selected";
	private static final String GLOBAL_CONSTANTS_BUTTON = "Global Constants";

	private final NewForm newFormWindow;

	public FormManager() {
		super();
		newFormWindow = new NewForm();
		addWindow(newFormWindow);
	}

	public ButtonElement getNewMenu() {
		return $(ButtonElement.class).caption("New").first();
	}

	public ButtonElement getNewForm() {
		getNewMenu().click();
		takeScreenshot("ABCD_getNewForm");
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
		clickAcceptProceed();
	}

	public void deleteForm(int row) {
		selectForm(row);
		deleteForm();
	}

	public boolean checkIfRowExists(int row) {
		return !getFormTable().findElements(By.vaadin("#row[" + row + "]")).isEmpty();
	}

	public void goToDesigner(int i) {
		selectForm(i);
		clickFormDesigner();
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

	public boolean isRowSelected(int formRow) {
		return getFormTable().getRow(formRow).getAttribute(CSS_CLASS).contains(SELECTED_ROW);
	}
	
	public String getFormName(int row){
		TableElement table = getFormTable();
		return table.getCell(row, 0).getText();
	}

	public void goToGlobalVariables() {
		toggleSettings();
		getButtonByCaption(GLOBAL_CONSTANTS_BUTTON).click();
	}

}

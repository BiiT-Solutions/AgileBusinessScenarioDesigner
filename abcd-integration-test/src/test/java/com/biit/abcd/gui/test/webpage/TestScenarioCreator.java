package com.biit.abcd.gui.test.webpage;

import com.vaadin.testbench.elements.TableElement;
import com.vaadin.testbench.elements.TreeTableElement;

public class TestScenarioCreator extends AbcdCommonWebpage {

	private static final String SAVE_BUTTON_CAPTION = "Save";
	private static final String BUTTON_NEW_CAPTION = "New";
	private static final String BUTTON_REMOVE_CAPTION = "Remove";
	private static final String BUTTON_FORMS_CAPTION = "Forms";
	private static final String BUTTON_SETTINGS_ID = "settingsButton";

	public void clickSaveButton() {
		getButtonByCaption(SAVE_BUTTON_CAPTION).click();
	}

	public void clickNewButton() {
		getButtonByCaption(BUTTON_NEW_CAPTION).click();
	}

	public void clickRemoveButton() {
		getButtonByCaption(BUTTON_REMOVE_CAPTION).click();
	}

	public void clickFormsButton() {
		getButtonByCaption(BUTTON_FORMS_CAPTION).click();
	}

	public void clickSettingsButton() {
		getButtonById(BUTTON_SETTINGS_ID).click();
	}

	public void selectTestScenario(int row) {
		$(TableElement.class).first().getCell(row, 0).click();
	}
	
	public void selectCategory(int row) {
		$(TreeTableElement.class).first().getCell(row, 0).click();
	}
}

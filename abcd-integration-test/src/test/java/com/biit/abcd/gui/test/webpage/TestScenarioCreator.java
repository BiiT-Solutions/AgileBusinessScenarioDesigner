package com.biit.abcd.gui.test.webpage;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (Test)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.abcd.gui.test.window.NewTestScenarioWindow;
import com.vaadin.testbench.elements.ComboBoxElement;
import com.vaadin.testbench.elements.TableElement;
import com.vaadin.testbench.elements.TreeTableElement;

public class TestScenarioCreator extends AbcdCommonWebpage {

	private static final String SAVE_BUTTON_CAPTION = "Save";
	private static final String BUTTON_NEW_CAPTION = "New";
	private static final String BUTTON_REMOVE_CAPTION = "Remove";
	private static final String BUTTON_FORMS_CAPTION = "Forms";
	private static final String BUTTON_SETTINGS_ID = "settingsButton";

	private final NewTestScenarioWindow newTestScenarioWindow;

	public TestScenarioCreator() {
		super();
		newTestScenarioWindow = new NewTestScenarioWindow();
		addWindow(newTestScenarioWindow);
	}

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
		$(TableElement.class).first().getRow(row).click();
	}

	public void selectCategory(int row) {
		$(TreeTableElement.class).first().getRow(row).click();
	}

	public void goToFormManager() {
		clickFormsButton();
	}

	public void createTestScenario(String testScenarioName) {
		clickNewButton();
		newTestScenarioWindow.createNewTestScenario(testScenarioName);
	}
	
	public void setComboBoxValue(String caption, String value){
		$(ComboBoxElement.class).caption(caption).first().selectByText(value);
	}
}

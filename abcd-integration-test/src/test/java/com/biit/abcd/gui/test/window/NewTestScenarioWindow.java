package com.biit.abcd.gui.test.window;

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

import com.biit.gui.tester.VaadinGuiWindow;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.TextFieldElement;

public class NewTestScenarioWindow extends VaadinGuiWindow {

	public static final String TEXT_FIELD_CAPTION = "Test Scenario Name";
	public static final String ACCEPT_BUTTON_CAPTION = "Accept";

	private TextFieldElement getNewTestScenarioTextField() {
		return $(TextFieldElement.class).caption(TEXT_FIELD_CAPTION).first();
	}

	private ButtonElement getAcceptButton() {
		return $(ButtonElement.class).caption(ACCEPT_BUTTON_CAPTION).first();
	}

	public void createNewTestScenario(String testScenarioName) {
		getNewTestScenarioTextField().setValue(testScenarioName);
		getAcceptButton().click();
	}

}

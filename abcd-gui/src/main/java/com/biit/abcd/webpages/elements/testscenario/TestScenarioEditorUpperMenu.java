package com.biit.abcd.webpages.elements.testscenario;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (GUI)
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

import java.util.HashSet;
import java.util.Set;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.webpages.components.IconButton;
import com.biit.abcd.webpages.components.ThemeIcon;
import com.biit.abcd.webpages.components.UpperMenu;
import com.vaadin.ui.Button;

public class TestScenarioEditorUpperMenu extends UpperMenu {
	private static final long serialVersionUID = -2155768749644989588L;
	private IconButton saveButton, newTestScenario, removeTestScenario;

	public TestScenarioEditorUpperMenu() {
		super();
		defineMenu();
	}

	private void defineMenu() {
		saveButton = new IconButton(LanguageCodes.TEST_SCENARIOS_EDITOR_BUTTON_SAVE_CAPTION, ThemeIcon.FORM_SAVE,
				LanguageCodes.TEST_SCENARIOS_EDITOR_BUTTON_SAVE_TOOLTIP);
		newTestScenario = new IconButton(LanguageCodes.TEST_SCENARIOS_EDITOR_BUTTON_ADD_CAPTION, ThemeIcon.TEST_ADD,
				LanguageCodes.TEST_SCENARIOS_EDITOR_BUTTON_ADD_TOOLTIP);
		removeTestScenario = new IconButton(LanguageCodes.TEST_SCENARIOS_EDITOR_BUTTON_REMOVE_CAPTION,
				ThemeIcon.TEST_REMOVE, LanguageCodes.TEST_SCENARIOS_EDITOR_BUTTON_REMOVE_TOOLTIP);

		addIconButton(saveButton);
		addIconButton(newTestScenario);
		addIconButton(removeTestScenario);

		for (Button button : getDisabledButtons()) {
			button.setEnabled(false);
		}
	}

	public void addSaveButtonClickListener(Button.ClickListener listener) {
		if (!getDisabledButtons().contains(saveButton)) {
			saveButton.addClickListener(listener);
		}
	}

	public void removeSaveButtonClickListener(Button.ClickListener listener) {
		saveButton.removeClickListener(listener);
	}

	public void addNewTestScenarioButtonClickListener(Button.ClickListener listener) {
		if (!getDisabledButtons().contains(newTestScenario)) {
			newTestScenario.addClickListener(listener);
		}
	}

	public void removeNewTestScenarioButtonClickListener(Button.ClickListener listener) {
		newTestScenario.removeClickListener(listener);
	}

	public void addRemoveTestScenarioButtonClickListener(Button.ClickListener listener) {
		if (!getDisabledButtons().contains(removeTestScenario)) {
			removeTestScenario.addClickListener(listener);
		}
	}

	public void removeRemoveTestScenarioButtonClickListener(Button.ClickListener listener) {
		removeTestScenario.removeClickListener(listener);
	}

	@Override
	public Set<Button> getSecuredButtons() {
		return new HashSet<Button>();
	}
}

package com.biit.abcd.webpages.elements.testscenario;

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
		newTestScenario = new IconButton(LanguageCodes.TEST_SCENARIOS_EDITOR_BUTTON_ADD_CAPTION,
				ThemeIcon.EXPRESSION_ADD, LanguageCodes.TEST_SCENARIOS_EDITOR_BUTTON_ADD_TOOLTIP);
		removeTestScenario = new IconButton(LanguageCodes.TEST_SCENARIOS_EDITOR_BUTTON_REMOVE_CAPTION,
				ThemeIcon.EXPRESSION_REMOVE, LanguageCodes.TEST_SCENARIOS_EDITOR_BUTTON_REMOVE_TOOLTIP);

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

}

package com.biit.abcd.webpages.elements.testscenario;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.webpages.components.IconButton;
import com.biit.abcd.webpages.components.ThemeIcon;
import com.biit.abcd.webpages.components.UpperMenu;
import com.vaadin.ui.Button;

public class TestScenarioEditorUpperMenu extends UpperMenu {
	private static final long serialVersionUID = -2155768749644989588L;
	private IconButton saveButton, newTestScenario, removeTestScenario, copyRepeatableGroup, removeRepeatableGroup;

	public TestScenarioEditorUpperMenu() {
		super();
		defineMenu();
		setEnabledGroupButtons();
	}

	private void defineMenu() {
		saveButton = new IconButton(LanguageCodes.TEST_SCENARIOS_EDITOR_BUTTON_SAVE_CAPTION, ThemeIcon.FORM_SAVE,
				LanguageCodes.TEST_SCENARIOS_EDITOR_BUTTON_SAVE_TOOLTIP);
		newTestScenario = new IconButton(LanguageCodes.TEST_SCENARIOS_EDITOR_BUTTON_ADD_CAPTION, ThemeIcon.TEST_ADD,
				LanguageCodes.TEST_SCENARIOS_EDITOR_BUTTON_ADD_TOOLTIP);
		removeTestScenario = new IconButton(LanguageCodes.TEST_SCENARIOS_EDITOR_BUTTON_REMOVE_CAPTION,
				ThemeIcon.TEST_REMOVE, LanguageCodes.TEST_SCENARIOS_EDITOR_BUTTON_REMOVE_TOOLTIP);
		copyRepeatableGroup = new IconButton(LanguageCodes.TEST_SCENARIOS_EDITOR_COPY_REPEATABLE_GROUP_CAPTION,
				ThemeIcon.TEST_COPY_GROUP, LanguageCodes.TEST_SCENARIOS_EDITOR_COPY_REPEATABLE_GROUP_TOOLTIP);
		removeRepeatableGroup = new IconButton(LanguageCodes.TEST_SCENARIOS_EDITOR_REMOVE_REPEATABLE_GROUP_CAPTION,
				ThemeIcon.TEST_REMOVE_GROUP, LanguageCodes.TEST_SCENARIOS_EDITOR_REMOVE_REPEATABLE_GROUP_TOOLTIP);

		addIconButton(saveButton);
		addIconButton(newTestScenario);
		addIconButton(removeTestScenario);
		addIconButton(copyRepeatableGroup);
		addIconButton(removeRepeatableGroup);

		for (Button button : getDisabledButtons()) {
			button.setEnabled(false);
		}
	}
	
	public void setEnabledGroupButtons() {
		copyRepeatableGroup.setEnabled(false);
		removeRepeatableGroup.setEnabled(false);
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

	public void addCopyRepeatableGroupButtonClickListener(Button.ClickListener listener) {
		if (!getDisabledButtons().contains(copyRepeatableGroup)) {
			copyRepeatableGroup.addClickListener(listener);
		}
	}

	public void removeCopyRepeatableGroupButtonClickListener(Button.ClickListener listener) {
		copyRepeatableGroup.removeClickListener(listener);
	}

	public void addRemoveRepeatableGroupButtonClickListener(Button.ClickListener listener) {
		if (!getDisabledButtons().contains(removeRepeatableGroup)) {
			removeRepeatableGroup.addClickListener(listener);
		}
	}

	public void removeRemoveRepeatableGroupButtonClickListener(Button.ClickListener listener) {
		removeRepeatableGroup.removeClickListener(listener);
	}
	
	public void enableRepeatableGroupsButtons(boolean enabled){
		copyRepeatableGroup.setEnabled(enabled);
		removeRepeatableGroup.setEnabled(enabled);
	}

}

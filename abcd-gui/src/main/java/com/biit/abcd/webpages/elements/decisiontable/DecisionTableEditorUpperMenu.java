package com.biit.abcd.webpages.elements.decisiontable;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.webpages.components.IconButton;
import com.biit.abcd.webpages.components.ThemeIcons;
import com.biit.abcd.webpages.components.UpperMenu;
import com.vaadin.ui.Button;

public class DecisionTableEditorUpperMenu extends UpperMenu {

	private static final long serialVersionUID = 1878327027307547248L;
	private IconButton saveButton, newConditionButton, deleteConditionButton, newRuleButton, deleteRuleButton;

	public DecisionTableEditorUpperMenu() {
		super();
		defineMenu();
	}

	private void defineMenu() {

		saveButton = new IconButton(LanguageCodes.CONDITION_TABLE_EDITOR_BUTTON_SAVE_CAPTION, ThemeIcons.SAVE,
				LanguageCodes.CONDITION_TABLE_EDITOR_BUTTON_SAVE_TOOLTIP);
		newConditionButton = new IconButton(LanguageCodes.CONDITION_TABLE_EDITOR_BUTTON_ADD_COLUMN_CAPTION,
				ThemeIcons.ADD_COLUMN, LanguageCodes.CONDITION_TABLE_EDITOR_BUTTON_ADD_COLUMN_TOOLTIP);
		deleteConditionButton = new IconButton(LanguageCodes.CONDITION_TABLE_EDITOR_BUTTON_REMOVE_COLUMN_CAPTION,
				ThemeIcons.REMOVE_COLUMN, LanguageCodes.CONDITION_TABLE_EDITOR_BUTTON_REMOVE_COLUMN_TOOLTIP);
		newRuleButton = new IconButton(LanguageCodes.CONDITION_TABLE_EDITOR_BUTTON_ADD_ROW_CAPTION, ThemeIcons.ADD_ROW,
				LanguageCodes.CONDITION_TABLE_EDITOR_BUTTON_ADD_ROW_TOOLTIP);
		deleteRuleButton = new IconButton(LanguageCodes.CONDITION_TABLE_EDITOR_BUTTON_REMOVE_ROW_CAPTION,
				ThemeIcons.REMOVE_ROW, LanguageCodes.CONDITION_TABLE_EDITOR_BUTTON_REMOVE_ROW_TOOLTIP);

		addIconButton(saveButton);
		addIconButton(newConditionButton);
		addIconButton(deleteConditionButton);
		addIconButton(newRuleButton);
		addIconButton(deleteRuleButton);
	}

	public void addSaveButtonClickListener(Button.ClickListener listener) {
		saveButton.addClickListener(listener);
	}

	public void removeSaveButtonClickListener(Button.ClickListener listener) {
		saveButton.removeClickListener(listener);
	}

	public void addNewConditionButtonClickListener(Button.ClickListener listener) {
		newConditionButton.addClickListener(listener);
	}

	public void removeNewConditionClickListener(Button.ClickListener listener) {
		newConditionButton.removeClickListener(listener);
	}

	public void addRemoveConditionButtonClickListener(Button.ClickListener listener) {
		deleteConditionButton.addClickListener(listener);
	}

	public void removeDeleteConditionClickListener(Button.ClickListener listener) {
		deleteConditionButton.removeClickListener(listener);
	}
	
	public void addNewRuleButtonClickListener(Button.ClickListener listener) {
		newRuleButton.addClickListener(listener);
	}

	public void removeAddRuleClickListener(Button.ClickListener listener) {
		newRuleButton.removeClickListener(listener);
	}

	public void addRemoveRuleButtonClickListener(Button.ClickListener listener) {
		deleteRuleButton.addClickListener(listener);
	}

	public void removeRemoveRuleClickListener(Button.ClickListener listener) {
		deleteRuleButton.removeClickListener(listener);
	}

}

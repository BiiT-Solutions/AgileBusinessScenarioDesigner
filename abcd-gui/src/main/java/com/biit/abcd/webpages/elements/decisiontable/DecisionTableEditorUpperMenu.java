package com.biit.abcd.webpages.elements.decisiontable;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.webpages.components.IconButton;
import com.biit.abcd.webpages.components.ThemeIcons;
import com.biit.abcd.webpages.components.UpperMenu;
import com.vaadin.ui.Button;

public class DecisionTableEditorUpperMenu extends UpperMenu {

	private static final long serialVersionUID = 1878327027307547248L;
	private IconButton saveButton, addConditionButton, removeConditionButton, addRuleButton, removeRuleButton;

	public DecisionTableEditorUpperMenu() {
		super();
		defineMenu();
	}

	private void defineMenu() {

		saveButton = new IconButton(LanguageCodes.CONDITION_TABLE_EDITOR_BUTTON_SAVE_CAPTION, ThemeIcons.SAVE,
				LanguageCodes.CONDITION_TABLE_EDITOR_BUTTON_SAVE_TOOLTIP);
		addConditionButton = new IconButton(LanguageCodes.CONDITION_TABLE_EDITOR_BUTTON_ADD_COLUMN_CAPTION,
				ThemeIcons.ADD_COLUMN, LanguageCodes.CONDITION_TABLE_EDITOR_BUTTON_ADD_COLUMN_TOOLTIP);
		removeConditionButton = new IconButton(LanguageCodes.CONDITION_TABLE_EDITOR_BUTTON_REMOVE_COLUMN_CAPTION,
				ThemeIcons.REMOVE_COLUMN, LanguageCodes.CONDITION_TABLE_EDITOR_BUTTON_REMOVE_COLUMN_TOOLTIP);
		addRuleButton = new IconButton(LanguageCodes.CONDITION_TABLE_EDITOR_BUTTON_ADD_ROW_CAPTION, ThemeIcons.ADD_ROW,
				LanguageCodes.CONDITION_TABLE_EDITOR_BUTTON_ADD_ROW_TOOLTIP);
		removeRuleButton = new IconButton(LanguageCodes.CONDITION_TABLE_EDITOR_BUTTON_REMOVE_ROW_CAPTION,
				ThemeIcons.REMOVE_ROW, LanguageCodes.CONDITION_TABLE_EDITOR_BUTTON_REMOVE_ROW_TOOLTIP);

		addIconButton(saveButton);
		addIconButton(addConditionButton);
		addIconButton(removeConditionButton);
		addIconButton(addRuleButton);
		addIconButton(removeRuleButton);
	}

	public void addSaveButtonClickListener(Button.ClickListener listener) {
		saveButton.addClickListener(listener);
	}

	public void removeSaveButtonClickListener(Button.ClickListener listener) {
		saveButton.removeClickListener(listener);
	}

	public void addAddConditionButtonClickListener(Button.ClickListener listener) {
		addConditionButton.addClickListener(listener);
	}

	public void removeAddConditionClickListener(Button.ClickListener listener) {
		addConditionButton.removeClickListener(listener);
	}

	public void addRemoveConditionButtonClickListener(Button.ClickListener listener) {
		removeConditionButton.addClickListener(listener);
	}

	public void removeRemoveConditionClickListener(Button.ClickListener listener) {
		removeConditionButton.removeClickListener(listener);
	}
	
	public void addAddRuleButtonClickListener(Button.ClickListener listener) {
		addRuleButton.addClickListener(listener);
	}

	public void removeAddRuleClickListener(Button.ClickListener listener) {
		addRuleButton.removeClickListener(listener);
	}

	public void addRemoveRuleButtonClickListener(Button.ClickListener listener) {
		removeRuleButton.addClickListener(listener);
	}

	public void removeRemoveRuleClickListener(Button.ClickListener listener) {
		removeRuleButton.removeClickListener(listener);
	}

}

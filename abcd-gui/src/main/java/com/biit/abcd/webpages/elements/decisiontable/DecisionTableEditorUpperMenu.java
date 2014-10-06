package com.biit.abcd.webpages.elements.decisiontable;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.webpages.components.IconButton;
import com.biit.abcd.webpages.components.ThemeIcon;
import com.biit.abcd.webpages.components.UpperMenu;
import com.vaadin.ui.Button;

public class DecisionTableEditorUpperMenu extends UpperMenu {
	private static final long serialVersionUID = 1878327027307547248L;
	private IconButton saveButton, newTable, removeTable, newConditionButton, deleteConditionButton, newRuleButton,
			deleteRuleButton, copyRowsButton, pasteRowsButton;

	public DecisionTableEditorUpperMenu() {
		super();
		defineMenu();
	}

	private void defineMenu() {
		saveButton = new IconButton(LanguageCodes.CONDITION_TABLE_EDITOR_BUTTON_SAVE_CAPTION, ThemeIcon.FORM_SAVE,
				LanguageCodes.CONDITION_TABLE_EDITOR_BUTTON_SAVE_TOOLTIP);
		newTable = new IconButton(LanguageCodes.CONDITION_TABLE_EDITOR_ADD_TABLE, ThemeIcon.TABLE_ADD,
				LanguageCodes.CONDITION_TABLE_EDITOR_ADD_TABLE_TOOLTIP);
		removeTable = new IconButton(LanguageCodes.CONDITION_TABLE_EDITOR_REMOVE_TABLE, ThemeIcon.TABLE_REMOVE,
				LanguageCodes.CONDITION_TABLE_EDITOR_REMOVE_TABLE_TOOLTIP);
		newConditionButton = new IconButton(LanguageCodes.CONDITION_TABLE_EDITOR_BUTTON_ADD_COLUMN_CAPTION,
				ThemeIcon.TABLE_ADD_COLUMN, LanguageCodes.CONDITION_TABLE_EDITOR_BUTTON_ADD_COLUMN_TOOLTIP);
		deleteConditionButton = new IconButton(LanguageCodes.CONDITION_TABLE_EDITOR_BUTTON_REMOVE_COLUMN_CAPTION,
				ThemeIcon.TABLE_REMOVE_COLUMN, LanguageCodes.CONDITION_TABLE_EDITOR_BUTTON_REMOVE_COLUMN_TOOLTIP);
		newRuleButton = new IconButton(LanguageCodes.CONDITION_TABLE_EDITOR_BUTTON_ADD_ROW_CAPTION, ThemeIcon.ADD_ROW,
				LanguageCodes.CONDITION_TABLE_EDITOR_BUTTON_ADD_ROW_TOOLTIP);
		deleteRuleButton = new IconButton(LanguageCodes.CONDITION_TABLE_EDITOR_BUTTON_REMOVE_ROW_CAPTION,
				ThemeIcon.REMOVE_ROW, LanguageCodes.CONDITION_TABLE_EDITOR_BUTTON_REMOVE_ROW_TOOLTIP);
		copyRowsButton = new IconButton(LanguageCodes.CONDITION_TABLE_EDITOR_BUTTON_COPY_ROWS_CAPTION,
				ThemeIcon.COPY_ROW, LanguageCodes.CONDITION_TABLE_EDITOR_BUTTON_COPY_ROWS_TOOLTIP);
		pasteRowsButton = new IconButton(LanguageCodes.CONDITION_TABLE_EDITOR_BUTTON_PASTE_ROWS_CAPTION,
				ThemeIcon.PASTE_ROW, LanguageCodes.CONDITION_TABLE_EDITOR_BUTTON_PASTE_ROWS_TOOLTIP);

		addIconButton(saveButton);
		addIconButton(newTable);
		addIconButton(removeTable);
		addIconButton(newConditionButton);
		addIconButton(deleteConditionButton);
		addIconButton(newRuleButton);
		addIconButton(deleteRuleButton);
		addIconButton(copyRowsButton);
		addIconButton(pasteRowsButton);

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

	public void addNewConditionButtonClickListener(Button.ClickListener listener) {
		if (!getDisabledButtons().contains(newConditionButton)) {
			newConditionButton.addClickListener(listener);
		}
	}

	public void removeNewConditionClickListener(Button.ClickListener listener) {
		newConditionButton.removeClickListener(listener);
	}

	public void addRemoveConditionButtonClickListener(Button.ClickListener listener) {
		if (!getDisabledButtons().contains(deleteConditionButton)) {
			deleteConditionButton.addClickListener(listener);
		}
	}

	public void removeDeleteConditionClickListener(Button.ClickListener listener) {
		deleteConditionButton.removeClickListener(listener);
	}

	public void addNewRuleButtonClickListener(Button.ClickListener listener) {
		if (!getDisabledButtons().contains(newRuleButton)) {
			newRuleButton.addClickListener(listener);
		}
	}

	public void removeAddRuleClickListener(Button.ClickListener listener) {
		newRuleButton.removeClickListener(listener);
	}

	public void addRemoveRuleButtonClickListener(Button.ClickListener listener) {
		if (!getDisabledButtons().contains(deleteRuleButton)) {
			deleteRuleButton.addClickListener(listener);
		}
	}

	public void removeRemoveRuleClickListener(Button.ClickListener listener) {
		deleteRuleButton.removeClickListener(listener);
	}

	public void addNewTableClickListener(Button.ClickListener listener) {
		if (!getDisabledButtons().contains(newTable)) {
			newTable.addClickListener(listener);
		}
	}

	public void removeNewTableClickListener(Button.ClickListener listener) {
		newTable.removeClickListener(listener);
	}

	public void addRemoveTableClickListener(Button.ClickListener listener) {
		if (!getDisabledButtons().contains(removeTable)) {
			removeTable.addClickListener(listener);
		}
	}

	public void removeRemoveTableClickListener(Button.ClickListener listener) {
		removeTable.removeClickListener(listener);
	}

	public void addCopyRowsClickListener(Button.ClickListener listener) {
		if (!getDisabledButtons().contains(copyRowsButton)) {
			copyRowsButton.addClickListener(listener);
		}
	}

	public void removeCopyRowsClickListener(Button.ClickListener listener) {
		copyRowsButton.removeClickListener(listener);
	}

	public void addPasteRowsClickListener(Button.ClickListener listener) {
		if (!getDisabledButtons().contains(pasteRowsButton)) {
			pasteRowsButton.addClickListener(listener);
		}
	}

	public void removePasteyRowsClickListener(Button.ClickListener listener) {
		if (!getDisabledButtons().contains(pasteRowsButton)) {
			pasteRowsButton.removeClickListener(listener);
		}
	}
}

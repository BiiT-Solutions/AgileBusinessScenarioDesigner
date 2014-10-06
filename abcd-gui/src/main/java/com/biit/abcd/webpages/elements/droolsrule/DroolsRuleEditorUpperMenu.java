package com.biit.abcd.webpages.elements.droolsrule;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.webpages.components.IconButton;
import com.biit.abcd.webpages.components.ThemeIcon;
import com.biit.abcd.webpages.components.UpperMenu;
import com.vaadin.ui.Button;

public class DroolsRuleEditorUpperMenu extends UpperMenu {
	private static final long serialVersionUID = 4790116559655965919L;
	private IconButton saveButton, newRule, removeRule;

	public DroolsRuleEditorUpperMenu() {
		super();
		defineMenu();
	}

	private void defineMenu() {
		saveButton = new IconButton(LanguageCodes.CONDITION_TABLE_EDITOR_BUTTON_SAVE_CAPTION, ThemeIcon.FORM_SAVE,
				LanguageCodes.FORM_DIAGRAM_BUILDER_UPPER_BUTTON_SAVE_TOOLTIP);
		newRule = new IconButton(LanguageCodes.MENU_RULE_ADD_CAPTION, ThemeIcon.RULE_ADD,
				LanguageCodes.MENU_RULE_ADD_TOOLTIP);
		removeRule = new IconButton(LanguageCodes.MENU_RULE_REMOVE_CAPTION, ThemeIcon.RULE_REMOVE,
				LanguageCodes.MENU_RULE_REMOVE_TOOLTIP);

		addIconButton(saveButton);
		addIconButton(newRule);
		addIconButton(removeRule);

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

	public void addNewRuleButtonClickListener(Button.ClickListener listener) {
		if (!getDisabledButtons().contains(newRule)) {
			newRule.addClickListener(listener);
		}
	}

	public void removeNewRuleButtonClickListener(Button.ClickListener listener) {
		newRule.removeClickListener(listener);
	}

	public void addRemoveRuleButtonClickListener(Button.ClickListener listener) {
		if (!getDisabledButtons().contains(removeRule)) {
			removeRule.addClickListener(listener);
		}
	}

	public void removeRemoveRuleButtonClickListener(Button.ClickListener listener) {
		removeRule.removeClickListener(listener);
	}

}

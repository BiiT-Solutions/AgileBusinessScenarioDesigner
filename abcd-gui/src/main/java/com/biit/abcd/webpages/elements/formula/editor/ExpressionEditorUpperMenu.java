package com.biit.abcd.webpages.elements.formula.editor;

import java.util.HashSet;
import java.util.Set;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.webpages.components.IconButton;
import com.biit.abcd.webpages.components.ThemeIcon;
import com.biit.abcd.webpages.components.UpperMenu;
import com.vaadin.ui.Button;

public class ExpressionEditorUpperMenu extends UpperMenu {
	private static final long serialVersionUID = 102598701730431578L;

	private IconButton saveButton, newExpression, removeExpression;

	public ExpressionEditorUpperMenu() {
		super();
		defineMenu();
	}

	private void defineMenu() {
		saveButton = new IconButton(LanguageCodes.CONDITION_TABLE_EDITOR_BUTTON_SAVE_CAPTION, ThemeIcon.FORM_SAVE,
				LanguageCodes.CONDITION_TABLE_EDITOR_BUTTON_SAVE_TOOLTIP);
		newExpression = new IconButton(LanguageCodes.MENU_EXPRESSIONS_ADD, ThemeIcon.EXPRESSION_ADD,
				LanguageCodes.MENU_EXPRESSIONS_ADD);
		removeExpression = new IconButton(LanguageCodes.MENU_EXPRESSIONS_REMOVE, ThemeIcon.EXPRESSION_REMOVE,
				LanguageCodes.MENU_EXPRESSIONS_REMOVE);

		addIconButton(saveButton);
		addIconButton(newExpression);
		addIconButton(removeExpression);

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

	public void addNewExpressionButtonClickListener(Button.ClickListener listener) {
		if (!getDisabledButtons().contains(newExpression)) {
			newExpression.addClickListener(listener);
		}
	}

	public void removeNewExpressionButtonClickListener(Button.ClickListener listener) {
		newExpression.removeClickListener(listener);
	}

	public void addRemoveExpressionButtonClickListener(Button.ClickListener listener) {
		if (!getDisabledButtons().contains(removeExpression)) {
			removeExpression.addClickListener(listener);
		}
	}

	public void removeRemoveExpressionButtonClickListener(Button.ClickListener listener) {
		removeExpression.removeClickListener(listener);
	}

	@Override
	public Set<Button> getSecuredButtons() {
		return new HashSet<Button>();
	}
}

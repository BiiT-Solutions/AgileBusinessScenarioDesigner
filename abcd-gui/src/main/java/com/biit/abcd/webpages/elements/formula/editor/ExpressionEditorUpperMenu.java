package com.biit.abcd.webpages.elements.formula.editor;

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

public class ExpressionEditorUpperMenu extends UpperMenu {
	private static final long serialVersionUID = 102598701730431578L;

	private IconButton saveButton, newExpression, removeExpression, copyExpression;

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
		copyExpression = new IconButton(LanguageCodes.MENU_RULE_DUPLICATE_CAPTION,
				ThemeIcon.COPY_ROW, LanguageCodes.MENU_RULE_DUPLICATE_TOOLTIP);

		addIconButton(saveButton);
		addIconButton(newExpression);
		addIconButton(removeExpression);
		addIconButton(copyExpression);

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

	public void addCopyExpressionButtonClickListener(Button.ClickListener listener) {
		if (!getDisabledButtons().contains(copyExpression)) {
			copyExpression.addClickListener(listener);
		}
	}

	public void removeRemoveExpressionButtonClickListener(Button.ClickListener listener) {
		removeExpression.removeClickListener(listener);
	}

	@Override
	public Set<Button> getSecuredButtons() {
		return new HashSet<>();
	}
}

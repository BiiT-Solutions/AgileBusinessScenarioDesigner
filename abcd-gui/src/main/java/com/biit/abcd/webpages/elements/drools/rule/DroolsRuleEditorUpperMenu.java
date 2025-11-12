package com.biit.abcd.webpages.elements.drools.rule;

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

public class DroolsRuleEditorUpperMenu extends UpperMenu {
	private static final long serialVersionUID = 4790116559655965919L;
	private IconButton saveButton, newRule, removeRule, copyRule;

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
		copyRule = new IconButton(LanguageCodes.MENU_RULE_DUPLICATE_CAPTION,
				ThemeIcon.COPY_ROW, LanguageCodes.MENU_RULE_DUPLICATE_TOOLTIP);

		addIconButton(saveButton);
		addIconButton(newRule);
		addIconButton(removeRule);
		addIconButton(copyRule);

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

	public void addCopyRuleButtonClickListener(Button.ClickListener listener) {
		if (!getDisabledButtons().contains(copyRule)) {
			copyRule.addClickListener(listener);
		}
	}

	@Override
	public Set<Button> getSecuredButtons() {
		return new HashSet<>();
	}

}

package com.biit.abcd.webpages.elements.form.variables;

import java.util.Set;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.webpages.components.IconButton;
import com.biit.abcd.webpages.components.ThemeIcon;
import com.biit.abcd.webpages.components.UpperMenu;
import com.vaadin.ui.Button;

public class FormVariablesUpperMenu extends UpperMenu {
	private static final long serialVersionUID = 851058355759250224L;
	private IconButton saveButton, addNewVariable, removeVariable;

	public FormVariablesUpperMenu() {
		super();
		defineMenu();
	}

	private void defineMenu() {
		// Save
		saveButton = new IconButton(LanguageCodes.MENU_SAVE, ThemeIcon.FORM_SAVE, LanguageCodes.MENU_SAVE);
		addIconButton(saveButton);

		// Add Variable
		addNewVariable = new IconButton(LanguageCodes.FORM_VARIABLE_ADD, ThemeIcon.FORM_VARIABLE_ADD,
				LanguageCodes.FORM_VARIABLE_ADD);
		addIconButton(addNewVariable);

		// Remove Variable
		removeVariable = new IconButton(LanguageCodes.FORM_VARIABLE_REMOVE, ThemeIcon.FORM_VARIABLE_REMOVE,
				LanguageCodes.FORM_VARIABLE_REMOVE);
		addIconButton(removeVariable);

		for (Button button : getDisabledButtons()) {
			button.setEnabled(false);
		}
	}

	public void addSaveButtonClickListener(Button.ClickListener listener) {
		if (!getDisabledButtons().contains(saveButton)) {
			saveButton.addClickListener(listener);
		}
	}

	public void addNewVariableButtonClickListener(Button.ClickListener listener) {
		if (!getDisabledButtons().contains(addNewVariable)) {
			addNewVariable.addClickListener(listener);
		}
	}

	public void addRemoveVariableButtonClickListener(Button.ClickListener listener) {
		if (!getDisabledButtons().contains(removeVariable)) {
			removeVariable.addClickListener(listener);
		}
	}
	
	@Override
	public Set<Button> getSecuredButtons() {
		return getButtons();
	}

}

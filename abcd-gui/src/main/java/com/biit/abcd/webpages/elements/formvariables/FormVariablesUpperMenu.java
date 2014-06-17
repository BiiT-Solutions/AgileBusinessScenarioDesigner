package com.biit.abcd.webpages.elements.formvariables;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.webpages.components.IconButton;
import com.biit.abcd.webpages.components.ThemeIcons;
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
		saveButton = new IconButton(LanguageCodes.MENU_SAVE, ThemeIcons.SAVE, LanguageCodes.MENU_SAVE);
		addIconButton(saveButton);

		// Add Variable
		addNewVariable = new IconButton(LanguageCodes.FORM_VARIABLE_ADD, ThemeIcons.VARIABLE_ADD, LanguageCodes.FORM_VARIABLE_ADD);
		addIconButton(addNewVariable);

		// Remove Variable
		removeVariable = new IconButton(LanguageCodes.FORM_VARIABLE_REMOVE, ThemeIcons.VARIABLE_REMOVE, LanguageCodes.FORM_VARIABLE_REMOVE);
		addIconButton(removeVariable);
	}

	public void addSaveButtonClickListener(Button.ClickListener listener) {
		saveButton.addClickListener(listener);
	}

	public void addNewVariableButtonClickListener(Button.ClickListener listener) {
		addNewVariable.addClickListener(listener);
	}

	public void addRemoveVariableButtonClickListener(Button.ClickListener listener) {
		removeVariable.addClickListener(listener);
	}

}

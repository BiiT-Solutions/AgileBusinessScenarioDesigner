package com.biit.abcd.webpages.elements.globalvariables;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.webpages.components.IconButton;
import com.biit.abcd.webpages.components.ThemeIcon;
import com.biit.abcd.webpages.components.UpperMenu;
import com.vaadin.ui.Button;

public class GlobalVariablesUpperMenu extends UpperMenu {
	private static final long serialVersionUID = -6726735583450218756L;

	private IconButton saveButton, addVariable, removeVariable, editVariable, addValue, removeValue, editValue;

	public GlobalVariablesUpperMenu() {
		super();
		defineMenu();
	}

	private void defineMenu() {
		saveButton = new IconButton(LanguageCodes.MENU_SAVE, ThemeIcon.SAVE, LanguageCodes.MENU_SAVE);
		addVariable = new IconButton(LanguageCodes.GLOBAL_VARIABLES_TABLE_ADD_VARIABLE_CAPTION,
				ThemeIcon.GLOBAL_VARIABLES_ADD_VARIABLE, LanguageCodes.GLOBAL_VARIABLES_TABLE_ADD_VARIABLE_TOOLTIP);
		removeVariable = new IconButton(LanguageCodes.GLOBAL_VARIABLES_TABLE_REMOVE_VARIABLE_CAPTION,
				ThemeIcon.GLOBAL_VARIABLES_REMOVE_VARIABLE,
				LanguageCodes.GLOBAL_VARIABLES_TABLE_REMOVE_VARIABLE_TOOLTIP);
		editVariable = new IconButton(LanguageCodes.GLOBAL_VARIABLES_TABLE_EDIT_VARIABLE_CAPTION,
				ThemeIcon.GLOBAL_VARIABLES_EDIT_VARIABLE, LanguageCodes.GLOBAL_VARIABLES_TABLE_EDIT_VARIABLE_TOOLTIP);
		addValue = new IconButton(LanguageCodes.GLOBAL_VARIABLES_TABLE_ADD_DATA_CAPTION,
				ThemeIcon.GLOBAL_VARIABLES_ADD_VALUE, LanguageCodes.GLOBAL_VARIABLES_TABLE_ADD_DATA_TOOLTIP);
		removeValue = new IconButton(LanguageCodes.GLOBAL_VARIABLES_TABLE_REMOVE_DATA_CAPTION,
				ThemeIcon.GLOBAL_VARIABLES_REMOVE_VALUE, LanguageCodes.GLOBAL_VARIABLES_TABLE_REMOVE_DATA_TOOLTIP);
		editValue = new IconButton(LanguageCodes.GLOBAL_VARIABLES_TABLE_EDIT_DATA_CAPTION,
				ThemeIcon.GLOBAL_VARIABLES_EDIT_VALUE, LanguageCodes.GLOBAL_VARIABLES_TABLE_EDIT_DATA_TOOLTIP);

		addIconButton(saveButton);
		addIconButton(addVariable);
		addIconButton(removeVariable);
		addIconButton(editVariable);
		addIconButton(addValue);
		addIconButton(removeValue);
		addIconButton(editValue);
	}

	public void addAddVariableButtonClickListener(Button.ClickListener listener) {
		addVariable.addClickListener(listener);
	}

	public void removeAddVariableButtonClickListener(Button.ClickListener listener) {
		addVariable.removeClickListener(listener);
	}

	public void addRemoveVariableButtonClickListener(Button.ClickListener listener) {
		removeVariable.addClickListener(listener);
	}

	public void removeRemoveVariableButtonClickListener(Button.ClickListener listener) {
		removeVariable.removeClickListener(listener);
	}
	
	public void addEditVariableButtonClickListener(Button.ClickListener listener) {
		editVariable.addClickListener(listener);
	}

	public void removeEditVariableButtonClickListener(Button.ClickListener listener) {
		editVariable.removeClickListener(listener);
	}

	public void addAddValueButtonClickListener(Button.ClickListener listener) {
		addValue.addClickListener(listener);
	}

	public void removeAddValueClickListener(Button.ClickListener listener) {
		addValue.removeClickListener(listener);
	}

	public void addRemoveValueButtonClickListener(Button.ClickListener listener) {
		removeValue.addClickListener(listener);
	}

	public void removeRemoveValueClickListener(Button.ClickListener listener) {
		removeValue.removeClickListener(listener);
	}
	
	public void addEditValueButtonClickListener(Button.ClickListener listener) {
		editValue.addClickListener(listener);
	}

	public void removeEditValueClickListener(Button.ClickListener listener) {
		editValue.removeClickListener(listener);
	}

	public void addSaveButtonClickListener(Button.ClickListener listener) {
		saveButton.addClickListener(listener);
	}
}

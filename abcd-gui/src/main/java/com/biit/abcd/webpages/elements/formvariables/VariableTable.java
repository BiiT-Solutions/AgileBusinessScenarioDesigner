package com.biit.abcd.webpages.elements.formvariables;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.CustomVariableScope;
import com.biit.abcd.persistence.entity.CustomVariableType;
import com.vaadin.data.Item;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;

public class VariableTable extends Table {
	private static final long serialVersionUID = 3067131269771569684L;

	enum FormVariablesProperties {
		VARIABLE_NAME, TYPE, SCOPE;
	};

	public VariableTable() {
		initContainerProperties();
	}

	private void initContainerProperties() {
		setSelectable(true);
		setImmediate(true);
		setMultiSelect(false);
		setSizeFull();

		setColumnCollapsingAllowed(false);

		addContainerProperty(FormVariablesProperties.VARIABLE_NAME, TextField.class, "",
				ServerTranslate.tr(LanguageCodes.FORM_VARIABLE_COLUMN_NAME), null, Align.LEFT);

		addContainerProperty(FormVariablesProperties.TYPE, ComboBox.class, "",
				ServerTranslate.tr(LanguageCodes.FORM_VARIABLE_COLUMN_TYPE), null, Align.LEFT);

		addContainerProperty(FormVariablesProperties.SCOPE, ComboBox.class, "",
				ServerTranslate.tr(LanguageCodes.FORM_VARIABLE_SCOPE), null, Align.LEFT);

		setColumnExpandRatio(FormVariablesProperties.VARIABLE_NAME, 1);
		setColumnExpandRatio(FormVariablesProperties.TYPE, 1);
		setColumnExpandRatio(FormVariablesProperties.SCOPE, 1);

		// setCellStyleGenerator(new FormTreeTableCellStyleGenerator());
	}

	@SuppressWarnings({ "unchecked" })
	public void addRow(CustomVariable customVariable) {
		Item item = addItem(customVariable);
		TextField nameTextField = createTextField();
		nameTextField.setValue(customVariable.getName());
		item.getItemProperty(FormVariablesProperties.VARIABLE_NAME).setValue(nameTextField);
		ComboBox typeComboBox = createTypeComboBox();
		typeComboBox.setValue(ServerTranslate.tr(customVariable.getType().getTranslationCode()));
		item.getItemProperty(FormVariablesProperties.TYPE).setValue(typeComboBox);
		ComboBox scopeComboBox = createScopeComboBox();
		scopeComboBox.setValue(ServerTranslate.tr(customVariable.getScope().getTranslationCode()));
		item.getItemProperty(FormVariablesProperties.SCOPE).setValue(scopeComboBox);
	}

	public TextField createTextField() {
		TextField nameTextField = new TextField();
		return nameTextField;
	}

	public ComboBox createTypeComboBox() {
		ComboBox typeComboBox = new ComboBox();
		for (CustomVariableType variableType : CustomVariableType.values()) {
			typeComboBox.addItem(ServerTranslate.tr(variableType.getTranslationCode()));
		}
		typeComboBox.setNullSelectionAllowed(false);
		return typeComboBox;
	}

	public ComboBox createScopeComboBox() {
		ComboBox scopeComboBox = new ComboBox();
		for (CustomVariableScope variablesScope : CustomVariableScope.values()) {
			scopeComboBox.addItem(ServerTranslate.tr(variablesScope.getTranslationCode()));
		}
		scopeComboBox.setNullSelectionAllowed(false);
		return scopeComboBox;
	}

	class VariableDefinition {

	}

}

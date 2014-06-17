package com.biit.abcd.webpages.elements.formvariables;

import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.CustomVariableScope;
import com.biit.abcd.persistence.entity.CustomVariableType;
import com.vaadin.data.Item;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
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
		TextField nameTextField = createTextField(customVariable);
		nameTextField.setValue(customVariable.getName());
		item.getItemProperty(FormVariablesProperties.VARIABLE_NAME).setValue(nameTextField);
		ComboBox typeComboBox = createTypeComboBox(customVariable);
		typeComboBox.setValue(customVariable.getType());
		item.getItemProperty(FormVariablesProperties.TYPE).setValue(typeComboBox);
		ComboBox scopeComboBox = createScopeComboBox(customVariable);
		scopeComboBox.setValue(customVariable.getScope());
		item.getItemProperty(FormVariablesProperties.SCOPE).setValue(scopeComboBox);
	}

	public void removeSelectedRows() {
		CustomVariable variable = (CustomVariable) getValue();
		if (variable != null) {
			removeItem(variable);
		}
	}

	public TextField createTextField(final CustomVariable customVariable) {
		final VariableTable thisTable = this;
		TextField nameTextField = new TextField();
		nameTextField.addFocusListener(new FocusListener() {

			@Override
			public void focus(FocusEvent event) {
				thisTable.select(customVariable);
			}
		});
		return nameTextField;
	}

	public ComboBox createTypeComboBox(final CustomVariable customVariable) {
		final VariableTable thisTable = this;
		ComboBox typeComboBox = new ComboBox();
		for (CustomVariableType variableType : CustomVariableType.values()) {
			typeComboBox.addItem(variableType);
			typeComboBox.setItemCaption(variableType, ServerTranslate.tr(variableType.getTranslationCode()));
		}
		typeComboBox.setNullSelectionAllowed(false);
		typeComboBox.addFocusListener(new FocusListener() {

			@Override
			public void focus(FocusEvent event) {
				thisTable.select(customVariable);
			}
		});
		return typeComboBox;
	}

	public ComboBox createScopeComboBox(final CustomVariable customVariable) {
		final VariableTable thisTable = this;
		ComboBox scopeComboBox = new ComboBox();
		for (CustomVariableScope variablesScope : CustomVariableScope.values()) {
			scopeComboBox.addItem(variablesScope);
			scopeComboBox.setItemCaption(variablesScope, ServerTranslate.tr(variablesScope.getTranslationCode()));
		}
		scopeComboBox.setNullSelectionAllowed(false);
		scopeComboBox.addFocusListener(new FocusListener() {

			@Override
			public void focus(FocusEvent event) {
				thisTable.select(customVariable);
			}
		});
		return scopeComboBox;
	}

	public List<CustomVariable> getCustomVariables() {
		List<CustomVariable> customVariables = new ArrayList<>();
		for (Object itemId : getItemIds()) {
			CustomVariable customVariable = (CustomVariable) itemId;
			String newName = (String) ((TextField) getItem(itemId).getItemProperty(
					FormVariablesProperties.VARIABLE_NAME).getValue()).getValue();
			if (!newName.equals(customVariable.getName())) {
				updateInfo(customVariable);
				customVariable.setName(newName);
			}
			CustomVariableType newType = (CustomVariableType) ((ComboBox) getItem(itemId).getItemProperty(
					FormVariablesProperties.TYPE).getValue()).getValue();
			if (!newType.equals(customVariable.getType())) {
				updateInfo(customVariable);
				customVariable.setType(newType);
			}
			CustomVariableScope newScope = (CustomVariableScope) ((ComboBox) getItem(itemId).getItemProperty(
					FormVariablesProperties.SCOPE).getValue()).getValue();
			if (!newScope.equals(customVariable.getScope())) {
				customVariable.setScope(newScope);
			}
			customVariables.add(customVariable);
		}
		return customVariables;
	}

	private void updateInfo(CustomVariable customVariable) {
		customVariable.setUpdatedBy(UserSessionHandler.getUser().getUserId());
		customVariable.setUpdateTime();
	}
}

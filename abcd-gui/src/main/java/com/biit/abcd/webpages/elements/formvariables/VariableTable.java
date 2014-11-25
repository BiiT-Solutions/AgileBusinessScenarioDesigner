package com.biit.abcd.webpages.elements.formvariables;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.CustomVariableScope;
import com.biit.abcd.persistence.entity.CustomVariableType;
import com.biit.abcd.security.AbcdFormAuthorizationService;
import com.vaadin.data.Item;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;

public class VariableTable extends Table {
	private static final long serialVersionUID = 3067131269771569684L;
	private boolean protectedElements = false;

	enum FormVariablesProperties {
		VARIABLE_NAME, TYPE, SCOPE;
	};

	public VariableTable() {
		initContainerProperties();
		protectedElements = AbcdFormAuthorizationService.getInstance().isFormReadOnly(
				UserSessionHandler.getFormController().getForm(), UserSessionHandler.getUser());
	}

	private void initContainerProperties() {
		setSelectable(true);
		setImmediate(true);
		setMultiSelect(false);
		setSizeFull();

		setColumnCollapsingAllowed(false);

		addContainerProperty(FormVariablesProperties.VARIABLE_NAME, TextField.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_VARIABLE_COLUMN_NAME), null, Align.LEFT);

		addContainerProperty(FormVariablesProperties.TYPE, ComboBox.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_VARIABLE_COLUMN_TYPE), null, Align.LEFT);

		addContainerProperty(FormVariablesProperties.SCOPE, ComboBox.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_VARIABLE_SCOPE), null, Align.LEFT);

		setColumnExpandRatio(FormVariablesProperties.VARIABLE_NAME, 1);
		setColumnExpandRatio(FormVariablesProperties.TYPE, 1);
		setColumnExpandRatio(FormVariablesProperties.SCOPE, 1);
	}

	@SuppressWarnings({ "unchecked" })
	public void addRow(CustomVariable customVariable) {
		Item item = addItem(customVariable);
		TextField nameTextField = createTextField(customVariable);
		nameTextField.setValue(customVariable.getName());
		nameTextField.setEnabled(!protectedElements);
		item.getItemProperty(FormVariablesProperties.VARIABLE_NAME).setValue(nameTextField);

		ComboBox typeComboBox = createTypeComboBox(customVariable);
		typeComboBox.setValue(customVariable.getType());
		typeComboBox.setEnabled(!protectedElements);
		item.getItemProperty(FormVariablesProperties.TYPE).setValue(typeComboBox);

		ComboBox scopeComboBox = createScopeComboBox(customVariable);
		scopeComboBox.setValue(customVariable.getScope());
		scopeComboBox.setEnabled(!protectedElements);
		item.getItemProperty(FormVariablesProperties.SCOPE).setValue(scopeComboBox);
	}

	public void removeSelectedRow() {
		CustomVariable variable = (CustomVariable) getValue();
		if (variable != null) {
			removeItem(variable);
		}
	}

	public TextField createTextField(final CustomVariable customVariable) {
		final VariableTable thisTable = this;
		final TextField nameTextField = new TextField();
		nameTextField.addFocusListener(new FocusListener() {
			private static final long serialVersionUID = -7714530380809948035L;

			@Override
			public void focus(FocusEvent event) {
				thisTable.select(customVariable);
			}
		});
		nameTextField.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 8130288971788878223L;

			@Override
			public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
				customVariable.setName(nameTextField.getValue());
				updateInfo(customVariable);
			}
		});
		return nameTextField;
	}

	public ComboBox createTypeComboBox(final CustomVariable customVariable) {
		final VariableTable thisTable = this;
		final ComboBox typeComboBox = new ComboBox();
		for (CustomVariableType variableType : CustomVariableType.values()) {
			typeComboBox.addItem(variableType);
			typeComboBox.setItemCaption(variableType, ServerTranslate.translate(variableType.getTranslationCode()));
		}
		typeComboBox.setNullSelectionAllowed(false);
		typeComboBox.addFocusListener(new FocusListener() {
			private static final long serialVersionUID = -6584097055890517037L;

			@Override
			public void focus(FocusEvent event) {
				thisTable.select(customVariable);
			}
		});
		typeComboBox.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -4030501323342468951L;

			@Override
			public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
				customVariable.setType((CustomVariableType) typeComboBox.getValue());
				updateInfo(customVariable);
			}
		});
		return typeComboBox;
	}

	public ComboBox createScopeComboBox(final CustomVariable customVariable) {
		final VariableTable thisTable = this;
		final ComboBox scopeComboBox = new ComboBox();
		for (CustomVariableScope variablesScope : CustomVariableScope.values()) {
			scopeComboBox.addItem(variablesScope);
			scopeComboBox
					.setItemCaption(variablesScope, ServerTranslate.translate(variablesScope.getTranslationCode()));
		}
		scopeComboBox.setNullSelectionAllowed(false);
		scopeComboBox.addFocusListener(new FocusListener() {
			private static final long serialVersionUID = -7131776594272787546L;

			@Override
			public void focus(FocusEvent event) {
				thisTable.select(customVariable);
			}
		});
		scopeComboBox.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 2144580236318623636L;

			@Override
			public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
				customVariable.setScope((CustomVariableScope) scopeComboBox.getValue());
				updateInfo(customVariable);
			}
		});
		return scopeComboBox;
	}

	private void updateInfo(CustomVariable customVariable) {
		customVariable.setUpdatedBy(UserSessionHandler.getUser().getUserId());
		customVariable.setUpdateTime();
	}
}

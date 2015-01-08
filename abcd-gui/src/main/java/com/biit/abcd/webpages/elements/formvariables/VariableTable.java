package com.biit.abcd.webpages.elements.formvariables;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.configuration.AbcdConfigurationReader;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.CustomVariableScope;
import com.biit.abcd.persistence.entity.CustomVariableType;
import com.biit.abcd.persistence.utils.CheckDependencies;
import com.biit.abcd.security.AbcdFormAuthorizationService;
import com.biit.abcd.webpages.components.ComparableComboBox;
import com.biit.abcd.webpages.components.ComparableTextField;
import com.biit.form.exceptions.DependencyExistException;
import com.vaadin.data.Item;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Table;

public class VariableTable extends Table {
	private static final long serialVersionUID = 3067131269771569684L;
	private boolean protectedElements = false;

	enum FormVariablesProperties {
		VARIABLE_NAME, TYPE, SCOPE, DEFAULT_VALUE;
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
		addContainerProperty(FormVariablesProperties.VARIABLE_NAME, ComparableTextField.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_VARIABLE_COLUMN_NAME), null, Align.LEFT);

		addContainerProperty(FormVariablesProperties.TYPE, ComboBox.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_VARIABLE_COLUMN_TYPE), null, Align.LEFT);

		addContainerProperty(FormVariablesProperties.SCOPE, ComparableComboBox.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_VARIABLE_COLUMN_SCOPE), null, Align.LEFT);

		addContainerProperty(FormVariablesProperties.DEFAULT_VALUE, ComparableTextField.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_VARIABLE_COLUMN_DEFAULT_VALUE), null, Align.LEFT);

		setColumnExpandRatio(FormVariablesProperties.VARIABLE_NAME, 1);
		setColumnExpandRatio(FormVariablesProperties.TYPE, 1);
		setColumnExpandRatio(FormVariablesProperties.SCOPE, 1);
		setColumnExpandRatio(FormVariablesProperties.DEFAULT_VALUE, 1);

		setSortContainerPropertyId(FormVariablesProperties.VARIABLE_NAME);
	}

	public void defaultSort() {
		sort(new Object[] { FormVariablesProperties.SCOPE, FormVariablesProperties.VARIABLE_NAME }, new boolean[] {
				true, true });
	}

	@SuppressWarnings({ "unchecked" })
	public void addRow(final CustomVariable customVariable) {
		Item item = addItem(customVariable);
		final ComparableTextField nameTextField = createTextField(customVariable);
		nameTextField.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 8130288971788878223L;

			@Override
			public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
				customVariable.setName(nameTextField.getValue());
				updateInfo(customVariable);
			}
		});
		nameTextField.setValue(customVariable.getName());
		nameTextField.setEnabled(!protectedElements);
		item.getItemProperty(FormVariablesProperties.VARIABLE_NAME).setValue(nameTextField);

		final ComparableTextField defaultValueTextField = createTextField(customVariable);
		defaultValueTextField.setImmediate(true);
		addDefaultValueValidators(customVariable, defaultValueTextField);
		defaultValueTextField.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 8846164295852977149L;

			@Override
			public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
				try {
					defaultValueTextField.validate();
					if (defaultValueTextField.getValue() != null) {
						String oldValue = customVariable.getDefaultValue();
						customVariable.setDefaultValue(defaultValueTextField.getValue());
						updateInfo(customVariable);
						
						AbcdLogger.info(this.getClass().getName(), "User '"
								+ UserSessionHandler.getUser().getEmailAddress()
								+ "' has changed the property 'Default Value' of the class '" + customVariable.getClass()
								+ "' from '" + oldValue + "' to '" + customVariable.getDefaultValue() + "'.");
					}
				} catch (InvalidValueException e) {
					AbcdLogger.errorMessage(this.getClass().getName(), e);
					MessageManager.showWarning(LanguageCodes.ERROR_INVALID_VALUE);
				}
			}
		});
		defaultValueTextField.setValue((customVariable.getDefaultValue() == null ? "" : customVariable
				.getDefaultValue()));
		defaultValueTextField.setEnabled(!protectedElements);
		item.getItemProperty(FormVariablesProperties.DEFAULT_VALUE).setValue(defaultValueTextField);

		ComboBox typeComboBox = createTypeComboBox(customVariable, defaultValueTextField);
		typeComboBox.setValue(customVariable.getType());
		typeComboBox.setEnabled(!protectedElements);
		typeComboBox.setImmediate(true);
		item.getItemProperty(FormVariablesProperties.TYPE).setValue(typeComboBox);

		ComboBox scopeComboBox = createScopeComboBox(customVariable);
		scopeComboBox.setValue(customVariable.getScope());
		scopeComboBox.setEnabled(!protectedElements);
		scopeComboBox.setImmediate(true);
		item.getItemProperty(FormVariablesProperties.SCOPE).setValue(scopeComboBox);
	}

	public void removeSelectedRow() {
		CustomVariable variable = (CustomVariable) getValue();
		if (variable != null) {
			removeItem(variable);
		}
	}

	public ComparableTextField createTextField(final CustomVariable customVariable) {
		final VariableTable thisTable = this;
		final ComparableTextField nameTextField = new ComparableTextField();
		nameTextField.addFocusListener(new FocusListener() {
			private static final long serialVersionUID = -7714530380809948035L;

			@Override
			public void focus(FocusEvent event) {
				thisTable.select(customVariable);
			}
		});
		return nameTextField;
	}

	public ComboBox createTypeComboBox(final CustomVariable customVariable,
			final ComparableTextField defaultValueTextField) {
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
				if (customVariable.getType() != null && typeComboBox.getValue() != null
						&& !customVariable.getType().equals((CustomVariableType) typeComboBox.getValue())) {
					try {
						CheckDependencies.checkCustomVariableDependencies(UserSessionHandler.getFormController()
								.getForm(), customVariable);
						CustomVariableType oldType = customVariable.getType();
						customVariable.setType((CustomVariableType) typeComboBox.getValue());
						updateInfo(customVariable);

						if (oldType != customVariable.getType()) {
							customVariable.setDefaultValue(null);
							addDefaultValueValidators(customVariable, defaultValueTextField);
						}

						AbcdLogger.info(this.getClass().getName(), "User '"
								+ UserSessionHandler.getUser().getEmailAddress()
								+ "' has changed the property Type of the class '" + customVariable.getClass()
								+ "' from '" + oldType + "' to '" + customVariable.getType() + "'.");
					} catch (DependencyExistException e) {
						// Forbid the edit action if exist dependency.
						MessageManager.showWarning(LanguageCodes.VARIABLE_DESIGNER_WARNING_CANNOT_REMOVE_VARIABLE);
						typeComboBox.setValue(customVariable.getType());
					}
				}
			}
		});
		return typeComboBox;
	}

	public ComboBox createScopeComboBox(final CustomVariable customVariable) {
		final VariableTable thisTable = this;
		final ComboBox scopeComboBox = new ComparableComboBox();
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
				if (customVariable.getScope() != null && scopeComboBox.getValue() != null
						&& !customVariable.getScope().equals((CustomVariableScope) scopeComboBox.getValue())) {
					try {
						CheckDependencies.checkCustomVariableDependencies(UserSessionHandler.getFormController()
								.getForm(), customVariable);
						CustomVariableScope oldScope = customVariable.getScope();
						customVariable.setScope((CustomVariableScope) scopeComboBox.getValue());
						updateInfo(customVariable);

						AbcdLogger.info(this.getClass().getName(), "User '"
								+ UserSessionHandler.getUser().getEmailAddress()
								+ "' has changed the property Scope of the class '" + customVariable.getClass()
								+ "' from '" + oldScope + "' to '" + customVariable.getScope() + "'.");
					} catch (DependencyExistException e) {
						// Forbid the edit action if exist dependency.
						MessageManager.showWarning(LanguageCodes.VARIABLE_DESIGNER_WARNING_CANNOT_REMOVE_VARIABLE);
						scopeComboBox.setValue(customVariable.getScope());
					}
				}
			}
		});
		return scopeComboBox;
	}

	private void updateInfo(CustomVariable customVariable) {
		customVariable.setUpdatedBy(UserSessionHandler.getUser().getUserId());
		customVariable.setUpdateTime();
	}

	private void addDefaultValueValidators(CustomVariable customVariable, ComparableTextField textField) {
		textField.removeAllValidators();
		switch (customVariable.getType()) {
		case NUMBER:
			textField.addValidator(new RegexpValidator(AbcdConfigurationReader.getInstance().getNumberMask(),
					ServerTranslate.translate(LanguageCodes.ERROR_INVALID_VALUE)));
			textField.setInputPrompt(AbcdConfigurationReader.getInstance().getNumberPromt());
			break;
		case DATE:
			textField.addValidator(new RegexpValidator(AbcdConfigurationReader.getInstance().getDateMask(),
					ServerTranslate.translate(LanguageCodes.ERROR_INVALID_DATE)));
			textField.setInputPrompt(AbcdConfigurationReader.getInstance().getDatePromt());
			break;
		default:
			// String value as default, we do not validate
			// anything
			textField.setInputPrompt(AbcdConfigurationReader.getInstance().getTextPromt());
			break;
		}
	}
}

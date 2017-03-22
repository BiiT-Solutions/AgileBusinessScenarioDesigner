package com.biit.abcd.webpages.elements.formvariables;

import java.text.ParseException;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.core.SpringContextHelper;
import com.biit.abcd.language.CustomVariableScopeUi;
import com.biit.abcd.language.CustomVariableTypeUi;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.language.UserLocaleStringToDoubleConverter;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.CustomVariableScope;
import com.biit.abcd.persistence.entity.CustomVariableType;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTimestamp;
import com.biit.abcd.persistence.utils.CheckDependencies;
import com.biit.abcd.security.IAbcdFormAuthorizationService;
import com.biit.abcd.webpages.components.ComparableComboBox;
import com.biit.abcd.webpages.components.ComparableTextField;
import com.biit.form.exceptions.DependencyExistException;
import com.vaadin.data.Item;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.util.converter.Converter.ConversionException;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;

public class VariableTable extends Table {
	private static final long serialVersionUID = 3067131269771569684L;
	private static final String VARIABLE_REGEX = "([a-zA-Z])([a-zA-Z0-9]){2,}";
	private boolean protectedElements = false;

	private IAbcdFormAuthorizationService securityService;

	enum FormVariablesProperties {
		VARIABLE_NAME, TYPE, SCOPE, DEFAULT_VALUE;
	};

	public VariableTable() {
		initContainerProperties();
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		securityService = (IAbcdFormAuthorizationService) helper.getBean("abcdSecurityService");
		protectedElements = securityService.isFormReadOnly(UserSessionHandler.getFormController().getForm(), UserSessionHandler.getUser());
	}

	private void initContainerProperties() {
		setSelectable(true);
		setImmediate(true);
		setMultiSelect(false);
		setSizeFull();
		// Avoid paging
		setPageLength(0);

		setColumnCollapsingAllowed(false);
		addContainerProperty(FormVariablesProperties.VARIABLE_NAME, ComparableTextField.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_VARIABLE_COLUMN_NAME), null, Align.LEFT);

		addContainerProperty(FormVariablesProperties.TYPE, ComboBox.class, "", ServerTranslate.translate(LanguageCodes.FORM_VARIABLE_COLUMN_TYPE), null,
				Align.LEFT);

		addContainerProperty(FormVariablesProperties.SCOPE, ComparableComboBox.class, "", ServerTranslate.translate(LanguageCodes.FORM_VARIABLE_COLUMN_SCOPE),
				null, Align.LEFT);

		addContainerProperty(FormVariablesProperties.DEFAULT_VALUE, AbstractField.class, "",
				ServerTranslate.translate(LanguageCodes.FORM_VARIABLE_COLUMN_DEFAULT_VALUE), null, Align.LEFT);

		setColumnExpandRatio(FormVariablesProperties.VARIABLE_NAME, 1);
		setColumnExpandRatio(FormVariablesProperties.TYPE, 1);
		setColumnExpandRatio(FormVariablesProperties.SCOPE, 1);
		setColumnExpandRatio(FormVariablesProperties.DEFAULT_VALUE, 1);

		setSortContainerPropertyId(FormVariablesProperties.VARIABLE_NAME);
	}

	public void defaultSort() {
		sort(new Object[] { FormVariablesProperties.SCOPE, FormVariablesProperties.VARIABLE_NAME }, new boolean[] { true, true });
	}

	@SuppressWarnings({ "unchecked" })
	public void addRow(final CustomVariable customVariable) {
		Item item = addItem(customVariable);
		final ComparableTextField nameTextField = createTextField(customVariable);
		nameTextField.addValidator(new RegexpValidator(VARIABLE_REGEX, LanguageCodes.FORM_VARIABLE_REGEX_ERROR.translate()));
		nameTextField.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 8130288971788878223L;

			@Override
			public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
				if (nameTextField.isValid()) {
					customVariable.setName(nameTextField.getValue());
					updateInfo(customVariable);
				}
			}
		});
		nameTextField.setValue(customVariable.getName());
		nameTextField.setEnabled(!protectedElements);
		item.getItemProperty(FormVariablesProperties.VARIABLE_NAME).setValue(nameTextField);

		final AbstractField<?> defaultValueField = createDefaultValueField(customVariable);
		addDefaultValueFieldValidatorsAndListeners(customVariable, defaultValueField);
		item.getItemProperty(FormVariablesProperties.DEFAULT_VALUE).setValue(defaultValueField);

		ComboBox typeComboBox = createTypeComboBox(customVariable, item);
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

	public ComboBox createTypeComboBox(final CustomVariable customVariable, final Item item) {
		final VariableTable thisTable = this;
		final ComboBox typeComboBox = new ComboBox();
		for (CustomVariableTypeUi variableType : CustomVariableTypeUi.values()) {
			typeComboBox.addItem(variableType.getCustomvariable());
			typeComboBox.setItemCaption(variableType.getCustomvariable(), ServerTranslate.translate(variableType.getLanguageCode()));
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

			@SuppressWarnings("unchecked")
			@Override
			public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
				if (customVariable.getType() != null && typeComboBox.getValue() != null
						&& !customVariable.getType().equals((CustomVariableType) typeComboBox.getValue())) {
					try {
						CheckDependencies.checkCustomVariableDependencies(UserSessionHandler.getFormController().getForm(), customVariable);
						CustomVariableType oldType = customVariable.getType();
						customVariable.setType((CustomVariableType) typeComboBox.getValue());
						updateInfo(customVariable);

						if (oldType != customVariable.getType()) {
							customVariable.setDefaultValue(null);
							final AbstractField<?> defaultValueField = createDefaultValueField(customVariable);
							addDefaultValueFieldValidatorsAndListeners(customVariable, defaultValueField);
							item.getItemProperty(FormVariablesProperties.DEFAULT_VALUE).setValue(defaultValueField);
						}

						AbcdLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress()
								+ "' has changed the property Type of the class '" + customVariable.getClass() + "' from '" + oldType + "' to '"
								+ customVariable.getType() + "'.");
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
		for (CustomVariableScopeUi variablesScope : CustomVariableScopeUi.values()) {
			scopeComboBox.addItem(variablesScope.getVariableScope());
			scopeComboBox.setItemCaption(variablesScope.getVariableScope(), ServerTranslate.translate(variablesScope.getLanguageCode()));
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
						CheckDependencies.checkCustomVariableDependencies(UserSessionHandler.getFormController().getForm(), customVariable);
						CustomVariableScope oldScope = customVariable.getScope();
						customVariable.setScope((CustomVariableScope) scopeComboBox.getValue());
						updateInfo(customVariable);

						AbcdLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress()
								+ "' has changed the property Scope of the class '" + customVariable.getClass() + "' from '" + oldScope + "' to '"
								+ customVariable.getScope() + "'.");
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
		customVariable.setUpdatedBy(UserSessionHandler.getUser().getId());
		customVariable.setUpdateTime();
	}

	private AbstractField<?> createDefaultValueField(CustomVariable customVariable) {
		AbstractField<?> defaultValueField = null;
		switch (customVariable.getType()) {
		case DATE:
			defaultValueField = new DateField();
			break;
		case NUMBER:
		case STRING:
			defaultValueField = new TextField();
			break;
		}
		if (defaultValueField != null) {
			defaultValueField.setImmediate(true);
			if (defaultValueField instanceof TextField) {
				((TextField) defaultValueField).setNullRepresentation("");
			}
			setPromtAndLocale(customVariable, defaultValueField);
		}
		return defaultValueField;
	}

	private void addDefaultValueFieldValidatorsAndListeners(final CustomVariable customVariable, final AbstractField<?> defaultValueField) {
		setValueDefaultValueField(customVariable, defaultValueField);
		defaultValueField.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 7024147984616759115L;

			@Override
			public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
				try {
					defaultValueField.validate();
					if (defaultValueField.getValue() != null) {
						String oldValue = customVariable.getDefaultValue();
						switch (customVariable.getType()) {
						case DATE:
							customVariable.setDefaultValue(ExpressionValueTimestamp.getFormatter().format(((DateField) defaultValueField).getValue()));
							break;
						case NUMBER:
							Double a = (Double) defaultValueField.getConvertedValue();
							customVariable.setDefaultValue(a + "");
							break;
						case STRING:
							customVariable.setDefaultValue(defaultValueField.getValue() + "");
							break;
						}
						updateInfo(customVariable);
						AbcdLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress()
								+ "' has changed the property 'Default Value' of the class '" + customVariable.getClass() + "' from '" + oldValue + "' to '"
								+ customVariable.getDefaultValue() + "'");
					}
					// The value can be set to null again
					else {
						String oldValue = customVariable.getDefaultValue();
						switch (customVariable.getType()) {
						case DATE:
						case NUMBER:
						case STRING:
							customVariable.setDefaultValue(null);
							break;
						}
						updateInfo(customVariable);
						AbcdLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress()
								+ "' has changed the property 'Default Value' of the class '" + customVariable.getClass() + "' from '" + oldValue + "' to '"
								+ customVariable.getDefaultValue() + "'");
					}
				} catch (InvalidValueException e) {
					AbcdLogger.errorMessage(this.getClass().getName(), e);
					MessageManager.showWarning(LanguageCodes.ERROR_INVALID_VALUE);
					customVariable.setDefaultValue(null);
					defaultValueField.setValue(null);
				}
			}
		});
		defaultValueField.setEnabled(!protectedElements);
	}

	private void setPromtAndLocale(CustomVariable customVariable, AbstractField<?> defaultValueField) {
		switch (customVariable.getType()) {
		case DATE:
			break;
		case NUMBER:
			((TextField) defaultValueField).setInputPrompt(ServerTranslate.translate(LanguageCodes.INPUT_PROMPT_FLOAT));
			((TextField) defaultValueField).setConverter(new UserLocaleStringToDoubleConverter());
			break;
		case STRING:
			((TextField) defaultValueField).setInputPrompt(ServerTranslate.translate(LanguageCodes.INPUT_PROMPT_TEXT));
			((TextField) defaultValueField).setConverter(String.class);
			break;
		}
	}

	public void setValueDefaultValueField(CustomVariable customVariable, AbstractField<?> defaultValueField) {
		if (customVariable.getDefaultValue() != null) {
			switch (customVariable.getType()) {
			case DATE:
				try {
					((DateField) defaultValueField).setValue(ExpressionValueTimestamp.getFormatter().parse(customVariable.getDefaultValue()));
				} catch (ReadOnlyException | ConversionException | ParseException e) {
					setValue(null);
				}
				break;
			case NUMBER:
			case STRING:
				((TextField) defaultValueField).setValue(customVariable.getDefaultValue() + "");
				break;
			default:
				setValue(null);
				break;
			}
		}
	}
}

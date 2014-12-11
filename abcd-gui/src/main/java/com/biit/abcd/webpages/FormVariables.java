package com.biit.abcd.webpages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.exception.ConstraintViolationException;

import com.biit.abcd.ApplicationFrame;
import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.core.exceptions.DuplicatedVariableException;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.CustomVariableScope;
import com.biit.abcd.persistence.entity.CustomVariableType;
import com.biit.abcd.persistence.utils.CheckDependencies;
import com.biit.abcd.security.AbcdActivity;
import com.biit.abcd.webpages.components.FormWebPageComponent;
import com.biit.abcd.webpages.elements.formvariables.FormVariablesUpperMenu;
import com.biit.abcd.webpages.elements.formvariables.VariableTable;
import com.biit.form.exceptions.DependencyExistException;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class FormVariables extends FormWebPageComponent {
	private static final long serialVersionUID = 8796076485600899730L;
	private static final List<AbcdActivity> activityPermissions = new ArrayList<AbcdActivity>(
			Arrays.asList(AbcdActivity.READ));
	private FormVariablesUpperMenu upperMenu;
	private VariableTable variableTable;

	public FormVariables() {
		updateButtons(true);
	}

	@Override
	protected void initContent() {
		upperMenu = initUpperMenu();
		setUpperMenu(upperMenu);

		variableTable = new VariableTable();
		variableTable.setSizeFull();
		variableTable.setSelectable(true);
		variableTable.setImmediate(true);

		getWorkingAreaLayout().addComponent(variableTable);

		if (UserSessionHandler.getFormController().getForm() == null) {
			AbcdLogger.warning(this.getClass().getName(), "No Form selected, redirecting to Form Manager.");
			ApplicationFrame.navigateTo(WebMap.FORM_MANAGER);
		} else {
			if (variableTable != null) {
				for (CustomVariable customVariable : UserSessionHandler.getFormController().getForm()
						.getCustomVariables()) {
					variableTable.addRow(customVariable);
				}
			}
		}

		// Sort the values
		variableTable.defaultSort();
	}

	private FormVariablesUpperMenu initUpperMenu() {
		FormVariablesUpperMenu upperMenu = new FormVariablesUpperMenu();

		upperMenu.addSaveButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = 7788465178005102302L;

			@Override
			public void buttonClick(ClickEvent event) {
				save();
			}
		});

		upperMenu.addNewVariableButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = 2218717226699355001L;

			@Override
			public void buttonClick(ClickEvent event) {
				CustomVariable customVariable = new CustomVariable(UserSessionHandler.getFormController().getForm(),
						"", CustomVariableType.STRING, CustomVariableScope.FORM);
				customVariable.setCreatedBy(UserSessionHandler.getUser().getUserId());
				addNewVariable(customVariable);
				UserSessionHandler.getFormController().getForm().getCustomVariables().add(customVariable);

				AbcdLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress()
						+ "' has created a " + customVariable.getClass() + " with 'Name: " + customVariable.getName()
						+ " - Type: " + customVariable.getType() + " - Scope: " + customVariable.getScope() + "'.");
			}
		});

		upperMenu.addRemoveVariableButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = -3064110201946542388L;

			@Override
			public void buttonClick(ClickEvent event) {
				CustomVariable customVariable = (CustomVariable) variableTable.getValue();
				if (customVariable != null) {
					try {
						CheckDependencies.checkCustomVariableDependencies(UserSessionHandler.getFormController()
								.getForm(), customVariable);
						removeSelectedRow();
						customVariable.remove();
						AbcdLogger.info(
								this.getClass().getName(),
								"User '" + UserSessionHandler.getUser().getEmailAddress() + "' has removed a "
										+ customVariable.getClass() + " with 'Name: " + customVariable.getName()
										+ " - Type: " + customVariable.getType() + " - Scope: "
										+ customVariable.getScope() + "'.");
					} catch (DependencyExistException e) {
						// Forbid the remove action if exist dependency.
						MessageManager.showError(LanguageCodes.VARIABLE_DESIGNER_WARNING_CANNOT_REMOVE_VARIABLE);
					}
				}
			}
		});

		return upperMenu;
	}

	private void addNewVariable(CustomVariable customVariable) {
		if ((variableTable != null) && (customVariable != null)) {
			variableTable.addRow(customVariable);
		}
	}

	private void removeSelectedRow() {
		if (variableTable != null) {
			variableTable.removeSelectedRow();
		}
	}

	private void save() {
		if (UserSessionHandler.getFormController() != null) {
			try {
				UserSessionHandler.getFormController().save();
				MessageManager.showInfo(LanguageCodes.INFO_DATA_STORED);
			} catch (DuplicatedVariableException e) {
				MessageManager.showError(LanguageCodes.ERROR_DATABASE_DUPLICATED_VARIABLE,
						LanguageCodes.ERROR_DATABASE_DUPLICATED_VARIABLE_CAPTION);
			} catch (ConstraintViolationException cve) {
				MessageManager.showError(LanguageCodes.VARIABLE_DESIGNER_WARNING_CANNOT_REMOVE_VARIABLE);
			} catch (UnexpectedDatabaseException e) {
				AbcdLogger.errorMessage(FormManager.class.getName(), e);
				MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE,
						LanguageCodes.ERROR_ACCESSING_DATABASE_DESCRIPTION);
			}
		}
	}

	@Override
	public List<AbcdActivity> accessAuthorizationsRequired() {
		return activityPermissions;
	}
}

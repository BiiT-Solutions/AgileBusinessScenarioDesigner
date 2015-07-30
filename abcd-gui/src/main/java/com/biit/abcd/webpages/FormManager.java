package com.biit.abcd.webpages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.biit.abcd.MessageManager;
import com.biit.abcd.UiAccesser;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.authentication.exceptions.NotEnoughRightsToChangeStatusException;
import com.biit.abcd.core.SpringContextHelper;
import com.biit.abcd.core.security.AbcdActivity;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.dao.IFormDao;
import com.biit.abcd.persistence.dao.ITestScenarioDao;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.FormWorkStatus;
import com.biit.abcd.persistence.entity.SimpleFormView;
import com.biit.abcd.persistence.entity.testscenarios.TestScenario;
import com.biit.abcd.security.IAbcdFormAuthorizationService;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.biit.abcd.webpages.components.AcceptCancelWindow.CancelActionListener;
import com.biit.abcd.webpages.components.AlertMessageWindow;
import com.biit.abcd.webpages.components.FormWebPageComponent;
import com.biit.abcd.webpages.components.IFormSelectedListener;
import com.biit.abcd.webpages.elements.formdesigner.RootForm;
import com.biit.abcd.webpages.elements.formmanager.FormManagerUpperMenu;
import com.biit.abcd.webpages.elements.formmanager.FormManagerUpperMenu.IFormRemove;
import com.biit.abcd.webpages.elements.formtable.FormsVersionsTreeTable;
import com.biit.abcd.webpages.elements.formtable.FormsVersionsTreeTable.IFormStatusChange;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.persistence.entity.exceptions.ElementCannotBeRemovedException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.ComboBox;

public class FormManager extends FormWebPageComponent {
	private static final long serialVersionUID = 8306642137791826056L;
	private static final List<AbcdActivity> activityPermissions = new ArrayList<AbcdActivity>(
			Arrays.asList(AbcdActivity.READ));
	private FormsVersionsTreeTable formTable;
	private FormManagerUpperMenu upperMenu;

	private IFormDao formDao;
	private ITestScenarioDao testScenarioDao;

	private IAbcdFormAuthorizationService securityService;

	public FormManager() {
		super();
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		formDao = (IFormDao) helper.getBean("formDao");
		testScenarioDao = (ITestScenarioDao) helper.getBean("testScenarioDao");
		securityService = (IAbcdFormAuthorizationService) helper.getBean("abcdSecurityService");
	}

	@Override
	protected void initContent() {
		this.upperMenu = createUpperMenu();
		upperMenu.addFormSelectedListener(new IFormSelectedListener() {

			@Override
			public void formSelected() {
				if (formTable.getValue() != null) {
					Form selectedForm = formDao.get(formTable.getValue().getId());
					selectedForm.setLastVersion(formTable.getValue().isLastVersion());
					UserSessionHandler.setForm(selectedForm);
				}
			}
		});
		upperMenu.addFormRemoveListener(new IFormRemove() {

			@Override
			public void removeButtonPressed() {
				if (formTable.getValue() != null) {
					final AlertMessageWindow windowAccept = new AlertMessageWindow(LanguageCodes.WARNING_REMOVE_ELEMENT);
					windowAccept.addAcceptActionListener(new AcceptActionListener() {
						@Override
						public void acceptAction(AcceptCancelWindow window) {
							removeSelectedForm();
							windowAccept.close();
						}
					});
					windowAccept.showCentered();
				}
			}
		});
		setUpperMenu(upperMenu);

		formTable = createTreeTable();
		getWorkingAreaLayout().addComponent(formTable);
		formTable.selectLastUsedForm();
		updateButtons(!(getForm() instanceof RootForm) && getForm() != null);

		getBottomMenu().addFormSelectedListener(new IFormSelectedListener() {
			@Override
			public void formSelected() {
				if (formTable.getValue() != null) {
					Form form = formDao.get(formTable.getValue().getId());
					form.setLastVersion(formTable.getValue().isLastVersion());
					UserSessionHandler.setForm(form);
					UiAccesser.lockForm(form, UserSessionHandler.getUser());
				}
			}
		});

		formTable.addFormStatusChangeListeners(new IFormStatusChange() {

			@Override
			public void comboBoxValueChanged(ComboBox statusComboBox) {
				formStatusValueChanged(statusComboBox);
			}
		});

		UserSessionHandler.getFormController().clearUnsavedChangesChecker();
	}

	private FormsVersionsTreeTable createTreeTable() {
		FormsVersionsTreeTable treeTable = new FormsVersionsTreeTable();
		treeTable.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -119450082492122880L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				updateButtons(!(getForm() instanceof RootForm) && getForm() != null);
				formTable.refreshSelectedRow();
			}
		});
		return treeTable;
	}

	@Override
	public void updateButtons(boolean enableFormButtons) {
		super.updateButtons(enableFormButtons);
		upperMenu.updateButtons(enableFormButtons);
		upperMenu.updateNewVersionButton(formTable.getValue());
		upperMenu.updateRemoveFormButton(formTable.getValue());
	}

	private FormManagerUpperMenu createUpperMenu() {
		FormManagerUpperMenu upperMenu = new FormManagerUpperMenu(this);
		return upperMenu;
	}

	@Override
	public List<AbcdActivity> accessAuthorizationsRequired() {
		return activityPermissions;
	}

	public SimpleFormView getForm() {
		return formTable.getValue();
	}

	public void addNewForm(Form form) {
		formDao.makePersistent(form);
		SimpleFormView simpleView = new SimpleFormView(form);
		formTable.addForm(simpleView);
		formTable.selectForm(simpleView);
		simpleView.setId(form.getId());
	}

	public void setFormById(Long formId) {
		UserSessionHandler.setForm(formDao.get(formId));
	}

	public void newFormVersion() {
		Form newForm;
		try {
			RootForm rootForm = formTable.getSelectedRootForm();
			SimpleFormView currentForm = rootForm.getLastFormVersion();
			currentForm.setLastVersion(false);
			newForm = createNewFormVersion(currentForm);
			newForm.setLastVersion(true);
			formDao.makePersistent(newForm);
			formTable.refreshFormTable();
			formTable.selectForm(newForm);
		} catch (NotValidTreeObjectException e) {
			MessageManager.showError(LanguageCodes.ERROR_NAME_TOO_LONG);
			AbcdLogger.errorMessage(FormManager.class.getName(), e);
		} catch (CharacterNotAllowedException e) {
			// Impossible
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		} catch (NotValidStorableObjectException e) {
			MessageManager.showError(LanguageCodes.ERROR_NEW_VERSION, LanguageCodes.ERROR_NEW_VERSION_DESCRIPTION);
			AbcdLogger.errorMessage(FormManager.class.getName(), e);
		} catch (UnexpectedDatabaseException e) {
			AbcdLogger.errorMessage(FormManager.class.getName(), e);
			MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE,
					LanguageCodes.ERROR_ACCESSING_DATABASE_DESCRIPTION);
		}
	}

	public Form createNewFormVersion(SimpleFormView form) throws NotValidStorableObjectException,
			CharacterNotAllowedException, UnexpectedDatabaseException {
		AbcdLogger.info(this.getClass().getName(), "User: " + UserSessionHandler.getUser().getEmailAddress()
				+ " createNewFormVersion " + form + " START");

		Form newFormVersion;
		try {
			Form realForm = formDao.getForm(form.getLabel(), form.getVersion(), form.getOrganizationId());
			newFormVersion = realForm.createNewVersion(UserSessionHandler.getUser());
		} catch (CharacterNotAllowedException | NotValidStorableObjectException ex) {
			AbcdLogger.severe(this.getClass().getName(), "User: " + UserSessionHandler.getUser().getEmailAddress()
					+ " createForm " + ex.getMessage());
			throw ex;
		}

		AbcdLogger.info(this.getClass().getName(), "User: " + UserSessionHandler.getUser().getEmailAddress()
				+ " createNewFormVersion " + form + " END");
		return newFormVersion;
	}

	@Transactional
	private void removeSelectedForm() {
		Form selectedForm;
		selectedForm = formDao.get(formTable.getValue().getId());
		if (selectedForm != null) {
			// If it is the last form, remove all its tests.
			RootForm rootForm = formTable.getSelectedRootForm();
			if (rootForm.getChildForms().size() <= 1) {
				List<TestScenario> testScenarios = testScenarioDao.getTestScenarioByForm(selectedForm.getLabel(),
						selectedForm.getOrganizationId());
				for (TestScenario testScenario : testScenarios) {
					try {
						testScenarioDao.makeTransient(testScenario);
						AbcdLogger.info(this.getClass().getName(), "User '"
								+ UserSessionHandler.getUser().getEmailAddress() + "' has removed test scenario '"
								+ testScenario.getName() + "' for form '" + selectedForm.getLabel() + "' (version "
								+ selectedForm.getVersion() + ").");
					} catch (ElementCannotBeRemovedException e) {
						// Impossible.
						AbcdLogger.errorMessage(this.getClass().getName(), e);
					}
				}
			}
			// Remove the form.
			try {
				formDao.makeTransient(selectedForm);
			} catch (ElementCannotBeRemovedException e) {
				// Impossible.
				AbcdLogger.errorMessage(this.getClass().getName(), e);
			}
			AbcdLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress()
					+ "' has removed form '" + selectedForm.getLabel() + "' (version " + selectedForm.getVersion()
					+ ").");
			formTable.refreshFormTable();
		}
	}

	private void formStatusValueChanged(final ComboBox statusComboBox) {
		if (getForm().getStatus().equals(statusComboBox.getValue())) {
			// Its the same status. Don't do anything.
			return;
		}

		final AlertMessageWindow windowAccept = new AlertMessageWindow(LanguageCodes.CAPTION_PROCEED_MODIFY_STATUS);

		windowAccept.addAcceptActionListener(new AcceptActionListener() {
			@Override
			public void acceptAction(AcceptCancelWindow window) {
				try {
					FormWorkStatus previousStatus = getForm().getStatus();

					getForm().setStatus((FormWorkStatus) statusComboBox.getValue());
					changeStatusOnDatabase(getForm(), statusComboBox, (FormWorkStatus) statusComboBox.getValue());
					formTable.refreshRow(getForm());
					windowAccept.close();
					updateButtons(!(getForm() instanceof RootForm) && getForm() != null);
					// In case we are changing the status of a form to design
					// and there are newer versions
					showEditableWarningIfNeeded(previousStatus, (FormWorkStatus) statusComboBox.getValue());

				} catch (NotEnoughRightsToChangeStatusException nercs) {
					// Nothing.
				}
			}
		});

		windowAccept.addCancelActionListener(new CancelActionListener() {
			@Override
			public void cancelAction(AcceptCancelWindow window) {
				statusComboBox.setValue(getForm().getStatus());
				windowAccept.close();
			}
		});

		windowAccept.showCentered();
	}

	private void showEditableWarningIfNeeded(FormWorkStatus previousStatus, FormWorkStatus comboBoxStatus) {
		if (!getForm().isLastVersion() && comboBoxStatus.equals(FormWorkStatus.DESIGN)
				&& previousStatus.equals(FormWorkStatus.FINAL_DESIGN)) {
			MessageManager.showWarning(LanguageCodes.WARNING_TITLE,
					LanguageCodes.WARNING_ONLY_RULES_AND_EXPRESSIONS_EDITABLE);
		}
	}

	private void changeStatusOnDatabase(SimpleFormView form, ComboBox statusComboBox, FormWorkStatus value)
			throws NotEnoughRightsToChangeStatusException {
		try {
			if (!securityService.isAuthorizedActivity(UserSessionHandler.getUser(), form.getOrganizationId(),
					AbcdActivity.FORM_STATUS_DOWNGRADE)) {
				throw new NotEnoughRightsToChangeStatusException("User '"
						+ UserSessionHandler.getUser().getEmailAddress()
						+ "' has not enought rights to change the status of form '" + form.getLabel() + "'!");
			}
			try {
				formDao.updateFormStatus(form.getId(), value);
			} catch (UnexpectedDatabaseException e) {
				MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE,
						LanguageCodes.ERROR_ACCESSING_DATABASE_DESCRIPTION);
			}

		} catch (NotEnoughRightsToChangeStatusException e) {
			statusComboBox.setValue(form.getStatus());
			MessageManager.showWarning(LanguageCodes.ERROR_OPERATION_NOT_ALLOWED);
		}
	}

}

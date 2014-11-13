package com.biit.abcd.webpages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.biit.abcd.MessageManager;
import com.biit.abcd.UiAccesser;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.core.SpringContextHelper;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.dao.IFormDao;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.SimpleFormView;
import com.biit.abcd.security.AbcdActivity;
import com.biit.abcd.webpages.components.FormWebPageComponent;
import com.biit.abcd.webpages.components.IFormSelectedListener;
import com.biit.abcd.webpages.elements.formdesigner.RootForm;
import com.biit.abcd.webpages.elements.formmanager.FormManagerUpperMenu;
import com.biit.abcd.webpages.elements.formtable.FormsVersionsTreeTable;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.NotValidTreeObjectException;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.VerticalLayout;

public class FormManager extends FormWebPageComponent {
	private static final long serialVersionUID = 8306642137791826056L;
	private static final List<AbcdActivity> activityPermissions = new ArrayList<AbcdActivity>(
			Arrays.asList(AbcdActivity.READ));
	private FormsVersionsTreeTable formTable;
	private FormManagerUpperMenu upperMenu;

	private IFormDao formDao;

	public FormManager() {
		super();
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		formDao = (IFormDao) helper.getBean("formDao");
	}

	@Override
	protected void initContent() {
		this.upperMenu = createUpperMenu();
		upperMenu.addFormSelectedListener(new IFormSelectedListener() {

			@Override
			public void formSelected() {
				if (formTable.getValue() != null) {
					try {
						Form selectedForm = formDao.read(formTable.getValue().getId());
						selectedForm.setLastVersion(formTable.getValue().isLastVersion());
						UserSessionHandler.getFormController().setForm(selectedForm);
					} catch (UnexpectedDatabaseException e) {
						AbcdLogger.errorMessage(FormManager.class.getName(), e);
						MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE,
								LanguageCodes.ERROR_ACCESSING_DATABASE_DESCRIPTION);
					}

				}
			}
		});
		setUpperMenu(upperMenu);

		formTable = createTreeTable();
		VerticalLayout rootLayout = new VerticalLayout(formTable);
		rootLayout.setSizeFull();
		rootLayout.setMargin(true);
		getWorkingAreaLayout().addComponent(rootLayout);
		formTable.selectLastUsedForm();
		updateButtons(!(getForm() instanceof RootForm) && getForm() != null);

		getBottomMenu().addFormSelectedListener(new IFormSelectedListener() {
			@Override
			public void formSelected() {
				if (formTable.getValue() != null) {
					try {
						Form form = formDao.read(formTable.getValue().getId());
						form.setLastVersion(formTable.getValue().isLastVersion());
						UserSessionHandler.getFormController().setForm(form);
						UiAccesser.lockForm(form, UserSessionHandler.getUser());
					} catch (UnexpectedDatabaseException e) {
						AbcdLogger.errorMessage(FormManager.class.getName(), e);
						MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE,
								LanguageCodes.ERROR_ACCESSING_DATABASE_DESCRIPTION);
					}

				}
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
				upperMenu.updateNewVersionButton(formTable.getValue());
			}
		});
		return treeTable;
	}

	@Override
	public void updateButtons(boolean enableFormButtons) {
		super.updateButtons(enableFormButtons);
		upperMenu.updateButtons(enableFormButtons);
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
		SimpleFormView simpleView = new SimpleFormView(form);
		try {
			formDao.makePersistent(form);
			formTable.addForm(simpleView);
			formTable.selectForm(simpleView);
			simpleView.setId(form.getId());
		} catch (UnexpectedDatabaseException e) {
			AbcdLogger.errorMessage(FormManager.class.getName(), e);
			MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE,
					LanguageCodes.ERROR_ACCESSING_DATABASE_DESCRIPTION);
		}

	}

	public void setFormById(Long formId) {
		try {
			UserSessionHandler.getFormController().setForm(formDao.read(formId));
		} catch (UnexpectedDatabaseException e) {
			AbcdLogger.errorMessage(FormManager.class.getName(), e);
			MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE,
					LanguageCodes.ERROR_ACCESSING_DATABASE_DESCRIPTION);
		}
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
		} catch (UnexpectedDatabaseException e) {
			AbcdLogger.errorMessage(FormManager.class.getName(), e);
			throw e;
		}

		AbcdLogger.info(this.getClass().getName(), "User: " + UserSessionHandler.getUser().getEmailAddress()
				+ " createNewFormVersion " + form + " END");
		return newFormVersion;
	}
}

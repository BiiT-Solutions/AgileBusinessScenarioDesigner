package com.biit.abcd.webpages.elements.formdesigner;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.core.SpringContextHelper;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.dao.IFormDao;
import com.biit.abcd.persistence.entity.Form;
import com.biit.form.entity.TreeObject;
import com.vaadin.data.Property.ReadOnlyException;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

public class FormProperties extends SecuredFormElementProperties<Form> {
	private static final long serialVersionUID = -7673405239560362757L;

	private Form instance;
	private TextField formLabel;
	private TextField formVersion;
	private DateField availableFrom;
	private IFormDao formDao;

	public FormProperties() {
		super(Form.class);
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		formDao = (IFormDao) helper.getBean("formDao");
	}

	@Override
	public void setElementForProperties(Form element) {
		instance = element;

		formLabel = new TextField(ServerTranslate.translate(LanguageCodes.FORM_PROPERTIES_NAME));
		//formLabel.addValidator(new ValidatorTreeObjectName(BaseForm.NAME_ALLOWED));
		//formLabel.addValidator(new ValidatorTreeObjectNameLength());
		formLabel.setValue(instance.getLabel());

		formVersion = new TextField(ServerTranslate.translate(LanguageCodes.FORM_PROPERTIES_VERSION));
		formVersion.setValue(instance.getVersion().toString());
		formVersion.setEnabled(false);

		availableFrom = new DateField(ServerTranslate.translate(LanguageCodes.TREE_OBJECT_PROPERTIES_AVAILABLE_FROM));
		availableFrom.setValue(instance.getAvailableFrom());

		FormLayout formForm = new FormLayout();
		formForm.setWidth(null);
		formForm.addComponent(formLabel);
		formForm.addComponent(formVersion);
		formForm.addComponent(availableFrom);

		addTab(formForm, ServerTranslate.translate(LanguageCodes.TREE_OBJECT_PROPERTIES_FORM_FORM_CAPTION), true, 0);
	}

	@Override
	protected void updateConcreteFormElement() {
		if (formLabel.isValid() && UserSessionHandler.getFormController() != null) {
			// To avoid setting repeated values
			if (!formLabel.getValue().equals(instance.getName())) {
				try {
					// Checks if already exists a form with this label and its
					// version.
					if (!formDao.exists(formLabel.getValue(), instance.getVersion(), instance.getOrganizationId(),
							instance.getId())) {
						UserSessionHandler.getFormController().updateForm(instance, formLabel.getValue());
					} else {
						formLabel.setValue(instance.getLabel());
						MessageManager.showWarning(LanguageCodes.COMMON_ERROR_NAME_IS_IN_USE,
								LanguageCodes.COMMON_ERROR_NAME_IS_IN_USE_DESCRIPTION);
						UserSessionHandler.getFormController().updateForm(instance, instance.getLabel());
					}
				} catch (ReadOnlyException e) {
					MessageManager.showError(LanguageCodes.ERROR_ACCESSING_DATABASE);
					AbcdLogger.errorMessage(this.getClass().getName(), e);
				}
			}

			if (availableFrom.getValue() != null) {
				Calendar cal = Calendar.getInstance(); // locale-specific
				cal.setTime(availableFrom.getValue());
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				long time = cal.getTimeInMillis();

				// To avoid setting repeated values
				Date dateToCompare = new Date(time);
				if (dateToCompare.compareTo(instance.getAvailableFrom()) != 0) {
					instance.setAvailableFrom(new Timestamp(time));
					AbcdLogger.info(this.getClass().getName(),
							"User '" + UserSessionHandler.getUser().getEmailAddress() + "' has modified the Form '"
									+ instance.getName() + "' property 'Valid From' to '" + instance.getAvailableFrom()
									+ "'.");
				}
			}
		}
	}

	@Override
	protected TreeObject getTreeObjectInstance() {
		return instance;
	}

	@Override
	protected Set<AbstractComponent> getProtectedElements() {
		return new HashSet<AbstractComponent>(Arrays.asList(formLabel, availableFrom));
	}

}

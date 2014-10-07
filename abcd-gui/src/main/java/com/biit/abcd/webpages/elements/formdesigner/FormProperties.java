package com.biit.abcd.webpages.elements.formdesigner;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.Form;
import com.biit.form.TreeObject;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

public class FormProperties extends SecuredFormElementProperties<Form> {
	private static final long serialVersionUID = -7673405239560362757L;

	private Form instance;
	private TextField formName;
	private TextField formVersion;
	private DateField availableFrom;
	private DateField availableTo;

	public FormProperties() {
		super(Form.class);
	}

	@Override
	public void setElementForProperties(Form element) {
		instance = element;
		formName = new TextField(ServerTranslate.translate(LanguageCodes.FORM_PROPERTIES_NAME));
		formName.setValue(instance.getName());
		formName.setEnabled(false);
		// formName.addValidator(new RegexpValidator("[a-zA-Z0-9]",
		// "ERROR !!!"));

		formVersion = new TextField(ServerTranslate.translate(LanguageCodes.FORM_PROPERTIES_VERSION));
		formVersion.setValue(instance.getVersion().toString());
		formVersion.setEnabled(false);

		availableFrom = new DateField(ServerTranslate.translate(LanguageCodes.TREE_OBJECT_PROPERTIES_AVAILABLE_FROM));
		availableFrom.setValue(instance.getAvailableFrom());

		availableTo = new DateField(ServerTranslate.translate(LanguageCodes.TREE_OBJECT_PROPERTIES_AVAILABLE_TO));
		availableTo.setValue(instance.getAvailableTo());

		FormLayout formForm = new FormLayout();
		formForm.setWidth(null);
		formForm.addComponent(formName);
		formForm.addComponent(formVersion);
		formForm.addComponent(availableFrom);
		formForm.addComponent(availableTo);

		addTab(formForm, ServerTranslate.translate(LanguageCodes.TREE_OBJECT_PROPERTIES_FORM_FORM_CAPTION), true, 0);
	}

	@Override
	protected void updateConcreteFormElement() {
		if (availableFrom.getValue() != null) {
			instance.setAvailableFrom(new Timestamp(availableFrom.getValue().getTime()));
			AbcdLogger.info(
					this.getClass().getName(),
					"User '" + UserSessionHandler.getUser().getEmailAddress() + "' has modified the Form '"
							+ instance.getName() + "' property 'Valid From' to '" + instance.getAvailableFrom() + "'.");
		}
		if (availableTo.getValue() != null) {
			instance.setAvailableTo(new Timestamp(availableTo.getValue().getTime()));
			AbcdLogger.info(
					this.getClass().getName(),
					"User '" + UserSessionHandler.getUser().getEmailAddress() + "' has modified the Form '"
							+ instance.getName() + "' property 'Valid To' to '" + instance.getAvailableTo() + "'.");
		}
		firePropertyUpdateListener(getTreeObjectInstance());
	}

	@Override
	protected TreeObject getTreeObjectInstance() {
		return instance;
	}

	@Override
	protected Set<AbstractComponent> getProtectedElements() {
		return new HashSet<AbstractComponent>(Arrays.asList(availableFrom, availableTo));
	}

}

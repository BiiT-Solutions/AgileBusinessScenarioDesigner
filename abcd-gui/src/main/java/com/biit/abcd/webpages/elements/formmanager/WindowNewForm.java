package com.biit.abcd.webpages.elements.formmanager;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.core.SpringContextHelper;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.persistence.dao.IFormDao;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.webpages.FormManager;
import com.biit.abcd.webpages.components.WindowCreateNewObject;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.TextField;

public class WindowNewForm extends WindowCreateNewObject {
	private static final long serialVersionUID = 2963807969133587359L;
	private Form form;

	private IFormDao formDao;

	public WindowNewForm(FormManager parentWindow, LanguageCodes windowCaption, LanguageCodes inputFieldCaption) {
		super(parentWindow, windowCaption, inputFieldCaption);

		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		formDao = (IFormDao) helper.getBean("formDao");
	}

	@Override
	public void acceptAction(TextField inputTextField) {
		if (formDao.getForm(inputTextField.getValue()) == null) {
			form = new Form();
			form.setName(inputTextField.getValue());
			form.setCreatedBy(UserSessionHandler.getUser());
			form.setUpdatedBy(UserSessionHandler.getUser());
			((FormManager) getParentWindow()).addNewForm(form);
			close();
		} else {
			MessageManager.showError(LanguageCodes.ERROR_REPEATED_FORM_NAME);
		}
	}

}

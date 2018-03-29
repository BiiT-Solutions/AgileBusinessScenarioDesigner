package com.biit.abcd;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.core.SpringContextHelper;
import com.biit.abcd.exceptions.FormWithSameNameException;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.dao.IFormDao;
import com.biit.abcd.persistence.entity.Form;
import com.biit.persistence.dao.exceptions.ElementCannotBePersistedException;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.google.gson.JsonParseException;
import com.vaadin.server.VaadinServlet;

public class FromJson {

	private IFormDao formDao;

	public FromJson() {
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		formDao = (IFormDao) helper.getBean("formDao");
	}

	/**
	 * Imports a form serialized in json with formLabel as name in
	 * organizacionId organization. If database contains a form with the same
	 * label a {@link ElementCannotBePersistedException} exception is thrown.
	 * 
	 * @param json
	 * @param formLabel
	 * @param organizationId
	 * @return
	 * @throws FormWithSameNameException
	 * @throws UnexpectedDatabaseException
	 * @throws FieldTooLongException
	 * @throws ElementCannotBePersistedException
	 */
	public Form importFormFromJson(String json, String formLabel, Long organizationId) throws JsonParseException, FormWithSameNameException,
			UnexpectedDatabaseException, FieldTooLongException, ElementCannotBePersistedException {
		// Check if database contains a form with the same name.
		if (formDao.exists(formLabel, organizationId)) {
			FormWithSameNameException ex = new FormWithSameNameException("Form with name: " + formLabel + " already exists");
			AbcdLogger.severe(FromJson.class.getName(), "createForm " + ex.getMessage());
			throw ex;
		}

		Form newForm = Form.fromJson(json);
		newForm.setOrganizationId(organizationId);
		newForm.setLabel(formLabel);
		newForm.resetUserTimestampInfo(UserSessionHandler.getUser().getUniqueId());

		// Reset ids before persisting buf after removing incorrect
		// webservices.
		newForm.resetIds();
		newForm = formDao.makePersistent(newForm);
		return newForm;
	}
}

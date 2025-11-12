package com.biit.abcd;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (GUI)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.core.SpringContextHelper;
import com.biit.abcd.core.providers.FormProvider;
import com.biit.abcd.exceptions.FormWithSameNameException;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.Form;
import com.biit.persistence.dao.exceptions.ElementCannotBePersistedException;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.JsonParseException;
import com.vaadin.server.VaadinServlet;

public class FromJson {

    private FormProvider formProvider;

    public FromJson() {
        SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
        this.formProvider = (FormProvider) helper.getBean("formProvider");
    }

    /**
     * Imports a form serialized in json with formLabel as name in
     * organizacionId organization. If database contains a form with the same
     * label a {@link ElementCannotBePersistedException} exception is thrown.
     *
     * @param json
     * @param formLabel
     * @param organizationId
     * @return the form.
     * @throws FormWithSameNameException
     * @throws UnexpectedDatabaseException
     * @throws FieldTooLongException
     * @throws ElementCannotBePersistedException
     */
    public Form importFormFromJson(String json, String formLabel, Long organizationId) throws JsonParseException, FormWithSameNameException,
            UnexpectedDatabaseException, FieldTooLongException, ElementCannotBePersistedException {
        // Check if database contains a form with the same name.
        if (formProvider.exists(formLabel, organizationId)) {
            FormWithSameNameException ex = new FormWithSameNameException("Form with name: " + formLabel + " already exists");
            AbcdLogger.severe(FromJson.class.getName(), "createForm " + ex.getMessage());
            throw ex;
        }

        try {
            Form newForm = Form.fromJson(json);
            newForm.setOrganizationId(organizationId);
            newForm.setLabel(formLabel);
            newForm.resetUserTimestampInfo(UserSessionHandler.getUser().getUniqueId());

            // Reset ids before persisting but after removing incorrect
            // webservices.
            newForm.resetIds();
            newForm = formProvider.saveForm(newForm);
            return newForm;
        } catch (JsonProcessingException e) {
            throw new JsonParseException(e);
        }
    }
}

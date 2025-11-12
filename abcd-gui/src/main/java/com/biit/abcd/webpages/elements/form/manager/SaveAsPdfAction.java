package com.biit.abcd.webpages.elements.form.manager;

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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.pdfgenerator.FormPdfUtils;
import com.biit.abcd.pdfgenerator.FormPdfGenerator;
import com.biit.abcd.webpages.components.SaveAction;
import com.lowagie.text.DocumentException;

public class SaveAsPdfAction implements SaveAction {

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public byte[] getInformationData() {
		try {
			InputStream is = FormPdfUtils.generatePdf(new FormPdfGenerator(UserSessionHandler.getFormController().getForm()));
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int reads = is.read();
			while (reads != -1) {
				baos.write(reads);
				reads = is.read();
			}
			return baos.toByteArray();

		} catch (IOException | DocumentException e) {
			AbcdLogger.errorMessage(SaveAsPdfAction.class.getName(), e);
			MessageManager.showError(LanguageCodes.ERROR_UNEXPECTED_ERROR);
		}
		return null;
	}

	@Override
	public String getExtension() {
		return "pdf";
	}

	@Override
	public String getMimeType() {
		return "application/pdf";
	}

	@Override
	public String getFileName() {
		return UserSessionHandler.getFormController().getForm().getName() + "." + getExtension();
	}

}

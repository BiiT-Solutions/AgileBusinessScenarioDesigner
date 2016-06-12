package com.biit.abcd.webpages.elements.formmanager;

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
		return "application/zip";
	}

	@Override
	public String getFileName() {
		return "rules." + getExtension();
	}

}

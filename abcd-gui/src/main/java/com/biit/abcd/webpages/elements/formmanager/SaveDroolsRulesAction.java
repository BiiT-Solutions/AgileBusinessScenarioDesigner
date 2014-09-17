package com.biit.abcd.webpages.elements.formmanager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.core.drools.FormToDroolsExporter;
import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleNotImplementedException;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.webpages.components.SaveAction;
import com.biit.abcd.webpages.components.SettingsWindow;

public class SaveDroolsRulesAction implements SaveAction {
	public static final String FILE_CODIFICATION = "UTF-8";

	@Override
	public String getMimeType() {
		return "application/zip";
	}

	@Override
	public String getExtension() {
		return "zip";
	}

	@Override
	public byte[] getInformationData() {
		try {
			List<String> filesToZip = new ArrayList<>();
			List<String> namesOfFiles = new ArrayList<>();
			FormToDroolsExporter droolsExporter = new FormToDroolsExporter();
			String rules = droolsExporter.getDroolRules(UserSessionHandler.getFormController().getForm(), null);
			filesToZip.add(rules);
			namesOfFiles.add("droolRules.drl");
			String variables =
					filesToZip.add(variables);
					namesOfFiles.add("globalVariables.json");

			if (filesToZip != null && filesToZip.size() > 0 && filesToZip.size() == namesOfFiles.size()) {
				try {
					return zipFiles(filesToZip, namesOfFiles);
				} catch (IOException e) {
					MessageManager.showError("Error generating zip file with the XML files.");
					e.printStackTrace();
				}
			}
		} catch (ExpressionInvalidException | RuleInvalidException | IOException e) {
			MessageManager.showError(LanguageCodes.ERROR_DROOLS_INVALID_RULE, e.getMessage());
			AbcdLogger.errorMessage(SettingsWindow.class.getName(), e);
		} catch (RuleNotImplementedException e) {
			MessageManager.showError(LanguageCodes.ERROR_RULE_NOT_IMPLEMENTED, e.getExpressionChain()
					.getRepresentation());
			AbcdLogger.errorMessage(SettingsWindow.class.getName(), e);
		} catch (Exception e) {
			MessageManager.showError(LanguageCodes.ERROR_UNEXPECTED_ERROR, LanguageCodes.ERROR_DROOLS_ENGINE);
			AbcdLogger.errorMessage(SettingsWindow.class.getName(), e);
		}
		return null;
	}

	private byte[] zipFiles(List<String> files, List<String> names) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);
		for (int i = 0; i < files.size(); i++) {
			String fileToZip = files.get(i);
			ZipEntry entry = new ZipEntry(names.get(i));
			entry.setSize(fileToZip.length());
			zos.putNextEntry(entry);
			zos.write(fileToZip.getBytes(FILE_CODIFICATION));
		}
		zos.closeEntry();
		zos.close();
		return baos.toByteArray();
	}

	@Override
	public boolean isValid() {
		return true;
	}

}
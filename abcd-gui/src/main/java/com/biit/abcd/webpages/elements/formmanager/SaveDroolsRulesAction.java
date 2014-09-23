package com.biit.abcd.webpages.elements.formmanager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.core.drools.FormToDroolsExporter;
import com.biit.abcd.core.drools.json.globalvariables.JSonConverter;
import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleNotImplementedException;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.utils.ZipUtils;
import com.biit.abcd.webpages.components.SaveAction;
import com.biit.abcd.webpages.components.SettingsWindow;

public class SaveDroolsRulesAction implements SaveAction {

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
			String rules = droolsExporter.getDroolRules(UserSessionHandler.getFormController().getForm(),
					UserSessionHandler.getGlobalVariablesController().getGlobalVariables());
			filesToZip.add(rules);
			namesOfFiles.add("droolRules.drl");
			String variables = JSonConverter.convertGlobalVariableListToJson(UserSessionHandler
					.getGlobalVariablesController().getGlobalVariables());
			filesToZip.add(variables);
			namesOfFiles.add("globalVariables.json");

			try {
				return ZipUtils.createZipFile(filesToZip, namesOfFiles);
			} catch (IOException e) {
				MessageManager.showError(LanguageCodes.ZIP_FILE_NOT_GENERATED);
				AbcdLogger.errorMessage(SettingsWindow.class.getName(), e);
			}
		} catch (ExpressionInvalidException | RuleInvalidException | IOException e) {
			MessageManager.showError(LanguageCodes.ERROR_DROOLS_INVALID_RULE, e.getMessage());
			AbcdLogger.errorMessage(SettingsWindow.class.getName(), e);
		} catch (RuleNotImplementedException e) {
			MessageManager.showError(LanguageCodes.ERROR_RULE_NOT_IMPLEMENTED, e.getExpressionChain()
					.getRepresentation());
			AbcdLogger.errorMessage(SettingsWindow.class.getName(), e);
		} catch (Exception e) {
			// MessageManager.showError(LanguageCodes.ERROR_UNEXPECTED_ERROR,
			// LanguageCodes.ERROR_DROOLS_ENGINE);
			MessageManager.showError(LanguageCodes.ERROR_UNEXPECTED_ERROR);
			AbcdLogger.errorMessage(SettingsWindow.class.getName(), e);
		}
		return null;
	}

	@Override
	public boolean isValid() {
		return true;
	}

	@Override
	public String getFileName() {
		return "rules." + getExtension();
	}

}
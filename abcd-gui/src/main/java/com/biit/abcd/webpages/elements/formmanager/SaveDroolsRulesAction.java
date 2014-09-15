package com.biit.abcd.webpages.elements.formmanager;

import java.io.IOException;

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

	@Override
	public String getMimeType() {
		return "text/plain";
	}

	@Override
	public String getExtension() {
		return "txt";
	}

	@Override
	public byte[] getInformationData() {
		try {
			FormToDroolsExporter droolsExporter = new FormToDroolsExporter();
			//TODO Global Variables not included. 
			String rules = droolsExporter.getDroolRules(UserSessionHandler.getFormController().getForm(), null);
			return rules.getBytes();
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

	@Override
	public boolean isValid() {
		return true;
	}

}
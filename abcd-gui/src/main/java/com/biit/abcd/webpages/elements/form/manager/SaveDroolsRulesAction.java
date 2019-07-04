package com.biit.abcd.webpages.elements.form.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.core.drools.FormToDroolsExporter;
import com.biit.abcd.core.drools.json.globalvariables.AbcdGlobalVariablesToJson;
import com.biit.abcd.core.drools.prattparser.exceptions.PrattParserException;
import com.biit.abcd.core.drools.prattparser.visitor.exceptions.NotCompatibleTypeException;
import com.biit.abcd.core.drools.rules.exceptions.ActionNotImplementedException;
import com.biit.abcd.core.drools.rules.exceptions.BetweenFunctionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.DateComparisonNotPossibleException;
import com.biit.abcd.core.drools.rules.exceptions.DroolsRuleCreationException;
import com.biit.abcd.core.drools.rules.exceptions.DroolsRuleGenerationException;
import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.InvalidRuleException;
import com.biit.abcd.core.drools.rules.exceptions.NullCustomVariableException;
import com.biit.abcd.core.drools.rules.exceptions.NullExpressionValueException;
import com.biit.abcd.core.drools.rules.exceptions.NullTreeObjectException;
import com.biit.abcd.core.drools.rules.exceptions.PluginInvocationException;
import com.biit.abcd.core.drools.rules.exceptions.RuleNotImplementedException;
import com.biit.abcd.core.drools.rules.exceptions.TreeObjectInstanceNotRecognizedException;
import com.biit.abcd.core.drools.rules.exceptions.TreeObjectParentNotValidException;
import com.biit.abcd.core.drools.rules.validators.InvalidExpressionException;
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
			String rules = droolsExporter.getDroolRules(UserSessionHandler.getFormController().getForm(), UserSessionHandler.getGlobalVariablesController()
					.getGlobalVariables());
			filesToZip.add(rules);
			Integer formVersion = UserSessionHandler.getFormController().getForm().getVersion();
			String formName = UserSessionHandler.getFormController().getForm().getLabel();
			namesOfFiles.add(formName + "_v" + formVersion + ".drl");
			String variables = AbcdGlobalVariablesToJson.toJson(UserSessionHandler.getGlobalVariablesController().getGlobalVariables());
			filesToZip.add(variables);
			namesOfFiles.add(formName + "_globalVariables_v" + formVersion + ".json");

			try {
				return ZipUtils.createZipFile(filesToZip, namesOfFiles);
			} catch (IOException e) {
				MessageManager.showError(LanguageCodes.ZIP_FILE_NOT_GENERATED);
				AbcdLogger.errorMessage(SettingsWindow.class.getName(), e);
			}
		} catch (DroolsRuleGenerationException | RuleNotImplementedException | ExpressionInvalidException | NullTreeObjectException
				| TreeObjectInstanceNotRecognizedException | TreeObjectParentNotValidException | NullCustomVariableException | NullExpressionValueException
				| BetweenFunctionInvalidException | DateComparisonNotPossibleException | PluginInvocationException | DroolsRuleCreationException
				| PrattParserException | ActionNotImplementedException | InvalidExpressionException e) {
			AbcdLogger.errorMessage(SettingsWindow.class.getName(), e);
			MessageManager.showError(LanguageCodes.ERROR_TITLE, LanguageCodes.DROOLS_RULES_GENERATION_EXCEPTION);
		} catch (NotCompatibleTypeException ncte) {
			MessageManager.showError(LanguageCodes.DROOLS_RULES_GENERATION_EXCEPTION, LanguageCodes.ERROR_DESCRIPTION, ncte.getDescription());
		} catch (InvalidRuleException e) {
			AbcdLogger.errorMessage(SettingsWindow.class.getName(), e);
			MessageManager.showError(LanguageCodes.ERROR_TITLE, LanguageCodes.DROOLS_RULE_INVALID, ((InvalidRuleException) e).getRuleName());
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
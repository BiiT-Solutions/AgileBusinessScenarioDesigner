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

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.core.drools.prattparser.exceptions.PrattParserException;
import com.biit.abcd.core.drools.prattparser.visitor.exceptions.NotCompatibleTypeException;
import com.biit.abcd.core.drools.rules.DroolsZipGenerator;
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
import com.biit.abcd.webpages.components.SaveAction;
import com.biit.abcd.webpages.components.SettingsWindow;

import java.io.IOException;

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
            return new DroolsZipGenerator().getInformationData(UserSessionHandler.getFormController().getForm(), UserSessionHandler.getGlobalVariablesController()
                    .getGlobalVariables());
        } catch (IOException e) {
            AbcdLogger.errorMessage(SettingsWindow.class.getName(), e);
            MessageManager.showError(LanguageCodes.ZIP_FILE_NOT_GENERATED);
        } catch (DroolsRuleGenerationException | RuleNotImplementedException | ExpressionInvalidException |
                 NullTreeObjectException
                 | TreeObjectInstanceNotRecognizedException | TreeObjectParentNotValidException |
                 NullCustomVariableException | NullExpressionValueException
                 | BetweenFunctionInvalidException | DateComparisonNotPossibleException | PluginInvocationException |
                 DroolsRuleCreationException
                 | PrattParserException | ActionNotImplementedException | InvalidExpressionException e) {
            AbcdLogger.errorMessage(SettingsWindow.class.getName(), e);
            MessageManager.showError(LanguageCodes.ERROR_TITLE, LanguageCodes.DROOLS_RULES_GENERATION_EXCEPTION);
        } catch (NotCompatibleTypeException ncte) {
            MessageManager.showError(LanguageCodes.DROOLS_RULES_GENERATION_EXCEPTION, LanguageCodes.ERROR_DESCRIPTION, ncte.getDescription());
        } catch (InvalidRuleException e) {
            AbcdLogger.errorMessage(SettingsWindow.class.getName(), e);
            MessageManager.showError(LanguageCodes.ERROR_TITLE, LanguageCodes.DROOLS_RULE_INVALID, e.getRuleName());
        } catch (Exception e) {
            AbcdLogger.errorMessage(SettingsWindow.class.getName(), e);
            MessageManager.showError(LanguageCodes.ERROR_TITLE, LanguageCodes.ERROR_UNEXPECTED_ERROR);
        }
        return null;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public String getFileName() {
        return UserSessionHandler.getFormController().getForm().getLabel() + "_v"
                + UserSessionHandler.getFormController().getForm().getVersion() + " - rules." + getExtension();
    }

}

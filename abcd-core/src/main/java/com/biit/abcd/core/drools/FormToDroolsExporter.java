package com.biit.abcd.core.drools;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (Core)
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.core.drools.prattparser.exceptions.PrattParserException;
import com.biit.abcd.core.drools.prattparser.visitor.exceptions.NotCompatibleTypeException;
import com.biit.abcd.core.drools.rules.DroolsRulesGenerator;
import com.biit.abcd.core.drools.rules.TreeObjectDroolsIdMap;
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
import com.biit.abcd.core.drools.utils.RuleGenerationUtils;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.drools.engine.DroolsRulesEngine;
import com.biit.drools.engine.exceptions.DroolsRuleExecutionException;
import com.biit.drools.form.provider.DroolsFormProvider;
import com.biit.drools.global.variables.interfaces.IGlobalVariable;
import com.biit.form.entity.BaseForm;
import com.biit.form.result.FormResult;
import com.biit.form.submitted.ISubmittedForm;

public class FormToDroolsExporter {

    /**
     * Parses the abcd form and loads the rules generated in the drools engine. <br>
     * If this method doesn't fail, it means that the drools rules are correctly
     * defined. <br>
     *
     * @param form            form to be parsed
     * @param globalVariables array with the global constants to be created
     * @throws DroolsRuleGenerationException
     * @throws ActionNotImplementedException
     * @throws InvalidRuleException
     * @throws PrattParserException
     * @throws DroolsRuleCreationException
     * @throws PluginInvocationException
     * @throws DateComparisonNotPossibleException
     * @throws BetweenFunctionInvalidException
     * @throws NullExpressionValueException
     * @throws NullCustomVariableException
     * @throws TreeObjectParentNotValidException
     * @throws TreeObjectInstanceNotRecognizedException
     * @throws NullTreeObjectException
     * @throws ExpressionInvalidException
     * @throws NotCompatibleTypeException
     * @throws RuleNotImplementedException
     * @throws InvalidExpressionException
     */
    public static DroolsRulesGenerator generateDroolRules(Form form, List<GlobalVariable> globalVariables) throws DroolsRuleGenerationException,
            RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
            TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
            BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException,
            InvalidRuleException, ActionNotImplementedException, InvalidExpressionException {
        if (form != null && form.getChildren() != null && !form.getChildren().isEmpty()) {
            DroolsRulesGenerator formRules;

            // Creation of the rules
            formRules = new DroolsRulesGenerator(form, globalVariables);
            try {
                Files.write(Paths.get(System.getProperty("java.io.tmpdir") + File.separator + "generatedRules.drl"), formRules.getRules().getBytes("UTF-8"));
            } catch (IOException e) {
                AbcdLogger.errorMessage(FormToDroolsExporter.class.getName(), e);
            }
            return formRules;

        }
        return null;
    }

    public static String getDroolRules(Form form, List<GlobalVariable> globalVariables) throws DroolsRuleGenerationException, RuleNotImplementedException,
            NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException, TreeObjectInstanceNotRecognizedException,
            TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException, BetweenFunctionInvalidException,
            DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException, InvalidRuleException,
            ActionNotImplementedException, InvalidExpressionException {
        if (form != null && form.getChildren() != null && !form.getChildren().isEmpty()) {
            DroolsRulesGenerator formRules;
            // Creation of the rules
            formRules = new DroolsRulesGenerator(form, globalVariables);
            return formRules.getRules();
        }
        return null;
    }

    /**
     * Process the test scenario. Orbeon not needed.
     *
     * @param form
     * @param globalVariables
     * @return the submitted form.
     * @throws DroolsRuleGenerationException
     * @throws DroolsRuleExecutionException
     * @throws ActionNotImplementedException
     * @throws InvalidRuleException
     * @throws PrattParserException
     * @throws DroolsRuleCreationException
     * @throws PluginInvocationException
     * @throws DateComparisonNotPossibleException
     * @throws BetweenFunctionInvalidException
     * @throws NullExpressionValueException
     * @throws NullCustomVariableException
     * @throws TreeObjectParentNotValidException
     * @throws TreeObjectInstanceNotRecognizedException
     * @throws NullTreeObjectException
     * @throws ExpressionInvalidException
     * @throws NotCompatibleTypeException
     * @throws RuleNotImplementedException
     * @throws InvalidExpressionException
     */
    public static ISubmittedForm processForm(Form form, List<GlobalVariable> globalVariables, ISubmittedForm submittedForm) throws DroolsRuleGenerationException,
            DroolsRuleExecutionException, RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
            TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
            BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException,
            InvalidRuleException, ActionNotImplementedException, InvalidExpressionException {
        // Generate all drools rules.
        TreeObjectDroolsIdMap.clearMap();
        DroolsRulesGenerator rulesGenerator = generateDroolRules(form, globalVariables);
        // Obtain results
        if ((rulesGenerator != null) && (submittedForm != null)) {
            DroolsRulesEngine droolsEngine = new DroolsRulesEngine();
            return droolsEngine.applyDrools(submittedForm, rulesGenerator.getRules(),
                    new ArrayList<>(RuleGenerationUtils.convertGlobalVariablesToDroolsGlobalVariables(globalVariables)));
        } else
            return null;
    }

    private static ISubmittedForm createDroolsSubmittedForm(BaseForm submittedBaseForm) {
        return DroolsFormProvider.createStructure(submittedBaseForm).getDroolsSubmittedForm();
    }

    public static ISubmittedForm processForm(Form form, List<GlobalVariable> globalVariables, FormResult submittedForm) throws DroolsRuleGenerationException,
            DroolsRuleExecutionException, RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
            TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
            BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException,
            InvalidRuleException, ActionNotImplementedException, InvalidExpressionException {
        return processForm(form, globalVariables, createDroolsSubmittedForm(submittedForm));
    }
}

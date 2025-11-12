package com.biit.abcd.core.drools.rules;

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

import com.biit.abcd.core.drools.FormToDroolsExporter;
import com.biit.abcd.core.drools.json.globalvariables.AbcdGlobalVariablesToJson;
import com.biit.abcd.core.drools.prattparser.exceptions.PrattParserException;
import com.biit.abcd.core.drools.prattparser.visitor.exceptions.NotCompatibleTypeException;
import com.biit.abcd.core.drools.rules.exceptions.*;
import com.biit.abcd.core.drools.rules.validators.InvalidExpressionException;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.abcd.utils.ZipUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DroolsZipGenerator {

    public byte[] getInformationData(Form form, List<GlobalVariable> globalVariables) throws IOException,
            TreeObjectInstanceNotRecognizedException, ExpressionInvalidException, DroolsRuleGenerationException,
            BetweenFunctionInvalidException, DroolsRuleCreationException, DateComparisonNotPossibleException,
            InvalidRuleException, NullExpressionValueException, NullTreeObjectException, PrattParserException,
            PluginInvocationException, RuleNotImplementedException, NullCustomVariableException,
            TreeObjectParentNotValidException, NotCompatibleTypeException, ActionNotImplementedException,
            InvalidExpressionException {
        List<String> filesToZip = new ArrayList<>();
        List<String> namesOfFiles = new ArrayList<>();
        String rules = FormToDroolsExporter.getDroolRules(form, globalVariables);
        filesToZip.add(rules);
        Integer formVersion = form.getVersion();
        String formName = form.getLabel();
        namesOfFiles.add(formName + "_v" + formVersion + ".drl");
        String variables = AbcdGlobalVariablesToJson.toJson(globalVariables);
        filesToZip.add(variables);
        namesOfFiles.add(formName + "_globalVariables_v" + formVersion + ".json");


        return ZipUtils.createZipFile(filesToZip, namesOfFiles);
    }
}

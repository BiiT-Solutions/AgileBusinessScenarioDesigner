package com.biit.abcd.core.drools.rules;

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

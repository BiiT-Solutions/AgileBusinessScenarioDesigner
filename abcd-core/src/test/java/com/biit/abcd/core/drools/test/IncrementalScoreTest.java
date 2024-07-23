package com.biit.abcd.core.drools.test;

import com.biit.abcd.core.drools.FormToDroolsExporter;
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
import com.biit.abcd.persistence.entity.Form;
import com.biit.drools.engine.exceptions.DroolsRuleExecutionException;
import com.biit.form.result.FormResult;
import com.biit.form.submitted.ISubmittedForm;
import com.biit.utils.file.FileReader;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Assert;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

@Test(groups = {"incrementalScore"})
@ContextConfiguration(locations = {"classpath:applicationContextTest.xml"})
public class IncrementalScoreTest {
    private static final String FORM_FILE_PATH = "forms/CADT_Score_1.json";
    private static final String FORM_RESULT_FILE_PATH = "formResults/cadtSubmittedForm.json";

    private Form form;
    private FormResult submittedForm;
    private String droolsRules;

    @BeforeClass
    public void readForm() throws FileNotFoundException, JsonProcessingException {
        form = Form.fromJson(FileReader.getResource(FORM_FILE_PATH, StandardCharsets.UTF_8));
        submittedForm = FormResult.fromJson(FileReader.getResource(FORM_RESULT_FILE_PATH, StandardCharsets.UTF_8));
    }

    @Test(enabled = false)
    public void createDroolsRules() throws BetweenFunctionInvalidException, PluginInvocationException, ActionNotImplementedException, ExpressionInvalidException, NullCustomVariableException, PrattParserException, NullTreeObjectException, TreeObjectParentNotValidException, NotCompatibleTypeException, TreeObjectInstanceNotRecognizedException, NullExpressionValueException, DateComparisonNotPossibleException, DroolsRuleCreationException, RuleNotImplementedException, DroolsRuleGenerationException, InvalidExpressionException, InvalidRuleException {
        //Create rules.
        droolsRules = FormToDroolsExporter.getDroolRules(form, new ArrayList<>());
        Assert.assertNotNull(droolsRules);
    }

    @Test
    public void executeDroolsRules() throws BetweenFunctionInvalidException, PluginInvocationException, ActionNotImplementedException, ExpressionInvalidException, NullCustomVariableException, PrattParserException, DroolsRuleExecutionException, NullTreeObjectException, TreeObjectParentNotValidException, NotCompatibleTypeException, TreeObjectInstanceNotRecognizedException, NullExpressionValueException, DateComparisonNotPossibleException, DroolsRuleCreationException, RuleNotImplementedException, DroolsRuleGenerationException, InvalidExpressionException, InvalidRuleException {
        final ISubmittedForm resultForm = FormToDroolsExporter.processForm(form, new ArrayList<>(), submittedForm);
        Assert.assertNotNull(resultForm);
    }

}

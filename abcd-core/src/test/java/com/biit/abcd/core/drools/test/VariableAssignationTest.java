package com.biit.abcd.core.drools.test;

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
import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.AnswerType;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.CustomVariableScope;
import com.biit.abcd.persistence.entity.CustomVariableType;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorLogic;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorMath;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueNumber;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueString;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.drools.engine.exceptions.DroolsRuleExecutionException;
import com.biit.drools.form.DroolsForm;
import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ChildrenNotFoundException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.TooManyResultsFoundException;
import com.biit.form.submitted.implementation.SubmittedCategory;
import com.biit.form.submitted.implementation.SubmittedQuestion;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import org.junit.Assert;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Test(groups = "droolsVariableTest")
@ContextConfiguration(locations = {"classpath:applicationContextTest.xml"})
public class VariableAssignationTest extends DroolsRulesBased {

    public static final String FORM_NAME = "VariablesTest";
    private static final Integer FORM_VERSION = 1;

    public static final String CATEGORY_NAME = "Category1";
    public static final String QUESTION_NAME = "question1";
    public static final String ANSWER_NAME = "answer1";
    public static final String VARIABLE_1_NAME = "Var1";
    public static final String VARIABLE_2_NAME = "Var2";
    public static final String VARIABLE_3_NAME = "Var3";

    private final static String FORM_TEXT = "formText";
    private final static String CATEGORY_TEXT = "categoryText";
    private final static String TEXT_SAMPLE = "This is a string";

    public Form createForm() throws CharacterNotAllowedException, FieldTooLongException, NotValidChildException, ElementIsReadOnly, TooManyResultsFoundException {
        Form form = new Form(FORM_NAME);
        form.setVersion(FORM_VERSION);

        Category category = new Category(CATEGORY_NAME);
        form.addChild(category);

        Question question1 = new Question(QUESTION_NAME);
        Answer answer1 = new Answer(ANSWER_NAME);
        Answer answer2 = new Answer("answer2");
        question1.setAnswerType(AnswerType.RADIO);
        question1.addChild(answer1);
        question1.addChild(answer2);

        category.addChild(question1);

        return form;
    }

    private void defineVariables(Form form) {
        Set<CustomVariable> customVariables = new HashSet<>();
        CustomVariable v1 = new CustomVariable(form, VARIABLE_1_NAME, CustomVariableType.STRING, CustomVariableScope.FORM);
        v1.setDefaultValue(" ");
        customVariables.add(v1);
        CustomVariable v2 = new CustomVariable(form, VARIABLE_2_NAME, CustomVariableType.NUMBER, CustomVariableScope.FORM);
        v2.setDefaultValue("0");
        customVariables.add(v2);
        CustomVariable v3 = new CustomVariable(form, VARIABLE_3_NAME, CustomVariableType.NUMBER, CustomVariableScope.FORM);
        v3.setDefaultValue(null);
        customVariables.add(v3);
        form.getCustomVariables().addAll(customVariables);
    }

    private void defineChainCustomVariablesRulesRules(Form form) throws TooManyResultsFoundException {
        defineVariables(form);

        Set<Rule> rules = new HashSet<>();

        CustomVariable v1 = form.getCustomVariable(VARIABLE_1_NAME, CustomVariableScope.FORM.toString());
        CustomVariable v2 = form.getCustomVariable(VARIABLE_2_NAME, CustomVariableScope.FORM.toString());
        CustomVariable v3 = form.getCustomVariable(VARIABLE_3_NAME, CustomVariableScope.FORM.toString());

        //q1 == a1 -> va1 = 1
        ExpressionChain condition1 = new ExpressionChain(v1.getName(), new ExpressionValueTreeObjectReference(form.getChild(Question.class, QUESTION_NAME)),
                new ExpressionOperatorLogic(AvailableOperator.EQUALS), new ExpressionValueTreeObjectReference(form.getChild(Answer.class, ANSWER_NAME)));
        ExpressionChain action1 = new ExpressionChain(v1.getName(), new ExpressionValueCustomVariable(form, v1),
                new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueString("1"));
        Rule ruleR1 = new Rule("R1", condition1, action1);
        rules.add(ruleR1);

        //v1 == '1' -> v2 = 2
        ExpressionChain condition2 = new ExpressionChain(v1.getName(), new ExpressionValueCustomVariable(form, v1),
                new ExpressionOperatorLogic(AvailableOperator.EQUALS), new ExpressionValueString("1"));
        ExpressionChain action2 = new ExpressionChain(v1.getName(), new ExpressionValueCustomVariable(form, v2),
                new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueNumber(2.0));
        Rule ruleV2 = new Rule("V2", condition2, action2);
        rules.add(ruleV2);

        //v2 == 2 -> v3 = 3
        ExpressionChain condition3 = new ExpressionChain(v2.getName(), new ExpressionValueCustomVariable(form, v2),
                new ExpressionOperatorLogic(AvailableOperator.EQUALS), new ExpressionValueNumber(2.0));
        ExpressionChain action3 = new ExpressionChain(v3.getName(), new ExpressionValueCustomVariable(form, v3),
                new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueNumber(3.0));
        Rule ruleV3 = new Rule("V3", condition3, action3);
        rules.add(ruleV3);

        form.setRules(rules);
    }

    @BeforeClass
    public DroolsSubmittedForm createSubmittedForm() {
        DroolsSubmittedForm submittedForm = new DroolsSubmittedForm(null, FORM_NAME, FORM_VERSION);

        SubmittedCategory categoryResult = new SubmittedCategory(CATEGORY_NAME);
        submittedForm.addChild(categoryResult);

        SubmittedQuestion question1 = new SubmittedQuestion(QUESTION_NAME);
        question1.addAnswer(ANSWER_NAME);
        categoryResult.addChild(question1);
        return submittedForm;
    }


    @Test
    public void executeVariableDrools() throws BetweenFunctionInvalidException, PluginInvocationException, ActionNotImplementedException,
            ExpressionInvalidException, NullCustomVariableException, PrattParserException, NullTreeObjectException, DroolsRuleExecutionException,
            TreeObjectParentNotValidException, NotCompatibleTypeException, TreeObjectInstanceNotRecognizedException, NullExpressionValueException,
            DateComparisonNotPossibleException, DroolsRuleCreationException, RuleNotImplementedException, DroolsRuleGenerationException,
            InvalidExpressionException, InvalidRuleException, NotValidChildException, ElementIsReadOnly, CharacterNotAllowedException,
            FieldTooLongException, TooManyResultsFoundException {

        Form form = createForm();
        defineChainCustomVariablesRulesRules(form);
        defineDiagram(form);

        DroolsSubmittedForm submittedForm = createSubmittedForm();

        DroolsForm droolsForm = executeDroolsEngine(form, submittedForm, new ArrayList<>());
        // Check result
        Assert.assertNotNull(droolsForm);
        org.testng.Assert.assertEquals(((DroolsSubmittedForm) (droolsForm).getDroolsSubmittedForm()).getFormVariables().values().iterator().next().get(VARIABLE_1_NAME), '1');
        org.testng.Assert.assertEquals(((DroolsSubmittedForm) (droolsForm).getDroolsSubmittedForm()).getFormVariables().values().iterator().next().get(VARIABLE_2_NAME), 2.0);
        org.testng.Assert.assertEquals(((DroolsSubmittedForm) (droolsForm).getDroolsSubmittedForm()).getFormVariables().values().iterator().next().get(VARIABLE_3_NAME), 3.0);
    }

    @Test
    public void executeExpressions() throws NotValidChildException, ElementIsReadOnly, CharacterNotAllowedException, FieldTooLongException,
            TooManyResultsFoundException, ChildrenNotFoundException, BetweenFunctionInvalidException, PluginInvocationException,
            ActionNotImplementedException, ExpressionInvalidException, NullCustomVariableException, PrattParserException, NullTreeObjectException,
            DroolsRuleExecutionException, TreeObjectParentNotValidException, NotCompatibleTypeException, TreeObjectInstanceNotRecognizedException,
            NullExpressionValueException, DateComparisonNotPossibleException, DroolsRuleCreationException, RuleNotImplementedException,
            DroolsRuleGenerationException, InvalidExpressionException, InvalidRuleException {
        Form form = createForm();

        CustomVariable categoryTextCustomVariable = new CustomVariable(form, CATEGORY_TEXT, CustomVariableType.STRING, CustomVariableScope.CATEGORY);
        CustomVariable formTextCustomVariable = new CustomVariable(form, FORM_TEXT, CustomVariableType.STRING, CustomVariableScope.FORM);

        Set<CustomVariable> customVariables = new HashSet<>();
        customVariables.add(categoryTextCustomVariable);
        customVariables.add(formTextCustomVariable);
        form.setCustomVariables(customVariables);

        Set<ExpressionChain> expressions = new HashSet<>();
        ExpressionChain expression1 = new ExpressionChain("AssignStrings1", new ExpressionValueCustomVariable(form.getChild(0), categoryTextCustomVariable),
                new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueString(TEXT_SAMPLE));
        expressions.add(expression1);
        // Assign variable1 to variable2
        ExpressionChain expression2 = new ExpressionChain("AssignStrings2", new ExpressionValueCustomVariable(form, formTextCustomVariable),
                new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueCustomVariable(form.getChild(0), categoryTextCustomVariable));
        expressions.add(expression2);

        form.setExpressionChains(expressions);
        defineDiagram(form);

        DroolsSubmittedForm submittedForm = createSubmittedForm();

        DroolsForm droolsForm = executeDroolsEngine(form, submittedForm, new ArrayList<>());
        // Check result
        Assert.assertNotNull(droolsForm);
        org.testng.Assert.assertEquals(((DroolsSubmittedForm) (droolsForm).getDroolsSubmittedForm()).getFormVariables().values().iterator().next().get(VARIABLE_1_NAME), '1');
    }
}

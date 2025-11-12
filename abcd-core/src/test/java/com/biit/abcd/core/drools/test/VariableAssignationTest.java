package com.biit.abcd.core.drools.test;

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
import com.biit.abcd.persistence.entity.GenericTreeObjectType;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.expressions.AvailableFunction;
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.AvailableSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionFunction;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorLogic;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorMath;
import com.biit.abcd.persistence.entity.expressions.ExpressionSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueGenericCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueNumber;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueString;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
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
    public static final String ANSWER1_NAME = "answer1";
    public static final String ANSWER2_NAME = "answer2";
    public static final String ANSWER3_NAME = "answer3";
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
        Answer answer1 = new Answer(ANSWER1_NAME);
        Answer answer2 = new Answer(ANSWER2_NAME);
        Answer answer3 = new Answer(ANSWER3_NAME);
        question1.setAnswerType(AnswerType.RADIO);
        question1.addChild(answer1);
        question1.addChild(answer2);
        question1.addChild(answer3);

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

        //q1 == a1 -> v1 = 1
        ExpressionChain condition1 = new ExpressionChain(v1.getName(), new ExpressionValueTreeObjectReference(form.getChild(Question.class, QUESTION_NAME)),
                new ExpressionOperatorLogic(AvailableOperator.EQUALS), new ExpressionValueTreeObjectReference(form.getChild(Answer.class, ANSWER1_NAME)));
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
        question1.addAnswer(ANSWER1_NAME);
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
        expression1.setSortSeq(0);
        expressions.add(expression1);
        // Assign variable1 to variable2
        ExpressionChain expression2 = new ExpressionChain("AssignStrings2", new ExpressionValueCustomVariable(form, formTextCustomVariable),
                new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueCustomVariable(form.getChild(0), categoryTextCustomVariable));
        expression2.setSortSeq(1);
        expressions.add(expression2);

        form.setExpressionChains(expressions);
        defineDiagram(form);

        DroolsSubmittedForm submittedForm = createSubmittedForm();

        DroolsForm droolsForm = executeDroolsEngine(form, submittedForm, new ArrayList<>());
        // Check result
        Assert.assertNotNull(droolsForm);
        org.testng.Assert.assertEquals(((DroolsSubmittedForm) (droolsForm).getDroolsSubmittedForm()).getFormVariables().values().iterator().next().get(FORM_TEXT), TEXT_SAMPLE);
    }

    @Test()
    public void executeCondition() throws FieldTooLongException, CharacterNotAllowedException, NotValidChildException,
            ElementIsReadOnly, TooManyResultsFoundException, BetweenFunctionInvalidException, PluginInvocationException,
            ActionNotImplementedException, ExpressionInvalidException, NullCustomVariableException, PrattParserException,
            NullTreeObjectException, DroolsRuleExecutionException, TreeObjectParentNotValidException, NotCompatibleTypeException,
            TreeObjectInstanceNotRecognizedException, NullExpressionValueException, DateComparisonNotPossibleException,
            DroolsRuleCreationException, RuleNotImplementedException, DroolsRuleGenerationException, InvalidExpressionException,
            InvalidRuleException {
        // Create a new form
        Form form = createForm();
        CustomVariable categoryCustomVariable = new CustomVariable(form, "catScore", CustomVariableType.NUMBER, CustomVariableScope.FORM);
        categoryCustomVariable.setDefaultValue("1");
        Set<CustomVariable> customVariables = new HashSet<>();
        customVariables.add(categoryCustomVariable);
        form.setCustomVariables(customVariables);

        final Set<ExpressionChain> expressions = new HashSet<>();
        // If expression
        ExpressionChain expression = new ExpressionChain("ifExpression", new ExpressionValueCustomVariable(form,
                categoryCustomVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionFunction(AvailableFunction.IF),
                new ExpressionValueGenericCustomVariable(GenericTreeObjectType.QUESTION_CATEGORY, categoryCustomVariable), new ExpressionOperatorLogic(
                AvailableOperator.LESS_THAN), new ExpressionValueNumber(56.), new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(
                7.1), new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(1.7), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
        expressions.add(expression);
        form.setExpressionChains(expressions);

        defineDiagram(form);
        // Create the rules and launch the engine
        DroolsSubmittedForm submittedForm = createSubmittedForm();

        DroolsForm droolsForm = executeDroolsEngine(form, submittedForm, new ArrayList<>());
        // Check result
        Assert.assertNotNull(droolsForm);
        org.testng.Assert.assertEquals(((DroolsSubmittedForm) (droolsForm).getDroolsSubmittedForm()).getFormVariables().values().iterator().next().get(VARIABLE_1_NAME), '1');
    }

    @Test()
    public void executeBetween() throws FieldTooLongException, CharacterNotAllowedException, NotValidChildException,
            ElementIsReadOnly, TooManyResultsFoundException, BetweenFunctionInvalidException, PluginInvocationException,
            ActionNotImplementedException, ExpressionInvalidException, NullCustomVariableException, PrattParserException,
            NullTreeObjectException, DroolsRuleExecutionException, TreeObjectParentNotValidException, NotCompatibleTypeException,
            TreeObjectInstanceNotRecognizedException, NullExpressionValueException, DateComparisonNotPossibleException,
            DroolsRuleCreationException, RuleNotImplementedException, DroolsRuleGenerationException, InvalidExpressionException,
            InvalidRuleException {
        // Create a new form
        Form form = createForm();

        // Define variables
        CustomVariable betweenCustomVariable = new CustomVariable(form, "betweenCustomVariable", CustomVariableType.NUMBER, CustomVariableScope.FORM);
        betweenCustomVariable.setDefaultValue("123");
        CustomVariable stringCustomVariable = new CustomVariable(form, "stringCustomVariable", CustomVariableType.STRING, CustomVariableScope.FORM);
        stringCustomVariable.setDefaultValue("asd");

        Set<CustomVariable> customVariables = new HashSet<>();
        customVariables.add(betweenCustomVariable);
        customVariables.add(stringCustomVariable);
        form.setCustomVariables(customVariables);

        Set<ExpressionChain> expressions = new HashSet<>();
        ExpressionChain expression1 = new ExpressionChain("AssignString", new ExpressionValueCustomVariable(form, betweenCustomVariable),
                new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueNumber(10d));
        expressions.add(expression1);
        form.setExpressionChains(expressions);

        // BETWEEN rule
        final Set<Rule> rules = new HashSet<>();
        Rule rule = new Rule();
        ExpressionChain condition = new ExpressionChain("betweenNumberExpression", new ExpressionValueCustomVariable(form, betweenCustomVariable),
                new ExpressionFunction(AvailableFunction.BETWEEN), new ExpressionValueNumber(2.), new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(11.),
                new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
        rule.setConditions(condition);
        ExpressionChain action = new ExpressionChain(new ExpressionValueCustomVariable(form, stringCustomVariable), new ExpressionOperatorMath(
                AvailableOperator.ASSIGNATION), new ExpressionValueString(TEXT_SAMPLE));
        rule.setActions(action);
        rules.add(rule);

        form.setRules(rules);
        defineDiagram(form);

        DroolsSubmittedForm submittedForm = createSubmittedForm();

        DroolsForm droolsForm = executeDroolsEngine(form, submittedForm, new ArrayList<>());
        // Check result
        Assert.assertNotNull(droolsForm);
        Assert.assertEquals(((DroolsSubmittedForm) (droolsForm).getDroolsSubmittedForm()).getFormVariables().values().iterator().next().get("stringCustomVariable"), TEXT_SAMPLE);
    }

    @Test()
    private void testTableRule() throws FieldTooLongException, NotValidChildException, TooManyResultsFoundException, ElementIsReadOnly,
            CharacterNotAllowedException, BetweenFunctionInvalidException, PluginInvocationException, ActionNotImplementedException,
            ExpressionInvalidException, NullCustomVariableException, PrattParserException, NullTreeObjectException, DroolsRuleExecutionException,
            TreeObjectParentNotValidException, NotCompatibleTypeException, TreeObjectInstanceNotRecognizedException, NullExpressionValueException,
            DateComparisonNotPossibleException, DroolsRuleCreationException, RuleNotImplementedException, DroolsRuleGenerationException,
            InvalidExpressionException, InvalidRuleException {
        // Create a simple form
        Form form = createForm();

        CustomVariable categoryVariable = new CustomVariable(form, VARIABLE_1_NAME, CustomVariableType.NUMBER, CustomVariableScope.CATEGORY, "0.0");

        Set<CustomVariable> customVariables = new HashSet<>();
        customVariables.add(categoryVariable);
        form.setCustomVariables(customVariables);

        ExpressionChain commonAction = new ExpressionChain(new ExpressionValueCustomVariable(form.getChild(Category.class, CATEGORY_NAME), categoryVariable),
                new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueCustomVariable(form.getChild(Category.class, CATEGORY_NAME),
                categoryVariable), new ExpressionOperatorMath(AvailableOperator.PLUS), new ExpressionValueNumber(1.0));

        // Create the tableRule
        // Only with one conditions column
        TableRule tableRule = new TableRule("TestTable");
        // Question == Answer
        TableRuleRow ruleRow = new TableRuleRow();

        if (form.getChild(Answer.class, ANSWER1_NAME) != null) {
            ruleRow.addCondition(new ExpressionValueTreeObjectReference(form.getChild(Question.class, QUESTION_NAME)));
            ruleRow.addCondition(new ExpressionChain(new ExpressionValueTreeObjectReference(form.getChild(Answer.class, ANSWER1_NAME))));
            ruleRow.setActions(commonAction);
            tableRule.getRules().add(ruleRow);
        }

        if (form.getChild(Answer.class, ANSWER2_NAME) != null) {
            ruleRow = new TableRuleRow();
            ruleRow.addCondition(new ExpressionValueTreeObjectReference(form.getChild(Question.class, QUESTION_NAME)));
            ruleRow.addCondition(new ExpressionChain(new ExpressionValueTreeObjectReference(form.getChild(Answer.class, ANSWER2_NAME))));
            ruleRow.setActions(commonAction);
            tableRule.getRules().add(ruleRow);
        }

        if (form.getChild(Answer.class, ANSWER3_NAME) != null) {
            ruleRow = new TableRuleRow();
            ruleRow.addCondition(new ExpressionValueTreeObjectReference(form.getChild(Question.class, QUESTION_NAME)));
            ruleRow.addCondition(new ExpressionChain(new ExpressionValueTreeObjectReference(form.getChild(Answer.class, ANSWER3_NAME))));
            ruleRow.setActions(commonAction);
            tableRule.getRules().add(ruleRow);
        }

        // Add the table rule
        form.getTableRules().add(tableRule);
        // Creation of a simple diagram to load the table rule
        defineDiagram(form);

        // Create the rules and launch the engine
        DroolsSubmittedForm submittedForm = createSubmittedForm();
        // Check result
        Assert.assertNotNull(submittedForm);

        DroolsForm droolsForm = executeDroolsEngine(form, submittedForm, new ArrayList<>());

        Assert.assertEquals(((DroolsSubmittedForm) (droolsForm).getDroolsSubmittedForm()).getFormVariables().values().iterator().next().get("stringCustomVariable"), 3.0);
    }
}

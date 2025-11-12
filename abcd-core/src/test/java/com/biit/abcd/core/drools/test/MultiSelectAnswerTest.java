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
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.biit.abcd.persistence.entity.diagram.DiagramObjectType;
import com.biit.abcd.persistence.entity.diagram.DiagramSink;
import com.biit.abcd.persistence.entity.diagram.DiagramSource;
import com.biit.abcd.persistence.entity.diagram.DiagramTable;
import com.biit.abcd.persistence.entity.diagram.Node;
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorMath;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueNumber;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.biit.abcd.persistence.utils.IdGenerator;
import com.biit.drools.engine.exceptions.DroolsRuleExecutionException;
import com.biit.drools.form.DroolsForm;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.submitted.ISubmittedCategory;
import com.biit.form.submitted.ISubmittedFormElement;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import org.dom4j.DocumentException;
import org.junit.Assert;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * Checks the correct creation and execution of table rules <br>
 * Also checks the correct loading in memory of the submitted form from orbeon
 */
@ContextConfiguration(locations = {"classpath:applicationContextTest.xml"})
public class MultiSelectAnswerTest extends KidsFormCreator {

    private static final String FORM_NAME = "MultiSelectAnswer";
    private static final Integer FORM_VERSION = 1;
    private static final String CATEGORY_NAME = "category1";
    private static final String QUESTION_NAME = "question1";
    private static final String ANSWER1_NAME = "answer1";
    private static final String ANSWER2_NAME = "answer2";
    private static final String ANSWER3_NAME = "answer3";
    private static final String CATEGORY_VARIABLE_NAME = "score";
    private final static Double MULTI_SELECT_VALUE = 3.0;
    protected Form testForm = null;

    @Override
    public Form createForm() throws FieldTooLongException, CharacterNotAllowedException, NotValidChildException, ElementIsReadOnly {
        Form form = new Form(FORM_NAME);
        form.setVersion(FORM_VERSION);
        Category category = new Category(CATEGORY_NAME);
        form.addChild(category);

        Question question = new Question(QUESTION_NAME);
        question.setAnswerType(AnswerType.MULTI_CHECKBOX);
        category.addChild(question);

        Answer answer1 = new Answer(ANSWER1_NAME);
        Answer answer2 = new Answer(ANSWER2_NAME);
        Answer answer3 = new Answer(ANSWER3_NAME);
        question.addChild(answer1);
        question.addChild(answer2);
        question.addChild(answer3);

        return form;
    }

    private Form createFormOnlyOneAnswer() throws FieldTooLongException, CharacterNotAllowedException, NotValidChildException, ElementIsReadOnly {
        Form form = new Form(FORM_NAME);
        form.setVersion(FORM_VERSION);
        Category category = new Category(CATEGORY_NAME);
        form.addChild(category);

        Question question = new Question(QUESTION_NAME);
        question.setAnswerType(AnswerType.MULTI_CHECKBOX);
        category.addChild(question);

        Answer answer1 = new Answer(ANSWER1_NAME);
        question.addChild(answer1);

        return form;
    }

    @Test(groups = {"multiSelectAnswer"})
    private void testQuestionOneAnswerTableRule() throws FieldTooLongException, NotValidChildException,
            CharacterNotAllowedException, ExpressionInvalidException, InvalidRuleException, IOException,
            RuleNotImplementedException, DocumentException, ActionNotImplementedException, NotCompatibleTypeException, NullTreeObjectException,
            TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
            BetweenFunctionInvalidException, ElementIsReadOnly,
            DroolsRuleGenerationException, DroolsRuleExecutionException, DateComparisonNotPossibleException, PluginInvocationException,
            DroolsRuleCreationException, PrattParserException, InvalidExpressionException {
        // Create a simple form
        Form form = createFormOnlyOneAnswer();
        // Create the table and diagram
        createQuestionAnswerTableRule(form);
        // Create the rules and launch the engine
        DroolsForm submittedForm = createAndRunDroolsRules(form);
        // Check result
        Assert.assertNotNull(submittedForm);
        Assert.assertEquals(1.0, ((ISubmittedFormElement) (submittedForm.getDroolsSubmittedForm()).getChild(
                ISubmittedCategory.class, CATEGORY_NAME)).getVariableValue(CATEGORY_VARIABLE_NAME));
    }

    @Test(groups = {"multiSelectAnswer"})
    private void testQuestionAnswerTableRule() throws FieldTooLongException, NotValidChildException,
            CharacterNotAllowedException, ExpressionInvalidException, InvalidRuleException, IOException,
            RuleNotImplementedException, DocumentException, ActionNotImplementedException, NotCompatibleTypeException, NullTreeObjectException,
            TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
            BetweenFunctionInvalidException, ElementIsReadOnly,
            DroolsRuleGenerationException, DroolsRuleExecutionException, DateComparisonNotPossibleException, PluginInvocationException,
            DroolsRuleCreationException, PrattParserException, InvalidExpressionException {
        // Create a simple form
        Form form = createForm();
        // Create the table and diagram
        createQuestionAnswerTableRule(form);
        // Create the rules and launch the engine
        DroolsForm submittedForm = createAndRunDroolsRules(form);
        // Check result
        Assert.assertNotNull(submittedForm);

        Assert.assertEquals(MULTI_SELECT_VALUE, ((ISubmittedFormElement) (submittedForm.getDroolsSubmittedForm())
                .getChild(ISubmittedCategory.class, CATEGORY_NAME)).getVariableValue(CATEGORY_VARIABLE_NAME));
    }

    private void createQuestionAnswerTableRule(Form form) {

        CustomVariable categoryVariable = new CustomVariable(form, CATEGORY_VARIABLE_NAME, CustomVariableType.NUMBER, CustomVariableScope.CATEGORY, "0.0");

        ExpressionChain commonAction = new ExpressionChain(new ExpressionValueCustomVariable((Category) getTreeObject(form, CATEGORY_NAME), categoryVariable),
                new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueCustomVariable((Category) getTreeObject(form, CATEGORY_NAME),
                categoryVariable), new ExpressionOperatorMath(AvailableOperator.PLUS), new ExpressionValueNumber(1.0));

        // Create the tableRule
        // Only with one conditions column
        TableRule tableRule = new TableRule("TestTable");
        // Question == Answer
        TableRuleRow ruleRow = new TableRuleRow();

        if (getTreeObject(form, ANSWER1_NAME) != null) {
            ruleRow.addCondition(new ExpressionValueTreeObjectReference((getTreeObject(form, QUESTION_NAME))));
            ruleRow.addCondition(new ExpressionChain(new ExpressionValueTreeObjectReference(getTreeObject(form, ANSWER1_NAME))));
            ruleRow.setActions(commonAction);
            tableRule.getRules().add(ruleRow);
        }

        if (getTreeObject(form, ANSWER2_NAME) != null) {
            ruleRow = new TableRuleRow();
            ruleRow.addCondition(new ExpressionValueTreeObjectReference((getTreeObject(form, QUESTION_NAME))));
            ruleRow.addCondition(new ExpressionChain(new ExpressionValueTreeObjectReference(getTreeObject(form, ANSWER2_NAME))));
            ruleRow.setActions(commonAction);
            tableRule.getRules().add(ruleRow);
        }

        if (getTreeObject(form, ANSWER3_NAME) != null) {
            ruleRow = new TableRuleRow();
            ruleRow.addCondition(new ExpressionValueTreeObjectReference((getTreeObject(form, QUESTION_NAME))));
            ruleRow.addCondition(new ExpressionChain(new ExpressionValueTreeObjectReference(getTreeObject(form, ANSWER3_NAME))));
            ruleRow.setActions(commonAction);
            tableRule.getRules().add(ruleRow);
        }

        // Add the table rule
        form.getTableRules().add(tableRule);
        // Creation of a simple diagram to load the table rule
        form.addDiagram(createSimpleTableDiagram(tableRule));
    }

    private Diagram createSimpleTableDiagram(TableRule tableRule) {
        Diagram mainDiagram = new Diagram("main");

        DiagramSource diagramStartNode = new DiagramSource();
        diagramStartNode.setJointjsId(IdGenerator.createId());
        diagramStartNode.setType(DiagramObjectType.SOURCE);
        Node nodeSource = new Node(diagramStartNode.getJointjsId());

        DiagramTable diagramTableRuleNode = new DiagramTable();
        diagramTableRuleNode.setTable(tableRule);
        diagramTableRuleNode.setJointjsId(IdGenerator.createId());
        diagramTableRuleNode.setType(DiagramObjectType.TABLE);
        Node nodeTable = new Node(diagramTableRuleNode.getJointjsId());

        DiagramSink diagramEndNode = new DiagramSink();
        diagramEndNode.setJointjsId(IdGenerator.createId());
        diagramEndNode.setType(DiagramObjectType.SINK);
        Node nodeSink = new Node(diagramEndNode.getJointjsId());

        DiagramLink startTable = new DiagramLink(nodeSource, nodeTable);
        startTable.setJointjsId(IdGenerator.createId());
        startTable.setType(DiagramObjectType.LINK);
        DiagramLink tableEnd = new DiagramLink(nodeTable, nodeSink);
        tableEnd.setJointjsId(IdGenerator.createId());
        tableEnd.setType(DiagramObjectType.LINK);

        mainDiagram.addDiagramObject(diagramStartNode);
        mainDiagram.addDiagramObject(diagramTableRuleNode);
        mainDiagram.addDiagramObject(diagramEndNode);
        mainDiagram.addDiagramObject(startTable);
        mainDiagram.addDiagramObject(tableEnd);

        return mainDiagram;
    }
}

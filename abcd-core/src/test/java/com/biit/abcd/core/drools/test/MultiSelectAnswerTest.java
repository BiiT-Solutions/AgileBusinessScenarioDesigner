package com.biit.abcd.core.drools.test;

import java.io.IOException;

import org.dom4j.DocumentException;
import org.junit.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.core.drools.prattparser.visitor.exceptions.NotCompatibleTypeException;
import com.biit.abcd.core.drools.rules.exceptions.ActionNotImplementedException;
import com.biit.abcd.core.drools.rules.exceptions.BetweenFunctionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.InvalidRuleException;
import com.biit.abcd.core.drools.rules.exceptions.NullCustomVariableException;
import com.biit.abcd.core.drools.rules.exceptions.NullExpressionValueException;
import com.biit.abcd.core.drools.rules.exceptions.NullTreeObjectException;
import com.biit.abcd.core.drools.rules.exceptions.RuleNotImplementedException;
import com.biit.abcd.core.drools.rules.exceptions.TreeObjectInstanceNotRecognizedException;
import com.biit.abcd.core.drools.rules.exceptions.TreeObjectParentNotValidException;
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
import com.biit.drools.form.DroolsForm;
import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.drools.global.variables.exceptions.NotValidTypeInVariableData;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.submitted.ISubmittedCategory;
import com.biit.form.submitted.ISubmittedForm;
import com.biit.form.submitted.ISubmittedFormElement;
import com.biit.form.submitted.exceptions.CategoryDoesNotExistException;
import com.biit.form.submitted.exceptions.GroupDoesNotExistException;
import com.biit.form.submitted.exceptions.QuestionDoesNotExistException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

/**
 * Checks the correct creation and execution of table rules <br>
 * Also checks the correct loading in memory of the submitted form from orbeon
 */
public class MultiSelectAnswerTest extends KidsFormCreator {

	private static final String FORM_NAME = "MultiSelectAnswer";
	private static final Integer FORM_VERSION = 1;
	private static final String CATEGORY_NAME = "Category1";
	private static final String QUESTION_NAME = "question1";
	private static final String ANSWER1_NAME = "answer1";
	private static final String ANSWER2_NAME = "answer2";
	private static final String ANSWER3_NAME = "answer3";
	private static final String CATEGORY_VARIABLE_NAME = "score";
	private final static Double MULTI_SELECT_ANSWER = 3.0;
	protected Form testForm = null;
	private Category category = null;
	private Question question = null;
	private Answer answer1 = null;
	private Answer answer2 = null;
	private Answer answer3 = null;

	@Override
	public Question getQuestion() {
		return question;
	}

	@Override
	public Category getCategory() {
		return category;
	}

	@Override
	public void setCategory(Category category) {
		this.category = category;
	}

	private void setQuestion(Question question) {
		this.question = question;
	}

	private Answer getAnswer1() {
		return answer1;
	}

	private void setAnswer1(Answer answer1) {
		this.answer1 = answer1;
	}

	private Answer getAnswer2() {
		return answer2;
	}

	private void setAnswer2(Answer answer2) {
		this.answer2 = answer2;
	}

	private Answer getAnswer3() {
		return answer3;
	}

	private void setAnswer3(Answer answer3) {
		this.answer3 = answer3;
	}

	private void createForm() throws FieldTooLongException, CharacterNotAllowedException, NotValidChildException, ElementIsReadOnly {
		setForm(new Form(FORM_NAME));
		getForm().setVersion(FORM_VERSION);

		Category category = new Category(CATEGORY_NAME);
		getForm().addChild(category);
		setCategory(category);

		Question question = new Question(QUESTION_NAME);
		question.setAnswerType(AnswerType.MULTI_CHECKBOX);
		category.addChild(question);

		Answer answer1 = new Answer(ANSWER1_NAME);
		Answer answer2 = new Answer(ANSWER2_NAME);
		Answer answer3 = new Answer(ANSWER3_NAME);
		question.addChild(answer1);
		question.addChild(answer2);
		question.addChild(answer3);

		setQuestion(question);
		setAnswer1(answer1);
		setAnswer2(answer2);
		setAnswer3(answer3);
	}

	@Test(groups = { "multiSelectAnswer" })
	private void testQuestionAnswerTableRule() throws FieldTooLongException, NotValidChildException, InvalidAnswerFormatException,
			CharacterNotAllowedException, NotValidTypeInVariableData, ExpressionInvalidException, InvalidRuleException, IOException,
			RuleNotImplementedException, DocumentException, ActionNotImplementedException, NotCompatibleTypeException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			QuestionDoesNotExistException, GroupDoesNotExistException, CategoryDoesNotExistException, BetweenFunctionInvalidException, ElementIsReadOnly {
		// Create a simple form
		createForm();
		// Create the table and diagram
		createQuestionAnswerTableRule();
		// Create the rules and launch the engine
		ISubmittedForm submittedForm = createAndRunDroolsRules();
		// Check result
		if (submittedForm != null) {
			Assert.assertEquals(MULTI_SELECT_ANSWER, ((ISubmittedFormElement) ((DroolsSubmittedForm) ((DroolsForm) submittedForm).getDroolsSubmittedForm())
					.getChild(ISubmittedCategory.class, "Category1")).getVariableValue(CATEGORY_VARIABLE_NAME));
		} else {
			Assert.fail();
		}
	}

	private void createQuestionAnswerTableRule() throws FieldTooLongException, NotValidChildException, InvalidAnswerFormatException {

		CustomVariable categoryVariable = new CustomVariable(getForm(), CATEGORY_VARIABLE_NAME, CustomVariableType.NUMBER, CustomVariableScope.CATEGORY, "0.0");

		ExpressionChain commonAction = new ExpressionChain(new ExpressionValueCustomVariable(getCategory(), categoryVariable), new ExpressionOperatorMath(
				AvailableOperator.ASSIGNATION), new ExpressionValueCustomVariable(getCategory(), categoryVariable), new ExpressionOperatorMath(
				AvailableOperator.PLUS), new ExpressionValueNumber(1.0));

		// Create the tableRule
		// Only with one conditions column
		TableRule tableRule = new TableRule("TestTable");
		// Question == Answer
		TableRuleRow ruleRow = new TableRuleRow();

		ruleRow.addCondition(new ExpressionValueTreeObjectReference(getQuestion()));
		ruleRow.addCondition(new ExpressionChain(new ExpressionValueTreeObjectReference(getAnswer1())));
		ruleRow.setAction(commonAction);
		tableRule.getRules().add(ruleRow);

		ruleRow = new TableRuleRow();
		ruleRow.addCondition(new ExpressionValueTreeObjectReference(getQuestion()));
		ruleRow.addCondition(new ExpressionChain(new ExpressionValueTreeObjectReference(getAnswer2())));
		ruleRow.setAction(commonAction);
		tableRule.getRules().add(ruleRow);

		ruleRow = new TableRuleRow();
		ruleRow.addCondition(new ExpressionValueTreeObjectReference(getQuestion()));
		ruleRow.addCondition(new ExpressionChain(new ExpressionValueTreeObjectReference(getAnswer3())));
		ruleRow.setAction(commonAction);
		tableRule.getRules().add(ruleRow);

		// Add the table rule
		getForm().getTableRules().add(tableRule);
		// Creation of a simple diagram to load the table rule
		getForm().addDiagram(createSimpleTableDiagram(tableRule));
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

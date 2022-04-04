package com.biit.abcd.core.drools.test;

import com.biit.abcd.core.drools.prattparser.exceptions.PrattParserException;
import com.biit.abcd.core.drools.prattparser.visitor.exceptions.NotCompatibleTypeException;
import com.biit.abcd.core.drools.rules.exceptions.*;
import com.biit.abcd.core.drools.rules.validators.InvalidExpressionException;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.CustomVariableScope;
import com.biit.abcd.persistence.entity.CustomVariableType;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.diagram.*;
import com.biit.abcd.persistence.entity.expressions.*;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.biit.abcd.persistence.utils.IdGenerator;
import com.biit.drools.engine.exceptions.DroolsRuleExecutionException;
import com.biit.drools.form.DroolsForm;
import com.biit.drools.form.DroolsSubmittedQuestion;
import com.biit.drools.global.variables.exceptions.NotValidTypeInVariableData;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.submitted.ISubmittedCategory;
import com.biit.form.submitted.ISubmittedGroup;
import com.biit.form.submitted.ISubmittedQuestion;
import com.biit.form.submitted.exceptions.CategoryDoesNotExistException;
import com.biit.form.submitted.exceptions.GroupDoesNotExistException;
import com.biit.form.submitted.exceptions.QuestionDoesNotExistException;
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
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
public class NestedGroupsTest extends KidsFormCreator {

	private final static String QUESTION_EQUALS_ANSWER = "works";
	private final static String QUESTION_NOT_EQUALS_ANSWER = "works";

	public NestedGroupsTest() throws FieldTooLongException, NotValidChildException, InvalidAnswerFormatException {
		super();
	}

	// Simple table question answer
	@Test(groups = { "droolsNestedGroups" })
	public void testSimpleTableRule() throws FieldTooLongException, NotValidChildException, InvalidAnswerFormatException, ExpressionInvalidException,
			InvalidRuleException, IOException, RuleNotImplementedException, DocumentException, QuestionDoesNotExistException, GroupDoesNotExistException,
			CategoryDoesNotExistException, ActionNotImplementedException, CharacterNotAllowedException, NotCompatibleTypeException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			NotValidTypeInVariableData, BetweenFunctionInvalidException, ElementIsReadOnly, DroolsRuleGenerationException, DroolsRuleExecutionException,
			DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException, InvalidExpressionException {

		// Restart the form to avoid test cross references
		Form form = createForm();
		// Create the table and form diagram
		createKidsFormSimpleConditionsTable(form);
		// Create the rules and launch the engine
		DroolsForm droolsForm = createAndRunDroolsRules(form);

		Assert.assertEquals(
				QUESTION_EQUALS_ANSWER,
				((DroolsSubmittedQuestion) droolsForm.getDroolsSubmittedForm().getChild(ISubmittedCategory.class, "Lifestyle")
						.getChild(ISubmittedGroup.class, "voeding").getChild(ISubmittedQuestion.class, "breakfast")).getVariableValue("qVar"));
	}

	private void createKidsFormSimpleConditionsTable(Form form) throws FieldTooLongException, NotValidChildException, InvalidAnswerFormatException {

		CustomVariable questionVariable = new CustomVariable(form, "qVar", CustomVariableType.STRING, CustomVariableScope.QUESTION);

		// Create the tableRule
		// Only with one conditions colum
		TableRule tableRule = new TableRule("TestTable");

		// Question == Answer
		TableRuleRow ruleRow = new TableRuleRow();
		ruleRow.addCondition(new ExpressionValueTreeObjectReference(getTreeObject(form, "breakfast")));
		ruleRow.addCondition(new ExpressionChain(new ExpressionOperatorLogic(AvailableOperator.EQUALS), new ExpressionValueTreeObjectReference(getAnswer(form,
				"breakfast", "a"))));
		ruleRow.setAction(new ExpressionChain(new ExpressionValueCustomVariable(getTreeObject(form, "breakfast"), questionVariable),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueString(QUESTION_NOT_EQUALS_ANSWER)));
		tableRule.getRules().add(ruleRow);
		ruleRow = new TableRuleRow();
		ruleRow.addCondition(new ExpressionValueTreeObjectReference(getTreeObject(form, "breakfast")));
		ruleRow.addCondition(new ExpressionChain(new ExpressionOperatorLogic(AvailableOperator.EQUALS), new ExpressionValueTreeObjectReference(getAnswer(form,
				"breakfast", "b"))));
		ruleRow.setAction(new ExpressionChain(new ExpressionValueCustomVariable(getTreeObject(form, "breakfast"), questionVariable),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueString(QUESTION_EQUALS_ANSWER)));
		tableRule.getRules().add(ruleRow);
		ruleRow = new TableRuleRow();
		ruleRow.addCondition(new ExpressionValueTreeObjectReference(getTreeObject(form, "breakfast")));
		ruleRow.addCondition(new ExpressionChain(new ExpressionOperatorLogic(AvailableOperator.EQUALS), new ExpressionValueTreeObjectReference(getAnswer(form,
				"breakfast", "c"))));
		ruleRow.setAction(new ExpressionChain(new ExpressionValueCustomVariable(getTreeObject(form, "breakfast"), questionVariable),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueString(QUESTION_NOT_EQUALS_ANSWER)));
		tableRule.getRules().add(ruleRow);
		ruleRow = new TableRuleRow();
		ruleRow.addCondition(new ExpressionValueTreeObjectReference(getTreeObject(form, "breakfast")));
		ruleRow.addCondition(new ExpressionChain(new ExpressionOperatorLogic(AvailableOperator.EQUALS), new ExpressionValueTreeObjectReference(getAnswer(form,
				"breakfast", "d"))));
		ruleRow.setAction(new ExpressionChain(new ExpressionValueCustomVariable(getTreeObject(form, "breakfast"), questionVariable),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueString(QUESTION_NOT_EQUALS_ANSWER)));
		tableRule.getRules().add(ruleRow);
		ruleRow = new TableRuleRow();
		ruleRow.addCondition(new ExpressionValueTreeObjectReference(getTreeObject(form, "breakfast")));
		ruleRow.addCondition(new ExpressionChain(new ExpressionOperatorLogic(AvailableOperator.EQUALS), new ExpressionValueTreeObjectReference(getAnswer(form,
				"breakfast", "e"))));
		ruleRow.setAction(new ExpressionChain(new ExpressionValueCustomVariable(getTreeObject(form, "breakfast"), questionVariable),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueString(QUESTION_NOT_EQUALS_ANSWER)));
		tableRule.getRules().add(ruleRow);

		// Add the table rule
		form.getTableRules().add(tableRule);
		// Creation of a simple diagram to load the table rule
		form.addDiagram(this.createSimpleTableDiagram(tableRule));
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

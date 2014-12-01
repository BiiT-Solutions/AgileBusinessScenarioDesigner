package com.biit.abcd.core.drools.test;

import java.io.IOException;

import org.dom4j.DocumentException;
import org.junit.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.core.drools.facts.inputform.DroolsForm;
import com.biit.abcd.core.drools.facts.inputform.SubmittedQuestion;
import com.biit.abcd.core.drools.prattparser.visitor.exceptions.NotCompatibleTypeException;
import com.biit.abcd.core.drools.rules.exceptions.ActionNotImplementedException;
import com.biit.abcd.core.drools.rules.exceptions.BetweenFunctionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.NullCustomVariableException;
import com.biit.abcd.core.drools.rules.exceptions.NullExpressionValueException;
import com.biit.abcd.core.drools.rules.exceptions.NullTreeObjectException;
import com.biit.abcd.core.drools.rules.exceptions.RuleInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleNotImplementedException;
import com.biit.abcd.core.drools.rules.exceptions.TreeObjectInstanceNotRecognizedException;
import com.biit.abcd.core.drools.rules.exceptions.TreeObjectParentNotValidException;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.CustomVariableScope;
import com.biit.abcd.persistence.entity.CustomVariableType;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.biit.abcd.persistence.entity.diagram.DiagramObjectType;
import com.biit.abcd.persistence.entity.diagram.DiagramSink;
import com.biit.abcd.persistence.entity.diagram.DiagramSource;
import com.biit.abcd.persistence.entity.diagram.DiagramTable;
import com.biit.abcd.persistence.entity.diagram.Node;
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorLogic;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorMath;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueString;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.globalvariables.exceptions.NotValidTypeInVariableData;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.biit.abcd.persistence.utils.IdGenerator;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.orbeon.exceptions.CategoryNameWithoutTranslation;
import com.biit.orbeon.form.ICategory;
import com.biit.orbeon.form.IGroup;
import com.biit.orbeon.form.IQuestion;
import com.biit.orbeon.form.ISubmittedForm;
import com.biit.orbeon.form.exceptions.CategoryDoesNotExistException;
import com.biit.orbeon.form.exceptions.GroupDoesNotExistException;
import com.biit.orbeon.form.exceptions.QuestionDoesNotExistException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

/**
 * Checks the correct creation and execution of table rules <br>
 * Also checks the correct loading in memory of the submitted form from orbeon
 */
public class NestedGroupsTest extends KidsFormCreator {

	private final static String QUESTION_EQUALS_ANSWER = "works";
	private final static String QUESTION_NOT_EQUALS_ANSWER = "works";

	public NestedGroupsTest() throws FieldTooLongException, NotValidChildException, InvalidAnswerFormatException {
		super();
	}

	// Simple table question answer
	@Test(groups = { "rules" }, dependsOnMethods = { "translateFormCategories" })
	public void testSimpleTableRule() throws FieldTooLongException, NotValidChildException,
			InvalidAnswerFormatException, ExpressionInvalidException, RuleInvalidException, IOException,
			RuleNotImplementedException, DocumentException, CategoryNameWithoutTranslation,
			QuestionDoesNotExistException, GroupDoesNotExistException, CategoryDoesNotExistException,
			ActionNotImplementedException, CharacterNotAllowedException, NotCompatibleTypeException,
			NullTreeObjectException, TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException,
			NullCustomVariableException, NullExpressionValueException, NotValidTypeInVariableData,
			BetweenFunctionInvalidException {

		// Restart the form to avoid test cross references
		initForm();
		// Create the table and form diagram
		createKidsFormSimpleConditionsTable();
		// Create the rules and launch the engine
		DroolsForm droolsForm = createAndRunDroolsRules();
		
		Assert.assertEquals(QUESTION_EQUALS_ANSWER,
				((SubmittedQuestion) droolsForm.getSubmittedForm().getChild(ICategory.class, "Lifestyle")
						.getChild(IGroup.class, "voeding").getChild(IQuestion.class, "breakfast"))
						.getVariableValue("qVar"));
	}

	private void createKidsFormSimpleConditionsTable() throws FieldTooLongException, NotValidChildException,
			InvalidAnswerFormatException {

		CustomVariable questionVariable = new CustomVariable(getForm(), "qVar", CustomVariableType.STRING,
				CustomVariableScope.QUESTION);

		// Create the tableRule
		// Only with one conditions colum
		TableRule tableRule = new TableRule("TestTable");

		// Question == Answer
		TableRuleRow ruleRow = new TableRuleRow();
		ruleRow.addCondition(new ExpressionValueTreeObjectReference(getTreeObject("breakfast")));
		ruleRow.addCondition(new ExpressionChain(new ExpressionOperatorLogic(AvailableOperator.EQUALS),
				new ExpressionValueTreeObjectReference(getAnswer("breakfast", "a"))));
		ruleRow.setAction(new ExpressionChain(new ExpressionValueCustomVariable(getTreeObject("breakfast"),
				questionVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionValueString(QUESTION_NOT_EQUALS_ANSWER)));
		tableRule.getRules().add(ruleRow);
		ruleRow = new TableRuleRow();
		ruleRow.addCondition(new ExpressionValueTreeObjectReference(getTreeObject("breakfast")));
		ruleRow.addCondition(new ExpressionChain(new ExpressionOperatorLogic(AvailableOperator.EQUALS),
				new ExpressionValueTreeObjectReference(getAnswer("breakfast", "b"))));
		ruleRow.setAction(new ExpressionChain(new ExpressionValueCustomVariable(getTreeObject("breakfast"),
				questionVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionValueString(QUESTION_EQUALS_ANSWER)));
		tableRule.getRules().add(ruleRow);
		ruleRow = new TableRuleRow();
		ruleRow.addCondition(new ExpressionValueTreeObjectReference(getTreeObject("breakfast")));
		ruleRow.addCondition(new ExpressionChain(new ExpressionOperatorLogic(AvailableOperator.EQUALS),
				new ExpressionValueTreeObjectReference(getAnswer("breakfast", "c"))));
		ruleRow.setAction(new ExpressionChain(new ExpressionValueCustomVariable(getTreeObject("breakfast"),
				questionVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionValueString(QUESTION_NOT_EQUALS_ANSWER)));
		tableRule.getRules().add(ruleRow);
		ruleRow = new TableRuleRow();
		ruleRow.addCondition(new ExpressionValueTreeObjectReference(getTreeObject("breakfast")));
		ruleRow.addCondition(new ExpressionChain(new ExpressionOperatorLogic(AvailableOperator.EQUALS),
				new ExpressionValueTreeObjectReference(getAnswer("breakfast", "d"))));
		ruleRow.setAction(new ExpressionChain(new ExpressionValueCustomVariable(getTreeObject("breakfast"),
				questionVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionValueString(QUESTION_NOT_EQUALS_ANSWER)));
		tableRule.getRules().add(ruleRow);
		ruleRow = new TableRuleRow();
		ruleRow.addCondition(new ExpressionValueTreeObjectReference(getTreeObject("breakfast")));
		ruleRow.addCondition(new ExpressionChain(new ExpressionOperatorLogic(AvailableOperator.EQUALS),
				new ExpressionValueTreeObjectReference(getAnswer("breakfast", "e"))));
		ruleRow.setAction(new ExpressionChain(new ExpressionValueCustomVariable(getTreeObject("breakfast"),
				questionVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionValueString(QUESTION_NOT_EQUALS_ANSWER)));
		tableRule.getRules().add(ruleRow);

		// Add the table rule
		getForm().getTableRules().add(tableRule);
		// Creation of a simple diagram to load the table rule
		getForm().addDiagram(this.createSimpleTableDiagram(tableRule));
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

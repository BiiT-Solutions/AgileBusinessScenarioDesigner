package com.biit.abcd.core.drools.test;

import java.io.IOException;

import org.dom4j.DocumentException;
import org.testng.annotations.Test;

import com.biit.abcd.core.drools.FormToDroolsExporter;
import com.biit.abcd.core.drools.rules.DroolsRulesGenerator;
import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleNotImplementedException;
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
import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidOperatorInExpression;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.biit.abcd.persistence.utils.IdGenerator;
import com.biit.form.exceptions.ChildrenNotFoundException;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.orbeon.exceptions.CategoryNameWithoutTranslation;
import com.biit.orbeon.form.ISubmittedForm;
import com.biit.orbeon.form.exceptions.CategoryDoesNotExistException;
import com.biit.orbeon.form.exceptions.GroupDoesNotExistException;
import com.biit.orbeon.form.exceptions.QuestionDoesNotExistException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

/**
 * Checks the correct creation and execution of table rules <br>
 * Also checks the correct loading in memory of the submitted form from orbeon
 */
public class NewTableRuleTest extends TestFormCreator {

	public NewTableRuleTest() throws FieldTooLongException, NotValidChildException, InvalidAnswerFormatException {
		super();
	}

	private final static String QUESTION_ANSWER_EQUALS = "questionAnswerEquals";

	@Test(groups = { "rules" }, dependsOnMethods = { "translateFormCategories" })
	public void testTableRuleLoadAndExecution() throws ExpressionInvalidException, NotValidChildException,
			NotValidOperatorInExpression, ChildrenNotFoundException, RuleInvalidException, FieldTooLongException,
			IOException, CategoryDoesNotExistException, QuestionDoesNotExistException, RuleNotImplementedException,
			GroupDoesNotExistException, InvalidAnswerFormatException, DocumentException, CategoryNameWithoutTranslation {
		
		// Create the table and form diagram
		createKidsFormSimpleTable();
		// Generate the drools rules.
		FormToDroolsExporter formDrools = new FormToDroolsExporter();
		DroolsRulesGenerator rulesGenerator = formDrools.generateDroolRules(getForm(), null);
		readStaticSubmittedForm();
		translateFormCategories();
		// Test the rules with the submitted form and returns a DroolsForm
		ISubmittedForm droolsForm = formDrools.applyDrools(getSubmittedForm(), rulesGenerator.getRules(), null);
	}

	private void createKidsFormSimpleTable() throws FieldTooLongException, NotValidChildException,
			InvalidAnswerFormatException {

		// Create the tableRule
		// Only with one conditions colum
		TableRule tableRule = new TableRule("TestTable");

			// Question == Answer
		TableRuleRow ruleRow = new TableRuleRow();
		ruleRow.addCondition(new ExpressionValueTreeObjectReference(getTreeObject("breakfast")));
		ruleRow.addCondition(new ExpressionChain(new ExpressionOperatorLogic(AvailableOperator.EQUALS),
				new ExpressionValueTreeObjectReference(breakfastB)));
		ruleRow.setAction(new ExpressionChain(new ExpressionValueCustomVariable(form, formQuestionAnswer),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueString(
						"QuestionEqualsAnswerWorking")));
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

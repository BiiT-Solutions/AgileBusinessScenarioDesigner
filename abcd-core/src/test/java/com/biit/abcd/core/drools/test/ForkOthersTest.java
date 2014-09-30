package com.biit.abcd.core.drools.test;

import java.io.IOException;

import org.dom4j.DocumentException;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.core.drools.facts.inputform.DroolsForm;
import com.biit.abcd.core.drools.facts.inputform.SubmittedForm;
import com.biit.abcd.core.drools.rules.exceptions.ActionNotImplementedException;
import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleNotImplementedException;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.CustomVariableScope;
import com.biit.abcd.persistence.entity.CustomVariableType;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.diagram.DiagramFork;
import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.biit.abcd.persistence.entity.diagram.DiagramObjectType;
import com.biit.abcd.persistence.entity.diagram.DiagramSink;
import com.biit.abcd.persistence.entity.diagram.DiagramSource;
import com.biit.abcd.persistence.entity.diagram.Node;
import com.biit.abcd.persistence.entity.expressions.AvailableFunction;
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.AvailableSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionFunction;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorLogic;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorMath;
import com.biit.abcd.persistence.entity.expressions.ExpressionSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueNumber;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.utils.IdGenerator;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.orbeon.exceptions.CategoryNameWithoutTranslation;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

public class ForkOthersTest extends KidsFormCreator {

	private final static String END2 = "end2";
	private final static String END3 = "end3";

	@Test(groups = { "rules" })
	public void testFork() throws FieldTooLongException, NotValidChildException, InvalidAnswerFormatException,
			ExpressionInvalidException, RuleInvalidException, IOException, RuleNotImplementedException,
			DocumentException, CategoryNameWithoutTranslation, ActionNotImplementedException {

		// Restart the form to avoid test cross references
		initForm();
		// Create the table and form diagram
		createFormSimpleFork();
		// Create the rules and launch the engine
		DroolsForm droolsForm = createAndRunDroolsRules();
		// Check Fork assignation
		Assert.assertEquals(((SubmittedForm) droolsForm.getSubmittedForm()).getVariableValue(END2), 3.78);
	}

	@Test(groups = { "rules" })
	public void testNestedForks() throws FieldTooLongException, NotValidChildException, InvalidAnswerFormatException,
			ExpressionInvalidException, RuleInvalidException, IOException, RuleNotImplementedException,
			DocumentException, CategoryNameWithoutTranslation, ActionNotImplementedException {

		// Restart the form to avoid test cross references
		initForm();
		// Create the table and form diagram
		createFormNestedForks();
		// Create the rules and launch the engine
		DroolsForm droolsForm = createAndRunDroolsRules();
		// Check Fork assignation
		Assert.assertEquals(((SubmittedForm) droolsForm.getSubmittedForm()).getVariableValue(END2), 3.78);
	}

	// @Test(groups = { "rules" })
	// public void testForkWithMultipleConditions() throws
	// FieldTooLongException, NotValidChildException,
	// InvalidAnswerFormatException, ExpressionInvalidException,
	// RuleInvalidException, IOException,
	// RuleNotImplementedException, DocumentException,
	// CategoryNameWithoutTranslation,
	// ActionNotImplementedException {
	//
	// // Restart the form to avoid test cross references
	// initForm();
	// // Create the table and form diagram
	// createFormMultipleConditionsFork();
	// // Create the rules and launch the engine
	// DroolsForm droolsForm = createAndRunDroolsRules();
	// // Check Fork assignation
	// // Assert.assertEquals(((SubmittedForm)
	// // droolsForm.getSubmittedForm()).getVariableValue(END2), 3.78);
	// }

	private void createFormSimpleFork() {
		getForm().addDiagram(createForkWithThreeOutputsDiagram());
	}

	private void createFormNestedForks() {
		getForm().addDiagram(createNestedForksDiagram());
	}

	private void createFormMultipleConditionsFork() {
		getForm().addDiagram(createMultipleConditionsFork());
	}

	private Diagram createForkWithThreeOutputsDiagram() {
		CustomVariable end2CustomVariable = new CustomVariable(getForm(), END2, CustomVariableType.NUMBER,
				CustomVariableScope.FORM);

		Diagram mainDiagram = new Diagram("main");

		DiagramSource diagramStartNode = new DiagramSource();
		diagramStartNode.setJointjsId(IdGenerator.createId());
		diagramStartNode.setType(DiagramObjectType.SOURCE);
		Node nodeSource = new Node(diagramStartNode.getJointjsId());

		DiagramFork diagramSecondFork = new DiagramFork();
		diagramSecondFork.setJointjsId(IdGenerator.createId());
		diagramSecondFork.setType(DiagramObjectType.FORK);
		diagramSecondFork.setReference(new ExpressionValueTreeObjectReference(getTreeObject("vegetables")));
		Node nodeSecondFork = new Node(diagramSecondFork.getJointjsId());

		DiagramSink secondEndNode = new DiagramSink();
		secondEndNode.setJointjsId(IdGenerator.createId());
		secondEndNode.setType(DiagramObjectType.SINK);
		secondEndNode.setFormExpression(new ExpressionChain(END2, new ExpressionValueCustomVariable(getForm(),
				end2CustomVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionValueNumber(7.5)));
		Node secondNodeSink = new Node(secondEndNode.getJointjsId());

		DiagramSink thirdEndNode = new DiagramSink();
		thirdEndNode.setJointjsId(IdGenerator.createId());
		thirdEndNode.setType(DiagramObjectType.SINK);
		thirdEndNode.setFormExpression(new ExpressionChain(END2, new ExpressionValueCustomVariable(getForm(),
				end2CustomVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionValueNumber(3.78)));
		Node thirdNodeSink = new Node(thirdEndNode.getJointjsId());

		DiagramSink fourthEndNode = new DiagramSink();
		fourthEndNode.setJointjsId(IdGenerator.createId());
		fourthEndNode.setType(DiagramObjectType.SINK);
		fourthEndNode.setFormExpression(new ExpressionChain(END2, new ExpressionValueCustomVariable(getForm(),
				end2CustomVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionValueNumber(4.75)));
		Node fourthNodeSink = new Node(fourthEndNode.getJointjsId());

		DiagramLink startSecondFork = new DiagramLink(nodeSource, nodeSecondFork);
		startSecondFork.setJointjsId(IdGenerator.createId());
		startSecondFork.setType(DiagramObjectType.LINK);

		DiagramLink secondForkSecondEnd = new DiagramLink(nodeSecondFork, secondNodeSink);
		secondForkSecondEnd.setJointjsId(IdGenerator.createId());
		secondForkSecondEnd.setType(DiagramObjectType.LINK);
		secondForkSecondEnd.setExpressionChain(new ExpressionChain(new ExpressionValueTreeObjectReference(
				getTreeObject("vegetables")), new ExpressionValueTreeObjectReference(getAnswer("vegetables", "b"))));

		DiagramLink secondForkThirdEnd = new DiagramLink(nodeSecondFork, thirdNodeSink);
		secondForkThirdEnd.setJointjsId(IdGenerator.createId());
		secondForkThirdEnd.setType(DiagramObjectType.LINK);
		secondForkThirdEnd.setExpressionChain(new ExpressionChain(new ExpressionValueTreeObjectReference(
				getTreeObject("vegetables"))));

		DiagramLink secondForkFourthEnd = new DiagramLink(nodeSecondFork, fourthNodeSink);
		secondForkFourthEnd.setJointjsId(IdGenerator.createId());
		secondForkFourthEnd.setType(DiagramObjectType.LINK);
		secondForkFourthEnd.setExpressionChain(new ExpressionChain(new ExpressionValueTreeObjectReference(
				getTreeObject("vegetables")), new ExpressionValueTreeObjectReference(getAnswer("vegetables", "a"))));

		mainDiagram.addDiagramObject(diagramStartNode);
		mainDiagram.addDiagramObject(diagramSecondFork);
		mainDiagram.addDiagramObject(secondEndNode);
		mainDiagram.addDiagramObject(thirdEndNode);
		mainDiagram.addDiagramObject(fourthEndNode);
		mainDiagram.addDiagramObject(startSecondFork);
		mainDiagram.addDiagramObject(secondForkSecondEnd);
		mainDiagram.addDiagramObject(secondForkThirdEnd);
		mainDiagram.addDiagramObject(secondForkFourthEnd);

		return mainDiagram;
	}

	private Diagram createMultipleConditionsFork() {
		CustomVariable end2CustomVariable = new CustomVariable(getForm(), END2, CustomVariableType.NUMBER,
				CustomVariableScope.FORM);

		Diagram mainDiagram = new Diagram("main");

		DiagramSource diagramStartNode = new DiagramSource();
		diagramStartNode.setJointjsId(IdGenerator.createId());
		diagramStartNode.setType(DiagramObjectType.SOURCE);
		Node nodeSource = new Node(diagramStartNode.getJointjsId());

		DiagramFork diagramFork = new DiagramFork();
		diagramFork.setJointjsId(IdGenerator.createId());
		diagramFork.setType(DiagramObjectType.FORK);
		diagramFork.setReference(new ExpressionValueTreeObjectReference(getTreeObject("vegetablesAmount")));
		Node nodeFork = new Node(diagramFork.getJointjsId());

		DiagramSink firstEndNode = new DiagramSink();
		firstEndNode.setJointjsId(IdGenerator.createId());
		firstEndNode.setType(DiagramObjectType.SINK);
		firstEndNode.setFormExpression(new ExpressionChain(END2, new ExpressionValueCustomVariable(getForm(),
				end2CustomVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionValueNumber(7.5)));
		Node secondNodeSink = new Node(firstEndNode.getJointjsId());

		DiagramSink secondEndNode = new DiagramSink();
		secondEndNode.setJointjsId(IdGenerator.createId());
		secondEndNode.setType(DiagramObjectType.SINK);
		secondEndNode.setFormExpression(new ExpressionChain(END2, new ExpressionValueCustomVariable(getForm(),
				end2CustomVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionValueNumber(3.78)));
		Node thirdNodeSink = new Node(secondEndNode.getJointjsId());

		DiagramSink thirdEndNode = new DiagramSink();
		thirdEndNode.setJointjsId(IdGenerator.createId());
		thirdEndNode.setType(DiagramObjectType.SINK);
		thirdEndNode.setFormExpression(new ExpressionChain(END2, new ExpressionValueCustomVariable(getForm(),
				end2CustomVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionValueNumber(4.75)));
		Node fourthNodeSink = new Node(thirdEndNode.getJointjsId());

		DiagramLink startFork = new DiagramLink(nodeSource, nodeFork);
		startFork.setJointjsId(IdGenerator.createId());
		startFork.setType(DiagramObjectType.LINK);

		DiagramLink forkFirstEnd = new DiagramLink(nodeFork, secondNodeSink);
		forkFirstEnd.setJointjsId(IdGenerator.createId());
		forkFirstEnd.setType(DiagramObjectType.LINK);
		forkFirstEnd.setExpressionChain(new ExpressionChain(new ExpressionValueTreeObjectReference(
				getTreeObject("vegetablesAmount")), new ExpressionFunction(AvailableFunction.BETWEEN),
				new ExpressionValueNumber(1.), new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(
						2.5), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET), new ExpressionOperatorLogic(
						AvailableOperator.OR),
				new ExpressionValueTreeObjectReference(getTreeObject("vegetablesAmount")), new ExpressionFunction(
						AvailableFunction.BETWEEN), new ExpressionValueNumber(3.), new ExpressionSymbol(
						AvailableSymbol.COMMA), new ExpressionValueNumber(3.5), new ExpressionSymbol(
						AvailableSymbol.RIGHT_BRACKET)));

		DiagramLink forkSecondEnd = new DiagramLink(nodeFork, thirdNodeSink);
		forkSecondEnd.setJointjsId(IdGenerator.createId());
		forkSecondEnd.setType(DiagramObjectType.LINK);
		forkSecondEnd.setExpressionChain(new ExpressionChain(new ExpressionValueTreeObjectReference(
				getTreeObject("vegetablesAmount"))));

		DiagramLink forkThirdEnd = new DiagramLink(nodeFork, fourthNodeSink);
		forkThirdEnd.setJointjsId(IdGenerator.createId());
		forkThirdEnd.setType(DiagramObjectType.LINK);
		forkThirdEnd.setExpressionChain(new ExpressionChain(new ExpressionValueTreeObjectReference(
				getTreeObject("vegetablesAmount")), new ExpressionOperatorLogic(AvailableOperator.LESS_THAN),
				new ExpressionValueNumber(3.5)));

		mainDiagram.addDiagramObject(diagramStartNode);
		mainDiagram.addDiagramObject(diagramFork);
		mainDiagram.addDiagramObject(firstEndNode);
		mainDiagram.addDiagramObject(secondEndNode);
		mainDiagram.addDiagramObject(thirdEndNode);
		mainDiagram.addDiagramObject(startFork);
		mainDiagram.addDiagramObject(forkFirstEnd);
		mainDiagram.addDiagramObject(forkSecondEnd);
		mainDiagram.addDiagramObject(forkThirdEnd);

		return mainDiagram;
	}

	private Diagram createNestedForksDiagram() {
		CustomVariable end2CustomVariable = new CustomVariable(getForm(), END2, CustomVariableType.NUMBER,
				CustomVariableScope.FORM);
		CustomVariable end3CustomVariable = new CustomVariable(getForm(), END3, CustomVariableType.NUMBER,
				CustomVariableScope.FORM);

		Diagram mainDiagram = new Diagram("main");

		DiagramSource diagramStartNode = new DiagramSource();
		diagramStartNode.setJointjsId(IdGenerator.createId());
		diagramStartNode.setType(DiagramObjectType.SOURCE);
		Node nodeSource = new Node(diagramStartNode.getJointjsId());

		DiagramFork diagramFirstFork = new DiagramFork();
		diagramFirstFork.setJointjsId(IdGenerator.createId());
		diagramFirstFork.setType(DiagramObjectType.FORK);
		diagramFirstFork.setReference(new ExpressionValueTreeObjectReference(getTreeObject("vegetablesAmount")));
		Node nodeFirstFork = new Node(diagramFirstFork.getJointjsId());

		DiagramSink firstEndNode = new DiagramSink();
		firstEndNode.setJointjsId(IdGenerator.createId());
		firstEndNode.setType(DiagramObjectType.SINK);
		firstEndNode.setFormExpression(new ExpressionChain(END3, new ExpressionValueCustomVariable(getForm(),
				end3CustomVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionValueNumber(2.1)));
		Node firstNodeSink = new Node(firstEndNode.getJointjsId());

		DiagramFork diagramSecondFork = new DiagramFork();
		diagramSecondFork.setJointjsId(IdGenerator.createId());
		diagramSecondFork.setType(DiagramObjectType.FORK);
		diagramSecondFork.setReference(new ExpressionValueTreeObjectReference(getTreeObject("vegetables")));
		Node nodeSecondFork = new Node(diagramSecondFork.getJointjsId());

		DiagramSink secondEndNode = new DiagramSink();
		secondEndNode.setJointjsId(IdGenerator.createId());
		secondEndNode.setType(DiagramObjectType.SINK);
		secondEndNode.setFormExpression(new ExpressionChain(END2, new ExpressionValueCustomVariable(getForm(),
				end2CustomVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionValueNumber(7.5)));
		Node secondNodeSink = new Node(secondEndNode.getJointjsId());

		DiagramSink thirdEndNode = new DiagramSink();
		thirdEndNode.setJointjsId(IdGenerator.createId());
		thirdEndNode.setType(DiagramObjectType.SINK);
		thirdEndNode.setFormExpression(new ExpressionChain(END2, new ExpressionValueCustomVariable(getForm(),
				end2CustomVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionValueNumber(3.78)));
		Node thirdNodeSink = new Node(thirdEndNode.getJointjsId());

		DiagramSink fourthEndNode = new DiagramSink();
		fourthEndNode.setJointjsId(IdGenerator.createId());
		fourthEndNode.setType(DiagramObjectType.SINK);
		fourthEndNode.setFormExpression(new ExpressionChain(END2, new ExpressionValueCustomVariable(getForm(),
				end2CustomVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionValueNumber(4.75)));
		Node fourthNodeSink = new Node(fourthEndNode.getJointjsId());

		DiagramLink startFirstFork = new DiagramLink(nodeSource, nodeFirstFork);
		startFirstFork.setJointjsId(IdGenerator.createId());
		startFirstFork.setType(DiagramObjectType.LINK);

		// Others for First fork
		DiagramLink firstForkFirstEnd = new DiagramLink(nodeFirstFork, firstNodeSink);
		firstForkFirstEnd.setJointjsId(IdGenerator.createId());
		firstForkFirstEnd.setType(DiagramObjectType.LINK);
		firstForkFirstEnd.setExpressionChain(new ExpressionChain(new ExpressionValueTreeObjectReference(
				getTreeObject("vegetablesAmount")), new ExpressionOperatorLogic(AvailableOperator.LESS_THAN),
				new ExpressionValueNumber(4.0)));

		DiagramLink firstForkSecondFork = new DiagramLink(nodeFirstFork, nodeSecondFork);
		firstForkSecondFork.setJointjsId(IdGenerator.createId());
		firstForkSecondFork.setType(DiagramObjectType.LINK);
		firstForkSecondFork.setExpressionChain(new ExpressionChain(new ExpressionValueTreeObjectReference(
				getTreeObject("vegetablesAmount"))));

		DiagramLink secondForkSecondEnd = new DiagramLink(nodeSecondFork, secondNodeSink);
		secondForkSecondEnd.setJointjsId(IdGenerator.createId());
		secondForkSecondEnd.setType(DiagramObjectType.LINK);
		secondForkSecondEnd.setExpressionChain(new ExpressionChain(new ExpressionValueTreeObjectReference(
				getTreeObject("vegetables")), new ExpressionValueTreeObjectReference(getAnswer("vegetables", "b"))));

		// Others for Second fork
		DiagramLink secondForkThirdEnd = new DiagramLink(nodeSecondFork, thirdNodeSink);
		secondForkThirdEnd.setJointjsId(IdGenerator.createId());
		secondForkThirdEnd.setType(DiagramObjectType.LINK);
		secondForkThirdEnd.setExpressionChain(new ExpressionChain(new ExpressionValueTreeObjectReference(
				getTreeObject("vegetables"))));

		DiagramLink secondForkFourthEnd = new DiagramLink(nodeSecondFork, fourthNodeSink);
		secondForkFourthEnd.setJointjsId(IdGenerator.createId());
		secondForkFourthEnd.setType(DiagramObjectType.LINK);
		secondForkFourthEnd.setExpressionChain(new ExpressionChain(new ExpressionValueTreeObjectReference(
				getTreeObject("vegetables")), new ExpressionValueTreeObjectReference(getAnswer("vegetables", "a"))));

		mainDiagram.addDiagramObject(diagramStartNode);
		mainDiagram.addDiagramObject(diagramFirstFork);
		mainDiagram.addDiagramObject(firstEndNode);
		mainDiagram.addDiagramObject(diagramSecondFork);
		mainDiagram.addDiagramObject(secondEndNode);
		mainDiagram.addDiagramObject(thirdEndNode);
		mainDiagram.addDiagramObject(fourthEndNode);
		mainDiagram.addDiagramObject(startFirstFork);
		mainDiagram.addDiagramObject(firstForkFirstEnd);
		mainDiagram.addDiagramObject(firstForkSecondFork);
		mainDiagram.addDiagramObject(secondForkSecondEnd);
		mainDiagram.addDiagramObject(secondForkThirdEnd);
		mainDiagram.addDiagramObject(secondForkFourthEnd);

		return mainDiagram;
	}
}

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

import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.CustomVariableScope;
import com.biit.abcd.persistence.entity.CustomVariableType;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.diagram.*;
import com.biit.abcd.persistence.entity.expressions.*;
import com.biit.abcd.persistence.utils.IdGenerator;
import com.biit.drools.form.DroolsForm;
import com.biit.drools.form.DroolsSubmittedForm;
import org.springframework.test.context.ContextConfiguration;
import org.testng.Assert;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
public class ForksTest extends KidsFormCreator {

	private final static String END2 = "end2";
	private final static String END3 = "end3";
	private final static String FORM_CUSTOM_VAR = "formCustomVar";
	private final static String CATEGORY_CUSTOM_VAR = "categoryCustomVar";

	@Test(groups = { "droolsFork" })
	public void simpleForkTest() {
		try {
			// Restart the form to avoid test cross references
			Form form = createForm();
			// Create the table and form diagram
			form.addDiagram(createForkWithThreeOutputsDiagram(form));
			// Create the rules and launch the engine
			DroolsForm droolsForm = createAndRunDroolsRules(form);
			// Check Fork assignation
			Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue(END2),
					3.78);
		} catch (Exception e) {
			Assert.fail("Exception in test");
		}
	}

	@Test(groups = { "droolsFork" })
	public void nestedForksTest() {
		try {
			// Restart the form to avoid test cross references
			Form form = createForm();
			// Create the table and form diagram
			form.addDiagram(createNestedForksDiagram(form));
			// Create the rules and launch the engine
			DroolsForm droolsForm = createAndRunDroolsRules(form);
			// Check Fork assignation
			Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue(END2),
					3.78);
		} catch (Exception e) {
			Assert.fail("Exception in test");
		}
	}

	// Test created to check if the or/and combination is generated correctly
	@Test(groups = { "droolsFork" })
	public void nestedForksWithOrConditionsTest() {
		try {
			// Restart the form to avoid test cross references
			Form form = createForm();
			// Create the table and form diagram
			form.addDiagram(createNestedForksWithOrConditionsDiagram(form));
			// Create the rules and launch the engine
			DroolsForm droolsForm = createAndRunDroolsRules(form);
			// Check Fork assignation
			Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue(END2),
					3.78);
		} catch (Exception e) {
			Assert.fail("Exception in test");
		}
	}

	@Test(groups = { "droolsFork" })
	public void forkWithMultipleConditionsTest() {
		try {
			// Restart the form to avoid test cross references
			Form form = createForm();
			// Create the table and form diagram
			form.addDiagram(createMultipleConditionsFork(form));
			// Create the rules and launch the engine
			DroolsForm droolsForm = createAndRunDroolsRules(form);
			// Check Fork assignation
			Assert.assertEquals(((DroolsSubmittedForm) droolsForm.getDroolsSubmittedForm()).getVariableValue(END2),
					4.75);
		} catch (Exception e) {
			Assert.fail("Exception in test");
		}
	}

	private Diagram createForkWithThreeOutputsDiagram(Form form) {
		CustomVariable end2CustomVariable = new CustomVariable(form, END2, CustomVariableType.NUMBER,
				CustomVariableScope.FORM);

		Diagram mainDiagram = new Diagram("main");

		DiagramSource diagramStartNode = new DiagramSource();
		diagramStartNode.setJointjsId(IdGenerator.createId());
		diagramStartNode.setType(DiagramObjectType.SOURCE);
		Node nodeSource = new Node(diagramStartNode.getJointjsId());

		DiagramFork diagramForkNode = new DiagramFork();
		diagramForkNode.setJointjsId(IdGenerator.createId());
		diagramForkNode.setType(DiagramObjectType.FORK);
		diagramForkNode.setReference(new ExpressionValueTreeObjectReference(getTreeObject(form, "vegetables")));
		Node nodeSecondFork = new Node(diagramForkNode.getJointjsId());

		DiagramSink firstEndNode = new DiagramSink();
		firstEndNode.setJointjsId(IdGenerator.createId());
		firstEndNode.setType(DiagramObjectType.SINK);
		firstEndNode
				.setExpression(new ExpressionChain(END2, new ExpressionValueCustomVariable(form, end2CustomVariable),
						new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueNumber(7.5)));
		Node secondNodeSink = new Node(firstEndNode.getJointjsId());

		DiagramSink secondEndNode = new DiagramSink();
		secondEndNode.setJointjsId(IdGenerator.createId());
		secondEndNode.setType(DiagramObjectType.SINK);
		secondEndNode
				.setExpression(new ExpressionChain(END2, new ExpressionValueCustomVariable(form, end2CustomVariable),
						new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueNumber(3.78)));
		Node thirdNodeSink = new Node(secondEndNode.getJointjsId());

		DiagramSink thirdEndNode = new DiagramSink();
		thirdEndNode.setJointjsId(IdGenerator.createId());
		thirdEndNode.setType(DiagramObjectType.SINK);
		thirdEndNode
				.setExpression(new ExpressionChain(END2, new ExpressionValueCustomVariable(form, end2CustomVariable),
						new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueNumber(4.75)));
		Node fourthNodeSink = new Node(thirdEndNode.getJointjsId());

		DiagramLink startFork = new DiagramLink(nodeSource, nodeSecondFork);
		startFork.setJointjsId(IdGenerator.createId());
		startFork.setType(DiagramObjectType.LINK);

		DiagramLink forkFirstEnd = new DiagramLink(nodeSecondFork, secondNodeSink);
		forkFirstEnd.setJointjsId(IdGenerator.createId());
		forkFirstEnd.setType(DiagramObjectType.LINK);
		forkFirstEnd.setExpressionChain(
				new ExpressionChain(new ExpressionValueTreeObjectReference(getTreeObject(form, "vegetables")),
						new ExpressionValueTreeObjectReference(getAnswer(form, "vegetables", "b"))));

		DiagramLink forkSecondEnd = new DiagramLink(nodeSecondFork, thirdNodeSink);
		forkSecondEnd.setJointjsId(IdGenerator.createId());
		forkSecondEnd.setType(DiagramObjectType.LINK);
		forkSecondEnd.setExpressionChain(
				new ExpressionChain(new ExpressionValueTreeObjectReference(getTreeObject(form, "vegetables"))));

		DiagramLink forkThirdEnd = new DiagramLink(nodeSecondFork, fourthNodeSink);
		forkThirdEnd.setJointjsId(IdGenerator.createId());
		forkThirdEnd.setType(DiagramObjectType.LINK);
		forkThirdEnd.setExpressionChain(
				new ExpressionChain(new ExpressionValueTreeObjectReference(getTreeObject(form, "vegetables")),
						new ExpressionValueTreeObjectReference(getAnswer(form, "vegetables", "a"))));

		mainDiagram.addDiagramObject(diagramStartNode);
		mainDiagram.addDiagramObject(diagramForkNode);
		mainDiagram.addDiagramObject(firstEndNode);
		mainDiagram.addDiagramObject(secondEndNode);
		mainDiagram.addDiagramObject(thirdEndNode);
		mainDiagram.addDiagramObject(startFork);
		mainDiagram.addDiagramObject(forkFirstEnd);
		mainDiagram.addDiagramObject(forkSecondEnd);
		mainDiagram.addDiagramObject(forkThirdEnd);

		return mainDiagram;
	}

	private Diagram createMultipleConditionsFork(Form form) {
		CustomVariable end2CustomVariable = new CustomVariable(form, END2, CustomVariableType.NUMBER,
				CustomVariableScope.FORM);
		CustomVariable formCustomVariableInCondition = new CustomVariable(form, FORM_CUSTOM_VAR,
				CustomVariableType.NUMBER, CustomVariableScope.FORM);
		CustomVariable categoryCustomVariableInCondition = new CustomVariable(form, CATEGORY_CUSTOM_VAR,
				CustomVariableType.NUMBER, CustomVariableScope.CATEGORY);

		Diagram mainDiagram = new Diagram("main");

		DiagramSource diagramStartNode = new DiagramSource();
		diagramStartNode.setJointjsId(IdGenerator.createId());
		diagramStartNode.setType(DiagramObjectType.SOURCE);
		Node nodeSource = new Node(diagramStartNode.getJointjsId());

		DiagramExpression diagramExpression = new DiagramExpression();
		diagramExpression.setExpression(new ExpressionChain("setCustomValue",
				new ExpressionValueCustomVariable(form, formCustomVariableInCondition),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueNumber(6.5)));
		diagramExpression.setJointjsId(IdGenerator.createId());
		diagramExpression.setType(DiagramObjectType.CALCULATION);
		Node nodeExpression = new Node(diagramExpression.getJointjsId());

		DiagramExpression diagramExpression2 = new DiagramExpression();
		diagramExpression2.setExpression(new ExpressionChain("setCustomValue",
				new ExpressionValueCustomVariable(getTreeObject(form, "Lifestyle"), categoryCustomVariableInCondition),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueNumber(2.5)));
		diagramExpression2.setJointjsId(IdGenerator.createId());
		diagramExpression2.setType(DiagramObjectType.CALCULATION);
		Node nodeExpression2 = new Node(diagramExpression2.getJointjsId());

		DiagramFork diagramFork = new DiagramFork();
		diagramFork.setJointjsId(IdGenerator.createId());
		diagramFork.setType(DiagramObjectType.FORK);
		diagramFork.setReference(new ExpressionValueTreeObjectReference(getTreeObject(form, "vegetablesAmount")));
		Node nodeFork = new Node(diagramFork.getJointjsId());

		DiagramSink firstEndNode = new DiagramSink();
		firstEndNode.setJointjsId(IdGenerator.createId());
		firstEndNode.setType(DiagramObjectType.SINK);
		firstEndNode.setExpression(new ExpressionChain(END2,
				new ExpressionValueCustomVariable(getTreeObject(form, "Lifestyle"), categoryCustomVariableInCondition),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueNumber(7.5)));
		Node secondNodeSink = new Node(firstEndNode.getJointjsId());

		DiagramSink secondEndNode = new DiagramSink();
		secondEndNode.setJointjsId(IdGenerator.createId());
		secondEndNode.setType(DiagramObjectType.SINK);
		secondEndNode
				.setExpression(new ExpressionChain(END2, new ExpressionValueCustomVariable(form, end2CustomVariable),
						new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueNumber(3.78)));
		Node thirdNodeSink = new Node(secondEndNode.getJointjsId());

		DiagramSink thirdEndNode = new DiagramSink();
		thirdEndNode.setJointjsId(IdGenerator.createId());
		thirdEndNode.setType(DiagramObjectType.SINK);
		thirdEndNode
				.setExpression(new ExpressionChain(END2, new ExpressionValueCustomVariable(form, end2CustomVariable),
						new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueNumber(4.75)));
		Node fourthNodeSink = new Node(thirdEndNode.getJointjsId());

		DiagramLink startExpression = new DiagramLink(nodeSource, nodeExpression);
		startExpression.setJointjsId(IdGenerator.createId());
		startExpression.setType(DiagramObjectType.LINK);

		DiagramLink expressionExpression2 = new DiagramLink(nodeExpression, nodeExpression2);
		expressionExpression2.setJointjsId(IdGenerator.createId());
		expressionExpression2.setType(DiagramObjectType.LINK);

		DiagramLink expression2Fork = new DiagramLink(nodeExpression2, nodeFork);
		expression2Fork.setJointjsId(IdGenerator.createId());
		expression2Fork.setType(DiagramObjectType.LINK);

		DiagramLink forkFirstEnd = new DiagramLink(nodeFork, secondNodeSink);
		forkFirstEnd.setJointjsId(IdGenerator.createId());
		forkFirstEnd.setType(DiagramObjectType.LINK);
		forkFirstEnd.setExpressionChain(new ExpressionChain(
				new ExpressionValueTreeObjectReference(getTreeObject(form, "vegetablesAmount")),
				new ExpressionFunction(AvailableFunction.BETWEEN), new ExpressionValueNumber(1.),
				new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(2.5),
				new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET), new ExpressionOperatorLogic(AvailableOperator.OR),
				new ExpressionValueTreeObjectReference(getTreeObject(form, "vegetablesAmount")),
				new ExpressionFunction(AvailableFunction.BETWEEN), new ExpressionValueNumber(3.),
				new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(3.5),
				new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET), new ExpressionOperatorLogic(AvailableOperator.OR),
				new ExpressionValueTreeObjectReference(getTreeObject(form, "vegetablesAmount")),
				new ExpressionFunction(AvailableFunction.BETWEEN), new ExpressionValueNumber(3.6),
				new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(3.8),
				new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET)));

		DiagramLink forkSecondEnd = new DiagramLink(nodeFork, thirdNodeSink);
		forkSecondEnd.setJointjsId(IdGenerator.createId());
		forkSecondEnd.setType(DiagramObjectType.LINK);
		forkSecondEnd.setExpressionChain(
				new ExpressionChain(new ExpressionValueTreeObjectReference(getTreeObject(form, "vegetablesAmount"))));

		DiagramLink forkThirdEnd = new DiagramLink(nodeFork, fourthNodeSink);
		forkThirdEnd.setJointjsId(IdGenerator.createId());
		forkThirdEnd.setType(DiagramObjectType.LINK);
		forkThirdEnd.setExpressionChain(new ExpressionChain(
				new ExpressionValueCustomVariable(getTreeObject(form, "Lifestyle"), categoryCustomVariableInCondition),
				new ExpressionOperatorLogic(AvailableOperator.LESS_THAN),
				new ExpressionValueCustomVariable(form, formCustomVariableInCondition)));

		mainDiagram.addDiagramObject(diagramStartNode);
		mainDiagram.addDiagramObject(diagramExpression);
		mainDiagram.addDiagramObject(diagramExpression2);
		mainDiagram.addDiagramObject(diagramFork);
		mainDiagram.addDiagramObject(firstEndNode);
		mainDiagram.addDiagramObject(secondEndNode);
		mainDiagram.addDiagramObject(thirdEndNode);
		mainDiagram.addDiagramObject(startExpression);
		mainDiagram.addDiagramObject(expressionExpression2);
		mainDiagram.addDiagramObject(expression2Fork);
		mainDiagram.addDiagramObject(forkFirstEnd);
		mainDiagram.addDiagramObject(forkSecondEnd);
		mainDiagram.addDiagramObject(forkThirdEnd);

		return mainDiagram;
	}

	private Diagram createNestedForksDiagram(Form form) {
		CustomVariable end2CustomVariable = new CustomVariable(form, END2, CustomVariableType.NUMBER,
				CustomVariableScope.FORM);
		CustomVariable end3CustomVariable = new CustomVariable(form, END3, CustomVariableType.NUMBER,
				CustomVariableScope.FORM);

		Diagram mainDiagram = new Diagram("main");

		DiagramSource diagramStartNode = new DiagramSource();
		diagramStartNode.setJointjsId(IdGenerator.createId());
		diagramStartNode.setType(DiagramObjectType.SOURCE);
		Node nodeSource = new Node(diagramStartNode.getJointjsId());

		DiagramFork diagramFirstFork = new DiagramFork();
		diagramFirstFork.setJointjsId(IdGenerator.createId());
		diagramFirstFork.setType(DiagramObjectType.FORK);
		diagramFirstFork.setReference(new ExpressionValueTreeObjectReference(getTreeObject(form, "vegetablesAmount")));
		Node nodeFirstFork = new Node(diagramFirstFork.getJointjsId());

		DiagramSink firstEndNode = new DiagramSink();
		firstEndNode.setJointjsId(IdGenerator.createId());
		firstEndNode.setType(DiagramObjectType.SINK);
		firstEndNode
				.setExpression(new ExpressionChain(END3, new ExpressionValueCustomVariable(form, end3CustomVariable),
						new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueNumber(2.1)));
		Node firstNodeSink = new Node(firstEndNode.getJointjsId());

		DiagramFork diagramSecondFork = new DiagramFork();
		diagramSecondFork.setJointjsId(IdGenerator.createId());
		diagramSecondFork.setType(DiagramObjectType.FORK);
		diagramSecondFork.setReference(new ExpressionValueTreeObjectReference(getTreeObject(form, "vegetables")));
		Node nodeSecondFork = new Node(diagramSecondFork.getJointjsId());

		DiagramSink secondEndNode = new DiagramSink();
		secondEndNode.setJointjsId(IdGenerator.createId());
		secondEndNode.setType(DiagramObjectType.SINK);
		secondEndNode
				.setExpression(new ExpressionChain(END2, new ExpressionValueCustomVariable(form, end2CustomVariable),
						new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueNumber(7.5)));
		Node secondNodeSink = new Node(secondEndNode.getJointjsId());

		DiagramSink thirdEndNode = new DiagramSink();
		thirdEndNode.setJointjsId(IdGenerator.createId());
		thirdEndNode.setType(DiagramObjectType.SINK);
		thirdEndNode
				.setExpression(new ExpressionChain(END2, new ExpressionValueCustomVariable(form, end2CustomVariable),
						new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueNumber(3.78)));
		Node thirdNodeSink = new Node(thirdEndNode.getJointjsId());

		DiagramSink fourthEndNode = new DiagramSink();
		fourthEndNode.setJointjsId(IdGenerator.createId());
		fourthEndNode.setType(DiagramObjectType.SINK);
		fourthEndNode
				.setExpression(new ExpressionChain(END2, new ExpressionValueCustomVariable(form, end2CustomVariable),
						new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueNumber(4.75)));
		Node fourthNodeSink = new Node(fourthEndNode.getJointjsId());

		DiagramLink startFirstFork = new DiagramLink(nodeSource, nodeFirstFork);
		startFirstFork.setJointjsId(IdGenerator.createId());
		startFirstFork.setType(DiagramObjectType.LINK);

		// Others for First fork
		DiagramLink firstForkFirstEnd = new DiagramLink(nodeFirstFork, firstNodeSink);
		firstForkFirstEnd.setJointjsId(IdGenerator.createId());
		firstForkFirstEnd.setType(DiagramObjectType.LINK);
		firstForkFirstEnd.setExpressionChain(
				new ExpressionChain(new ExpressionValueTreeObjectReference(getTreeObject(form, "vegetablesAmount")),
						new ExpressionOperatorLogic(AvailableOperator.LESS_THAN), new ExpressionValueNumber(4.0)));

		DiagramLink firstForkSecondFork = new DiagramLink(nodeFirstFork, nodeSecondFork);
		firstForkSecondFork.setJointjsId(IdGenerator.createId());
		firstForkSecondFork.setType(DiagramObjectType.LINK);
		firstForkSecondFork.setExpressionChain(
				new ExpressionChain(new ExpressionValueTreeObjectReference(getTreeObject(form, "vegetablesAmount"))));

		DiagramLink secondForkSecondEnd = new DiagramLink(nodeSecondFork, secondNodeSink);
		secondForkSecondEnd.setJointjsId(IdGenerator.createId());
		secondForkSecondEnd.setType(DiagramObjectType.LINK);
		secondForkSecondEnd.setExpressionChain(
				new ExpressionChain(new ExpressionValueTreeObjectReference(getTreeObject(form, "vegetables")),
						new ExpressionValueTreeObjectReference(getAnswer(form, "vegetables", "b"))));

		// Others for Second fork
		DiagramLink secondForkThirdEnd = new DiagramLink(nodeSecondFork, thirdNodeSink);
		secondForkThirdEnd.setJointjsId(IdGenerator.createId());
		secondForkThirdEnd.setType(DiagramObjectType.LINK);
		secondForkThirdEnd.setExpressionChain(
				new ExpressionChain(new ExpressionValueTreeObjectReference(getTreeObject(form, "vegetables"))));

		DiagramLink secondForkFourthEnd = new DiagramLink(nodeSecondFork, fourthNodeSink);
		secondForkFourthEnd.setJointjsId(IdGenerator.createId());
		secondForkFourthEnd.setType(DiagramObjectType.LINK);
		secondForkFourthEnd.setExpressionChain(
				new ExpressionChain(new ExpressionValueTreeObjectReference(getTreeObject(form, "vegetables")),
						new ExpressionValueTreeObjectReference(getAnswer(form, "vegetables", "a"))));

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

	private Diagram createNestedForksWithOrConditionsDiagram(Form form) {
		CustomVariable end2CustomVariable = new CustomVariable(form, END2, CustomVariableType.NUMBER,
				CustomVariableScope.FORM);
		CustomVariable end3CustomVariable = new CustomVariable(form, END3, CustomVariableType.NUMBER,
				CustomVariableScope.FORM);

		Diagram mainDiagram = new Diagram("main");

		DiagramSource diagramStartNode = new DiagramSource();
		diagramStartNode.setJointjsId(IdGenerator.createId());
		diagramStartNode.setType(DiagramObjectType.SOURCE);
		Node nodeSource = new Node(diagramStartNode.getJointjsId());

		DiagramFork diagramFirstFork = new DiagramFork();
		diagramFirstFork.setJointjsId(IdGenerator.createId());
		diagramFirstFork.setType(DiagramObjectType.FORK);
		diagramFirstFork.setReference(new ExpressionValueTreeObjectReference(getTreeObject(form, "vegetablesAmount")));
		Node nodeFirstFork = new Node(diagramFirstFork.getJointjsId());

		DiagramSink firstEndNode = new DiagramSink();
		firstEndNode.setJointjsId(IdGenerator.createId());
		firstEndNode.setType(DiagramObjectType.SINK);
		firstEndNode
				.setExpression(new ExpressionChain(END2, new ExpressionValueCustomVariable(form, end3CustomVariable),
						new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueNumber(2.1)));
		Node firstNodeSink = new Node(firstEndNode.getJointjsId());

		DiagramFork diagramSecondFork = new DiagramFork();
		diagramSecondFork.setJointjsId(IdGenerator.createId());
		diagramSecondFork.setType(DiagramObjectType.FORK);
		diagramSecondFork.setReference(new ExpressionValueTreeObjectReference(getTreeObject(form, "vegetables")));
		Node nodeSecondFork = new Node(diagramSecondFork.getJointjsId());

		DiagramSink secondEndNode = new DiagramSink();
		secondEndNode.setJointjsId(IdGenerator.createId());
		secondEndNode.setType(DiagramObjectType.SINK);
		secondEndNode
				.setExpression(new ExpressionChain(END2, new ExpressionValueCustomVariable(form, end2CustomVariable),
						new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueNumber(7.5)));
		Node secondNodeSink = new Node(secondEndNode.getJointjsId());

		DiagramSink thirdEndNode = new DiagramSink();
		thirdEndNode.setJointjsId(IdGenerator.createId());
		thirdEndNode.setType(DiagramObjectType.SINK);
		thirdEndNode
				.setExpression(new ExpressionChain(END2, new ExpressionValueCustomVariable(form, end2CustomVariable),
						new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionValueNumber(3.78)));
		Node thirdNodeSink = new Node(thirdEndNode.getJointjsId());

		DiagramLink startFirstFork = new DiagramLink(nodeSource, nodeFirstFork);
		startFirstFork.setJointjsId(IdGenerator.createId());
		startFirstFork.setType(DiagramObjectType.LINK);

		// Others for First fork
		DiagramLink firstForkFirstEnd = new DiagramLink(nodeFirstFork, firstNodeSink);
		firstForkFirstEnd.setJointjsId(IdGenerator.createId());
		firstForkFirstEnd.setType(DiagramObjectType.LINK);
		// Others condition
		firstForkFirstEnd.setExpressionChain(
				new ExpressionChain(new ExpressionValueTreeObjectReference(getTreeObject(form, "vegetablesAmount"))));

		DiagramLink firstForkSecondFork = new DiagramLink(nodeFirstFork, nodeSecondFork);
		firstForkSecondFork.setJointjsId(IdGenerator.createId());
		firstForkSecondFork.setType(DiagramObjectType.LINK);
		// Fork conditions: vegetablesAmount == 1 OR vegetablesAmount == 2 OR
		// vegetablesAmount BETWEEN(4, 6)
		firstForkSecondFork.setExpressionChain(
				new ExpressionChain(new ExpressionValueTreeObjectReference(getTreeObject(form, "vegetablesAmount")),
						new ExpressionOperatorLogic(AvailableOperator.EQUALS), new ExpressionValueNumber(1.0),
						new ExpressionOperatorLogic(AvailableOperator.OR),
						new ExpressionValueTreeObjectReference(getTreeObject(form, "vegetablesAmount")),
						new ExpressionOperatorLogic(AvailableOperator.EQUALS), new ExpressionValueNumber(2.0),
						new ExpressionOperatorLogic(AvailableOperator.OR),
						new ExpressionValueTreeObjectReference(getTreeObject(form, "vegetablesAmount")),
						new ExpressionFunction(AvailableFunction.BETWEEN), new ExpressionValueNumber(4.0),
						new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueNumber(6.0),
						new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET)));

		DiagramLink secondForkSecondEnd = new DiagramLink(nodeSecondFork, secondNodeSink);
		secondForkSecondEnd.setJointjsId(IdGenerator.createId());
		secondForkSecondEnd.setType(DiagramObjectType.LINK);
		// Others condition
		secondForkSecondEnd.setExpressionChain(
				new ExpressionChain(new ExpressionValueTreeObjectReference(getTreeObject(form, "vegetables"))));

		// Others for Second fork
		DiagramLink secondForkThirdEnd = new DiagramLink(nodeSecondFork, thirdNodeSink);
		secondForkThirdEnd.setJointjsId(IdGenerator.createId());
		secondForkThirdEnd.setType(DiagramObjectType.LINK);
		secondForkThirdEnd.setExpressionChain(
				new ExpressionChain(new ExpressionValueTreeObjectReference(getTreeObject(form, "vegetables")),
						new ExpressionValueTreeObjectReference(getAnswer(form, "vegetables", "d"))));

		mainDiagram.addDiagramObject(diagramStartNode);
		mainDiagram.addDiagramObject(diagramFirstFork);
		mainDiagram.addDiagramObject(firstEndNode);
		mainDiagram.addDiagramObject(diagramSecondFork);
		mainDiagram.addDiagramObject(secondEndNode);
		mainDiagram.addDiagramObject(thirdEndNode);
		mainDiagram.addDiagramObject(startFirstFork);
		mainDiagram.addDiagramObject(firstForkFirstEnd);
		mainDiagram.addDiagramObject(firstForkSecondFork);
		mainDiagram.addDiagramObject(secondForkSecondEnd);
		mainDiagram.addDiagramObject(secondForkThirdEnd);

		return mainDiagram;
	}
}

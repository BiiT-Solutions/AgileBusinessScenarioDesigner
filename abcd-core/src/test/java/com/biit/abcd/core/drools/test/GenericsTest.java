package com.biit.abcd.core.drools.test;

import java.io.IOException;
import java.text.ParseException;

import org.dom4j.DocumentException;
import org.testng.annotations.Test;

import com.biit.abcd.core.drools.facts.inputform.DroolsForm;
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
import com.biit.abcd.persistence.entity.GenericTreeObjectType;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.diagram.DiagramChild;
import com.biit.abcd.persistence.entity.diagram.DiagramExpression;
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
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorMath;
import com.biit.abcd.persistence.entity.expressions.ExpressionSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueGenericCustomVariable;
import com.biit.abcd.persistence.entity.expressions.exceptions.NotValidOperatorInExpression;
import com.biit.abcd.persistence.entity.globalvariables.exceptions.NotValidTypeInVariableData;
import com.biit.abcd.persistence.utils.IdGenerator;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ChildrenNotFoundException;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.orbeon.exceptions.CategoryNameWithoutTranslation;
import com.biit.orbeon.form.exceptions.CategoryDoesNotExistException;
import com.biit.orbeon.form.exceptions.GroupDoesNotExistException;
import com.biit.orbeon.form.exceptions.QuestionDoesNotExistException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

public class GenericsTest extends KidsFormCreator {

	private final static String FORM_SCORE = "formScore";
	private final static String CATEGORY_SCORE = "catScore";
	private final static String GROUP_SCORE = "groupScore";
	private final static String QUESTION_SCORE = "questScore";

	@Test(groups = { "rules" })
	public void testGenericVariables() throws ExpressionInvalidException, NotValidChildException,
			NotValidOperatorInExpression, ChildrenNotFoundException, RuleInvalidException, FieldTooLongException,
			IOException, CategoryDoesNotExistException, DocumentException, CategoryNameWithoutTranslation,
			RuleNotImplementedException, InvalidAnswerFormatException, ActionNotImplementedException, ParseException,
			GroupDoesNotExistException, QuestionDoesNotExistException, CharacterNotAllowedException,
			NotCompatibleTypeException, NullTreeObjectException, TreeObjectInstanceNotRecognizedException,
			TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			NotValidTypeInVariableData, BetweenFunctionInvalidException {

		// Restart the form to avoid test cross references
		initForm();
		// Create the expressions
		createKidsFormSimpleGenericExpression();
		// Create the rules and launch the engine
		DroolsForm droolsForm = createAndRunDroolsRules();

	}

	private void createKidsFormSimpleGenericExpression() throws FieldTooLongException, NotValidChildException,
			InvalidAnswerFormatException {

		// Create custom variables
		// Form
		CustomVariable formCustomVariable = new CustomVariable(getForm(), FORM_SCORE, CustomVariableType.NUMBER,
				CustomVariableScope.FORM);
		// Category
		CustomVariable categoryCustomVariable = new CustomVariable(getForm(), CATEGORY_SCORE,
				CustomVariableType.NUMBER, CustomVariableScope.CATEGORY);
		// Group
		CustomVariable groupCustomVariable = new CustomVariable(getForm(), GROUP_SCORE, CustomVariableType.NUMBER,
				CustomVariableScope.GROUP);
		// Question
		CustomVariable questionCustomVariable = new CustomVariable(getForm(), QUESTION_SCORE,
				CustomVariableType.NUMBER, CustomVariableScope.QUESTION);

		// Generic expression (Generic category = generic category questions)
		ExpressionChain expression1 = new ExpressionChain("genericCategoryQuestions",
				new ExpressionValueGenericCustomVariable(GenericTreeObjectType.CATEGORY, categoryCustomVariable),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionFunction(AvailableFunction.MIN), new ExpressionValueGenericCustomVariable(
						GenericTreeObjectType.QUESTION_CATEGORY, questionCustomVariable), new ExpressionSymbol(
						AvailableSymbol.RIGHT_BRACKET));
		getForm().getExpressionChains().add(expression1);

		// Generic expression (Generic group = generic group questions)
		ExpressionChain expression2 = new ExpressionChain("genericGroupQuestions",
				new ExpressionValueGenericCustomVariable(GenericTreeObjectType.GROUP, groupCustomVariable),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionFunction(AvailableFunction.MIN), new ExpressionValueGenericCustomVariable(
						GenericTreeObjectType.QUESTION_GROUP, questionCustomVariable), new ExpressionSymbol(
						AvailableSymbol.RIGHT_BRACKET));
		getForm().getExpressionChains().add(expression2);

		// Generic expression (Generic category = generic category groups)
		ExpressionChain expression3 = new ExpressionChain("genericCategoryGroups",
				new ExpressionValueGenericCustomVariable(GenericTreeObjectType.CATEGORY, categoryCustomVariable),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionFunction(AvailableFunction.MIN), new ExpressionValueGenericCustomVariable(
						GenericTreeObjectType.GROUP, groupCustomVariable), new ExpressionSymbol(
						AvailableSymbol.RIGHT_BRACKET));
		getForm().getExpressionChains().add(expression3);

		// Generic expression (Form = generic categories)
		ExpressionChain expression4 = new ExpressionChain("formGenericCategories", new ExpressionValueCustomVariable(
				getForm(), formCustomVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionFunction(AvailableFunction.MIN), new ExpressionValueGenericCustomVariable(
						GenericTreeObjectType.CATEGORY, categoryCustomVariable), new ExpressionSymbol(
						AvailableSymbol.RIGHT_BRACKET));
		getForm().getExpressionChains().add(expression4);

		// Generic expression with several generics (Generic category =
		// generic category groups, generic category questions)
		ExpressionChain expression5 = new ExpressionChain("genericCategoryGroupsQuestions",
				new ExpressionValueGenericCustomVariable(GenericTreeObjectType.CATEGORY, categoryCustomVariable),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionFunction(AvailableFunction.MIN), new ExpressionValueGenericCustomVariable(
						GenericTreeObjectType.GROUP, groupCustomVariable), new ExpressionSymbol(AvailableSymbol.COMMA),
				new ExpressionValueGenericCustomVariable(GenericTreeObjectType.QUESTION_CATEGORY,
						questionCustomVariable), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		getForm().getExpressionChains().add(expression5);

		// Generic expression with several generics (Generic category =
		// generic category questions, generic category groups)
		ExpressionChain expression6 = new ExpressionChain("genericCategoryQuestionsGroups",
				new ExpressionValueGenericCustomVariable(GenericTreeObjectType.CATEGORY, categoryCustomVariable),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionFunction(AvailableFunction.MIN), new ExpressionValueGenericCustomVariable(
						GenericTreeObjectType.QUESTION_CATEGORY, questionCustomVariable), new ExpressionSymbol(
						AvailableSymbol.COMMA), new ExpressionValueGenericCustomVariable(GenericTreeObjectType.GROUP,
						groupCustomVariable), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		getForm().getExpressionChains().add(expression6);

		// Generic expression with several generics (Generic group = generic
		// groups, generic group questions)
		ExpressionChain expression7 = new ExpressionChain("genericGroupQuestionsGroups",
				new ExpressionValueGenericCustomVariable(GenericTreeObjectType.GROUP, categoryCustomVariable),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionFunction(AvailableFunction.MIN), new ExpressionValueGenericCustomVariable(
						GenericTreeObjectType.GROUP, groupCustomVariable), new ExpressionSymbol(AvailableSymbol.COMMA),
				new ExpressionValueGenericCustomVariable(GenericTreeObjectType.QUESTION_GROUP, questionCustomVariable),
				new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		getForm().getExpressionChains().add(expression7);

		// Creation of a simple diagram to load the table rule
		getForm().addDiagram(createSimpleTableDiagram());
	}

	private Diagram createSimpleTableDiagram() {
		Diagram mainDiagram = new Diagram("main");

		DiagramSource diagramStartNode = new DiagramSource();
		diagramStartNode.setJointjsId(IdGenerator.createId());
		diagramStartNode.setType(DiagramObjectType.SOURCE);
		Node nodeSource = new Node(diagramStartNode.getJointjsId());

		DiagramChild subDiagramExpressionNode = new DiagramChild();
		subDiagramExpressionNode.setDiagram(createExpressionsSubdiagram());
		subDiagramExpressionNode.setJointjsId(IdGenerator.createId());
		subDiagramExpressionNode.setType(DiagramObjectType.DIAGRAM_CHILD);
		Node nodeTable = new Node(subDiagramExpressionNode.getJointjsId());

		DiagramSink diagramEndNode = new DiagramSink();
		diagramEndNode.setJointjsId(IdGenerator.createId());
		diagramEndNode.setType(DiagramObjectType.SINK);
		Node nodeSink = new Node(diagramEndNode.getJointjsId());

		DiagramLink startExpression = new DiagramLink(nodeSource, nodeTable);
		startExpression.setJointjsId(IdGenerator.createId());
		startExpression.setType(DiagramObjectType.LINK);
		DiagramLink expressionEnd = new DiagramLink(nodeTable, nodeSink);
		expressionEnd.setJointjsId(IdGenerator.createId());
		expressionEnd.setType(DiagramObjectType.LINK);

		mainDiagram.addDiagramObject(diagramStartNode);
		mainDiagram.addDiagramObject(subDiagramExpressionNode);
		mainDiagram.addDiagramObject(diagramEndNode);
		mainDiagram.addDiagramObject(startExpression);
		mainDiagram.addDiagramObject(expressionEnd);

		return mainDiagram;
	}

	private Diagram createExpressionsSubdiagram() {
		Diagram subDiagram = new Diagram("expressionDiagram");
		for (ExpressionChain expressionChain : getForm().getExpressionChains()) {

			DiagramSource diagramSource = new DiagramSource();
			diagramSource.setJointjsId(IdGenerator.createId());
			diagramSource.setType(DiagramObjectType.SOURCE);
			Node nodeSource = new Node(diagramSource.getJointjsId());

			DiagramExpression diagramExpression = new DiagramExpression();
			diagramExpression.setExpression(expressionChain);
			diagramExpression.setJointjsId(IdGenerator.createId());
			diagramExpression.setType(DiagramObjectType.CALCULATION);
			Node nodeRule = new Node(diagramExpression.getJointjsId());

			DiagramSink diagramSink = new DiagramSink();
			diagramSink.setJointjsId(IdGenerator.createId());
			diagramSink.setType(DiagramObjectType.SINK);
			Node nodeSink = new Node(diagramSink.getJointjsId());

			DiagramLink diagramLinkSourceRule = new DiagramLink(nodeSource, nodeRule);
			diagramLinkSourceRule.setJointjsId(IdGenerator.createId());
			diagramLinkSourceRule.setType(DiagramObjectType.LINK);
			DiagramLink diagramLinkRuleSink = new DiagramLink(nodeRule, nodeSink);
			diagramLinkRuleSink.setJointjsId(IdGenerator.createId());
			diagramLinkRuleSink.setType(DiagramObjectType.LINK);

			subDiagram.addDiagramObject(diagramSource);
			subDiagram.addDiagramObject(diagramExpression);
			subDiagram.addDiagramObject(diagramSink);
			subDiagram.addDiagramObject(diagramLinkSourceRule);
			subDiagram.addDiagramObject(diagramLinkRuleSink);
		}
		return subDiagram;
	}
}

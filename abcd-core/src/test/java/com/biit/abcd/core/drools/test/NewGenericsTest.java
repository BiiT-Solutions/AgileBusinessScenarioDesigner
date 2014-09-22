package com.biit.abcd.core.drools.test;

import java.io.IOException;
import java.text.ParseException;

import org.dom4j.DocumentException;
import org.testng.annotations.Test;

import com.biit.abcd.core.drools.facts.inputform.DroolsForm;
import com.biit.abcd.core.drools.rules.exceptions.ActionNotImplementedException;
import com.biit.abcd.core.drools.rules.exceptions.ExpressionInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleInvalidException;
import com.biit.abcd.core.drools.rules.exceptions.RuleNotImplementedException;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.CustomVariableScope;
import com.biit.abcd.persistence.entity.CustomVariableType;
import com.biit.abcd.persistence.entity.GenericTreeObjectType;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.diagram.DiagramCalculation;
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
import com.biit.abcd.persistence.utils.IdGenerator;
import com.biit.form.exceptions.ChildrenNotFoundException;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.orbeon.exceptions.CategoryNameWithoutTranslation;
import com.biit.orbeon.form.exceptions.CategoryDoesNotExistException;
import com.biit.orbeon.form.exceptions.GroupDoesNotExistException;
import com.biit.orbeon.form.exceptions.QuestionDoesNotExistException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

public class NewGenericsTest extends TestFormCreator {

	private final static String FORM_SCORE = "formScore";
	private final static String CATEGORY_SCORE = "catScore";
	private final static String GROUP_SCORE = "groupScore";
	private final static String QUESTION_SCORE = "questScore";

	@Test(groups = { "rules" })
	public void testGenericVariables() throws ExpressionInvalidException, NotValidChildException,
			NotValidOperatorInExpression, ChildrenNotFoundException, RuleInvalidException, FieldTooLongException,
			IOException, CategoryDoesNotExistException, DocumentException, CategoryNameWithoutTranslation,
			RuleNotImplementedException, InvalidAnswerFormatException, ActionNotImplementedException, ParseException,
			GroupDoesNotExistException, QuestionDoesNotExistException {

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
		ExpressionChain expression = new ExpressionChain("genericCategoryQuestions",
				new ExpressionValueGenericCustomVariable(GenericTreeObjectType.CATEGORY, categoryCustomVariable),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
				new ExpressionFunction(AvailableFunction.MIN), new ExpressionValueGenericCustomVariable(
						GenericTreeObjectType.QUESTION_CATEGORY, questionCustomVariable), new ExpressionSymbol(
						AvailableSymbol.RIGHT_BRACKET));
		getForm().getExpressionChain().add(expression);

		// Generic expression (Generic group = generic group questions)
		expression = new ExpressionChain("genericGroupQuestions", new ExpressionValueGenericCustomVariable(
				GenericTreeObjectType.GROUP, groupCustomVariable), new ExpressionOperatorMath(
				AvailableOperator.ASSIGNATION), new ExpressionFunction(AvailableFunction.MIN),
				new ExpressionValueGenericCustomVariable(GenericTreeObjectType.QUESTION_GROUP, questionCustomVariable),
				new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		getForm().getExpressionChain().add(expression);

		// Generic expression (Generic category = generic category groups)
		expression = new ExpressionChain("genericCategoryGroups", new ExpressionValueGenericCustomVariable(
				GenericTreeObjectType.CATEGORY, categoryCustomVariable), new ExpressionOperatorMath(
				AvailableOperator.ASSIGNATION), new ExpressionFunction(AvailableFunction.MIN),
				new ExpressionValueGenericCustomVariable(GenericTreeObjectType.GROUP, groupCustomVariable),
				new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		getForm().getExpressionChain().add(expression);

		// Generic expression (Form = generic categories)
		expression = new ExpressionChain("formGenericCategories", new ExpressionValueCustomVariable(getForm(),
				formCustomVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionFunction(
				AvailableFunction.MIN), new ExpressionValueGenericCustomVariable(GenericTreeObjectType.CATEGORY,
				categoryCustomVariable), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		getForm().getExpressionChain().add(expression);

		// Generic expression with several generics (Generic category = generic
		// category groups, generic category questions)
		expression = new ExpressionChain("genericCategoryGroupsQuestions", new ExpressionValueGenericCustomVariable(
				GenericTreeObjectType.CATEGORY, categoryCustomVariable), new ExpressionOperatorMath(
				AvailableOperator.ASSIGNATION), new ExpressionFunction(AvailableFunction.MIN),
				new ExpressionValueGenericCustomVariable(GenericTreeObjectType.GROUP, groupCustomVariable),
				new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueGenericCustomVariable(
						GenericTreeObjectType.QUESTION_CATEGORY, questionCustomVariable), new ExpressionSymbol(
						AvailableSymbol.RIGHT_BRACKET));
		getForm().getExpressionChain().add(expression);

		// Generic expression with several generics (Generic category = generic
		// category groups, generic category questions)
		expression = new ExpressionChain("genericCategoryQuestionsGroups", new ExpressionValueGenericCustomVariable(
				GenericTreeObjectType.CATEGORY, categoryCustomVariable), new ExpressionOperatorMath(
				AvailableOperator.ASSIGNATION), new ExpressionFunction(AvailableFunction.MIN),
				new ExpressionValueGenericCustomVariable(GenericTreeObjectType.QUESTION_CATEGORY,
						questionCustomVariable), new ExpressionSymbol(AvailableSymbol.COMMA),
				new ExpressionValueGenericCustomVariable(GenericTreeObjectType.GROUP, groupCustomVariable),
				new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		getForm().getExpressionChain().add(expression);

		// Creation of a simple diagram to load the table rule
		getForm().addDiagram(createExpressionsSubdiagram());
	}

	private Diagram createExpressionsSubdiagram() {
		Diagram subDiagram = new Diagram("expressionDiagram");
		for (ExpressionChain expressionChain : getForm().getExpressionChain()) {

			DiagramSource diagramSource = new DiagramSource();
			diagramSource.setJointjsId(IdGenerator.createId());
			diagramSource.setType(DiagramObjectType.SOURCE);
			Node nodeSource = new Node(diagramSource.getJointjsId());

			DiagramCalculation diagramExpression = new DiagramCalculation();
			diagramExpression.setFormExpression(expressionChain);
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

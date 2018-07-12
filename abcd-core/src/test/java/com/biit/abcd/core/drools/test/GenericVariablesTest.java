package com.biit.abcd.core.drools.test;

import java.io.IOException;

import org.dom4j.DocumentException;
import org.testng.annotations.Test;

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
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.CustomVariableScope;
import com.biit.abcd.persistence.entity.CustomVariableType;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.GenericTreeObjectType;
import com.biit.abcd.persistence.entity.expressions.AvailableFunction;
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.AvailableSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionFunction;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorMath;
import com.biit.abcd.persistence.entity.expressions.ExpressionSymbol;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueGenericCustomVariable;
import com.biit.drools.engine.exceptions.DroolsRuleExecutionException;
import com.biit.drools.global.variables.exceptions.NotValidTypeInVariableData;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

public class GenericVariablesTest extends KidsFormCreator {

	private final static String FORM_SCORE = "formScore";
	private final static String CATEGORY_SCORE = "catScore";
	private final static String GROUP_SCORE = "groupScore";
	private final static String QUESTION_SCORE = "questScore";
	private CustomVariable formCustomVariable = null;
	private CustomVariable categoryCustomVariable = null;
	private CustomVariable groupCustomVariable = null;
	private CustomVariable questionCustomVariable = null;

	@Test(groups = { "droolsGeneric" })
	private void genericCategoryQuestionsTest() throws DroolsRuleGenerationException, DocumentException, IOException, DroolsRuleExecutionException,
			RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException,
			InvalidRuleException, ActionNotImplementedException, FieldTooLongException, CharacterNotAllowedException, NotValidChildException,
			InvalidAnswerFormatException, NotValidTypeInVariableData, ElementIsReadOnly, InvalidExpressionException {
		// Generate the form
		Form form = initFormAndVariables();
		// Generic expression (Generic category = generic category
		// questions)
		ExpressionChain expression = new ExpressionChain("genericCategoryQuestions", new ExpressionValueGenericCustomVariable(GenericTreeObjectType.CATEGORY,
				getCategoryCustomVariable()), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionFunction(AvailableFunction.MIN),
				new ExpressionValueGenericCustomVariable(GenericTreeObjectType.QUESTION_CATEGORY, getQuestionCustomVariable()), new ExpressionSymbol(
						AvailableSymbol.RIGHT_BRACKET));
		// Generate the drools rules
		checkResults(form, expression);
	}

	@Test(groups = { "droolsGeneric" })
	private void genericGroupQuestionsTest() throws FieldTooLongException, CharacterNotAllowedException, NotValidChildException, InvalidAnswerFormatException,
			NotValidTypeInVariableData, ElementIsReadOnly, DroolsRuleGenerationException, DocumentException, IOException, DroolsRuleExecutionException,
			RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException,
			InvalidRuleException, ActionNotImplementedException, InvalidExpressionException {
		// Generate the form
		Form form = initFormAndVariables();
		// Generic expression (Generic group = generic group questions)
		ExpressionChain expression = new ExpressionChain("genericGroupQuestions", new ExpressionValueGenericCustomVariable(GenericTreeObjectType.GROUP,
				groupCustomVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionFunction(AvailableFunction.MIN),
				new ExpressionValueGenericCustomVariable(GenericTreeObjectType.QUESTION_GROUP, questionCustomVariable), new ExpressionSymbol(
						AvailableSymbol.RIGHT_BRACKET));
		// Generate the drools rules
		checkResults(form, expression);
	}

	@Test(groups = { "droolsGeneric" })
	private void genericCategoryGroupsTest() throws DroolsRuleGenerationException, DocumentException, IOException, DroolsRuleExecutionException,
			RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException,
			InvalidRuleException, ActionNotImplementedException, FieldTooLongException, CharacterNotAllowedException, NotValidChildException,
			InvalidAnswerFormatException, NotValidTypeInVariableData, ElementIsReadOnly, InvalidExpressionException {
		// Generate the form
		Form form = initFormAndVariables();
		// Generic expression (Generic category = generic category groups)
		ExpressionChain expression = new ExpressionChain("genericCategoryGroups", new ExpressionValueGenericCustomVariable(GenericTreeObjectType.CATEGORY,
				categoryCustomVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionFunction(AvailableFunction.MIN),
				new ExpressionValueGenericCustomVariable(GenericTreeObjectType.GROUP, groupCustomVariable), new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		// Generate the drools rules
		checkResults(form, expression);
	}

	@Test(groups = { "droolsGeneric" })
	private void formGenericCategoriesTest() throws DroolsRuleGenerationException, DocumentException, IOException, DroolsRuleExecutionException,
			RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException,
			InvalidRuleException, ActionNotImplementedException, FieldTooLongException, CharacterNotAllowedException, NotValidChildException,
			InvalidAnswerFormatException, NotValidTypeInVariableData, ElementIsReadOnly, InvalidExpressionException {
		// Generate the form
		Form form = initFormAndVariables();
		// Generic expression (Form = generic categories)
		ExpressionChain expression = new ExpressionChain("formGenericCategories", new ExpressionValueCustomVariable(form, formCustomVariable),
				new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionFunction(AvailableFunction.MIN),
				new ExpressionValueGenericCustomVariable(GenericTreeObjectType.CATEGORY, categoryCustomVariable), new ExpressionSymbol(
						AvailableSymbol.RIGHT_BRACKET));
		// Generate the drools rules
		checkResults(form, expression);
	}

	@Test(groups = { "droolsGeneric" })
	private void genericCategoryGroupsQuestionsTest() throws FieldTooLongException, CharacterNotAllowedException, NotValidChildException,
			InvalidAnswerFormatException, NotValidTypeInVariableData, ElementIsReadOnly, DroolsRuleGenerationException, DocumentException, IOException,
			DroolsRuleExecutionException, RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException,
			InvalidRuleException, ActionNotImplementedException, InvalidExpressionException {
		// Generate the form
		Form form = initFormAndVariables();
		// Generic expression with several generics (Generic category =
		// generic category groups, generic category questions)
		ExpressionChain expression = new ExpressionChain("genericCategoryGroupsQuestions", new ExpressionValueGenericCustomVariable(
				GenericTreeObjectType.CATEGORY, categoryCustomVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionFunction(
				AvailableFunction.MIN), new ExpressionValueGenericCustomVariable(GenericTreeObjectType.GROUP, groupCustomVariable), new ExpressionSymbol(
				AvailableSymbol.COMMA), new ExpressionValueGenericCustomVariable(GenericTreeObjectType.QUESTION_CATEGORY, questionCustomVariable),
				new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		// Generate the drools rules
		checkResults(form, expression);
	}

	@Test(groups = { "droolsGeneric" })
	private void genericCategoryQuestionsGroupsTest() throws DroolsRuleGenerationException, DocumentException, IOException, DroolsRuleExecutionException,
			RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException,
			InvalidRuleException, ActionNotImplementedException, FieldTooLongException, CharacterNotAllowedException, NotValidChildException,
			InvalidAnswerFormatException, NotValidTypeInVariableData, ElementIsReadOnly, InvalidExpressionException {
		// Generate the form
		Form form = initFormAndVariables();
		// Generic expression with several generics (Generic category =
		// generic category questions, generic category groups)
		ExpressionChain expression = new ExpressionChain("genericCategoryQuestionsGroups", new ExpressionValueGenericCustomVariable(
				GenericTreeObjectType.CATEGORY, categoryCustomVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionFunction(
				AvailableFunction.MIN), new ExpressionValueGenericCustomVariable(GenericTreeObjectType.QUESTION_CATEGORY, questionCustomVariable),
				new ExpressionSymbol(AvailableSymbol.COMMA), new ExpressionValueGenericCustomVariable(GenericTreeObjectType.GROUP, groupCustomVariable),
				new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
		// Generate the drools rules
		checkResults(form, expression);
	}

	@Test(groups = { "droolsGeneric" })
	private void genericGroupQuestionsGroupsTest() throws DroolsRuleGenerationException, DocumentException, IOException, DroolsRuleExecutionException,
			RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException,
			InvalidRuleException, ActionNotImplementedException, FieldTooLongException, CharacterNotAllowedException, NotValidChildException,
			InvalidAnswerFormatException, NotValidTypeInVariableData, ElementIsReadOnly, InvalidExpressionException {
		// Generate the form
		Form form = initFormAndVariables();
		// Generic expression with several generics (Generic group = generic
		// groups, generic group questions)
		ExpressionChain expression = new ExpressionChain("genericGroupQuestionsGroups", new ExpressionValueGenericCustomVariable(GenericTreeObjectType.GROUP,
				categoryCustomVariable), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionFunction(AvailableFunction.MIN),
				new ExpressionValueGenericCustomVariable(GenericTreeObjectType.GROUP, groupCustomVariable), new ExpressionSymbol(AvailableSymbol.COMMA),
				new ExpressionValueGenericCustomVariable(GenericTreeObjectType.QUESTION_GROUP, questionCustomVariable), new ExpressionSymbol(
						AvailableSymbol.RIGHT_BRACKET));
		// Generate the drools rules
		checkResults(form, expression);
	}

	private Form initFormAndVariables() throws FieldTooLongException, CharacterNotAllowedException, NotValidChildException, InvalidAnswerFormatException,
			NotValidTypeInVariableData, ElementIsReadOnly {
		// Restart the form to avoid test cross references
		Form form = createForm();
		// Create custom variables
		setFormCustomVariable(new CustomVariable(form, FORM_SCORE, CustomVariableType.NUMBER, CustomVariableScope.FORM));
		setCategoryCustomVariable(new CustomVariable(form, CATEGORY_SCORE, CustomVariableType.NUMBER, CustomVariableScope.CATEGORY));
		setGroupCustomVariable(new CustomVariable(form, GROUP_SCORE, CustomVariableType.NUMBER, CustomVariableScope.GROUP));
		setQuestionCustomVariable(new CustomVariable(form, QUESTION_SCORE, CustomVariableType.NUMBER, CustomVariableScope.QUESTION));
		return form;
	}

	private void checkResults(Form form, ExpressionChain expression) throws DroolsRuleGenerationException, DocumentException, IOException,
			DroolsRuleExecutionException, RuleNotImplementedException, NotCompatibleTypeException, ExpressionInvalidException, NullTreeObjectException,
			TreeObjectInstanceNotRecognizedException, TreeObjectParentNotValidException, NullCustomVariableException, NullExpressionValueException,
			BetweenFunctionInvalidException, DateComparisonNotPossibleException, PluginInvocationException, DroolsRuleCreationException, PrattParserException,
			InvalidRuleException, ActionNotImplementedException, InvalidExpressionException {
		// Add the rule to the form
		form.getExpressionChains().add(expression);
		// Create the node rule
		createExpressionNode(expression);
		// Create the diagram
		createDiagram(form);
		// Create the rules and launch the engine
		createAndRunDroolsRules(form);
	}

	public CustomVariable getFormCustomVariable() {
		return formCustomVariable;
	}

	public void setFormCustomVariable(CustomVariable formCustomVariable) {
		this.formCustomVariable = formCustomVariable;
	}

	public CustomVariable getCategoryCustomVariable() {
		return categoryCustomVariable;
	}

	public void setCategoryCustomVariable(CustomVariable categoryCustomVariable) {
		this.categoryCustomVariable = categoryCustomVariable;
	}

	public CustomVariable getGroupCustomVariable() {
		return groupCustomVariable;
	}

	public void setGroupCustomVariable(CustomVariable groupCustomVariable) {
		this.groupCustomVariable = groupCustomVariable;
	}

	public CustomVariable getQuestionCustomVariable() {
		return questionCustomVariable;
	}

	public void setQuestionCustomVariable(CustomVariable questionCustomVariable) {
		this.questionCustomVariable = questionCustomVariable;
	}
}

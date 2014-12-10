package com.biit.abcd.core.drools.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.CustomVariableScope;
import com.biit.abcd.persistence.entity.CustomVariableType;
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

public class GenericsTest extends KidsFormCreator {

	private final static String FORM_SCORE = "formScore";
	private final static String CATEGORY_SCORE = "catScore";
	private final static String GROUP_SCORE = "groupScore";
	private final static String QUESTION_SCORE = "questScore";
	private CustomVariable formCustomVariable = null;
	private CustomVariable categoryCustomVariable = null;
	private CustomVariable groupCustomVariable = null;
	private CustomVariable questionCustomVariable = null;

	@Test(groups = { "rules2" })
	private void genericCategoryQuestionsTest() {
		try {
			// Generate the form
			initFormAndVariables();
			// Generic expression (Generic category = generic category
			// questions)
			ExpressionChain expression = new ExpressionChain("genericCategoryQuestions",
					new ExpressionValueGenericCustomVariable(GenericTreeObjectType.CATEGORY,
							getCategoryCustomVariable()), new ExpressionOperatorMath(AvailableOperator.ASSIGNATION),
					new ExpressionFunction(AvailableFunction.MIN), new ExpressionValueGenericCustomVariable(
							GenericTreeObjectType.QUESTION_CATEGORY, getQuestionCustomVariable()),
					new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
			// Generate the drools rules
			checkResults(expression);
		} catch (Exception e) {
			Assert.fail();
		}
	}

	@Test(groups = { "rules" })
	private void genericGroupQuestionsTest() {
		try {
			// Generate the form
			initFormAndVariables();
			// Generic expression (Generic group = generic group questions)
			ExpressionChain expression = new ExpressionChain("genericGroupQuestions",
					new ExpressionValueGenericCustomVariable(GenericTreeObjectType.GROUP, groupCustomVariable),
					new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionFunction(
							AvailableFunction.MIN), new ExpressionValueGenericCustomVariable(
							GenericTreeObjectType.QUESTION_GROUP, questionCustomVariable), new ExpressionSymbol(
							AvailableSymbol.RIGHT_BRACKET));
			// Generate the drools rules
			checkResults(expression);
		} catch (Exception e) {
			Assert.fail();
		}
	}

	@Test(groups = { "rules" })
	private void genericCategoryGroupsTest() {
		try {
			// Generate the form
			initFormAndVariables();
			// Generic expression (Generic category = generic category groups)
			ExpressionChain expression = new ExpressionChain("genericCategoryGroups",
					new ExpressionValueGenericCustomVariable(GenericTreeObjectType.CATEGORY, categoryCustomVariable),
					new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionFunction(
							AvailableFunction.MIN), new ExpressionValueGenericCustomVariable(
							GenericTreeObjectType.GROUP, groupCustomVariable), new ExpressionSymbol(
							AvailableSymbol.RIGHT_BRACKET));
			// Generate the drools rules
			checkResults(expression);
		} catch (Exception e) {
			Assert.fail();
		}
	}

	@Test(groups = { "rules" })
	private void formGenericCategoriesTest() {
		try {
			// Generate the form
			initFormAndVariables();
			// Generic expression (Form = generic categories)
			ExpressionChain expression = new ExpressionChain("formGenericCategories",
					new ExpressionValueCustomVariable(getForm(), formCustomVariable), new ExpressionOperatorMath(
							AvailableOperator.ASSIGNATION), new ExpressionFunction(AvailableFunction.MIN),
					new ExpressionValueGenericCustomVariable(GenericTreeObjectType.CATEGORY, categoryCustomVariable),
					new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));
			// Generate the drools rules
			checkResults(expression);
		} catch (Exception e) {
			Assert.fail();
		}
	}

	@Test(groups = { "rules" })
	private void genericCategoryGroupsQuestionsTest() {
		try {
			// Generate the form
			initFormAndVariables();
			// Generic expression with several generics (Generic category =
			// generic category groups, generic category questions)
			ExpressionChain expression = new ExpressionChain("genericCategoryGroupsQuestions",
					new ExpressionValueGenericCustomVariable(GenericTreeObjectType.CATEGORY, categoryCustomVariable),
					new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionFunction(
							AvailableFunction.MIN), new ExpressionValueGenericCustomVariable(
							GenericTreeObjectType.GROUP, groupCustomVariable), new ExpressionSymbol(
							AvailableSymbol.COMMA), new ExpressionValueGenericCustomVariable(
							GenericTreeObjectType.QUESTION_CATEGORY, questionCustomVariable), new ExpressionSymbol(
							AvailableSymbol.RIGHT_BRACKET));
			// Generate the drools rules
			checkResults(expression);
		} catch (Exception e) {
			Assert.fail();
		}
	}

	@Test(groups = { "rules" })
	private void genericCategoryQuestionsGroupsTest() {
		try {
			// Generate the form
			initFormAndVariables();
			// Generic expression with several generics (Generic category =
			// generic category questions, generic category groups)
			ExpressionChain expression = new ExpressionChain("genericCategoryQuestionsGroups",
					new ExpressionValueGenericCustomVariable(GenericTreeObjectType.CATEGORY, categoryCustomVariable),
					new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionFunction(
							AvailableFunction.MIN), new ExpressionValueGenericCustomVariable(
							GenericTreeObjectType.QUESTION_CATEGORY, questionCustomVariable), new ExpressionSymbol(
							AvailableSymbol.COMMA), new ExpressionValueGenericCustomVariable(
							GenericTreeObjectType.GROUP, groupCustomVariable), new ExpressionSymbol(
							AvailableSymbol.RIGHT_BRACKET));
			// Generate the drools rules
			checkResults(expression);
		} catch (Exception e) {
			Assert.fail();
		}
	}

	@Test(groups = { "rules" })
	private void genericGroupQuestionsGroupsTest() {
		try {
			// Generate the form
			initFormAndVariables();
			// Generic expression with several generics (Generic group = generic
			// groups, generic group questions)
			ExpressionChain expression = new ExpressionChain("genericGroupQuestionsGroups",
					new ExpressionValueGenericCustomVariable(GenericTreeObjectType.GROUP, categoryCustomVariable),
					new ExpressionOperatorMath(AvailableOperator.ASSIGNATION), new ExpressionFunction(
							AvailableFunction.MIN), new ExpressionValueGenericCustomVariable(
							GenericTreeObjectType.GROUP, groupCustomVariable), new ExpressionSymbol(
							AvailableSymbol.COMMA), new ExpressionValueGenericCustomVariable(
							GenericTreeObjectType.QUESTION_GROUP, questionCustomVariable), new ExpressionSymbol(
							AvailableSymbol.RIGHT_BRACKET));
			// Generate the drools rules
			checkResults(expression);
		} catch (Exception e) {
			Assert.fail();
		}
	}

	private void initFormAndVariables() {
		try {
			// Restart the form to avoid test cross references
			initForm();
			// Create custom variables
			setFormCustomVariable(new CustomVariable(getForm(), FORM_SCORE, CustomVariableType.NUMBER,
					CustomVariableScope.FORM));
			setCategoryCustomVariable(new CustomVariable(getForm(), CATEGORY_SCORE, CustomVariableType.NUMBER,
					CustomVariableScope.CATEGORY));
			setGroupCustomVariable(new CustomVariable(getForm(), GROUP_SCORE, CustomVariableType.NUMBER,
					CustomVariableScope.GROUP));
			setQuestionCustomVariable(new CustomVariable(getForm(), QUESTION_SCORE, CustomVariableType.NUMBER,
					CustomVariableScope.QUESTION));
		} catch (Exception e) {
			Assert.fail();
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}
	}

	private void checkResults(ExpressionChain expression) {
		try {
			// Add the rule to the form
			getForm().getExpressionChains().add(expression);
			// Create the node rule
			createExpressionNode(expression);
			// Create the diagram
			createDiagram();
			// Create the rules and launch the engine
			createAndRunDroolsRules();
		} catch (Exception e) {
			Assert.fail();
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}
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

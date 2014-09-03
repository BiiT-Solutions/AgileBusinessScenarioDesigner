package com.biit.abcd.persistence.entity.expressions;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.CustomVariableScope;
import com.biit.abcd.persistence.entity.CustomVariableType;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.expressions.AvailableOperator;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionOperatorMath;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueCustomVariable;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueNumber;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.jexeval.exceptions.ExpressionException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

@Test(groups = { "expressionEvaluator" })
public class ExpressionTest {
	private final static String CATEGORY_NAME = "Category1";
	private final static String QUESTION_NAME = "Question1";

	@Test
	public void basicExpressionConverter() throws FieldTooLongException, NotValidChildException {
		// Create the form
		Form form = new Form("DhszwForm");
		Category category = new Category(CATEGORY_NAME);
		form.addChild(category);

		CustomVariable customVarCategory = new CustomVariable(form, "cScore", CustomVariableType.NUMBER,
				CustomVariableScope.CATEGORY);

		ExpressionChain expressionChain = new ExpressionChain();
		ExpressionValueCustomVariable customVariable = new ExpressionValueCustomVariable(category, customVarCategory);

		// Category.Score=1+1;
		expressionChain.addExpression(customVariable);
		expressionChain.addExpression(new ExpressionOperatorMath(AvailableOperator.ASSIGNATION));
		expressionChain.addExpression(new ExpressionValueNumber(1d));
		expressionChain.addExpression(new ExpressionOperatorMath(AvailableOperator.PLUS));
		expressionChain.addExpression(new ExpressionValueNumber(1d));

		Assert.assertEquals(expressionChain.getExpression(), "Category1_cScore = 1 + 1");
		expressionChain.getExpressionEvaluator().eval();
	}

	@Test(expectedExceptions = ExpressionException.class)
	public void basicInvalidExpressionConverter() throws FieldTooLongException, NotValidChildException {
		// Create the form
		Form form = new Form("DhszwForm");
		Category category = new Category(CATEGORY_NAME);
		form.addChild(category);
		Question question = new Question(QUESTION_NAME);
		category.addChild(question);

		// Create the custom variables
		CustomVariable customVarCategory = new CustomVariable(form, "cScore", CustomVariableType.NUMBER,
				CustomVariableScope.CATEGORY);
		CustomVariable customVarQuestion = new CustomVariable(form, "qScore", CustomVariableType.NUMBER,
				CustomVariableScope.QUESTION);

		ExpressionChain expressionChain = new ExpressionChain();
		ExpressionValueCustomVariable customCategoryVariable = new ExpressionValueCustomVariable(category,
				customVarCategory);
		ExpressionValueCustomVariable customQuestionVariable = new ExpressionValueCustomVariable(question,
				customVarQuestion);

		// Category.Score=Category.Score Question.Score;
		expressionChain.addExpression(customCategoryVariable);
		expressionChain.addExpression(new ExpressionOperatorMath(AvailableOperator.ASSIGNATION));
		expressionChain.addExpression(customCategoryVariable);
		expressionChain.addExpression(customQuestionVariable);

		expressionChain.getExpressionEvaluator().eval();
	}

	@Test(expectedExceptions = ExpressionException.class)
	public void variableWithNumberError() throws FieldTooLongException, NotValidChildException {
		// Create the form
		Form form = new Form("DhszwForm");
		Category category = new Category(CATEGORY_NAME);
		form.addChild(category);
		Question question = new Question(QUESTION_NAME);
		category.addChild(question);

		// Create the custom variables
		CustomVariable customVarCategory = new CustomVariable(form, "cScore", CustomVariableType.NUMBER,
				CustomVariableScope.CATEGORY);
		CustomVariable customVarQuestion = new CustomVariable(form, "qScore", CustomVariableType.NUMBER,
				CustomVariableScope.QUESTION);

		ExpressionChain expressionChain = new ExpressionChain();
		ExpressionValueCustomVariable customCategoryVariable = new ExpressionValueCustomVariable(category,
				customVarCategory);
		ExpressionValueCustomVariable customQuestionVariable = new ExpressionValueCustomVariable(question,
				customVarQuestion);

		// Category.Score=Category.Score+1Question.Score;
		expressionChain.addExpression(customCategoryVariable);
		expressionChain.addExpression(new ExpressionOperatorMath(AvailableOperator.ASSIGNATION));
		expressionChain.addExpression(new ExpressionValueString("1"));
		expressionChain.addExpression(new ExpressionOperatorMath(AvailableOperator.PLUS));
		expressionChain.addExpression(new ExpressionValueString("1"));
		expressionChain.addExpression(customQuestionVariable);

		expressionChain.getExpressionEvaluator().eval();
	}

	@Test
	public void functionIn() throws FieldTooLongException, NotValidChildException {
		// Create the form
		Form form = new Form("DhszwForm");
		Category category = new Category(CATEGORY_NAME);
		form.addChild(category);
		Question question = new Question(QUESTION_NAME);
		category.addChild(question);

		// Create the custom variables
		CustomVariable customVarCategory = new CustomVariable(form, "cScore", CustomVariableType.NUMBER,
				CustomVariableScope.CATEGORY);
		CustomVariable customVarQuestion = new CustomVariable(form, "qScore", CustomVariableType.NUMBER,
				CustomVariableScope.QUESTION);

		ExpressionChain expressionChain = new ExpressionChain();
		ExpressionValueCustomVariable customCategoryVariable = new ExpressionValueCustomVariable(category,
				customVarCategory);

		// Category.Score=Category.Score+1Question.Score;
		expressionChain.addExpression(customCategoryVariable);
		expressionChain.addExpression(new ExpressionFunction(AvailableFunction.IN));
		expressionChain.addExpression(new ExpressionValueString("1"));
		expressionChain.addExpression(new ExpressionSymbol(AvailableSymbol.COMMA));
		expressionChain.addExpression(new ExpressionValueString("1"));
		expressionChain.addExpression(new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));

		expressionChain.getExpressionEvaluator().eval();
	}

	@Test(expectedExceptions = ExpressionException.class)
	public void functionInBad() throws FieldTooLongException, NotValidChildException {
		// Create the form
		Form form = new Form("DhszwForm");
		Category category = new Category(CATEGORY_NAME);
		form.addChild(category);
		Question question = new Question(QUESTION_NAME);
		category.addChild(question);

		// Create the custom variables
		CustomVariable customVarCategory = new CustomVariable(form, "cScore", CustomVariableType.NUMBER,
				CustomVariableScope.CATEGORY);
		CustomVariable customVarQuestion = new CustomVariable(form, "qScore", CustomVariableType.NUMBER,
				CustomVariableScope.QUESTION);

		ExpressionChain expressionChain = new ExpressionChain();
		ExpressionValueCustomVariable customCategoryVariable = new ExpressionValueCustomVariable(category,
				customVarCategory);

		// Category.Score=Category.Score+1Question.Score;
		expressionChain.addExpression(customCategoryVariable);
		expressionChain.addExpression(new ExpressionValueString("1"));
		expressionChain.addExpression(new ExpressionOperatorMath(AvailableOperator.PLUS));
		expressionChain.addExpression(new ExpressionFunction(AvailableFunction.IN));
		expressionChain.addExpression(new ExpressionValueString("1"));
		expressionChain.addExpression(new ExpressionSymbol(AvailableSymbol.COMMA));
		expressionChain.addExpression(new ExpressionValueString("1"));
		expressionChain.addExpression(new ExpressionSymbol(AvailableSymbol.RIGHT_BRACKET));

		expressionChain.getExpressionEvaluator().eval();
	}
}

package com.biit.abcd.persistence.entity.expressions;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.persistence.dao.IFormDao;
import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.abcd.persistence.entity.AnswerType;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.CustomVariableScope;
import com.biit.abcd.persistence.entity.CustomVariableType;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Question;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.jexeval.exceptions.ExpressionException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
@Test(groups = { "expressionEvaluator" })
public class ExpressionTest extends AbstractTransactionalTestNGSpringContextTests {
	private final static String CATEGORY_NAME = "Category1";
	private final static String QUESTION_NAME = "Question1";

	@Autowired
	private IFormDao formDao;

	@Test
	public void basicExpressionConverter() throws FieldTooLongException, NotValidChildException,
			CharacterNotAllowedException {
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
	public void basicInvalidExpressionConverter() throws FieldTooLongException, NotValidChildException,
			CharacterNotAllowedException {
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
	public void variableWithNumberError() throws FieldTooLongException, NotValidChildException,
			CharacterNotAllowedException {
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
	public void functionIn() throws FieldTooLongException, NotValidChildException, CharacterNotAllowedException {
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
	public void functionInBad() throws FieldTooLongException, NotValidChildException, CharacterNotAllowedException {
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

	@Test
	public void checkExpressionStorageAndOrder() throws FieldTooLongException, NotValidChildException,
			InvalidAnswerFormatException, CharacterNotAllowedException {
		// Create the form
		Form form = new Form("DhszwForm");
		Category category = new Category(CATEGORY_NAME);
		form.addChild(category);

		Question birthdate = new Question("birthdate");
		birthdate.setAnswerType(AnswerType.INPUT);
		birthdate.setAnswerFormat(AnswerFormat.DATE);
		category.addChild(birthdate);

		CustomVariable customVarCategory = new CustomVariable(form, "cScore", CustomVariableType.NUMBER,
				CustomVariableScope.CATEGORY);

		form.getCustomVariables().add(customVarCategory);

		ExpressionChain expressionChain = new ExpressionChain();
		ExpressionValueCustomVariable customVariable = new ExpressionValueCustomVariable(category, customVarCategory);

		expressionChain.addExpression(customVariable);
		expressionChain.addExpression(new ExpressionOperatorMath(AvailableOperator.ASSIGNATION));
		expressionChain.addExpression(new ExpressionValueTreeObjectReference(birthdate, QuestionDateUnit.YEARS));
		Assert.assertEquals(expressionChain.getExpression(), "Category1_cScore = birthdate");
		expressionChain.getExpressionEvaluator().eval();

		// Check the order
		form.getExpressionChain().add(expressionChain);
		formDao.makePersistent(form);
		Form retrievedForm = formDao.read(form.getId());
		formDao.makeTransient(form);
		for (ExpressionChain expressionChainAux : retrievedForm.getExpressionChain()) {
			Assert.assertEquals(expressionChainAux.getExpression(), "Category1_cScore = birthdate");
		}
	}
}

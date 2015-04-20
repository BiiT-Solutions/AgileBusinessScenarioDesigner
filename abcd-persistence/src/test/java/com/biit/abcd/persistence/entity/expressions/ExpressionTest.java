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
import com.biit.form.exceptions.DependencyExistException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.dao.exceptions.ElementCannotBePersistedException;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.persistence.entity.exceptions.ElementCannotBeRemovedException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
@Test(groups = { "expressionEvaluator" })
public class ExpressionTest extends AbstractTransactionalTestNGSpringContextTests {
	private final static String CATEGORY_NAME = "Category1";

	@Autowired
	private IFormDao formDao;

	@Test
	public void checkExpressionStorageAndOrder() throws FieldTooLongException, NotValidChildException,
			InvalidAnswerFormatException, CharacterNotAllowedException, UnexpectedDatabaseException, ElementIsReadOnly,
			ElementCannotBePersistedException, ElementCannotBeRemovedException {
		// Create the form
		Form form = new Form("DhszwForm");
		form.setOrganizationId(0l);
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

		// Check the order
		form.getExpressionChains().add(expressionChain);
		formDao.makePersistent(form);
		Form retrievedForm = formDao.get(form.getId());
		formDao.makeTransient(form);
		for (ExpressionChain expressionChainAux : retrievedForm.getExpressionChains()) {
			Assert.assertEquals(expressionChainAux.getExpression(), "Category1_cScore = birthdate");
		}
	}

	@Test(expectedExceptions = { DependencyExistException.class })
	public void checkExpressionDependencies() throws FieldTooLongException, NotValidChildException,
			InvalidAnswerFormatException, CharacterNotAllowedException, UnexpectedDatabaseException,
			DependencyExistException, ElementIsReadOnly, ElementCannotBePersistedException,
			ElementCannotBeRemovedException {
		// Create the form
		Form form = new Form("DhszwForm");
		form.setOrganizationId(0l);
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
		form.getExpressionChains().add(expressionChain);

		// Persist.
		formDao.makePersistent(form);

		// Remove in incorrect order.
		try {
			birthdate.remove();
		} finally {
			formDao.makeTransient(form);
		}
	}

	@Test
	public void checkExpressionDependenciesAcomplished() throws FieldTooLongException, NotValidChildException,
			InvalidAnswerFormatException, CharacterNotAllowedException, UnexpectedDatabaseException,
			DependencyExistException, ElementIsReadOnly, ElementCannotBePersistedException,
			ElementCannotBeRemovedException {
		// Create the form
		Form form = new Form("DhszwForm");
		form.setOrganizationId(0l);
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
		form.getExpressionChains().add(expressionChain);

		// Persist.
		formDao.makePersistent(form);

		// Remove in order.
		form.getExpressionChains().remove(form.getExpressionChains().iterator().next());
		birthdate.remove();
		form.remove(customVarCategory);

		// No errors in database.
		formDao.makePersistent(form);
		formDao.makeTransient(form);
	}
}

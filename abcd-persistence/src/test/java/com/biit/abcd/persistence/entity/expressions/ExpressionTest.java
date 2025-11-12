package com.biit.abcd.persistence.entity.expressions;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (Persistence)
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

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.persistence.dao.IFormDao;
import com.biit.abcd.persistence.dao.IGlobalVariablesDao;
import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.abcd.persistence.entity.AnswerType;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.CustomVariableScope;
import com.biit.abcd.persistence.entity.CustomVariableType;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.drools.global.variables.exceptions.NotValidTypeInVariableData;
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

	private final static String GLOBAL_VARIABLE_NAME = "GlobalVariable1";
	private static final Timestamp VARIABLE_DATA_VALID_FROM = new Timestamp((new Date().getTime() / 1000) * 1000);
	private static final Timestamp VARIABLE_DATA_VALID_TO = new Timestamp(VARIABLE_DATA_VALID_FROM.getTime() + (3600 * 1000));
	private static final Timestamp VARIABLE_DATA_VALID_TO_2 = new Timestamp(VARIABLE_DATA_VALID_TO.getTime() + (3600 * 1000));
	private static final String VARIABLE_DATA_VALUE_1 = "AAA";
	private static final String VARIABLE_DATA_VALUE_2 = "BBB";

	@Autowired
	private IFormDao formDao;

	@Autowired
	private IGlobalVariablesDao globalVariablesDao;

	@Test
	public void checkExpressionStorageAndOrder() throws FieldTooLongException, NotValidChildException, InvalidAnswerFormatException,
			CharacterNotAllowedException, UnexpectedDatabaseException, ElementIsReadOnly, ElementCannotBePersistedException, ElementCannotBeRemovedException {
		// Create the form
		Form form = new Form("DhszwForm");
		form.setOrganizationId(0l);
		Category category = new Category(CATEGORY_NAME);
		form.addChild(category);

		Question birthdate = new Question("birthdate");
		birthdate.setAnswerType(AnswerType.INPUT);
		birthdate.setAnswerFormat(AnswerFormat.DATE);
		category.addChild(birthdate);

		CustomVariable customVarCategory = new CustomVariable(form, "cScore", CustomVariableType.NUMBER, CustomVariableScope.CATEGORY);

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
	public void checkExpressionDependencies() throws FieldTooLongException, NotValidChildException, InvalidAnswerFormatException, CharacterNotAllowedException,
			UnexpectedDatabaseException, DependencyExistException, ElementIsReadOnly, ElementCannotBePersistedException, ElementCannotBeRemovedException {
		// Create the form
		Form form = new Form("DhszwForm");
		form.setOrganizationId(0l);
		Category category = new Category(CATEGORY_NAME);
		form.addChild(category);

		Question birthdate = new Question("birthdate");
		birthdate.setAnswerType(AnswerType.INPUT);
		birthdate.setAnswerFormat(AnswerFormat.DATE);
		category.addChild(birthdate);

		CustomVariable customVarCategory = new CustomVariable(form, "cScore", CustomVariableType.NUMBER, CustomVariableScope.CATEGORY);

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
	public void checkExpressionDependenciesAcomplished() throws FieldTooLongException, NotValidChildException, InvalidAnswerFormatException,
			CharacterNotAllowedException, UnexpectedDatabaseException, DependencyExistException, ElementIsReadOnly, ElementCannotBePersistedException,
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

		CustomVariable customVarCategory = new CustomVariable(form, "cScore", CustomVariableType.NUMBER, CustomVariableScope.CATEGORY);

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

	@Test
	public void checkExpressionGlobalVariable() throws FieldTooLongException, NotValidChildException, InvalidAnswerFormatException,
			CharacterNotAllowedException, UnexpectedDatabaseException, DependencyExistException, ElementIsReadOnly, ElementCannotBePersistedException,
			ElementCannotBeRemovedException, NotValidTypeInVariableData {
		// Create the form
		Form form = new Form("DhszwForm");
		form.setOrganizationId(0l);
		Category category = new Category(CATEGORY_NAME);
		form.addChild(category);

		Question birthdate = new Question("birthdate");
		birthdate.setAnswerType(AnswerType.INPUT);
		birthdate.setAnswerFormat(AnswerFormat.DATE);
		category.addChild(birthdate);

		CustomVariable customVarCategory = new CustomVariable(form, "cScore", CustomVariableType.NUMBER, CustomVariableScope.CATEGORY);

		form.getCustomVariables().add(customVarCategory);

		GlobalVariable globalVariable = new GlobalVariable(AnswerFormat.TEXT);
		globalVariable.setName(GLOBAL_VARIABLE_NAME);

		globalVariable.addVariableData(VARIABLE_DATA_VALUE_1, VARIABLE_DATA_VALID_FROM, VARIABLE_DATA_VALID_TO);
		globalVariable.addVariableData(VARIABLE_DATA_VALUE_2, VARIABLE_DATA_VALID_TO, VARIABLE_DATA_VALID_TO_2);

		globalVariablesDao.makePersistent(globalVariable);

		ExpressionChain expressionChain = new ExpressionChain();
		ExpressionValueCustomVariable customVariable = new ExpressionValueCustomVariable(category, customVarCategory);
		ExpressionValueGlobalVariable globalVariableExpression = new ExpressionValueGlobalVariable(globalVariable);

		expressionChain.addExpression(customVariable);
		expressionChain.addExpression(new ExpressionOperatorMath(AvailableOperator.ASSIGNATION));
		expressionChain.addExpression(globalVariableExpression);
		Assert.assertEquals(expressionChain.getExpression(), "Category1_cScore = " + GLOBAL_VARIABLE_NAME);
		form.getExpressionChains().add(expressionChain);

		// Persist.
		formDao.makePersistent(form);

		// Check some functions.
		Assert.assertEquals(globalVariablesDao.getFormNumberUsing(globalVariable), 1);
		Set<GlobalVariable> globalVariables = new HashSet<>();
		globalVariables.add(globalVariable);
		Assert.assertEquals(globalVariablesDao.getFormNumberUsing(globalVariables), 1);

		// Remove in order.
		form.getExpressionChains().remove(form.getExpressionChains().iterator().next());
		birthdate.remove();
		form.remove(customVarCategory);

		// No errors in database.
		formDao.makePersistent(form);

		formDao.makeTransient(form);

		globalVariablesDao.makeTransient(globalVariable);
	}
}

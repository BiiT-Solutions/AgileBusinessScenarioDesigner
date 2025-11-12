package com.biit.abcd.persistence.dao;

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

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.persistence.FormUtils;
import com.biit.abcd.persistence.entity.Form;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.dao.exceptions.ElementCannotBePersistedException;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.persistence.entity.exceptions.ElementCannotBeRemovedException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
@Test(groups = { "removeForm" })
public class RemoveForm extends AbstractTransactionalTestNGSpringContextTests {

	@Autowired
	IDiagramObjectDao diagramObjectDao;

	@Autowired
	IFormDao formDao;

	@Autowired
	ICustomVariableDao customVariableDao;

	@Autowired
	IDiagramDao diagramDao;

	@Autowired
	IExpressionChainDao expressionChainDao;

	@Autowired
	IGlobalVariablesDao globalVariableDao;

	@Autowired
	IRuleDao ruleDao;

	@Autowired
	ITableRuleDao tableRuleDao;

	@Autowired
	ITableRuleRowDao tableRuleRowDao;

	@Autowired
	ITestScenarioDao testScenarioDao;

	@Autowired
	IVariableDataDao variableDataDao;

	@Test
	public void removeDiagramElements() throws FieldTooLongException, NotValidChildException,
			CharacterNotAllowedException, InvalidAnswerFormatException, ElementIsReadOnly, UnexpectedDatabaseException,
			ElementCannotBePersistedException, ElementCannotBeRemovedException {
		Form form = FormUtils.createCompleteForm();

		int previousDiagramObjects = diagramObjectDao.getRowCount();
		int previousFormbjects = formDao.getRowCount();
		int previousCustomVariables = customVariableDao.getRowCount();
		int previousDiagram = diagramDao.getRowCount();
		int previousChains = expressionChainDao.getRowCount();
		int previousGlobalVariableDao = globalVariableDao.getRowCount();
		int previousRuleDao = ruleDao.getRowCount();
		int previousTableRuleDao = tableRuleDao.getRowCount();
		int previousTableRuleRowDao = tableRuleRowDao.getRowCount();
		int previousTestScenarioDao = testScenarioDao.getRowCount();
		int previousVariableDataDao = variableDataDao.getRowCount();

		formDao.makePersistent(form);

		Assert.assertTrue(diagramObjectDao.getRowCount() > previousDiagramObjects);
		Assert.assertTrue(formDao.getRowCount() > previousFormbjects);
		Assert.assertTrue(customVariableDao.getRowCount() > previousCustomVariables);
		Assert.assertTrue(diagramDao.getRowCount() > previousDiagram);
		Assert.assertTrue(expressionChainDao.getRowCount() > previousChains);
		Assert.assertTrue(ruleDao.getRowCount() > previousRuleDao);
		Assert.assertTrue(tableRuleDao.getRowCount() > previousTableRuleDao);

		formDao.makeTransient(form);
		
		Assert.assertEquals(diagramObjectDao.getRowCount(), previousDiagramObjects);
		Assert.assertEquals(formDao.getRowCount(), previousFormbjects);
		Assert.assertEquals(customVariableDao.getRowCount(), previousCustomVariables);
		Assert.assertEquals(diagramDao.getRowCount(), previousDiagram);
		Assert.assertEquals(expressionChainDao.getRowCount(), previousChains);
		Assert.assertEquals(globalVariableDao.getRowCount(), previousGlobalVariableDao);
		Assert.assertEquals(ruleDao.getRowCount(), previousRuleDao);
		Assert.assertEquals(tableRuleDao.getRowCount(), previousTableRuleDao);
		Assert.assertEquals(tableRuleRowDao.getRowCount(), previousTableRuleRowDao);
		Assert.assertEquals(testScenarioDao.getRowCount(), previousTestScenarioDao);
		Assert.assertEquals(variableDataDao.getRowCount(), previousVariableDataDao);
	}
}

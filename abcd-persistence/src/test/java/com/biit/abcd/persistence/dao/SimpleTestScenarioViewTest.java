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

import java.util.List;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.SimpleTestScenarioView;
import com.biit.abcd.persistence.entity.testscenarios.TestScenario;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.dao.exceptions.ElementCannotBePersistedException;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.persistence.entity.exceptions.ElementCannotBeRemovedException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
@Test(groups = { "simpleTestScenarioViewDao" })
public class SimpleTestScenarioViewTest extends AbstractTransactionalTestNGSpringContextTests {
	private final static String DUMMY_TEST_SCENARIO = "Dummy Test Scenario View";
	private static final String FORM_LABEL = "Form";
	private static final String CATEGORY_NAME = "Category";
	private static final Long FORM_ORGANIZATION_ID = 0l;
	private static final Integer FORM_VERSION = 1;

	@Autowired
	private ITestScenarioDao testScenarioDao;

	@Autowired
	private IFormDao formDao;

	@Autowired
	private ISimpleTestScenarioViewDao simpleTestScenarioViewDao;

	@Test
	public void getView() throws FieldTooLongException, CharacterNotAllowedException, NotValidChildException,
			NotValidStorableObjectException, UnexpectedDatabaseException, ElementIsReadOnly,
			ElementCannotBePersistedException, ElementCannotBeRemovedException {

		Form form = new Form();
		form.setLabel(FORM_LABEL);
		form.setVersion(FORM_VERSION);
		form.setOrganizationId(FORM_ORGANIZATION_ID);
		Category category = new Category();
		category.setName(CATEGORY_NAME);
		form.addChild(category);
		formDao.makePersistent(form);

		TestScenario testScenario = new TestScenario(DUMMY_TEST_SCENARIO, form);
		testScenarioDao.makePersistent(testScenario);

		Assert.assertEquals(testScenarioDao.getRowCount(), 1);

		List<SimpleTestScenarioView> views = simpleTestScenarioViewDao.getSimpleTestScenariosByFormId(form.getId());
		Assert.assertEquals(views.size(), 1);
		Assert.assertEquals(views.get(0).getName(), DUMMY_TEST_SCENARIO);

		formDao.makeTransient(form);
		testScenarioDao.makeTransient(testScenario);
	}
}

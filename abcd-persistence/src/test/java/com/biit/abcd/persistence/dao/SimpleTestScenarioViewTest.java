package com.biit.abcd.persistence.dao;

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
import com.biit.form.exceptions.NotValidChildException;
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

	@Autowired
	private ITestScenarioDao testScenarioDao;

	@Autowired
	private ISimpleTestScenarioViewDao simpleTestScenarioViewDao;

	@Test
	public void getView() throws FieldTooLongException, CharacterNotAllowedException, NotValidChildException,
			NotValidStorableObjectException {
		
		Form form = new Form();
		form.setOrganizationId(FORM_ORGANIZATION_ID);
		form.setLabel(FORM_LABEL);
		Category category = new Category();
		category.setName(CATEGORY_NAME);
		form.addChild(category);
		
		TestScenario testScenario = new TestScenario(DUMMY_TEST_SCENARIO, form);
		testScenarioDao.makePersistent(testScenario);
		
		Assert.assertEquals(testScenarioDao.getRowCount(), 1);
		Assert.assertEquals(simpleTestScenarioViewDao.getRowCount(), 1);
		
		List<SimpleTestScenarioView> views = simpleTestScenarioViewDao.getAll();
		Assert.assertEquals(views.size(), 1);
		Assert.assertEquals(views.get(0).getName(), DUMMY_TEST_SCENARIO);
		
		testScenarioDao.makeTransient(testScenario);
	}
}

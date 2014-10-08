package com.biit.abcd.persistence.dao;

import java.util.List;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.SimpleTestScenarioView;
import com.biit.abcd.persistence.entity.testscenarios.TestScenario;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
@Test(groups = { "simpleTestScenarioViewDao" })
public class SimpleTestScenarioViewTest extends AbstractTransactionalTestNGSpringContextTests {
	private final static String DUMMY_FORM = "Dummy Form View";
	private final static String DUMMY_TEST_SCENARIO = "Dummy Test Scenario View";

	@Autowired
	private IFormDao formDao;

	@Autowired
	private ISimpleTestScenarioViewDao simpleTestScenarioViewDao;

	@Test
	public void getView() throws FieldTooLongException, CharacterNotAllowedException {
		Form form = new Form();
		form.setOrganizationId(0l);
		form.setLabel(DUMMY_FORM);
		form.setCreatedBy(1l);
		form.setUpdatedBy(1l);
		TestScenario testScenario = new TestScenario(DUMMY_TEST_SCENARIO);
		testScenario.setCreatedBy(1l);
		testScenario.setUpdatedBy(1l);
		form.addTestScenario(testScenario);
		formDao.makePersistent(form);
		
		Assert.assertEquals(formDao.getRowCount(), 1);
		Assert.assertEquals(simpleTestScenarioViewDao.getRowCount(), 1);
		
		List<SimpleTestScenarioView> views = simpleTestScenarioViewDao.getAll();
		Assert.assertEquals(views.size(), 1);
		Assert.assertEquals(views.get(0).getName(), DUMMY_TEST_SCENARIO);
		
		views = simpleTestScenarioViewDao.getSimpleTestScenarioByFormId(form.getId());
		Assert.assertEquals(views.size(), 1);
		Assert.assertEquals(views.get(0).getName(), DUMMY_TEST_SCENARIO);
		
		formDao.makeTransient(form);
	}
}

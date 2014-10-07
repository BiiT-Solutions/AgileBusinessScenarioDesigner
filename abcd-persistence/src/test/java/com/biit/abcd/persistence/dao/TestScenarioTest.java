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
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.testscenarios.TestAnswer;
import com.biit.abcd.persistence.entity.testscenarios.TestAnswerInputNumber;
import com.biit.abcd.persistence.entity.testscenarios.TestScenario;
import com.biit.abcd.persistence.entity.testscenarios.exceptions.NotValidAnswerValue;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.NotValidFormException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
@Test(groups = { "testScenarioDao" })
public class TestScenarioTest extends AbstractTransactionalTestNGSpringContextTests {

	private static final String TEST_SCENARIO_NAME = "test1";
	private static final Double TEST_ANSWER_VALUE = 5.0;

	@Autowired
	private ITestScenarioDao testScenarioDao;
	@Autowired
	private IFormDao formDao;

	@Test
	public void storeRemoveTestScenarios() throws NotValidFormException, FieldTooLongException {
		TestScenario testScenario = new TestScenario(TEST_SCENARIO_NAME);

		testScenarioDao.makePersistent(testScenario);
		Assert.assertEquals(testScenarioDao.getRowCount(), 1);

		List<TestScenario> persistedList = testScenarioDao.getAll();
		Assert.assertEquals(persistedList.size(), 1);
		Assert.assertEquals(persistedList.get(0).getName(), TEST_SCENARIO_NAME);

		testScenarioDao.makeTransient(testScenario);
		Assert.assertEquals(testScenarioDao.getRowCount(), 0);
	}
	
	@Test
	public void storeRemoveTestScenariosFromForm() throws NotValidFormException, FieldTooLongException {
		Form form = new Form();
		form.setOrganizationId(0l);
		form.setLabel("Form");
		TestScenario testScenario = new TestScenario(TEST_SCENARIO_NAME);
		form.addTestScenario(testScenario);

		formDao.makePersistent(form);
		Assert.assertEquals(formDao.getRowCount(), 1);

		List<Form> persistedList = formDao.getAll();
		Assert.assertEquals(persistedList.size(), 1);
		Assert.assertEquals(persistedList.get(0).getTestScenarios().size(), 1);
		Assert.assertEquals(persistedList.get(0).getTestScenarios().iterator().next().getName(), TEST_SCENARIO_NAME);

		formDao.makeTransient(form);
		Assert.assertEquals(formDao.getRowCount(), 0);
	}
	
	@Test
	public void storeRemoveTestScenariosMapDataFromForm() throws NotValidFormException, FieldTooLongException,
			CharacterNotAllowedException, NotValidAnswerValue, NotValidChildException {
		Form form = new Form();
		form.setOrganizationId(0l);
		form.setLabel("Form");

		Category category = new Category();
		category.setName("Category1");
		form.addChild(category);

		Group group = new Group();
		group.setName("Group1");
		category.addChild(group);

		Question question1 = new Question();
		question1.setName("Question1");
		group.addChild(question1);

		Question question2 = new Question();
		question2.setName("Question1");
		group.addChild(question2);

		TestScenario testScenario = new TestScenario(TEST_SCENARIO_NAME);
		TestAnswer testAnswer1 = new TestAnswerInputNumber(TEST_ANSWER_VALUE);
		testScenario.addData(question1, testAnswer1);

		form.addTestScenario(testScenario);
		formDao.makePersistent(form);
		Assert.assertEquals(formDao.getRowCount(), 1);

		List<Form> persistedList = formDao.getAll();
		Assert.assertEquals(persistedList.size(), 1);

		testScenario = persistedList.get(0).getTestScenarios().iterator().next();
		Assert.assertEquals(testScenario.getName(), TEST_SCENARIO_NAME);
		Assert.assertEquals(testScenario.getTestAnswer(question1), testAnswer1);
		TestAnswer testAnswer = testScenario.getTestAnswer(question1);
		Assert.assertEquals(testAnswer.getValue(), TEST_ANSWER_VALUE);

		formDao.makeTransient(form);
		Assert.assertEquals(formDao.getRowCount(), 0);
	}

	@Test
	public void storeRemoveTestScenariosMapData() throws NotValidFormException, FieldTooLongException,
			CharacterNotAllowedException, NotValidAnswerValue, NotValidChildException {
		Form form = new Form();
		form.setOrganizationId(0l);
		form.setLabel("Form");

		Category category = new Category();
		category.setName("Category1");
		form.addChild(category);

		Group group = new Group();
		group.setName("Group1");
		category.addChild(group);

		Question question1 = new Question();
		question1.setName("Question1");
		group.addChild(question1);

		Question question2 = new Question();
		question2.setName("Question1");
		group.addChild(question2);

		TestScenario testScenario = new TestScenario(TEST_SCENARIO_NAME);
		TestAnswer testAnswer1 = new TestAnswerInputNumber(TEST_ANSWER_VALUE);
		testScenario.addData(question1, testAnswer1);

		formDao.makePersistent(form);
		testScenarioDao.makePersistent(testScenario);
		Assert.assertEquals(testScenarioDao.getRowCount(), 1);

		List<TestScenario> persistedList = testScenarioDao.getAll();
		Assert.assertEquals(persistedList.size(), 1);

		testScenario = persistedList.get(0);
		Assert.assertEquals(testScenario.getName(), TEST_SCENARIO_NAME);
		Assert.assertEquals(testScenario.getTestAnswer(question1), testAnswer1);
		TestAnswer testAnswer = testScenario.getTestAnswer(question1);
		Assert.assertEquals(testAnswer.getValue(), TEST_ANSWER_VALUE);

		testScenarioDao.makeTransient(testScenario);
		formDao.makeTransient(form);
		Assert.assertEquals(testScenarioDao.getRowCount(), 0);
	}
}

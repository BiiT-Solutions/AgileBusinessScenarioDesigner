package com.biit.abcd.persistence.dao;

import java.util.List;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.persistence.entity.testscenarios.TestAnswer;
import com.biit.abcd.persistence.entity.testscenarios.TestAnswerInputNumber;
import com.biit.abcd.persistence.entity.testscenarios.TestScenario;
import com.biit.abcd.persistence.entity.testscenarios.TestScenarioObject;
import com.biit.abcd.persistence.entity.testscenarios.TestScenarioQuestionAnswer;
import com.biit.abcd.persistence.entity.testscenarios.exceptions.NotValidAnswerValue;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ChildrenNotFoundException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.NotValidFormException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
@Test(groups = { "testScenarioDao" })
public class TestScenarioTest extends AbstractTransactionalTestNGSpringContextTests {

	private static final String FORM_LABEL = "Form";
	private static final String FORM_LABEL_ERROR = "Bad_Form";
	private static final Long FORM_ORGANIZATION_ID = 0l;
	private static final Integer FORM_VERSION = 1;
	private static final String TEST_SCENARIO_NAME = "test1";
	private static final Double TEST_ANSWER_VALUE = 5.0;
	private static final String FORM_NAME = "Form";
	private static final String CATEGORY_NAME = "Category";
	private static final String QUESTION_NAME = "Question";

	@Autowired
	private ITestScenarioDao testScenarioDao;
	@Autowired
	private IFormDao formDao;

	@Test
	public void storeRemoveTestScenarios() throws NotValidFormException, FieldTooLongException {
		TestScenario testScenario = new TestScenario(TEST_SCENARIO_NAME);
		testScenario.setFormLabel(FORM_LABEL);
		testScenario.setFormOrganizationId(FORM_ORGANIZATION_ID);
		testScenario.setFormVersion(FORM_VERSION);

		testScenarioDao.makePersistent(testScenario);
		Assert.assertEquals(testScenarioDao.getRowCount(), 1);

		List<TestScenario> persistedList = testScenarioDao.getAll();
		Assert.assertEquals(persistedList.size(), 1);
		Assert.assertEquals(persistedList.get(0).getName(), TEST_SCENARIO_NAME);

		persistedList = testScenarioDao.getTestScenarioByFormLabelVersionOrganizationId(FORM_LABEL, FORM_VERSION,
				FORM_ORGANIZATION_ID);
		Assert.assertEquals(persistedList.size(), 1);
		Assert.assertEquals(persistedList.get(0).getName(), TEST_SCENARIO_NAME);

		persistedList = testScenarioDao.getTestScenarioByFormLabelVersionOrganizationId(FORM_LABEL_ERROR, FORM_VERSION,
				FORM_ORGANIZATION_ID);
		Assert.assertEquals(persistedList.size(), 0);

		testScenarioDao.makeTransient(testScenario);
		Assert.assertEquals(testScenarioDao.getRowCount(), 0);
	}

	@Test
	public void storeRemoveTestScenariosMapData() throws NotValidFormException, FieldTooLongException,
			CharacterNotAllowedException, NotValidAnswerValue, NotValidChildException, ChildrenNotFoundException {
		TestScenario testScenario = new TestScenario(TEST_SCENARIO_NAME);
		testScenario.setFormLabel("Form");
		testScenario.setFormOrganizationId(0l);
		testScenario.setFormVersion(1);

		TestScenarioObject formObject = new TestScenarioObject();
		formObject.setName(FORM_NAME);
		testScenario.setTestScenarioForm(formObject);

		TestScenarioObject categoryObject = new TestScenarioObject();
		categoryObject.setName(CATEGORY_NAME);
		formObject.addChild(categoryObject);

		TestAnswer testAnswerNumber = new TestAnswerInputNumber(TEST_ANSWER_VALUE);
		TestScenarioQuestionAnswer testScenarioQuestionAnswer = new TestScenarioQuestionAnswer(testAnswerNumber);
		testScenarioQuestionAnswer.setName(QUESTION_NAME);
		categoryObject.addChild(testScenarioQuestionAnswer);

		testScenarioDao.makePersistent(testScenario);
		Assert.assertEquals(testScenarioDao.getRowCount(), 1);

		List<TestScenario> persistedList = testScenarioDao.getAll();
		Assert.assertEquals(persistedList.size(), 1);

		testScenario = persistedList.get(0);
		Assert.assertEquals(testScenario.getName(), TEST_SCENARIO_NAME);

		TestScenarioObject testScenarioForm = testScenario.getTestScenarioForm();
		Assert.assertEquals(testScenarioForm, formObject);
		Assert.assertEquals(testScenarioForm.getChildren().size(), 1);
		
		TestScenarioObject testScenarioCategory = (TestScenarioObject) testScenarioForm.getChild(0);
		Assert.assertEquals(testScenarioCategory.getChildren().size(), 1);

		TestAnswer answer = ((TestScenarioQuestionAnswer) testScenarioCategory.getChild(0)).getTestAnswer();
		Assert.assertEquals(answer, testAnswerNumber);
		Assert.assertEquals(answer.getValue(), TEST_ANSWER_VALUE);

		testScenarioDao.makeTransient(testScenario);
		Assert.assertEquals(testScenarioDao.getRowCount(), 0);
	}
}

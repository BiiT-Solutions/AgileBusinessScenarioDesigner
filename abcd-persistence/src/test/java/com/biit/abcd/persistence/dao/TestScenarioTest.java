package com.biit.abcd.persistence.dao;

import java.util.List;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.abcd.persistence.entity.AnswerType;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.testscenarios.TestAnswerInputNumber;
import com.biit.abcd.persistence.entity.testscenarios.TestScenario;
import com.biit.abcd.persistence.entity.testscenarios.TestScenarioForm;
import com.biit.abcd.persistence.entity.testscenarios.TestScenarioQuestion;
import com.biit.abcd.persistence.entity.testscenarios.exceptions.NotValidAnswerValue;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ChildrenNotFoundException;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.exceptions.NotValidFormException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
@Test(groups = { "testScenarioDao" })
public class TestScenarioTest extends AbstractTransactionalTestNGSpringContextTests {

	private static final String FORM_LABEL = "Form";
	private static final String FORM_LABEL_ERROR = "Bad_Form";
	private static final Long FORM_ORGANIZATION_ID = 0l;
	private static final Integer FORM_VERSION = 0;
	private static final String TEST_SCENARIO_NAME = "test1";
	private static final String CATEGORY_NAME = "Category";
	private static final String QUESTION_NAME = "Question";

	@Autowired
	private ITestScenarioDao testScenarioDao;
	@Autowired
	private IFormDao formDao;

	@Test
	public void storeRemoveTestScenarios() throws NotValidFormException, FieldTooLongException,
			NotValidStorableObjectException, CharacterNotAllowedException, NotValidChildException {
		Form form = new Form();
		form.setOrganizationId(FORM_ORGANIZATION_ID);
		form.setLabel(FORM_LABEL);
		Category category = new Category();
		category.setName(CATEGORY_NAME);
		form.addChild(category);

		TestScenario testScenario = new TestScenario(TEST_SCENARIO_NAME, form);

		testScenarioDao.makePersistent(testScenario);
		Assert.assertEquals(testScenarioDao.getRowCount(), 1);

		List<TestScenario> persistedList = testScenarioDao.getAll();
		Assert.assertEquals(persistedList.size(), 1);
		Assert.assertEquals(persistedList.get(0).getName(), TEST_SCENARIO_NAME);

		persistedList = testScenarioDao.getTestScenarioByFormLabelVersionOrganizationId(form.getLabel(), form.getVersion(),
				form.getOrganizationId());
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
			CharacterNotAllowedException, NotValidAnswerValue, NotValidChildException, ChildrenNotFoundException,
			InvalidAnswerFormatException, NotValidStorableObjectException {
		Form form = new Form();
		form.setOrganizationId(FORM_ORGANIZATION_ID);
		form.setLabel(FORM_LABEL);
		Category category = new Category();
		category.setName(CATEGORY_NAME);
		form.addChild(category);
		Question question = new Question();
		question.setName(QUESTION_NAME);
		question.setAnswerType(AnswerType.INPUT);
		question.setAnswerFormat(AnswerFormat.NUMBER);
		category.addChild(question);

		TestScenario testScenario = new TestScenario(TEST_SCENARIO_NAME, form);
		TestScenarioForm testScenarioForm = testScenario.getTestScenarioForm();
		Assert.assertEquals(testScenarioForm.getLabel(), FORM_LABEL);
		Assert.assertEquals(testScenarioForm.getChildren().size(), 1);

		TreeObject testScenarioCategory = testScenarioForm.getChild(0);
		Assert.assertEquals(testScenarioCategory.getName(), CATEGORY_NAME);
		Assert.assertEquals(testScenarioCategory.getChildren().size(), 1);

		TreeObject testQuestion = testScenarioCategory.getChild(0);
		Assert.assertTrue(testQuestion instanceof TestScenarioQuestion);
		TestScenarioQuestion testScenarioQuestion = (TestScenarioQuestion) testQuestion;
		Assert.assertTrue(testScenarioQuestion.getTestAnswer() instanceof TestAnswerInputNumber);

		testScenarioDao.makePersistent(testScenario);
		Assert.assertEquals(testScenarioDao.getRowCount(), 1);

		List<TestScenario> persistedList = testScenarioDao.getAll();
		Assert.assertEquals(persistedList.size(), 1);

		testScenario = persistedList.get(0);
		Assert.assertEquals(testScenario.getName(), TEST_SCENARIO_NAME);

		testScenarioForm = testScenario.getTestScenarioForm();
		Assert.assertEquals(testScenarioForm.getLabel(), FORM_LABEL);
		Assert.assertEquals(testScenarioForm.getChildren().size(), 1);

		testScenarioCategory = testScenarioForm.getChild(0);
		Assert.assertEquals(testScenarioCategory.getName(), CATEGORY_NAME);
		Assert.assertEquals(testScenarioCategory.getChildren().size(), 1);

		testQuestion = testScenarioCategory.getChild(0);
		Assert.assertTrue(testQuestion instanceof TestScenarioQuestion);
		testScenarioQuestion = (TestScenarioQuestion) testQuestion;
		Assert.assertTrue(testScenarioQuestion.getTestAnswer() instanceof TestAnswerInputNumber);

		testScenarioDao.makeTransient(testScenario);
		Assert.assertEquals(testScenarioDao.getRowCount(), 0);
	}
}

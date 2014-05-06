package com.biit.abcd.persistence.dao;

import java.util.List;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.abcd.persistence.entity.AnswerType;
import com.biit.abcd.persistence.entity.exceptions.InvalidAnswerFormatException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
public class AnswerTest extends AbstractTransactionalTestNGSpringContextTests {
	private final static String DUMMY_ANSWER = "Dummy Answer";
	private final static String ANSWER_INPUT_FIELD = "Answer With Input Field.";

	@Autowired
	private IAnswerDao answerDao;

	@Test(groups = { "answerDao" })
	public void testEmptyDatabase() {
		// Read
		Assert.assertEquals(answerDao.getRowCount(), 0);
	}

	@Test(groups = { "answerDao" }, dependsOnMethods = "testEmptyDatabase")
	public void storeDummyAnswer() {
		Answer answer = new Answer();
		answer.setTechnicalName(DUMMY_ANSWER);
		answerDao.makePersistent(answer);
		Assert.assertEquals(answerDao.getRowCount(), 1);
	}

	@Test(groups = { "answerDao" }, dependsOnMethods = "storeDummyAnswer")
	public void getDummyAnswer() {
		List<Answer> answers = answerDao.getAll();
		Assert.assertEquals(answers.get(0).getTechnicalName(), DUMMY_ANSWER);
	}

	@Test(groups = { "answerDao" }, dependsOnMethods = "getDummyAnswer")
	public void removeDummyAnswer() {
		List<Answer> answers = answerDao.getAll();
		answerDao.makeTransient(answers.get(0));
		Assert.assertEquals(answerDao.getRowCount(), 0);
	}

	@Test(groups = { "answerDao" }, dependsOnMethods = "removeDummyAnswer")
	public void storeAnswerInputField() throws InvalidAnswerFormatException {
		Answer answer = new Answer();
		answer.setTechnicalName(ANSWER_INPUT_FIELD);
		answer.setAnswerType(AnswerType.INPUT);
		answer.setAnswerFormat(AnswerFormat.TEXT);
		answerDao.makePersistent(answer);
		Assert.assertEquals(answerDao.getRowCount(), 1);
	}

	@Test(groups = { "answerDao" }, dependsOnMethods = "storeAnswerInputField")
	public void getAnswerInputField() {
		List<Answer> answers = answerDao.getAll();
		Assert.assertEquals(answers.get(0).getTechnicalName(), ANSWER_INPUT_FIELD);
		Assert.assertEquals(answers.get(0).getAnswerType(), AnswerType.INPUT);
		Assert.assertEquals(answers.get(0).getAnswerFormat(), AnswerFormat.TEXT);
	}
	
	@Test(groups = { "answerDao" }, dependsOnMethods = "getAnswerInputField")
	public void removeAnswer() {
		List<Answer> answers = answerDao.getAll();
		answerDao.makeTransient(answers.get(0));
		Assert.assertEquals(answerDao.getRowCount(), 0);
	}
}

package com.biit.abcd.persistence.dao;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.Test;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
public class QuestionTest extends AbstractTransactionalTestNGSpringContextTests {
	private final static String DUMMY_QUESTION = "Dummy Question";

	@Autowired
	private IQuestionDao questionDao;
	
	@Test(groups = { "questionDao" })
	public void testEmptyDatabase() {
		// Read
		// Assert.assertEquals(questionDao.getRowCount(), 0);
	}
}

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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
public class FormTest extends AbstractTransactionalTestNGSpringContextTests {
	private final static String DUMMY_FORM = "Dummy Form";

	@Autowired
	private IFormDao formDao;

	@Test(groups = { "formDao" })
	public void testEmptyDatabase() {
		// Read
		Assert.assertEquals(formDao.getRowCount(), 0);
	}

	@Test(groups = { "formDao" }, dependsOnMethods = "testEmptyDatabase")
	public void storeDummyForm() {
		Form form = new Form();
		form.setName(DUMMY_FORM);
		formDao.makePersistent(form);
		Assert.assertEquals(formDao.getRowCount(), 1);
	}

	@Test(groups = { "formDao" }, dependsOnMethods = "storeDummyForm")
	public void getDummyForm() {
		List<Form> forms = formDao.getAll();
		Assert.assertEquals(forms.get(0).getName(), DUMMY_FORM);
	}
}

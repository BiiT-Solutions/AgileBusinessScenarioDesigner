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
import com.biit.abcd.persistence.entity.FormCustomVariables;
import com.biit.abcd.persistence.entity.exceptions.NotValidFormException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
public class FormCustomVariablesTest extends AbstractTransactionalTestNGSpringContextTests {
	private final static String DUMMY_FORM = "Form with custom variables";

	@Autowired
	private IFormCustomVariablesDao formCustomVariablesDao;

	@Autowired
	private IFormDao formDao;

	private Form form;

	@Test(groups = { "formCustomVariablesDao" })
	public void testEmptyDatabase() {
		// Read
		Assert.assertEquals(formCustomVariablesDao.getRowCount(), 0);
	}

	@Test(groups = { "formCustomVariablesDao" }, dependsOnMethods = "testEmptyDatabase")
	public void storeDummyVariables() throws NotValidFormException {
		form = new Form();
		form.setName(DUMMY_FORM);
		formDao.makePersistent(form);

		FormCustomVariables formCustomVariables = new FormCustomVariables(form);
		formCustomVariablesDao.makePersistent(formCustomVariables);

		Assert.assertEquals(formCustomVariablesDao.getRowCount(), 1);
	}

	@Test(groups = { "formCustomVariablesDao" }, dependsOnMethods = "storeDummyVariables")
	public void getDummyVariables() {
		List<FormCustomVariables> customVariables = formCustomVariablesDao.getAll();
		Assert.assertEquals(customVariables.get(0).getForm().getName(), DUMMY_FORM);
	}

	@Test(groups = { "formCustomVariablesDao" }, dependsOnMethods = { "getDummyVariables" })
	public void removeDummyVariables() {
		List<FormCustomVariables> customVariables = formCustomVariablesDao.getAll();
		for (FormCustomVariables formCustomVariables : customVariables) {
			formCustomVariablesDao.makeTransient(formCustomVariables);
		}
		Assert.assertEquals(formCustomVariablesDao.getRowCount(), 0);

		List<Form> forms = formDao.getAll();
		for (Form form : forms) {
			formDao.makeTransient(form);
		}
		Assert.assertEquals(formDao.getRowCount(), 0);
	}

}

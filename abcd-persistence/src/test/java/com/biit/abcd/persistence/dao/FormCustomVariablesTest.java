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
		Assert.assertNotNull(formCustomVariablesDao.getFormCustomVariables(form));
	}

	@Test(groups = { "formCustomVariablesDao" }, dependsOnMethods = "storeDummyVariables")
	public void storeIntegerVariables() {
		form = new Form();
		form.setName(DUMMY_FORM + "_v2");
		formDao.makePersistent(form);

		FormCustomVariables formCustomVariables = new FormCustomVariables(form);
		formCustomVariables.addCustomIntegerVariable("Score", Category.class);

		formCustomVariablesDao.makePersistent(formCustomVariables);
		FormCustomVariables formCustomVariablesInDB = formCustomVariablesDao.getFormCustomVariables(form);
		Assert.assertNotNull(formCustomVariablesInDB);
		Assert.assertEquals(formCustomVariablesInDB.getCustomIntegerVariables(Category.class).get(0), "Score");
	}

	@Test(groups = { "formCustomVariablesDao" }, dependsOnMethods = "storeDummyVariables")
	public void storeStringVariables() {
		form = new Form();
		form.setName(DUMMY_FORM + "_v3");
		formDao.makePersistent(form);

		FormCustomVariables formCustomVariables = new FormCustomVariables(form);
		formCustomVariables.addCustomStringVariable("Name", Category.class);

		formCustomVariablesDao.makePersistent(formCustomVariables);
		FormCustomVariables formCustomVariablesInDB = formCustomVariablesDao.getFormCustomVariables(form);
		Assert.assertNotNull(formCustomVariablesInDB);
		Assert.assertEquals(formCustomVariablesInDB.getCustomStringVariables(Category.class).get(0), "Name");
	}

	@Test(groups = { "formCustomVariablesDao" }, dependsOnMethods = "storeDummyVariables")
	public void storeDateVariables() {
		form = new Form();
		form.setName(DUMMY_FORM + "_v4");
		formDao.makePersistent(form);

		FormCustomVariables formCustomVariables = new FormCustomVariables(form);
		formCustomVariables.addCustomDateVariable("CreationDate", Category.class);

		formCustomVariablesDao.makePersistent(formCustomVariables);
		FormCustomVariables formCustomVariablesInDB = formCustomVariablesDao.getFormCustomVariables(form);
		Assert.assertNotNull(formCustomVariablesInDB);
		Assert.assertEquals(formCustomVariablesInDB.getCustomDateVariables(Category.class).get(0), "CreationDate");
	}

	@Test(groups = { "formCustomVariablesDao" }, dependsOnMethods = { "getDummyVariables", "storeIntegerVariables",
			"storeStringVariables", "storeDateVariables" })
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

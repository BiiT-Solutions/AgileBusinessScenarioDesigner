package com.biit.abcd.persistence.dao;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.CustomVariableScope;
import com.biit.abcd.persistence.entity.CustomVariableType;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.exceptions.NotValidFormException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
public class FormCustomVariablesTest extends AbstractTransactionalTestNGSpringContextTests {
	private final static String DUMMY_FORM = "Form with custom variables";

	@Autowired
	private IFormCustomVariablesDao formCustomVariablesDao;

	@Autowired
	private IFormDao formDao;

	private Form basicForm;

	@Test(groups = { "formCustomVariablesDao" })
	public void testEmptyDatabase() {
		// Read
		Assert.assertEquals(formCustomVariablesDao.getRowCount(), 0);
	}

	@Test(groups = { "formCustomVariablesDao" }, dependsOnMethods = "testEmptyDatabase")
	public void storeDummyVariables() throws NotValidFormException {
		basicForm = new Form();
		basicForm.setName(DUMMY_FORM);

		CustomVariable formCustomVariables = new CustomVariable();
		basicForm.getCustomVariables().add(formCustomVariables);
		formDao.makePersistent(basicForm);

		Assert.assertEquals(formCustomVariablesDao.getRowCount(), 1);
	}

	@Test(groups = { "formCustomVariablesDao" }, dependsOnMethods = "storeDummyVariables")
	public void storeIntegerVariables() {
		Form form = new Form();
		form.setName(DUMMY_FORM + "_v2");

		CustomVariable formCustomVariables = new CustomVariable("Score", CustomVariableType.NUMBER,
				CustomVariableScope.CATEGORY);
		form.getCustomVariables().add(formCustomVariables);

		formDao.makePersistent(form);

		Form inForm = formDao.getForm(DUMMY_FORM + "_v2");
		inForm.getCustomVariables();
		Assert.assertNotNull(inForm.getCustomVariables());
		Assert.assertFalse(inForm.getCustomVariables().isEmpty());
		Assert.assertEquals(inForm.getCustomVariables().get(0).getName(), "Score");
		Assert.assertEquals(inForm.getCustomVariables().get(0).getType(), CustomVariableType.NUMBER);
		Assert.assertEquals(inForm.getCustomVariables().get(0).getScope(), CustomVariableScope.CATEGORY);
	}

	@Test(groups = { "formCustomVariablesDao" }, dependsOnMethods = "storeDummyVariables")
	public void storeStringVariables() {
		Form form = new Form();
		form.setName(DUMMY_FORM + "_v3");

		CustomVariable formCustomVariables = new CustomVariable("Name", CustomVariableType.STRING,
				CustomVariableScope.QUESTION);
		form.getCustomVariables().add(formCustomVariables);

		formDao.makePersistent(form);

		Form inForm = formDao.getForm(DUMMY_FORM + "_v3");
		inForm.getCustomVariables();
		Assert.assertNotNull(inForm.getCustomVariables());
		Assert.assertFalse(inForm.getCustomVariables().isEmpty());
		Assert.assertEquals(inForm.getCustomVariables().get(0).getName(), "Name");
		Assert.assertEquals(inForm.getCustomVariables().get(0).getType(), CustomVariableType.STRING);
		Assert.assertEquals(inForm.getCustomVariables().get(0).getScope(), CustomVariableScope.QUESTION);
	}

	@Test(groups = { "formCustomVariablesDao" }, dependsOnMethods = "storeDummyVariables")
	public void storeDateVariables() {
		Form form = new Form();
		form.setName(DUMMY_FORM + "_v4");
		formDao.makePersistent(form);

		CustomVariable formCustomVariables = new CustomVariable("CreationDate", CustomVariableType.DATE,
				CustomVariableScope.FORM);
		form.getCustomVariables().add(formCustomVariables);

		formDao.makePersistent(form);

		Form inForm = formDao.getForm(DUMMY_FORM + "_v4");
		inForm.getCustomVariables();
		Assert.assertNotNull(inForm.getCustomVariables());
		Assert.assertFalse(inForm.getCustomVariables().isEmpty());
		Assert.assertEquals(inForm.getCustomVariables().get(0).getName(), "CreationDate");
		Assert.assertEquals(inForm.getCustomVariables().get(0).getType(), CustomVariableType.DATE);
		Assert.assertEquals(inForm.getCustomVariables().get(0).getScope(), CustomVariableScope.FORM);
	}

	@Test(groups = { "formCustomVariablesDao" }, dependsOnMethods = { "storeIntegerVariables", "storeStringVariables",
			"storeDateVariables" })
	public void removeDummyVariables() {
		formDao.removeAll();
		Assert.assertEquals(formDao.getRowCount(), 0);
		Assert.assertEquals(formCustomVariablesDao.getRowCount(), 0);
	}

}

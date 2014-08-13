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
import com.biit.form.exceptions.FieldTooLongException;
import com.biit.form.exceptions.NotValidFormException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
@Test(groups = { "formCustomVariablesDao" })
public class FormCustomVariablesTest extends AbstractTransactionalTestNGSpringContextTests {
	private final static String DUMMY_FORM = "Form with custom variables";

	@Autowired
	private IFormCustomVariablesDao formCustomVariablesDao;

	@Autowired
	private IFormDao formDao;

	@Test
	public void storeDummyVariables() throws NotValidFormException, FieldTooLongException {
		Form basicForm = new Form();
		basicForm.setName(DUMMY_FORM);

		CustomVariable formCustomVariables = new CustomVariable();
		basicForm.getCustomVariables().add(formCustomVariables);
		formDao.makePersistent(basicForm);

		Assert.assertEquals(formCustomVariablesDao.getRowCount(), 1);
		formDao.makeTransient(basicForm);
		Assert.assertEquals(formCustomVariablesDao.getRowCount(), 0);
	}

	@Test
	public void storeIntegerVariables() throws FieldTooLongException {
		Form form = new Form();
		form.setName(DUMMY_FORM + "_v2");

		CustomVariable formCustomVariables = new CustomVariable(form, "Score", CustomVariableType.NUMBER,
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
		formDao.makeTransient(form);
	}

	@Test
	public void storeStringVariables() throws FieldTooLongException {
		Form form = new Form();
		form.setName(DUMMY_FORM + "_v3");

		CustomVariable formCustomVariables = new CustomVariable(form, "Name", CustomVariableType.STRING,
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
		formDao.makeTransient(form);
	}

	@Test
	public void storeDateVariables() throws FieldTooLongException {
		Form form = new Form();
		form.setName(DUMMY_FORM + "_v4");
		formDao.makePersistent(form);

		CustomVariable formCustomVariables = new CustomVariable(form, "CreationDate", CustomVariableType.DATE,
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
		formDao.makeTransient(form);
	}

}

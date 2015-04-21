package com.biit.abcd.persistence.dao;

import java.util.Iterator;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.CustomVariableScope;
import com.biit.abcd.persistence.entity.CustomVariableType;
import com.biit.abcd.persistence.entity.Form;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.NotValidFormException;
import com.biit.persistence.dao.exceptions.ElementCannotBePersistedException;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.persistence.entity.exceptions.ElementCannotBeRemovedException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
@Test(groups = { "formCustomVariablesDao" })
@TransactionConfiguration(defaultRollback = false)
public class FormCustomVariablesTest extends AbstractTransactionalTestNGSpringContextTests {
	private final static String DUMMY_FORM = "Form with custom variables";

	@Autowired
	private ICustomVariableDao customVariablesDao;

	@Autowired
	private IFormDao formDao;

//	@Test
//	public void storeDummyVariables() throws NotValidFormException, FieldTooLongException,
//			CharacterNotAllowedException, UnexpectedDatabaseException, ElementCannotBePersistedException,
//			ElementCannotBeRemovedException {
//		Form form = new Form();
//		form.setOrganizationId(0l);
//		form.setLabel(DUMMY_FORM);
//
//		CustomVariable formCustomVariables = new CustomVariable();
//		form.add(formCustomVariables);
//		formDao.makePersistent(form);
//
//		Assert.assertEquals(customVariablesDao.getRowCount(), 1);
//		formDao.makeTransient(form);
//		Assert.assertEquals(customVariablesDao.getRowCount(), 0);
//	}
//
//	@Test
//	public void storeIntegerVariables() throws FieldTooLongException, CharacterNotAllowedException,
//			UnexpectedDatabaseException, ElementCannotBePersistedException, ElementCannotBeRemovedException {
//		Form form = new Form();
//		form.setOrganizationId(0l);
//		form.setLabel(DUMMY_FORM + "_v2");
//
//		CustomVariable formCustomVariables = new CustomVariable(form, "Score", CustomVariableType.NUMBER,
//				CustomVariableScope.CATEGORY);
//		form.add(formCustomVariables);
//
//		formDao.makePersistent(form);
//
//		Form inForm = formDao.get(form.getId());
//		inForm.getCustomVariables();
//		Assert.assertNotNull(inForm.getCustomVariables());
//		Assert.assertFalse(inForm.getCustomVariables().isEmpty());
//
//		Iterator<CustomVariable> iterator = inForm.getCustomVariables().iterator();
//		Assert.assertTrue(iterator.hasNext());
//		CustomVariable retrievedVariable = iterator.next();
//		Assert.assertEquals(retrievedVariable.getName(), "Score");
//		Assert.assertEquals(retrievedVariable.getType(), CustomVariableType.NUMBER);
//		Assert.assertEquals(retrievedVariable.getScope(), CustomVariableScope.CATEGORY);
//		formDao.makeTransient(form);
//	}
//
//	@Test
//	public void storeStringVariables() throws FieldTooLongException, CharacterNotAllowedException,
//			UnexpectedDatabaseException, ElementCannotBePersistedException, ElementCannotBeRemovedException {
//		Form form = new Form();
//		form.setOrganizationId(0l);
//		form.setLabel(DUMMY_FORM + "_v3");
//
//		CustomVariable formCustomVariables = new CustomVariable(form, "Name", CustomVariableType.STRING,
//				CustomVariableScope.QUESTION);
//		form.add(formCustomVariables);
//
//		formDao.makePersistent(form);
//
//		Form inForm = formDao.get(form.getId());
//		inForm.getCustomVariables();
//		Assert.assertNotNull(inForm.getCustomVariables());
//		Assert.assertFalse(inForm.getCustomVariables().isEmpty());
//
//		Iterator<CustomVariable> iterator = inForm.getCustomVariables().iterator();
//		Assert.assertTrue(iterator.hasNext());
//		CustomVariable retrievedVariable = iterator.next();
//
//		Assert.assertEquals(retrievedVariable.getName(), "Name");
//		Assert.assertEquals(retrievedVariable.getType(), CustomVariableType.STRING);
//		Assert.assertEquals(retrievedVariable.getScope(), CustomVariableScope.QUESTION);
//		formDao.makeTransient(form);
//	}
//
//	@Test
//	public void storeDateVariables() throws FieldTooLongException, CharacterNotAllowedException,
//			UnexpectedDatabaseException, ElementCannotBePersistedException, ElementCannotBeRemovedException {
//		Form form = new Form();
//		form.setOrganizationId(0l);
//		form.setLabel(DUMMY_FORM + "_v4");
//		formDao.makePersistent(form);
//
//		CustomVariable formCustomVariables = new CustomVariable(form, "CreationDate", CustomVariableType.DATE,
//				CustomVariableScope.FORM);
//		
//		form.add(formCustomVariables);
//
//		formDao.makePersistent(form);
//
//		Form inForm = formDao.get(form.getId());
//		inForm.getCustomVariables();
//		Assert.assertNotNull(inForm.getCustomVariables());
//		Assert.assertFalse(inForm.getCustomVariables().isEmpty());
//
//		Iterator<CustomVariable> iterator = inForm.getCustomVariables().iterator();
//		Assert.assertTrue(iterator.hasNext());
//		CustomVariable retrievedVariable = iterator.next();
//
//		Assert.assertEquals(retrievedVariable.getName(), "CreationDate");
//		Assert.assertEquals(retrievedVariable.getType(), CustomVariableType.DATE);
//		Assert.assertEquals(retrievedVariable.getScope(), CustomVariableScope.FORM);
//		formDao.makeTransient(form);
//	}
//	
//	/**
//	 * Check Orphan removal.
//	 * 
//	 * @throws FieldTooLongException
//	 * @throws UnexpectedDatabaseException
//	 * @throws ElementCannotBePersistedException
//	 * @throws ElementCannotBeRemovedException
//	 */
//	@Test
//	public void variableOrphan() throws FieldTooLongException, UnexpectedDatabaseException,
//		ElementCannotBePersistedException, ElementCannotBeRemovedException {
//		Form form = new Form();
//		form.setOrganizationId(0l);
//		form.setLabel(DUMMY_FORM + "_v5");
//		formDao.makePersistent(form);
//		
//		int previousVariables = customVariablesDao.getRowCount();
//		
//		CustomVariable formCustomVariables = new CustomVariable(form, "var1", CustomVariableType.DATE,
//				CustomVariableScope.FORM);
//		form.add(formCustomVariables);
//		
//		CustomVariable formCustomVariables2 = new CustomVariable(form, "var2", CustomVariableType.DATE,
//				CustomVariableScope.FORM);
//		form.add(formCustomVariables2);
//		
//		formDao.makePersistent(form);
//		Assert.assertEquals(customVariablesDao.getRowCount(), previousVariables + 2);
//
//		formDao.makeTransient(form);
//		
//		Assert.assertEquals(customVariablesDao.getRowCount(), previousVariables);
//		
//	}

	/**
	 * Removes a variable and other changes the name to the same that the previous one.
	 * 
	 * @throws FieldTooLongException
	 * @throws UnexpectedDatabaseException
	 * @throws ElementCannotBePersistedException
	 * @throws ElementCannotBeRemovedException
	 */
	@Test
	public void variableReplaced() throws FieldTooLongException, UnexpectedDatabaseException,
			ElementCannotBePersistedException, ElementCannotBeRemovedException {
		Form form = new Form();
		form.setOrganizationId(0l);
		form.setLabel(DUMMY_FORM + "_v5");
		formDao.makePersistent(form);

		int previousVariables = customVariablesDao.getRowCount();

		CustomVariable formCustomVariables = new CustomVariable(form, "var1", CustomVariableType.DATE,
				CustomVariableScope.FORM);
		form.add(formCustomVariables);

		CustomVariable formCustomVariables2 = new CustomVariable(form, "var2", CustomVariableType.DATE,
				CustomVariableScope.FORM);
		form.add(formCustomVariables2);

		formDao.makePersistent(form);

		formCustomVariables.remove();
		formCustomVariables2.setName("var1");

		formDao.makePersistent(form);

		Assert.assertEquals(customVariablesDao.getRowCount(), previousVariables + 1);
		formDao.makeTransient(form);
	}

}

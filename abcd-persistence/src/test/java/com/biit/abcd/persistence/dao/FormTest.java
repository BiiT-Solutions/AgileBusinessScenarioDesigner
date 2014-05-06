package com.biit.abcd.persistence.dao;

import java.util.ArrayList;
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
import com.biit.abcd.persistence.entity.TreeObject;
import com.biit.abcd.persistence.entity.exceptions.NotValidChildException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
public class FormTest extends AbstractTransactionalTestNGSpringContextTests {
	private final static String DUMMY_FORM = "Dummy Form";
	private final static String FULL_FORM = "Complete Form";

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

	@Test(groups = { "answerDao" }, dependsOnMethods = "getDummyForm")
	public void removeDummyForm() {
		List<Form> forms = formDao.getAll();
		formDao.makeTransient(forms.get(0));
		Assert.assertEquals(formDao.getRowCount(), 0);
	}

	@Test(groups = { "formDao" }, dependsOnMethods = "removeDummyForm")
	public void storeFormWithCategory() throws NotValidChildException {
		Form form = new Form();
		form.setName(FULL_FORM);
		Category category = new Category();
		form.addChild(category);
		System.out.println("Form childs : " + form.getChildren().size());
		formDao.makePersistent(form);
		Form retrievedForm = formDao.read(form.getId());

		Assert.assertEquals(retrievedForm.getId(), form.getId());
		Assert.assertEquals(retrievedForm.getChildren().size(), 1);
	}
	
	@Test(groups = { "answerDao" }, dependsOnMethods = "storeFormWithCategory")
	public void removeForm() {
		List<Form> forms = formDao.getAll();
		formDao.makeTransient(forms.get(0));
		Assert.assertEquals(formDao.getRowCount(), 0);
	}
}

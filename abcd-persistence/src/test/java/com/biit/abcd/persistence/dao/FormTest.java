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
import com.biit.abcd.persistence.entity.exceptions.NotValidChildException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
public class FormTest extends AbstractTransactionalTestNGSpringContextTests {
	private final static String DUMMY_FORM = "Dummy Form";
	private final static String FULL_FORM = "Complete Form";
	private final static String OTHER_FORM = "Other Form";
	private final static String CATEGORY_LABEL = "Category1";

	@Autowired
	private IFormDao formDao;

	private Form form;

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

	@Test(groups = { "formDao" }, dependsOnMethods = "getDummyForm")
	public void removeDummyForm() {
		List<Form> forms = formDao.getAll();
		formDao.makeTransient(forms.get(0));
		Assert.assertEquals(formDao.getRowCount(), 0);
	}

	@Test(groups = { "formDao" }, dependsOnMethods = "removeDummyForm")
	public void storeFormWithCategory() throws NotValidChildException {
		form = new Form();
		form.setName(FULL_FORM);
		Category category = new Category();
		category.setLabel(CATEGORY_LABEL);
		form.addChild(category);
		formDao.makePersistent(form);
		Form retrievedForm = formDao.read(form.getId());

		Assert.assertEquals(retrievedForm.getId(), form.getId());
		Assert.assertEquals(retrievedForm.getChildren().size(), 1);
	}

//	@Test(groups = { "formDao" }, dependsOnMethods = "storeFormWithCategory", expectedExceptions = { ConstraintViolationException.class })
//	public void addNewCategoryWithSameName() throws NotValidChildException, ConstraintViolationException {
//		Category category = new Category();
//		category.setLabel(CATEGORY_LABEL);
//		form.addChild(category);
//		formDao.makePersistent(form);
//	}
//
//	@Test(groups = { "formDao" }, dependsOnMethods = "addNewCategoryWithSameName")
//	public void removeRepeatedCategoryWithSameName() throws NotValidChildException, ConstraintViolationException,
//			ChildrenNotFoundException {
//		form.removeChild(0);
//		formDao.makePersistent(form);
//	}

	@Test(groups = { "formDao" }, dependsOnMethods = "storeFormWithCategory")
	public void increaseVersion() {
		Form oldForm = formDao.read(form.getId());
		form.increaseVersion();
		Assert.assertEquals(oldForm.getVersion() + 1, (int) form.getVersion());
		formDao.makePersistent(form);
		Assert.assertEquals(formDao.getLastVersion(oldForm), 2);
		Assert.assertEquals(formDao.getLastVersion(oldForm), formDao.getLastVersion(form));
	}

	@Test(groups = { "formDao" }, dependsOnMethods = "increaseVersion")
	public void storeOtherFormWithSameLabelCategory() throws NotValidChildException {
		Form form2 = new Form();
		form2.setName(OTHER_FORM);
		Category category = new Category();
		category.setLabel(CATEGORY_LABEL);
		form2.addChild(category);
		formDao.makePersistent(form2);
		Form retrievedForm = formDao.read(form2.getId());

		Assert.assertEquals(retrievedForm.getId(), form2.getId());
		Assert.assertEquals(retrievedForm.getChildren().size(), 1);
	}

	@Test(groups = { "formDao" }, dependsOnMethods = "storeOtherFormWithSameLabelCategory")
	public void removeForm() {
		List<Form> forms = formDao.getAll();
		for (Form form : forms) {
			formDao.makeTransient(form);
		}
		Assert.assertEquals(formDao.getRowCount(), 0);
	}

}

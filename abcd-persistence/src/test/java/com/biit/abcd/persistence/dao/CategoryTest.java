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
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.exceptions.NotValidChildException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
public class CategoryTest extends AbstractTransactionalTestNGSpringContextTests {
	private final static String DUMMY_CATEGORY = "Dummy Category";
	private final static String CATEGORY_WITH_GROUP = "Category With Group";

	@Autowired
	private ICategoryDao categoryDao;

	@Test(groups = { "categoryDao" })
	public void testEmptyDatabase() {
		// Read
		Assert.assertEquals(categoryDao.getRowCount(), 0);
	}

	@Test(groups = { "categoryDao" }, dependsOnMethods = "testEmptyDatabase")
	public void storeDummyCategory() {
		Category category = new Category();
		category.setName(DUMMY_CATEGORY);
		categoryDao.makePersistent(category);
		Assert.assertEquals(categoryDao.getRowCount(), 1);
	}

	@Test(groups = { "categoryDao" }, dependsOnMethods = "storeDummyCategory")
	public void getDummyCategory() {
		List<Category> categories = categoryDao.getAll();
		Assert.assertEquals(categories.get(0).getName(), DUMMY_CATEGORY);
	}

	@Test(groups = { "answerDao" }, dependsOnMethods = "getDummyCategory")
	public void removeDummyCategory() {
		List<Category> categories = categoryDao.getAll();
		categoryDao.makeTransient(categories.get(0));
		Assert.assertEquals(categoryDao.getRowCount(), 0);
	}

	@Test(groups = { "categoryDao" }, dependsOnMethods = "removeDummyCategory")
	public void storeCategoryWithGroup() throws NotValidChildException {
		Category category = new Category();
		category.setName(CATEGORY_WITH_GROUP);
		Group group = new Group();
		category.addChild(group);

		categoryDao.makePersistent(category);
		Category retrievedCategory = categoryDao.read(category.getId());

		Assert.assertEquals(retrievedCategory.getId(), category.getId());
		Assert.assertEquals(retrievedCategory.getChildren().size(), 1);
	}
	
	@Test(groups = { "answerDao" }, dependsOnMethods = "storeCategoryWithGroup")
	public void removeCategory() {
		List<Category> categories = categoryDao.getAll();
		categoryDao.makeTransient(categories.get(0));
		Assert.assertEquals(categoryDao.getRowCount(), 0);
	}

}

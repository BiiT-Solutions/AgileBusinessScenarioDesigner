package com.biit.abcd.persistence.dao;

import java.util.List;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.exceptions.FieldTooLongException;
import com.biit.abcd.persistence.entity.exceptions.NotValidChildException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
public class GroupTest extends AbstractTransactionalTestNGSpringContextTests {
	private final static String DUMMY_GROUP = "Dummy Group";
	private final static String GROUP_WITH_QUESTIONS = "Group With Questions";

	@Autowired
	private IGroupDao groupDao;

	@Test(groups = { "groupDao" })
	public void testEmptyDatabase() {
		// Read
		Assert.assertEquals(groupDao.getRowCount(), 0);
	}

	@Test(groups = { "groupDao" }, dependsOnMethods = "testEmptyDatabase")
	public void storeDummyGroup() throws FieldTooLongException {
		Group group = new Group();
		group.setName(DUMMY_GROUP);
		groupDao.makePersistent(group);
		Assert.assertEquals(groupDao.getRowCount(), 1);
	}

	@Test(groups = { "groupDao" }, dependsOnMethods = "storeDummyGroup")
	public void getDummyGroup() {
		List<Group> groups = groupDao.getAll();
		Assert.assertEquals(groups.get(0).getName(), DUMMY_GROUP);
	}

	@Test(groups = { "groupDao" }, dependsOnMethods = "testEmptyDatabase")
	public void storeGroupWithQuestion() throws NotValidChildException, FieldTooLongException {
		Group group = new Group();
		group.setName(GROUP_WITH_QUESTIONS);
		Question question = new Question();
		group.addChild(question);

		groupDao.makePersistent(group);
		Group retrievedGroup = groupDao.read(group.getId());

		Assert.assertEquals(retrievedGroup.getId(), group.getId());
		Assert.assertEquals(retrievedGroup.getChildren().size(), 1);
	}

	@Test(groups = { "groupDao" }, dependsOnMethods = "storeGroupWithQuestion")
	public void getGroup() {
		List<Group> groups = groupDao.getAll();
		Assert.assertEquals(groups.get(0).getName(), DUMMY_GROUP);
	}

}

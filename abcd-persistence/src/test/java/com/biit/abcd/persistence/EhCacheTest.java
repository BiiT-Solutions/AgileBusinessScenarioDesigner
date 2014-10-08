package com.biit.abcd.persistence;

import org.hibernate.stat.EntityStatistics;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.persistence.dao.IFormDao;
import com.biit.abcd.persistence.entity.Form;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
@Test(groups = { "ehCache" })
@DirtiesContext
public class EhCacheTest extends AbstractTransactionalTestNGSpringContextTests {
	private final static String DUMMY_FORM = "Dummy Form with cache";

	@Autowired
	private IFormDao formDao;

	@Test
	public void testSecondLevelCache() throws FieldTooLongException {

		formDao.getSessionFactory().getStatistics().clear();

		Form form = new Form();
		form.setOrganizationId(0l);
		form.setLabel(DUMMY_FORM);
		formDao.makePersistent(form);

		Assert.assertEquals(formDao.getSessionFactory().getStatistics().getEntityFetchCount(), 0);
		Assert.assertEquals(formDao.getSessionFactory().getStatistics().getSecondLevelCacheMissCount(), 0);
		Assert.assertEquals(formDao.getSessionFactory().getStatistics().getSecondLevelCacheHitCount(), 0);

		// fetch the form entity from database first time
		form = formDao.getForm(DUMMY_FORM, 0l);
		Assert.assertNotNull(form);

		EntityStatistics entityStats = formDao.getSessionFactory().getStatistics()
				.getEntityStatistics(Form.class.getName());
		Assert.assertEquals(entityStats.getLoadCount(), 1);
		Assert.assertEquals(entityStats.getFetchCount(), 0);

		Assert.assertEquals(formDao.getSessionFactory().getStatistics().getEntityFetchCount(), 0);
		Assert.assertTrue(formDao.getSessionFactory().getStatistics().getSecondLevelCacheMissCount() > 0);
		Assert.assertEquals(formDao.getSessionFactory().getStatistics().getSecondLevelCacheHitCount(), 0);

		// Here entity is already in second level cache (session has been closed) so no database query will be hit
		form = formDao.getForm(DUMMY_FORM, 0l);
		Assert.assertNotNull(form);

		Assert.assertEquals(formDao.getSessionFactory().getStatistics().getEntityFetchCount(), 0);
		Assert.assertTrue(formDao.getSessionFactory().getStatistics().getSecondLevelCacheHitCount() > 0);

		// Removed forms also are removed from cache.
		long id = form.getId();
		formDao.makeTransient(form);
		form = formDao.read(id);
		Assert.assertNull(form);

	}
}

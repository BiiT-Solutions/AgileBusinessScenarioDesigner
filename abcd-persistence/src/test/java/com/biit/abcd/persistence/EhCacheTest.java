package com.biit.abcd.persistence;

import javax.persistence.Cache;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Test;

import com.biit.abcd.persistence.dao.IFormDao;
import com.biit.abcd.persistence.entity.Form;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
@Test(groups = { "ehCache" })
public class EhCacheTest extends AbstractTransactionalTestNGSpringContextTests {
	private final static String DUMMY_FORM = "Dummy Form with cache";

	@Autowired
	private IFormDao formDao;

	private Long formId;

	@BeforeGroups(value = { "ehCache" })
	@Rollback(value = false)
	@Transactional(value = TxType.NEVER)
	private void createForm() throws FieldTooLongException, NotValidChildException, CharacterNotAllowedException,
			InvalidAnswerFormatException, ElementIsReadOnly {
		Form form = FormUtils.createCompleteForm();
		form.setOrganizationId(0l);
		form.setLabel(DUMMY_FORM);
		formDao.makePersistent(form);
		formId = form.getId();
	}

	@Test
	public void formIsInCache() throws FieldTooLongException, NotValidChildException, CharacterNotAllowedException,
			InvalidAnswerFormatException, ElementIsReadOnly {
		formDao.get(formId);
		Cache cache = formDao.getEntityManager().getEntityManagerFactory().getCache();
		Assert.assertFalse(cache.contains(Form.class, formId));
		formDao.get(formId);
		Assert.assertTrue(cache.contains(Form.class, formId));
	}
}

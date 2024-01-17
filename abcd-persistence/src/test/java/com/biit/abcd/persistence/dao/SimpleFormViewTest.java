package com.biit.abcd.persistence.dao;

import java.util.List;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.SimpleFormView;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.persistence.dao.exceptions.ElementCannotBePersistedException;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.persistence.entity.exceptions.ElementCannotBeRemovedException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
@Test(groups = { "simpleFormViewDao" })
public class SimpleFormViewTest extends AbstractTransactionalTestNGSpringContextTests {
	private final static String DUMMY_FORM = "Dummy Form View";

	@Autowired
	private IFormDao formDao;

	@Autowired
	private ISimpleFormViewDao simpleFormViewDao;

	@Test
	public void getView() throws FieldTooLongException, UnexpectedDatabaseException,
			ElementCannotBeRemovedException {
		Form form = new Form();
		form.setOrganizationId(0l);
		form.setLabel(DUMMY_FORM);
		form.setCreatedBy(1l);
		form.setUpdatedBy(1l);
		formDao.makePersistent(form);
		Assert.assertEquals(formDao.getRowCount(), 1);

		Assert.assertEquals(simpleFormViewDao.getRowCount(), 1);
		List<SimpleFormView> views = simpleFormViewDao.getAll();
		Assert.assertEquals(views.size(), 1);
		Assert.assertEquals(views.get(0).getLabel(), DUMMY_FORM);
		formDao.makeTransient(form);
	}
}

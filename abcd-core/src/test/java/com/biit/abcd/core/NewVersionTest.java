package com.biit.abcd.core;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.persistence.dao.IFormDao;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.utils.FormVersionComparator;
import com.biit.abcd.persistence.utils.Exceptions.BiitTextNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.CustomVariableNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.DiagramNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.DiagramObjectNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.ExpressionNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.FormNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.GlobalVariableNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.GroupNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.NodeNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.PointNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.QuestionNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.RuleNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.SizeNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.StorableObjectNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.TableRuleNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.TreeObjectNotEqualsException;
import com.biit.abcd.persistence.utils.Exceptions.VariableDataNotEqualsException;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.dao.exceptions.ElementCannotBePersistedException;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.persistence.entity.exceptions.ElementCannotBeRemovedException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
@Test(groups = { "newVersion" })
public class NewVersionTest extends AbstractTransactionalTestNGSpringContextTests {

	@Autowired
	private IFormDao formDao;

	private Form form;
	private Form newVersionForm;

	@Test
	public void createForm() throws FieldTooLongException, NotValidChildException, CharacterNotAllowedException,
			InvalidAnswerFormatException, UnexpectedDatabaseException, ElementIsReadOnly,
			ElementCannotBePersistedException {
		form = FormUtils.createCompleteForm();
		Assert.assertNotNull(form);
		formDao.makePersistent(form);
	}

	@Test(dependsOnMethods = { "createForm" })
	public void newVersion() throws FieldTooLongException, NotValidChildException, CharacterNotAllowedException,
			InvalidAnswerFormatException, NotValidStorableObjectException, TreeObjectNotEqualsException,
			StorableObjectNotEqualsException, FormNotEqualsException, GroupNotEqualsException,
			QuestionNotEqualsException, CustomVariableNotEqualsException, ExpressionNotEqualsException,
			GlobalVariableNotEqualsException, VariableDataNotEqualsException, TableRuleNotEqualsException,
			RuleNotEqualsException, DiagramNotEqualsException, DiagramObjectNotEqualsException, NodeNotEqualsException,
			SizeNotEqualsException, PointNotEqualsException, BiitTextNotEqualsException, UnexpectedDatabaseException,
			ElementCannotBePersistedException, ElementCannotBeRemovedException {
		newVersionForm = form.createNewVersion(null);
		Assert.assertNotNull(newVersionForm);
		Assert.assertEquals((int) form.getVersion() + 1, (int) newVersionForm.getVersion());

		new FormVersionComparator(true).compare(form, newVersionForm);
		formDao.makePersistent(newVersionForm);

		formDao.makeTransient(form);
		formDao.makeTransient(newVersionForm);
	}

}

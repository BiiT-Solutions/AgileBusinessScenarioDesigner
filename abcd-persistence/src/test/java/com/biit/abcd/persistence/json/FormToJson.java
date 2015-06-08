package com.biit.abcd.persistence.json;

import java.util.List;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.persistence.dao.IFormDao;
import com.biit.abcd.persistence.dao.ISimpleFormViewDao;
import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.abcd.persistence.entity.AnswerType;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.SimpleFormView;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.InvalidAnswerFormatException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.dao.exceptions.ElementCannotBePersistedException;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.persistence.entity.exceptions.ElementCannotBeRemovedException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
public class FormToJson extends AbstractTransactionalTestNGSpringContextTests {

	private final static String FULL_FORM = "Complete Form";
	private final static String JSON_FORM_NAME = "\"label\": \"Complete Form\"";

	@Autowired
	private IFormDao formDao;

	@Autowired
	private ISimpleFormViewDao simpleFormViewDao;

	@Test(groups = { "abcdToJson" })
	public void createJsonForm() throws NotValidChildException, FieldTooLongException, CharacterNotAllowedException,
			UnexpectedDatabaseException, ElementCannotBePersistedException, ElementIsReadOnly,
			ElementCannotBeRemovedException, InvalidAnswerFormatException {
		String jsonForm = createForm(FULL_FORM).toJson();
		Assert.assertEquals(jsonForm.contains(JSON_FORM_NAME), true);
	}

	@Test(groups = { "abcdToJson" })
	public void createJsonSimpleForm() throws NotValidChildException, FieldTooLongException,
			CharacterNotAllowedException, UnexpectedDatabaseException, ElementCannotBePersistedException,
			ElementIsReadOnly, ElementCannotBeRemovedException, InvalidAnswerFormatException {

		for (int i = 0; i < 10; i++) {
			Form form = createForm(FULL_FORM + "_" + i);
			formDao.makePersistent(form);
		}

		List<SimpleFormView> views = simpleFormViewDao.getAll();
		for (int i = 0; i < views.size(); i++) {
			String jsonForm = views.get(i).toJson();
			String formName = JSON_FORM_NAME.substring(0, JSON_FORM_NAME.length() - 1);
			Assert.assertEquals(jsonForm.contains(formName + "_" + i + "\""), true);
		}
	}

	private Form createForm(String formName) throws NotValidChildException, FieldTooLongException,
			CharacterNotAllowedException, UnexpectedDatabaseException, ElementCannotBePersistedException,
			ElementIsReadOnly, ElementCannotBeRemovedException, InvalidAnswerFormatException {
		Form form = new Form();
		form.setOrganizationId(0l);
		form.setCreatedBy(1l);
		form.setUpdatedBy(1l);
		form.setLabel(formName);

		Category category = new Category();
		category.setName("Category1");
		form.addChild(category);

		Category category2 = new Category();
		category2.setName("Category2");
		form.addChild(category2);

		Category category3 = new Category();
		category3.setName("Category3");
		form.addChild(category3);

		Group group1 = new Group();
		group1.setName("Group1");
		category2.addChild(group1);

		Group group2 = new Group();
		group2.setName("Group2");
		category2.addChild(group2);

		Group group3 = new Group();
		group3.setName("Group3");
		group3.setRepeatable(true);
		category2.addChild(group3);

		Question question1 = new Question();
		question1.setName("Question1");
		question1.setAnswerType(AnswerType.MULTI_CHECKBOX);
		group2.addChild(question1);

		Question question2 = new Question();
		question2.setName("Question2");
		question2.setAnswerType(AnswerType.RADIO);
		group2.addChild(question2);

		Question question3 = new Question();
		question3.setName("Question3");
		question3.setAnswerType(AnswerType.INPUT);
		question3.setAnswerFormat(AnswerFormat.TEXT);
		group2.addChild(question3);

		Answer answer1 = new Answer();
		answer1.setName("Answer1");
		question2.addChild(answer1);

		Answer answer2 = new Answer();
		answer2.setName("Answer2");
		question2.addChild(answer2);

		Answer answer3 = new Answer();
		answer3.setName("Answer3");
		question2.addChild(answer3);
		return form;
	}
}

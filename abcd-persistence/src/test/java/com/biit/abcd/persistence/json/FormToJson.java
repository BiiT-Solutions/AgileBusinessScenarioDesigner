package com.biit.abcd.persistence.json;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testng.annotations.Test;

import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.dao.exceptions.ElementCannotBePersistedException;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.persistence.entity.exceptions.ElementCannotBeRemovedException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContextTest.xml" })
public class FormToJson {

	private final static String FULL_FORM = "Complete Form";
	
	@Test(groups = { "abcdToJson" })
	public void storeFormWithCategory() throws NotValidChildException, FieldTooLongException,
			CharacterNotAllowedException, UnexpectedDatabaseException, ElementCannotBePersistedException,
			ElementIsReadOnly, ElementCannotBeRemovedException {
		
		Form form = new Form();
		form.setOrganizationId(0l);
		form.setLabel(FULL_FORM);

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
		category2.addChild(group3);

		Question question1 = new Question();
		question1.setName("Question1");
		group2.addChild(question1);

		Question question2 = new Question();
		question2.setName("Question2");
		group2.addChild(question2);

		Question question3 = new Question();
		question3.setName("Question3");
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
		
		System.out.println(form.toJsonOnlyFormStructure());
		
	}
}

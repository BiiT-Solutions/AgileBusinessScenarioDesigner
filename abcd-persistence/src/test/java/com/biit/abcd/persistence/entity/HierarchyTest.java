package com.biit.abcd.persistence.entity;

import java.util.ArrayList;

import org.testng.annotations.Test;

import com.biit.abcd.persistence.entity.exceptions.NotValidChildException;
import com.biit.abcd.persistence.entity.exceptions.NotValidChildException;
import com.biit.abcd.persistence.entity.exceptions.NotValidParentException;

@Test
public class HierarchyTest {
	private Form form = new Form();
	private Category category = new Category();
	private Group group = new Group();
	private Question question = new Question();
	private Answer answer = new Answer();

	@Test(groups = { "hierarchyTest" }, expectedExceptions = NotValidParentException.class)
	public void formParentNotAllowed() throws NotValidParentException {
		form.setParent(form);
	}

	@Test(groups = { "hierarchyTest" }, expectedExceptions = NotValidChildException.class)
	public void formChildsNotAllowed() throws NotValidChildException {
		form.addChild(form);
	}

	@Test(groups = { "hierarchyTest" }, expectedExceptions = NotValidChildException.class)
	public void formChildsNotAllowed2() throws NotValidChildException {
		form.addChild(question);
	}

	@Test(groups = { "hierarchyTest" }, expectedExceptions = NotValidChildException.class)
	public void formChildsNotAllowed3() throws NotValidChildException {
		form.addChild(group);
	}

	@Test(groups = { "hierarchyTest" }, expectedExceptions = NotValidChildException.class)
	public void formChildsNotAllowed4() throws NotValidChildException {
		form.addChild(answer);
	}

	@Test(groups = { "hierarchyTest" }, expectedExceptions = NotValidParentException.class)
	public void categoryParentNotAllowed() throws NotValidParentException {
		category.setParent(group);
	}

	@Test(groups = { "hierarchyTest" })
	public void categoryParentAllowed() throws NotValidParentException {
		category.setParent(form);
	}

	@Test(groups = { "hierarchyTest" }, expectedExceptions = NotValidParentException.class)
	public void categoryParentNotAllowed2() throws NotValidParentException {
		category.setParent(category);
	}

	@Test(groups = { "hierarchyTest" }, expectedExceptions = NotValidChildException.class)
	public void categoryChildsNotAllowed() throws NotValidChildException {
		category.addChild(answer);
	}

	@Test(groups = { "hierarchyTest" }, expectedExceptions = NotValidChildException.class)
	public void categoryChildsNotAllowed2() throws NotValidChildException {
		category.addChild(category);
	}

	@Test(groups = { "hierarchyTest" }, expectedExceptions = NotValidChildException.class)
	public void categoryChildsArrayNotAllowed() throws NotValidChildException {
		ArrayList<ITreeObject> array = new ArrayList<ITreeObject>();
		array.add(answer);
		category.setChildren(array);
	}

	@Test(groups = { "hierarchyTest" })
	public void categoryChildsAllowed() throws NotValidChildException {
		category.addChild(group);
		category.addChild(question);
	}

	@Test(groups = { "hierarchyTest" })
	public void groupParentAllowed() throws NotValidParentException {
		group.setParent(category);
	}

	@Test(groups = { "hierarchyTest" }, expectedExceptions = NotValidParentException.class)
	public void groupParentNotAllowed() throws NotValidParentException {
		group.setParent(group);
	}

	@Test(groups = { "hierarchyTest" }, expectedExceptions = NotValidParentException.class)
	public void groupParentNotAllowed2() throws NotValidParentException {
		group.setParent(question);
	}

	@Test(groups = { "hierarchyTest" }, expectedExceptions = NotValidParentException.class)
	public void groupParentNotAllowed3() throws NotValidParentException {
		group.setParent(answer);
	}

	@Test(groups = { "hierarchyTest" }, expectedExceptions = NotValidChildException.class)
	public void groupChildsNotAllowed() throws NotValidChildException {
		group.addChild(answer);
	}

	@Test(groups = { "hierarchyTest" }, expectedExceptions = NotValidChildException.class)
	public void groupChildsNotAllowed2() throws NotValidChildException {
		group.addChild(category);
	}

	@Test(groups = { "hierarchyTest" })
	public void groupChildsAllowed() throws NotValidChildException {
		group.addChild(group);
		group.addChild(question);
	}

	@Test(groups = { "hierarchyTest" })
	public void questionParentAllowed() throws NotValidParentException {
		question.setParent(group);
	}

	@Test(groups = { "hierarchyTest" })
	public void questionParentAllowed2() throws NotValidParentException {
		question.setParent(category);
	}

	@Test(groups = { "hierarchyTest" }, expectedExceptions = NotValidParentException.class)
	public void questionParentNotAllowed() throws NotValidParentException {
		question.setParent(answer);
	}

	@Test(groups = { "hierarchyTest" }, expectedExceptions = NotValidChildException.class)
	public void questionChildsNotAllowed() throws NotValidChildException {
		question.addChild(category);
	}

	@Test(groups = { "hierarchyTest" }, expectedExceptions = NotValidChildException.class)
	public void questionChildsNotAllowed2() throws NotValidChildException {
		question.addChild(question);
	}

	@Test(groups = { "hierarchyTest" }, expectedExceptions = NotValidChildException.class)
	public void questionChildsNotAllowed3() throws NotValidChildException {
		question.addChild(group);
	}

	@Test(groups = { "hierarchyTest" })
	public void questionChildsAllowed() throws NotValidChildException {
		question.addChild(answer);
	}

	@Test(groups = { "hierarchyTest" })
	public void answerParentAllowed() throws NotValidParentException {
		answer.setParent(question);
	}

	@Test(groups = { "hierarchyTest" }, expectedExceptions = NotValidParentException.class)
	public void answerParentNotAllowed() throws NotValidParentException {
		answer.setParent(group);
	}

	@Test(groups = { "hierarchyTest" }, expectedExceptions = NotValidParentException.class)
	public void answerParentNotAllowed2() throws NotValidParentException {
		answer.setParent(category);
	}

	@Test(groups = { "hierarchyTest" }, expectedExceptions = NotValidChildException.class)
	public void answerChildsNotAllowed() throws NotValidChildException {
		answer.addChild(answer);
	}

	@Test(groups = { "hierarchyTest" }, expectedExceptions = NotValidChildException.class)
	public void answerChildsNotAllowed2() throws NotValidChildException {
		answer.addChild(question);
	}

	@Test(groups = { "hierarchyTest" }, expectedExceptions = NotValidChildException.class)
	public void answerChildsNotAllowed3() throws NotValidChildException {
		answer.addChild(group);
	}

	@Test(groups = { "hierarchyTest" }, expectedExceptions = NotValidChildException.class)
	public void answerChildsNotAllowed4() throws NotValidChildException {
		answer.addChild(category);
	}

}

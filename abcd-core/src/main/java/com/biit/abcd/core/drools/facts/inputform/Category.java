package com.biit.abcd.core.drools.facts.inputform;

import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.core.drools.facts.inputform.exceptions.GroupDoesNotExistException;
import com.biit.abcd.core.drools.facts.inputform.exceptions.QuestionDoesNotExistException;
import com.biit.abcd.core.drools.facts.interfaces.ICategory;
import com.biit.abcd.core.drools.facts.interfaces.IGroup;
import com.biit.abcd.core.drools.facts.interfaces.IQuestion;
import com.biit.abcd.core.drools.facts.interfaces.ISubmittedForm;

public class Category extends CommonAttributes implements ICategory {

	private List<IGroup> groups;
	private List<IQuestion> questions;
	private ISubmittedForm parent;

	public Category(String tag) {
		setTag(tag);
		setGroups(new ArrayList<IGroup>());
	}

	@Override
	public List<IGroup> getGroups() {
		return groups;
	}

	@Override
	public IGroup getGroup(String tag) throws GroupDoesNotExistException {
		for (IGroup group : getGroups()) {
			if (group.getTag().equals(tag)) {
				return group;
			}
		}
		throw new GroupDoesNotExistException("Group '" + tag + "' does not exists.");
	}

	public void setGroups(List<IGroup> groups) {
		this.groups = groups;
	}

	@Override
	public void addGroups(List<IGroup> groups) {
		if (this.groups == null) {
			setGroups(new ArrayList<IGroup>());
		}
		this.groups.addAll(groups);
	}

	public void addGroup(IGroup group) {
		if (groups == null) {
			setGroups(new ArrayList<IGroup>());
		}
		groups.add(group);
	}

	public void setQuestions(List<IQuestion> questions) {
		this.questions = questions;
	}

	@Override
	public void addQuestions(List<IQuestion> questions) {
		if (this.questions == null) {
			setQuestions(new ArrayList<IQuestion>());
		}
		this.questions.addAll(questions);
	}

	@Override
	public List<IQuestion> getQuestions() {
		return questions;
	}

	@Override
	public IQuestion getQuestion(String questionTag) throws QuestionDoesNotExistException {
		for (IQuestion question : getQuestions()) {
			if (question.getTag().equals(questionTag)) {
				return question;
			}
		}
		throw new QuestionDoesNotExistException("Question '" + questionTag + "' does not exists.");
	}

	public void setParent(ISubmittedForm form){
		this.parent = form;
	}

	public ISubmittedForm getParent(){
		return parent;
	}

	public boolean isScoreSet() {
		// Retrieve the form which will have the variables
		if(((SubmittedForm)getParent()).hasScoreSet(this)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isScoreNotSet() {
		return !isScoreSet();
	}
}

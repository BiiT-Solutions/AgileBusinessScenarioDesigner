package com.biit.abcd.core.drools.facts.inputform;

import java.util.ArrayList;
import java.util.List;

import com.biit.orbeon.form.ICategory;
import com.biit.orbeon.form.IGroup;
import com.biit.orbeon.form.IQuestion;
import com.biit.orbeon.form.ISubmittedForm;
import com.biit.orbeon.form.exceptions.GroupDoesNotExistException;
import com.biit.orbeon.form.exceptions.QuestionDoesNotExistException;

public class Category extends CommonAttributes implements ICategory {

	private List<IGroup> groups;
	private ISubmittedForm parent;
	private List<IQuestion> questions;

	public Category(String tag) {
		this.setTag(tag);
		this.setGroups(new ArrayList<IGroup>());
	}

	public void addGroup(IGroup group) {
		if (this.groups == null) {
			this.setGroups(new ArrayList<IGroup>());
		}
		((Group)group).setParent(this);
		this.groups.add(group);
	}

	@Override
	public void addGroups(List<IGroup> groups) {
		if (this.groups == null) {
			this.setGroups(new ArrayList<IGroup>());
		}
		for(IGroup group: groups) {
			this.addGroup(group);
		}
	}

	public void addQuestion(IQuestion question) {
		if (this.questions == null) {
			this.setQuestions(new ArrayList<IQuestion>());
		}
		((Question)question).setParent(this);
		this.questions.add(question);
	}

	@Override
	public void addQuestions(List<IQuestion> questions) {
		if (this.questions == null) {
			this.setQuestions(new ArrayList<IQuestion>());
		}
		for(IQuestion question: questions) {
			this.addQuestion(question);
		}
	}

	@Override
	public IGroup getGroup(String tag) throws GroupDoesNotExistException {
		for (IGroup group : this.getGroups()) {
			if (group.getTag().equals(tag)) {
				return group;
			}
		}
		throw new GroupDoesNotExistException("Group '" + tag + "' does not exists.");
	}

	@Override
	public List<IGroup> getGroups() {
		return this.groups;
	}

	public Number getNumberVariableValue(String varName){
		return ((SubmittedForm)this.getParent()).getNumberVariableValue(this, varName);
	}

	public ISubmittedForm getParent(){
		return this.parent;
	}

	@Override
	public IQuestion getQuestion(String questionTag) throws QuestionDoesNotExistException {
		for (IQuestion question : this.getQuestions()) {
			if (question.getTag().equals(questionTag)) {
				return question;
			}
		}
		throw new QuestionDoesNotExistException("Question '" + questionTag + "' does not exists.");
	}

	@Override
	public List<IQuestion> getQuestions() {
		return this.questions;
	}

	public Object getVariableValue(String varName){
		return ((SubmittedForm)this.getParent()).getVariableValue(this, varName);
	}

	public boolean isScoreNotSet(String varName) {
		return !this.isScoreSet(varName);
	}

	public boolean isScoreSet(String varName) {
		// Retrieve the form which will have the variables
		if(((SubmittedForm)this.getParent()).hasScoreSet(this, varName)) {
			return true;
		} else {
			return false;
		}
	}

	public void setGroups(List<IGroup> groups) {
		this.groups = groups;
	}

	public void setParent(ISubmittedForm form){
		this.parent = form;
	}

	public void setQuestions(List<IQuestion> questions) {
		this.questions = questions;
	}

	public void setVariableValue(String varName, Object value){
		((SubmittedForm)this.getParent()).setVariableValue(this, varName, value);
	}
}

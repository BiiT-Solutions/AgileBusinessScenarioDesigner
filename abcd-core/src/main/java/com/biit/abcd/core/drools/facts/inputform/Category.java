package com.biit.abcd.core.drools.facts.inputform;

import java.util.ArrayList;
import java.util.List;

import com.biit.orbeon.form.ICategory;
import com.biit.orbeon.form.IGroup;
import com.biit.orbeon.form.IQuestion;
import com.biit.orbeon.form.ISubmittedForm;
import com.biit.orbeon.form.exceptions.GroupDoesNotExistException;
import com.biit.orbeon.form.exceptions.QuestionDoesNotExistException;

public class Category extends SubmittedFormObject implements ICategory, IDroolsForm, IXmlGenerator {

	private List<IGroup> groups;
	private ISubmittedForm parent;
	private List<IQuestion> questions;

	public Category(String tag) {
		setTag(tag);
		// Needed to make compatible the different importers
		setText(tag);
		setGroups(new ArrayList<IGroup>());
		setQuestions(new ArrayList<IQuestion>());
	}

	@Override
	public void addGroup(IGroup group) {
		if (this.groups == null) {
			setGroups(new ArrayList<IGroup>());
		}
		((Group) group).setParent(this);
		this.groups.add(group);
	}

	@Override
	public void addGroups(List<IGroup> groups) {
		if (this.groups == null) {
			setGroups(new ArrayList<IGroup>());
		}
		for (IGroup group : groups) {
			addGroup(group);
		}
	}

	@Override
	public void addQuestion(IQuestion question) {
		if (this.questions == null) {
			setQuestions(new ArrayList<IQuestion>());
		}
		((Question) question).setParent(this);
		this.questions.add(question);
	}

	@Override
	public void addQuestions(List<IQuestion> questions) {
		if (this.questions == null) {
			setQuestions(new ArrayList<IQuestion>());
		}
		for (IQuestion question : questions) {
			addQuestion(question);
		}
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

	@Override
	public List<IGroup> getGroups() {
		return this.groups;
	}

	public ISubmittedForm getParent() {
		return this.parent;
	}

	@Override
	public IQuestion getQuestion(String questionTag) throws QuestionDoesNotExistException {
		if (this.questions != null) {
			for (IQuestion question : getQuestions()) {
				if (question.getTag().equals(questionTag)) {
					return question;
				}
			}
		}
		for (IGroup group : groups) {
			try {
				return group.getQuestion(questionTag);
			} catch (QuestionDoesNotExistException qne) {
				// Not found in group. Continue.
			}
		}
		throw new QuestionDoesNotExistException("Question '" + questionTag + "' does not exists.");
	}

	@Override
	public List<IQuestion> getQuestions() {
		return this.questions;
	}

	public boolean isScoreNotSet(String varName) {
		return !isScoreSet(varName);
	}

	public boolean isScoreSet(String varName) {
		// Retrieve the form which will have the variables
		return isScoreSet(this, varName);
	}

	public boolean isScoreSet(Object submittedFormTreeObject, String varName) {
		// Retrieve the form which will have the variables
		return ((SubmittedForm) getParent()).isScoreSet(submittedFormTreeObject, varName);
	}

	public void setGroups(List<IGroup> groups) {
		this.groups = groups;
	}

	public void setParent(ISubmittedForm form) {
		this.parent = form;
	}

	public void setQuestions(List<IQuestion> questions) {
		this.questions = questions;
	}

	public Object getVariableValue(String varName) {
		return getVariableValue(this, varName);
	}

	public Object getVariableValue(Object submmitedFormObject, String varName) {
		return ((SubmittedForm) this.getParent()).getVariableValue(submmitedFormObject, varName);
	}

	public void setVariableValue(String varName, Object value) {
		setVariableValue(this, varName, value);
	}

	public void setVariableValue(Object submmitedFormObject, String varName, Object value) {
		((SubmittedForm) this.getParent()).setVariableValue(submmitedFormObject, varName, value);
	}

	@Override
	public String generateXML(String tabs) {
		String xmlFile = tabs + "<" + getTag() + ">\n";
		if (getGroups() != null) {
			for (IGroup iGroup : getGroups()) {
				xmlFile += ((IXmlGenerator) iGroup).generateXML(tabs + "\t");
			}
		}
		if (getQuestions() != null) {
			for (IQuestion iQuestion : getQuestions()) {
				xmlFile += ((IXmlGenerator) iQuestion).generateXML(tabs + "\t");
			}
		}
		xmlFile += tabs + "</" + getTag() + ">\n";
		return xmlFile;
	}
}

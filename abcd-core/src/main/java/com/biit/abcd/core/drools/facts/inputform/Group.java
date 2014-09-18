package com.biit.abcd.core.drools.facts.inputform;

import java.util.ArrayList;
import java.util.List;

import com.biit.orbeon.form.ICategory;
import com.biit.orbeon.form.IGroup;
import com.biit.orbeon.form.IQuestion;
import com.biit.orbeon.form.exceptions.GroupDoesNotExistException;
import com.biit.orbeon.form.exceptions.QuestionDoesNotExistException;

public class Group extends CommonAttributes implements IGroup {

	private List<IQuestion> questions;
	private List<IGroup> groups;
	private IGroup parent;

	public Group(String tag) {
		this.setTag(tag);
		this.setQuestions(new ArrayList<IQuestion>());
	}

	@Override
	public List<IQuestion> getQuestions() {
		return this.questions;
	}

	@Override
	public IQuestion getQuestion(String questionTag) throws QuestionDoesNotExistException {
		for (IQuestion question : this.getQuestions()) {
			if (question.getTag().equals(questionTag)) {
				return question;
			}
		}
		// Check in inner groups.
		if (groups != null) {
			for (IGroup group : groups) {
				try {
					return group.getQuestion(questionTag);
				} catch (QuestionDoesNotExistException qne) {
					// Not found in group. Continue.
				}
			}
		}
		throw new QuestionDoesNotExistException("Question '" + questionTag + "' does not exists.");
	}

	public IGroup getGroup(String tag) throws GroupDoesNotExistException {
		if (getGroups() != null) {
			for (IGroup group : this.getGroups()) {
				if (group.getTag().equals(tag)) {
					return group;
				}
			}
		}
		throw new GroupDoesNotExistException("Group '" + tag + "' does not exists.");
	}

	public void setGroups(List<IGroup> groups) {
		this.groups = groups;
	}

	public List<IGroup> getGroups() {
		return this.groups;
	}

	public void setQuestions(List<IQuestion> questions) {
		this.questions = questions;
	}

	@Override
	public void addGroup(IGroup group) {
		if (this.groups == null) {
			this.setGroups(new ArrayList<IGroup>());
		}
		((Group) group).setParent(this);
		this.groups.add(group);
	}

	@Override
	public void addQuestions(List<IQuestion> questions) {
		if (this.questions == null) {
			this.setQuestions(new ArrayList<IQuestion>());
		}
		this.questions.addAll(questions);
	}

	@Override
	public void addQuestion(IQuestion question) {
		if (this.questions == null) {
			this.setQuestions(new ArrayList<IQuestion>());
		}
		((Question) question).setParent(this);
		this.questions.add(question);
	}

	public IGroup getParent() {
		return this.parent;
	}

	public void setParent(ICategory parent) {
		this.parent = parent;
	}

	public void setParent(IGroup parent) {
		this.parent = parent;
	}

	public boolean isScoreSet(String varName) {
		return isScoreSet(this, varName);
	}

	public boolean isScoreSet(Object submittedFormTreeObject, String varName) {
		if (this.getParent() instanceof ICategory) {
			return ((Category) getParent()).isScoreSet(submittedFormTreeObject, varName);
		} else {
			return ((Group) getParent()).isScoreSet(submittedFormTreeObject, varName);
		}
	}

	public boolean isScoreNotSet(String varName) {
		return !isScoreSet(varName);
	}

	// public Number getNumberVariableValue(String varName) {
	// return ((SubmittedForm) ((Category)
	// this.getParent()).getParent()).getNumberVariableValue(this, varName);
	// }

	public Object getVariableValue(String varName) {
		return getVariableValue(this, varName);
	}

	public Object getVariableValue(Object submmitedFormObject, String varName) {
		if (this.getParent() instanceof ICategory) {
			return ((Category) this.getParent()).getVariableValue(submmitedFormObject, varName);
		} else {
			return ((Group) this.getParent()).getVariableValue(submmitedFormObject, varName);
		}
	}

	public void setVariableValue(String varName, Object value) {
		setVariableValue(this, varName, value);
	}

	public void setVariableValue(Object submmitedFormObject, String varName, Object value) {
		if (this.getParent() instanceof ICategory) {
			((Category) this.getParent()).setVariableValue(submmitedFormObject, varName, value);
		} else {
			((Group) this.getParent()).setVariableValue(submmitedFormObject, varName, value);
		}
	}
}

package com.biit.abcd.core.drools.facts.inputform;

import java.util.ArrayList;
import java.util.List;

import com.biit.orbeon.form.ICategory;
import com.biit.orbeon.form.IGroup;
import com.biit.orbeon.form.IQuestion;
import com.biit.orbeon.form.exceptions.QuestionDoesNotExistException;

public class Group extends CommonAttributes implements IGroup {

	private List<IQuestion> questions;
	private ICategory parent;

	public Group(String tag){
		this.setTag(tag);
		this.setQuestions(new ArrayList<IQuestion>());
	}

	@Override
	public List<IQuestion> getQuestions() {
		return this.questions;
	}

	@Override
	public IQuestion getQuestion(String tag) throws QuestionDoesNotExistException {
		for (IQuestion question : this.getQuestions()) {
			if (question.getTag().equals(tag)) {
				return question;
			}
		}
		throw new QuestionDoesNotExistException("Question '" + tag + "' does not exists.");
	}

	public void setQuestions(List<IQuestion> questions) {
		this.questions = questions;
	}

	@Override
	public void addQuestions(List<IQuestion> questions) {
		if (this.questions == null) {
			this.setQuestions(new ArrayList<IQuestion>());
		}
		this.questions.addAll(questions);
	}

	public void addQuestion(IQuestion question) {
		if (this.questions == null) {
			this.setQuestions(new ArrayList<IQuestion>());
		}
		((Question)question).setParent(this);
		this.questions.add(question);
	}

	public ICategory getParent() {
		return this.parent;
	}

	public void setParent(ICategory parent) {
		this.parent = parent;
	}

	public boolean isScoreSet(String varName) {
		// Retrieve the form which will have the variables
		if(((SubmittedForm)((Category)this.getParent()).getParent()).hasScoreSet(this, varName)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isScoreNotSet(String varName) {
		return !this.isScoreSet(varName);
	}

	public Object getVariableValue(String varName){
		return ((SubmittedForm)((Category)this.getParent()).getParent()).getVariableValue(this, varName);
	}

	public Number getNumberVariableValue(String varName){
		return ((SubmittedForm)((Category)this.getParent()).getParent()).getNumberVariableValue(this, varName);
	}

	public void setVariableValue(String varName, Object value){
		((SubmittedForm)((Category)this.getParent()).getParent()).setVariableValue(this, varName, value);
	}
}

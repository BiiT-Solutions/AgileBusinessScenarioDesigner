package com.biit.abcd.core.drools.facts.inputform;

import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.core.drools.facts.inputform.exceptions.QuestionDoesNotExistException;
import com.biit.abcd.core.drools.facts.interfaces.ICategory;
import com.biit.abcd.core.drools.facts.interfaces.IGroup;
import com.biit.abcd.core.drools.facts.interfaces.IQuestion;

public class Group extends CommonAttributes implements IGroup {

	private List<IQuestion> questions;
	private ICategory parent;

	public Group(String tag){
		setTag(tag);
		setQuestions(new ArrayList<IQuestion>());
	}

	@Override
	public List<IQuestion> getQuestions() {
		return questions;
	}

	@Override
	public IQuestion getQuestion(String tag) throws QuestionDoesNotExistException {
		for (IQuestion question : getQuestions()) {
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
			setQuestions(new ArrayList<IQuestion>());
		}
		this.questions.addAll(questions);
	}

	public void addQuestion(IQuestion question) {
		if (questions == null) {
			setQuestions(new ArrayList<IQuestion>());
		}
		((Question)question).setParent(this);
		questions.add(question);
	}

	public ICategory getParent() {
		return parent;
	}

	public void setParent(ICategory parent) {
		this.parent = parent;
	}

	public boolean isScoreSet() {
		// Retrieve the form which will have the variables
		if(((SubmittedForm)((Category)getParent()).getParent()).hasScoreSet(this)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isScoreNotSet() {
		return !isScoreSet();
	}
}

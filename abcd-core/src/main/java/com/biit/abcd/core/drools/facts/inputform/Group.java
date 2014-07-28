package com.biit.abcd.core.drools.facts.inputform;

import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.core.drools.facts.inputform.exceptions.QuestionDoesNotExistException;
import com.biit.abcd.core.drools.facts.interfaces.IGroup;
import com.biit.abcd.core.drools.facts.interfaces.IQuestion;
import com.biit.abcd.core.drools.rules.VariablesMap;
import com.biit.abcd.persistence.entity.CustomVariableScope;

public class Group extends CommonAttributes implements IGroup {

	private List<IQuestion> questions;

	public Group(String tag){
		setTag(tag);
		setQuestions(new ArrayList<IQuestion>());
	}

	public List<IQuestion> getQuestions() {
		return questions;
	}

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
		questions.add(question);
	}

	@Override
	public Object getCustomVariable() {
		return VariablesMap.getInstance().getVariableValue(CustomVariableScope.GROUP, getTag());
	}

	@Override
	public void setCustomVariable(Object value) {
		VariablesMap.getInstance().addVariableValue(CustomVariableScope.GROUP, getTag(), value);
	}
}

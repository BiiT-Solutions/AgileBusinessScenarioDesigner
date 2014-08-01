package com.biit.abcd.core.drools.facts.interfaces;

import java.util.List;

import com.biit.abcd.core.drools.facts.inputform.exceptions.QuestionDoesNotExistException;

public interface IGroup extends ICommonAttributes{

	void addQuestions(List<IQuestion> questions);

	List<IQuestion> getQuestions();

	IQuestion getQuestion(String questionTag) throws QuestionDoesNotExistException;
}

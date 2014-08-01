package com.biit.abcd.core.drools.facts.interfaces;

import java.util.List;

import com.biit.abcd.core.drools.facts.inputform.exceptions.GroupDoesNotExistException;
import com.biit.abcd.core.drools.facts.inputform.exceptions.QuestionDoesNotExistException;

public interface ICategory extends ICommonAttributes{

	void addGroups(List<IGroup> groups);

	List<IGroup> getGroups();

	IGroup getGroup(String groupTag) throws GroupDoesNotExistException;

	void addQuestions(List<IQuestion> questions);

	List<IQuestion> getQuestions();

	IQuestion getQuestion(String groupTag) throws QuestionDoesNotExistException;
}

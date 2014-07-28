package com.biit.abcd.core.drools.facts.interfaces;

import java.util.List;

import com.biit.abcd.core.drools.facts.inputform.exceptions.GroupDoesNotExistException;


public interface ICategory extends ICommonAttributes {

	void addGroups(List<IGroup> groups);

	List<IGroup> getGroups();

	IGroup getGroup(String groupTag) throws GroupDoesNotExistException;
}

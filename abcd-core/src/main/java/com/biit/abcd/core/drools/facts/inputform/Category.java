package com.biit.abcd.core.drools.facts.inputform;

import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.core.drools.facts.inputform.exceptions.GroupDoesNotExistException;
import com.biit.abcd.core.drools.facts.interfaces.ICategory;
import com.biit.abcd.core.drools.facts.interfaces.IGroup;
import com.biit.abcd.core.drools.rules.VariablesMap;
import com.biit.abcd.persistence.entity.CustomVariableScope;

public class Category extends CommonAttributes implements ICategory {

	private List<IGroup> groups;

	public Category(String tag) {
		setTag(tag);
		setGroups(new ArrayList<IGroup>());
	}

	public List<IGroup> getGroups() {
		return groups;
	}

	public IGroup getGroup(String tag) throws GroupDoesNotExistException {
		for (IGroup group : getGroups()) {
			if (group.getTag().equals(tag)) {
				return group;
			}
		}
		throw new GroupDoesNotExistException("Group '" + tag + "' does not exists.");
	}

	public void setGroups(List<IGroup> questions) {
		this.groups = questions;
	}

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

	public Object getCustomVariable() {
		return VariablesMap.getInstance().getVariableValue(CustomVariableScope.CATEGORY, getTag());
	}

	public void setCustomVariable(Object value) {
		VariablesMap.getInstance().addVariableValue(CustomVariableScope.CATEGORY, getTag(), value);
	}
}

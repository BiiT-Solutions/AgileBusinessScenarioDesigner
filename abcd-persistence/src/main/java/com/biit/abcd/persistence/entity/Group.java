package com.biit.abcd.persistence.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "GROUPS")
public class Group extends TreeObject {
	private static final String DEFAULT_GROUP_TECHNICAL_NAME = "Group";
	private static final List<Class<?>> ALLOWED_CHILDS = new ArrayList<Class<?>>(Arrays.asList(Question.class,
			Group.class));
	private static final List<Class<?>> ALLOWED_PARENTS = new ArrayList<Class<?>>(Arrays.asList(Category.class,
			Group.class));

	private String technicalName;
	private boolean repetable;

	public Group() {
	}

	@Override
	protected List<Class<?>> getAllowedChilds() {
		return ALLOWED_CHILDS;
	}

	@Override
	protected List<Class<?>> getAllowedParents() {
		return ALLOWED_PARENTS;
	}

	public String getTechnicalName() {
		return technicalName;
	}

	public void setTechnicalName(String technicalName) {
		this.technicalName = technicalName;
	}

	public boolean isRepetable() {
		return repetable;
	}

	public void setRepetable(boolean repetable) {
		this.repetable = repetable;
	}

	/**
	 * Creates a default Group technical name, different for each Group of the same parent.
	 * 
	 * @param startingIndex
	 * @return
	 */
	public String getDefaultTechnicalName(TreeObject parent, int startingIndex) {
		String name;
		if (parent != null) {
			name = DEFAULT_GROUP_TECHNICAL_NAME + startingIndex;
			for (TreeObject child : parent.getChildren()) {
				if (child instanceof Group && ((Group) child).getTechnicalName() != null
						&& ((Group) child).getTechnicalName().equals(name)) {
					return getDefaultTechnicalName(parent, startingIndex + 1);
				}
			}
		} else {
			name = DEFAULT_GROUP_TECHNICAL_NAME;
		}
		return name;
	}

	@Override
	public String toString() {
		return getTechnicalName();
	}
}

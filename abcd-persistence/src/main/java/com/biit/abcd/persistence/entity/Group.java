package com.biit.abcd.persistence.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.exceptions.FieldTooLongException;

@Entity
@Table(name = "TREE_GROUPS")
public class Group extends TreeObject {
	private static final String DEFAULT_GROUP_TECHNICAL_NAME = "Group";
	private static final List<Class<?>> ALLOWED_CHILDS = new ArrayList<Class<?>>(Arrays.asList(Question.class,
			Group.class));
	private static final List<Class<?>> ALLOWED_PARENTS = new ArrayList<Class<?>>(Arrays.asList(Category.class,
			Group.class));

	private boolean repetable;

	public Group() {
	}

	public Group(String name) throws FieldTooLongException {
		setName(name);
	}

	@Override
	protected List<Class<?>> getAllowedChilds() {
		return ALLOWED_CHILDS;
	}

	@Override
	protected List<Class<?>> getAllowedParents() {
		return ALLOWED_PARENTS;
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
	public String getDefaultName(TreeObject parent, int startingIndex) {
		String name;
		if (parent != null) {
			name = DEFAULT_GROUP_TECHNICAL_NAME + startingIndex;
			for (TreeObject child : parent.getChildren()) {
				if ((child instanceof Group) && (((Group) child).getName() != null)
						&& ((Group) child).getName().equals(name)) {
					return getDefaultName(parent, startingIndex + 1);
				}
			}
		} else {
			name = DEFAULT_GROUP_TECHNICAL_NAME;
		}
		return name;
	}

	@Override
	public String toString() {
		return getName();
	}
}

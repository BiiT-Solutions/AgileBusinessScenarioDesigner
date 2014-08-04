package com.biit.abcd.persistence.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "TREE_CATEGORIES")
public class Category extends TreeObject {
	private static final String DEFAULT_CATEGORY_NAME = "Category";
	private static final List<Class<?>> ALLOWED_CHILDS = new ArrayList<Class<?>>(Arrays.asList(Question.class,
			Group.class));
	private static final List<Class<?>> ALLOWED_PARENTS = new ArrayList<Class<?>>(Arrays.asList(Form.class));

	public Category() {
	}

	public Category(String name) {
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

	/**
	 * Creates a default Category name, different for each category of the same form.
	 * 
	 * @param startingIndex
	 * @return
	 */
	public String getDefaultName(TreeObject parent, int startingIndex) {
		String name;
		if (parent != null) {
			name = DEFAULT_CATEGORY_NAME + startingIndex;
			for (TreeObject child : parent.getChildren()) {
				if ((child instanceof Category) && (((Category) child).getName() != null)
						&& ((Category) child).getName().equals(name)) {
					return getDefaultName(parent, startingIndex + 1);
				}
			}
		} else {
			name = DEFAULT_CATEGORY_NAME;
		}
		return name;
	}

	@Override
	public String toString() {
		return getName();
	}

}

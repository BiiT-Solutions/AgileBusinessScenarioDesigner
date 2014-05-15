package com.biit.abcd.persistence.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "CATEGORIES")
public class Category extends TreeObject {
	private static final String DEFAULT_CATEGORY_NAME = "Category";
	private static final List<Class<?>> ALLOWED_CHILDS = new ArrayList<Class<?>>(Arrays.asList(Question.class,
			Group.class));
	private static final List<Class<?>> ALLOWED_PARENTS = new ArrayList<Class<?>>(Arrays.asList(Form.class));

	private String label;

	public Category() {
	}

	@Override
	protected List<Class<?>> getAllowedChilds() {
		return ALLOWED_CHILDS;
	}

	@Override
	protected List<Class<?>> getAllowedParents() {
		return ALLOWED_PARENTS;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Creates a default Category name, different for each category of the same form.
	 * 
	 * @param startingIndex
	 * @return
	 */
	public String getDefaultLabel(TreeObject parent, int startingIndex) {
		String name;
		if (parent != null) {
			name = DEFAULT_CATEGORY_NAME + startingIndex;
			for (TreeObject child : parent.getChildren()) {
				if (child instanceof Category && ((Category) child).getLabel() != null
						&& ((Category) child).getLabel().equals(name)) {
					return getDefaultLabel(parent, startingIndex + 1);
				}
			}
		} else {
			name = DEFAULT_CATEGORY_NAME;
		}
		return name;
	}

}

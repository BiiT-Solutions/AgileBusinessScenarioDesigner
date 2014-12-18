package com.biit.abcd.webpages.components;

import com.biit.form.TreeObject;
import com.vaadin.data.Item;

/**
 * Filter for TableTreeObject. Does a contains of the ElementName.
 * 
 */
public class FilterTreeObjectTableContainsName implements IFilterContainsText {
	private static final long serialVersionUID = 852831365303680293L;
	private String text;

	@Override
	public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
		return checkIfTreeObjectPasses((TreeObject) itemId);
	}

	@Override
	public boolean appliesToProperty(Object propertyId) {
		if (text == null || text.isEmpty()) {
			return true;
		}
		if (TableTreeObjectLabel.TreeObjectTableProperties.ELEMENT_NAME.equals(propertyId)) {
			return true;
		}
		return false;
	}

	protected boolean checkNameWithFilter(TreeObject element) {
		String name = element.getName().toLowerCase();

		if (name.contains(getFilterText())) {
			return true;
		}
		return false;
	}

	public boolean checkIfTreeObjectPasses(TreeObject element) {
		if (checkNameWithFilter(element)) {
			return true;
		}

		if (element.getParent() == null) {
			return false;
		} else {
			return checkIfTreeObjectPasses(element.getParent());
		}
	}

	@Override
	public void setFilterText(String text) {
		this.text = text.toLowerCase();
	}

	public String getFilterText() {
		return text;
	}

}

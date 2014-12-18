package com.biit.abcd.webpages.components;

import com.biit.form.TreeObject;
import com.vaadin.data.Item;

/**
 * Filter for TreeObjectTable. Does a contains of the Name (super) and the label
 * properties.
 * 
 */
public class FilterTreeObjectTableContainsNameLabel extends FilterTreeObjectTableContainsName {
	private static final long serialVersionUID = 3867634034792955643L;

	@Override
	public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
		return checkIfTreeObjectPasses((TreeObject) itemId);
	}

	@Override
	public boolean appliesToProperty(Object propertyId) {
		if (TableTreeObjectLabel.TreeObjectTableDesignerProperties.ELEMENT_LABEL.equals(propertyId)) {
			return true;
		}
		return super.appliesToProperty(propertyId);
	}

	public boolean checkIfTreeObjectPasses(TreeObject element) {
		if (checkNameWithFilter(element)) {
			return true;
		}

		String label = element.getLabel().toLowerCase();
		if (label.contains(getFilterText())) {
			return true;
		}

		if (element.getParent() == null) {
			return false;
		} else {
			return checkIfTreeObjectPasses(element.getParent());
		}
	}
}

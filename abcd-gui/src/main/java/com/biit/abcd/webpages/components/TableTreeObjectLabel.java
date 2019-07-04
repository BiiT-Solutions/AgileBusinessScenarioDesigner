package com.biit.abcd.webpages.components;

import com.biit.form.entity.TreeObject;
import com.vaadin.data.Item;

/**
 * Table tree object with a label property added.
 *
 */
public class TableTreeObjectLabel extends TreeObjectTable {
	private static final long serialVersionUID = 2882643672843469056L;
	private static final int LABEL_MAX_LENGTH = 50;

	public enum TreeObjectTableDesignerProperties {
		ELEMENT_LABEL
	};

	@Override
	protected void initContainerProperties() {
		super.initContainerProperties();
		addContainerProperty(TreeObjectTableDesignerProperties.ELEMENT_LABEL, String.class, null, "", null, Align.LEFT);
	}

	@Override
	protected void setValuesToItem(Item item, TreeObject element) {
		super.setValuesToItem(item, element);
		String label = element.getLabel();
		if (label == null) {
			label = new String();
		}
		setLabelToItem(item, label);
	}

	/**
	 * Sets label to item and cuts label to max length.
	 * 
	 * @param item
	 * @param label
	 */
	@SuppressWarnings("unchecked")
	protected void setLabelToItem(Item item, String label) {
		if (label.length() > LABEL_MAX_LENGTH) {
			label = label.substring(0, LABEL_MAX_LENGTH - 1);
			label += "...";
		}
		item.getItemProperty(TreeObjectTableDesignerProperties.ELEMENT_LABEL).setValue(label);
	}

	/**
	 * Expands the tree until treeObject
	 * 
	 * @param treeObject
	 *            the tree object.
	 */
	public void expand(TreeObject treeObject) {
		if (treeObject.getParent() != null) {
			expand(treeObject.getParent());
		}
		setCollapsed(treeObject, false);
	}
}

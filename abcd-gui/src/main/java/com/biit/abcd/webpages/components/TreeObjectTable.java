package com.biit.abcd.webpages.components;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.TreeObject;
import com.vaadin.data.Item;
import com.vaadin.ui.TreeTable;

/**
 * TreeObjectTable component
 * 
 * This is a customized component to represent a TreeObject in a tree table.
 * 
 */
public class TreeObjectTable extends TreeTable {
	private static final long serialVersionUID = -6949123334668973540L;

	protected enum TreeObjectTableProperties {
		ELEMENT_NAME
	};

	public TreeObjectTable() {
		initContainerProperties();
		setImmediate(true);
	}

	private void initContainerProperties() {
		addContainerProperty(TreeObjectTableProperties.ELEMENT_NAME, String.class, "",
				ServerTranslate.tr(LanguageCodes.FORM_TREE_PROPERTY_NAME), null, Align.LEFT);
		setCellStyleGenerator(new FormTreeTableCellStyleGenerator());
	}

	private void loadTreeObject(TreeObject element, TreeObject parent) {
		addItem(element, parent);

		List<TreeObject> children = element.getChildren();
		for (TreeObject child : children) {
			loadTreeObject(child, element);
		}
	}

	/**
	 * Adds item to table. This function is a specialization of {@link TreeTable#addItem(Object)} for form members.
	 * 
	 * @param element
	 */
	@SuppressWarnings("unchecked")
	public void addItem(TreeObject element, TreeObject parent) {
		if (element != null) {
			String name = getItemName(element);
			Item item = addItem((Object) element);
			if (parent != null) {
				setChildrenAllowed(parent, true);
				setParent(element, parent);
				setCollapsed(parent, false);
			}
			item.getItemProperty(TreeObjectTableProperties.ELEMENT_NAME).setValue(name);
			setValue(element);
			setChildrenAllowed(element, false);
		}
	}

	@SuppressWarnings("unchecked")
	public void updateItem(TreeObject element) {
		Item item = getItem(element);
		if(item!=null){
			//If the item still exists on table.
			String name = getItemName(element);
			item.getItemProperty(TreeObjectTableProperties.ELEMENT_NAME).setValue(name);
		}
	}

	/**
	 * Adds item to table. This function is a specialization of {@link TreeTable#addItemAfter(Object, Object)} for form
	 * members.
	 * 
	 * @param element
	 */
	@SuppressWarnings("unchecked")
	public void addItemAfter(Object previousItemId, TreeObject element, TreeObject parent) {
		if (element != null) {
			String name = getItemName(element);
			Item item = addItemAfter(previousItemId, (Object) element);
			if (parent != null) {
				setChildrenAllowed(parent, true);
				setParent(element, parent);
				setCollapsed(parent, false);
			}
			item.getItemProperty(TreeObjectTableProperties.ELEMENT_NAME).setValue(name);
			setValue(element);
			setChildrenAllowed(element, false);
		}
	}

	/**
	 * Gets Name property to show form a TreeObject element. If the name can't be defined, then raises a
	 * {@link UnsupportedOperationException}
	 * 
	 * @param element
	 * @return
	 */
	public static String getItemName(TreeObject element) {
		String name = null;
		if (element instanceof Form) {
			name = ((Form) element).getName();
		}
		if (element instanceof Category) {
			name = ((Category) element).getName();
		}
		if (element instanceof Group) {
			name = ((Group) element).getName();
		}
		if (element instanceof Question) {
			name = ((Question) element).getName();
		}
		if (element instanceof Answer) {
			name = ((Answer) element).getName();
		}
		if (name == null) {
			throw new UnsupportedOperationException(TreeObject.class.getName() + " subtype unknown.");
		}
		return name;
	}

	public void setRootElement(TreeObject root) {
		this.removeAllItems();
		select(null);
		loadTreeObject(root, null);
		if (root != null) {
			try {
				setCollapsed(root, false);
			} catch (Exception e) {
				// Root is not inserted. Ignore error.
			}
		}
	}

	@Override
	public TreeObject getValue() {
		Object value = super.getValue();
		if (value instanceof TreeObject) {
			return (TreeObject) value;
		}
		return null;
	}

	@Override
	public Collection<?> getSortableContainerPropertyIds() {
		return Collections.EMPTY_LIST;
	}
}

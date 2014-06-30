package com.biit.abcd.webpages.components;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.TreeObject;
import com.vaadin.data.Item;
import com.vaadin.ui.Component;
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
		addContainerProperty(TreeObjectTableProperties.ELEMENT_NAME, Component.class, null,
				ServerTranslate.translate(LanguageCodes.FORM_TREE_PROPERTY_NAME), null, Align.LEFT);
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
			// String name = getItemName(element);
			TreeObjectWithIconComponent treeObjectIcon = new TreeObjectWithIconComponent(element, getIcon(element),
					element.getName());
			Item item = addItem((Object) element);
			if (parent != null) {
				setChildrenAllowed(parent, true);
				setParent(element, parent);
				setCollapsed(parent, false);
			}
			item.getItemProperty(TreeObjectTableProperties.ELEMENT_NAME).setValue(treeObjectIcon);
			setValue(element);
			setChildrenAllowed(element, false);
		}
	}

	@SuppressWarnings("unchecked")
	public void updateItem(TreeObject element) {
		Item item = getItem(element);
		if (item != null) {
			// If the item still exists on table.
			TreeObjectWithIconComponent treeObjectIcon = new TreeObjectWithIconComponent(element, getIcon(element),
					element.getName());
			item.getItemProperty(TreeObjectTableProperties.ELEMENT_NAME).setValue(treeObjectIcon);
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
			TreeObjectWithIconComponent treeObjectIcon = new TreeObjectWithIconComponent(element, getIcon(element),
					element.getName());
			Item item = addItemAfter(previousItemId, (Object) element);
			if (parent != null) {
				setChildrenAllowed(parent, true);
				setParent(element, parent);
				setCollapsed(parent, false);
			}
			item.getItemProperty(TreeObjectTableProperties.ELEMENT_NAME).setValue(treeObjectIcon);
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
		String name = element.getName();
		if (name == null) {
			throw new UnsupportedOperationException(TreeObject.class.getName() + " subtype unknown.");
		}
		return name;
	}

	public static ThemeIcons getIcon(TreeObject element) {
		if (element instanceof Question) {
			Question question = (Question) element;
			switch (question.getAnswerType()) {
			case MULTI_CHECKBOX:
				return ThemeIcons.TREE_DESIGNER_QUESTION_CHECKLIST;
			case RADIO:
				return ThemeIcons.TREE_DESIGNER_QUESTION_RADIOBUTTON;
			case INPUT:
				switch (question.getAnswerFormat()) {
				case DATE:
					return ThemeIcons.TREE_DESIGNER_QUESTION_DATE;
				case NUMBER:
					return ThemeIcons.TREE_DESIGNER_QUESTION_NUMBER;
				case POSTAL_CODE:
					return ThemeIcons.TREE_DESIGNER_QUESTION_POSTALCODE;
				case TEXT:
					return ThemeIcons.TREE_DESIGNER_QUESTION_TEXT;
				}
			}
		}

		return null;
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

	public TreeObject getTreeObjectSelected() {
		Object value = super.getValue();
		if (value instanceof TreeObject) {
			return (TreeObject) value;
		}
		return null;
	}

	public void setTreeObjectsSelected(Set<TreeObject> treeObjects) {
		super.setValue(treeObjects);
	}

	public void setTreeObjectSelected(TreeObject treeObject) {
		super.setValue(treeObject);
	}

	@SuppressWarnings("unchecked")
	public Set<TreeObject> getTreeObjectsSelected() {
		Object value = super.getValue();
		if (value instanceof Set<?>) {
			Set<Object> setObject = (Set<Object>) value;
			Set<TreeObject> setTreeObject = new HashSet<TreeObject>();
			for (Object obj : setObject) {
				setTreeObject.add((TreeObject) obj);
			}
			return setTreeObject;
		}
		return null;
	}

	@Override
	public Collection<?> getSortableContainerPropertyIds() {
		return Collections.EMPTY_LIST;
	}

	public boolean isElementFiltered(Object itemId) {
		return false;
	}

}

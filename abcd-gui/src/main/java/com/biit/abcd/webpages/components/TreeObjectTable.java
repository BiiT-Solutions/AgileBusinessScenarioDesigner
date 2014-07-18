package com.biit.abcd.webpages.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.biit.abcd.MessageManager;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.AnswerType;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.TreeObject;
import com.biit.abcd.persistence.entity.exceptions.ChildrenNotFoundException;
import com.biit.abcd.persistence.entity.exceptions.DependencyExistException;
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

	protected void initContainerProperties() {
		addContainerProperty(TreeObjectTableProperties.ELEMENT_NAME, Component.class, null,
				ServerTranslate.translate(LanguageCodes.FORM_TREE_PROPERTY_NAME), null, Align.LEFT);
		setCellStyleGenerator(new TreeObjectTableCellStyleGenerator());
	}

	private void loadTreeObject(TreeObject element, TreeObject parent) {
		addItem(element, parent);

		List<TreeObject> children = element.getChildren();
		for (TreeObject child : children) {
			loadTreeObject(child, element);
		}
	}

	/**
	 * Adds item to table. This function is a specialization of
	 * {@link TreeTable#addItem(Object)} for form members.
	 * 
	 * @param element
	 */
	@SuppressWarnings("unchecked")
	public void addItem(TreeObject element, TreeObject parent) {
		if (element != null) {
			Object treeObjectIcon = createElementWithIcon(element);
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

	public void updateItem(TreeObject element) {
		Item item = getItem(element);
		if (item != null) {
			// Remove children if are not valid.
			try {
				removeChildren(element);
			} catch (DependencyExistException e) {
				MessageManager.showWarning(LanguageCodes.TREE_DESIGNER_WARNING_NO_UPDATE,
						LanguageCodes.TREE_DESIGNER_WARNING_NO_UPDATE_DESCRIPTION);
				// Impossible to remove children.
				return;
			}

			// Update
			ComponentCellTreeObject cell = (ComponentCellTreeObject) item.getItemProperty(
					TreeObjectTableProperties.ELEMENT_NAME).getValue();
			cell.update(element);
		}
	}

	/**
	 * Input fields cannot have children. Therefore remove any if exists.
	 * 
	 * @param element
	 */
	private void removeChildren(TreeObject element) throws DependencyExistException {
		if (element instanceof Question) {
			if (((Question) element).getAnswerType().equals(AnswerType.INPUT)) {
				try {
					List<Object> children = new ArrayList<Object>(getChildren(element));
					for (Object child : children) {
						try {
							element.removeChild((TreeObject) child);
							removeItem(child);
						} catch (ChildrenNotFoundException e) {

						}
					}
					setChildrenAllowed(element, false);
				} catch (NullPointerException npe) {
					// No children. Do nothing.
				}
			}
		}
	}

	/**
	 * Adds item to table. This function is a specialization of
	 * {@link TreeTable#addItemAfter(Object, Object)} for form members.
	 * 
	 * @param element
	 */
	@SuppressWarnings("unchecked")
	public void addItemAfter(Object previousItemId, TreeObject element, TreeObject parent) {
		if (element != null) {
			Object treeObjectIcon = createElementWithIcon(element);
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
	 * Gets Name property to show form a TreeObject element. If the name can't
	 * be defined, then raises a {@link UnsupportedOperationException}
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

	protected Object createElementWithIcon(final TreeObject element) {
		ComponentCellTreeObject cell = new ComponentCellTreeObject();
		cell.update(element);
		cell.registerTouchCallBack(this, element);

		return cell;
	}

	/**
	 * Collapse the tree in a specific hierarchy level to inner levels. The
	 * level is specified by a class.
	 * 
	 * @param collapseFrom
	 */
	public void collapseFrom(Class<?> collapseFrom) {
		for (Object item : getItemIds()) {
			if (item.getClass() == collapseFrom) {
				this.setCollapsed(item, true);
			} else {
				if (this.getParent(item) != null && this.isCollapsed(this.getParent(item))) {
					this.setCollapsed(item, true);
				}
			}
		}
	}

}

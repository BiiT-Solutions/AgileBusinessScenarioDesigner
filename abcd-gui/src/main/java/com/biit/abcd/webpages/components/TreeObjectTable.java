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
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.AnswerType;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.testscenarios.TestScenarioForm;
import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.DependencyExistException;
import com.biit.form.exceptions.ElementIsReadOnly;
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
	protected enum TreeObjectTableProperties {
		ELEMENT_NAME
	}

	public TreeObjectTable() {
		initContainerProperties();
		setImmediate(true);
		setPageLength(0);
	}

	private static final long serialVersionUID = -6949123334668973540L;;

	/**
	 * Gets Name property to show form a TreeObject element. If the name can't be defined, then raises a
	 * {@link UnsupportedOperationException}
	 * 
	 * @param element
	 * @return
	 */
	public static String getItemName(TreeObject element) {
		String name = null;
		if ((element instanceof Form) || (element instanceof TestScenarioForm)) {
			name = element.getLabel();
		} else {
			name = element.getName();
		}
		if (name == null) {
			throw new UnsupportedOperationException(TreeObject.class.getName() + " subtype unknown.");
		}
		return name;
	}

	/**
	 * Adds item to table. This function is a specialization of {@link TreeTable#addItem(Object)} for form members.
	 * 
	 * @param element
	 */
	@SuppressWarnings("unchecked")
	public void addItem(TreeObject element, TreeObject parent, boolean selectRow) {
		if (element != null) {
			Object treeObjectIcon = createElementWithIcon(element);
			Item item = addItem((Object) element);
			if (parent != null) {
				// This status must be true before setting the relationship.
				setChildrenAllowed(parent, true);
				setParent(element, parent);
				setCollapsed(parent, false);
			}
			item.getItemProperty(TreeObjectTableProperties.ELEMENT_NAME).setValue(treeObjectIcon);
			if (selectRow) {
				setValue(element);
			}
			setChildrenAllowed(element, false);
			uncollapse(element);
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
			Object treeObjectIcon = createElementWithIcon(element);
			Item item = addItemAfter(previousItemId, (Object) element);
			if (parent != null) {
				setChildrenAllowed(parent, true);
				setParent(element, parent);
				setCollapsed(parent, false);
			}
			item.getItemProperty(TreeObjectTableProperties.ELEMENT_NAME).setValue(treeObjectIcon);
			setChildrenAllowed(element, false);
			// If it is a new element, still has no parent. Uncollapse the
			// futures parent.
			setValue(element);
			uncollapse(element);
		}
	}

	/**
	 * Collapse the tree in a specific hierarchy level to inner levels. The level is specified by a class.
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

	protected Object createElementWithIcon(final TreeObject element) {
		ComponentCellTreeObject cell = new ComponentCellTreeObject();
		cell.update(element);
		cell.registerTouchCallBack(this, element);

		return cell;
	}

	@Override
	public Collection<?> getSortableContainerPropertyIds() {
		return Collections.EMPTY_LIST;
	}

	public TreeObject getTreeObjectSelected() {
		Object value = super.getValue();
		if (value instanceof TreeObject) {
			return (TreeObject) value;
		}
		return null;
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

	protected void initContainerProperties() {
		addContainerProperty(TreeObjectTableProperties.ELEMENT_NAME, Component.class, null,
				ServerTranslate.translate(LanguageCodes.FORM_TREE_PROPERTY_NAME), null, Align.LEFT);
		setCellStyleGenerator(new TreeObjectTableCellStyleGenerator());
	}

	public boolean isElementFiltered(Object itemId) {
		return false;
	}

	public void loadTreeObject(TreeObject element, TreeObject parent) {
		addItem(element, parent, false);

		List<TreeObject> children = element.getChildren();
		for (TreeObject child : children) {
			loadTreeObject(child, element);
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
							((TreeObject) child).remove();
						} catch (ElementIsReadOnly e) {
							// Impossible
							AbcdLogger.errorMessage(this.getClass().getName(), e);
						}
						removeItem(child);
					}
					setChildrenAllowed(element, false);
				} catch (NullPointerException npe) {
					// No children. Do nothing.
				}
			}
		}
	}

	public void setRootElement(TreeObject root) {
		this.removeAllItems();
		select(null);
		if (root != null) {
			loadTreeObject(root, null);
			try {
				setCollapsed(root, false);
			} catch (Exception e) {
				// Root is not inserted. Ignore error.
			}
		}
	}

	public void setTreeObjectSelected(TreeObject treeObject) {
		super.setValue(treeObject);
	}

	public void setTreeObjectsSelected(Set<TreeObject> treeObjects) {
		super.setValue(treeObjects);
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
	 * Set values and selects it by default.
	 * 
	 * @param element
	 */
	public void setValue(TreeObject element) {
		super.setValue(element);
		uncollapse(element);
	}

	public void uncollapse(TreeObject element) {
		if (element != null) {
			List<TreeObject> ancestors = element.getAncestors();
			Collections.reverse(ancestors);
			for (TreeObject ancestor : ancestors) {
				setCollapsed(ancestor, false);
			}
		}
	}

	public void uncollapseAll() {
		Collection<?> items = this.getItemIds();
		for (Object item : items) {
			setCollapsed(item, false);
		}
	}

	@SuppressWarnings("unchecked")
	protected void setValuesToItem(Item item, TreeObject element) {
		Object treeObjectIcon = createElementWithIcon(element);
		item.getItemProperty(TreeObjectTableProperties.ELEMENT_NAME).setValue(treeObjectIcon);
	}

	/**
	 * Loads a tree object structure recursively. At the end of the process selects the root element inserted. element.
	 * It can also be specified an array of filterClasses. If this is not specified, then every kind of element is
	 * allowed. Else only the elements in the hierarchy whose path is made of valid elements.
	 * 
	 * @param element
	 * @param parent
	 */
	public void loadTreeObject(TreeObject element, TreeObject parent, Class<?>... filterClases) {
		loadTreeObject(element, parent, true, filterClases);
	}

	public void loadTreeObject(TreeObject element, TreeObject parent, boolean select, Class<?>... filterClases) {
		if (element != null) {
			if (isAdmitedInFilter(element, filterClases)) {
				addItem(element, parent, select);

				List<TreeObject> children = element.getChildren();
				for (TreeObject child : children) {
					loadTreeObject(child, element, false, filterClases);
				}
			}
		}
	}

	private boolean isAdmitedInFilter(TreeObject element, Class<?>... filterClases) {
		if (filterClases == null || filterClases.length == 0) {
			// No filter, then everything is admited.
			return true;
		}
		for (Class<?> filterClass : filterClases) {
			if (filterClass.isInstance(element)) {
				return true;
			}
		}
		return false;
	}

}

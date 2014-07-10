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
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.TreeObject;
import com.biit.abcd.persistence.entity.exceptions.ChildrenNotFoundException;
import com.biit.abcd.persistence.entity.exceptions.DependencyExistException;
import com.vaadin.data.Item;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
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
			TreeObjectWithIconComponent treeObjectIcon = createElementWithIcon(element);
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
			// Remove children if are not valid.
			try {
				removeChildren(element);
			} catch (DependencyExistException e) {
				MessageManager.showWarning(LanguageCodes.TREE_DESIGNER_WARNING_NO_UPDATE,
						LanguageCodes.TREE_DESIGNER_WARNING_NO_UPDATE_DESCRIPTION);
				// Impossible to remove children. Set as previous value (still stored at the icon).
				TreeObjectWithIconComponent treeObjectIcon = (TreeObjectWithIconComponent) item.getItemProperty(
						TreeObjectTableProperties.ELEMENT_NAME).getValue();
				switch (treeObjectIcon.getThemeIcon()) {
				case TREE_DESIGNER_QUESTION_CHECKLIST:
					((Question) element).setAnswerType(AnswerType.MULTI_CHECKBOX);
					break;
				default:
					((Question) element).setAnswerType(AnswerType.RADIO);
					break;
				}
			}

			// Update element.
			TreeObjectWithIconComponent treeObjectIcon = createElementWithIcon(element);
			item.getItemProperty(TreeObjectTableProperties.ELEMENT_NAME).setValue(treeObjectIcon);
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
	 * Adds item to table. This function is a specialization of {@link TreeTable#addItemAfter(Object, Object)} for form
	 * members.
	 * 
	 * @param element
	 */
	@SuppressWarnings("unchecked")
	public void addItemAfter(Object previousItemId, TreeObject element, TreeObject parent) {
		if (element != null) {
			TreeObjectWithIconComponent treeObjectIcon = createElementWithIcon(element);
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

	private static ThemeIcon getIcon(TreeObject element) {
		if (element instanceof Question) {
			Question question = (Question) element;
			switch (question.getAnswerType()) {
			case MULTI_CHECKBOX:
				return ThemeIcon.TREE_DESIGNER_QUESTION_CHECKLIST;
			case RADIO:
				return ThemeIcon.TREE_DESIGNER_QUESTION_RADIOBUTTON;
			case INPUT:
				switch (question.getAnswerFormat()) {
				case DATE:
					return ThemeIcon.TREE_DESIGNER_QUESTION_DATE;
				case NUMBER:
					return ThemeIcon.TREE_DESIGNER_QUESTION_NUMBER;
				case POSTAL_CODE:
					return ThemeIcon.TREE_DESIGNER_QUESTION_POSTALCODE;
				case TEXT:
					return ThemeIcon.TREE_DESIGNER_QUESTION_TEXT;
				}
			}
		} else if (element instanceof Group) {
			Group group = (Group) element;
			if (group.isRepetable()) {
				return ThemeIcon.TREE_DESIGNER_GROUP_LOOP;
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

	private TreeObjectWithIconComponent createElementWithIcon(final TreeObject element) {
		final TreeObjectTable thisObject = this;
		TreeObjectWithIconComponent treeObjectWithIconComponent = new TreeObjectWithIconComponent(element,
				getIcon(element), element.getName());

		treeObjectWithIconComponent.addLayoutClickListener(new LayoutClickListener() {
			private static final long serialVersionUID = 1176852269853300260L;

			@Override
			public void layoutClick(LayoutClickEvent event) {
				// Select table row if the element is clicked.
				if (thisObject.getValue() != element) {
					thisObject.setValue(element);
				} else {
					// Unselect with second click.
					thisObject.setValue(null);
				}
			}
		});

		return treeObjectWithIconComponent;
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

}

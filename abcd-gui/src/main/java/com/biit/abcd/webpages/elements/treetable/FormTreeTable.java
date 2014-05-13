package com.biit.abcd.webpages.elements.treetable;

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
 * FormTreeTable component
 * 
 * This is a customized component to represent a Form in a tree table.
 * 
 */
public class FormTreeTable extends TreeTable {
	private static final long serialVersionUID = -6949123334668973540L;
	private Form form;

	enum FormTreeTableProperties {
		ELEMENT_NAME
	};

	public FormTreeTable() {
		initContainerProperties();
	}

	private void initContainerProperties() {
		addContainerProperty(FormTreeTableProperties.ELEMENT_NAME, String.class, "",
				ServerTranslate.tr(LanguageCodes.FORM_TREE_PROPERTY_NAME), null, Align.LEFT);
	}

	private void loadForm(TreeObject element, TreeObject parent) {
		addItem(element, parent);
		if (parent != null) {
			setParent(element, parent);
		}
		List<TreeObject> children = element.getChildren();
		for (TreeObject child : children) {
			loadForm(child, element);
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
			String name = getItemName(element);
			Item item = addItem((Object) element);
			if (parent != null) {
				setParent(element, parent);
				setCollapsed(parent, false);
			}
			item.getItemProperty(FormTreeTableProperties.ELEMENT_NAME).setValue(name);
			setValue(element);
		}
	}

	@SuppressWarnings("unchecked")
	public void updateItem(TreeObject element) {
		Item item = getItem(element);
		String name = getItemName(element);
		item.getItemProperty(FormTreeTableProperties.ELEMENT_NAME).setValue(name);
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
			String name = getItemName(element);
			Item item = addItemAfter(previousItemId, (Object) element);
			if (parent != null) {
				setParent(element, parent);
				setCollapsed(parent, false);
			}
			item.getItemProperty(FormTreeTableProperties.ELEMENT_NAME).setValue(name);
			setValue(element);
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
		String name = null;
		if (element instanceof Form) {
			name = ((Form) element).getName();
		}
		if (element instanceof Category) {
			name = ((Category) element).getLabel();
		}
		if (element instanceof Group) {
			name = ((Group) element).getTechnicalName();
		}
		if (element instanceof Question) {
			name = ((Question) element).getTechnicalName();
		}
		if (element instanceof Answer) {
			name = ((Answer) element).getTechnicalName();
		}
		if (name == null) {
			throw new UnsupportedOperationException(TreeObject.class.getName() + " subtype unknown.");
		}
		return name;
	}

	public void setForm(Form form) {
		this.form = form;
		this.removeAllItems();
		loadForm(form, null);
		if (form != null) {
			setCollapsed(form, false);
		}
		selectFirstRow();
	}

	@Override
	public TreeObject getValue() {
		Object value = super.getValue();
		if (value instanceof TreeObject) {
			return (TreeObject) value;
		}
		return null;
	}

	/**
	 * Selects the first row.
	 */
	private void selectFirstRow() {
		setValue(firstItemId());
	}

	@Override
	public Collection<?> getSortableContainerPropertyIds() {
		return Collections.EMPTY_LIST;
	}
}

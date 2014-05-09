package com.biit.abcd.webpages.components;

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

	public void loadFormElement(TreeObject element) {
		addItem(element);
		List<TreeObject> children = element.getChildren();
		for (TreeObject child : children) {
			loadFormElement(child);
			setParent(child, element);
		}
	}

	/**
	 * Adds item to table. This function is a specialization of
	 * {@link TreeTable#addItem(Object)} for form members.
	 * 
	 * @param element
	 */
	@SuppressWarnings("unchecked")
	public void addItem(TreeObject element) {
		String name = getNameFromElement(element);
		Item item = addItem((Object) element);
		item.getItemProperty(FormTreeTableProperties.ELEMENT_NAME).setValue(name);
	}

	/**
	 * Adds item to table. This function is a specialization of
	 * {@link TreeTable#addItemAfter(Object, Object)} for form members.
	 * 
	 * @param element
	 */
	@SuppressWarnings("unchecked")
	public void addItemAfter(Object previousItemId, TreeObject element) {
		String name = getNameFromElement(element);
		Item item = addItemAfter(previousItemId, (Object) element);
		item.getItemProperty(FormTreeTableProperties.ELEMENT_NAME).setValue(name);
	}

	/**
	 * Gets Name property to show form a TreeObject element. If the name can't
	 * be defined, then raises a {@link UnsupportedOperationException}
	 * 
	 * @param element
	 * @return
	 */
	private static String getNameFromElement(TreeObject element) {
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
}

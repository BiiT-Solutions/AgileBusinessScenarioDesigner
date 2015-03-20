package com.biit.abcd.webpages.elements.droolsresults;

import java.util.List;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.drools.form.SubmittedForm;
import com.biit.form.submitted.ISubmittedObject;
import com.biit.form.submitted.implementation.SubmittedQuestion;
import com.vaadin.data.Item;
import com.vaadin.ui.TreeTable;

/**
 * TreeObjectTable component
 * 
 * This is a customized component to represent a TreeObject in a tree table.
 * 
 */
public class DroolsTreeObjectTable extends TreeTable {
	private static final long serialVersionUID = 7444822513424426513L;

	protected enum DroolsTreeObjectTableProperties {
		ELEMENT_NAME, ORIGINAL_VALUE
	}

	public DroolsTreeObjectTable() {
		initContainerProperties();
		setImmediate(true);
	}

	protected void initContainerProperties() {
		addContainerProperty(DroolsTreeObjectTableProperties.ELEMENT_NAME, String.class, null,
				ServerTranslate.translate(LanguageCodes.SUBMITTED_FORM_TREE_PROPERTY_NAME), null, Align.LEFT);
		addContainerProperty(DroolsTreeObjectTableProperties.ORIGINAL_VALUE, String.class, null,
				ServerTranslate.translate(LanguageCodes.SUBMITTED_FORM_TREE_PROPERTY_ORIGINAL_VALUE), null, Align.LEFT);
		setColumnWidth(DroolsTreeObjectTableProperties.ORIGINAL_VALUE, 150);
		setCellStyleGenerator(new DroolsObjectTableCellStyleGenerator());
	}

	@SuppressWarnings("unchecked")
	private void addItem(ISubmittedObject element, ISubmittedObject parent) {
		if (element != null) {
			Item item = addItem((Object) element);
			if (parent != null) {
				setChildrenAllowed(parent, true);
				setParent(element, parent);
				setCollapsed(parent, false);
			}
			item.getItemProperty(DroolsTreeObjectTableProperties.ELEMENT_NAME).setValue(element.getTag());
			if (element instanceof SubmittedQuestion) {
				item.getItemProperty(DroolsTreeObjectTableProperties.ORIGINAL_VALUE).setValue(
						((SubmittedQuestion) element).getAnswer());
			} else {
				item.getItemProperty(DroolsTreeObjectTableProperties.ORIGINAL_VALUE).setValue("");
			}
			setValue(element);
			setChildrenAllowed(element, false);
		}
	}

	private void loadTreeObject(ISubmittedObject element, ISubmittedObject parent) {
		addItem(element, parent);
		List<ISubmittedObject> children = element.getChildren();
		if (children != null) {
			for (ISubmittedObject child : children) {
				loadTreeObject(child, element);
			}
		}
	}

	public void setRootElement(SubmittedForm root) {
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
}

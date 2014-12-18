package com.biit.abcd.webpages.components;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.Form;
import com.biit.form.BaseForm;
import com.biit.form.TreeObject;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

public class SelectTreeObjectWindow extends AcceptCancelWindow {

	private static final long serialVersionUID = -4090805671578721633L;
	private TreeObjectTable treeObjectTable;
	private TableWithSearch tableWithSearch;

	public SelectTreeObjectWindow(Form form, boolean multiselect, Class<?>... loadFilter) {
		super();
		setWidth("50%");
		setHeight("75%");
		setResizable(false);
		cancelButton.setCaption(ServerTranslate.translate(LanguageCodes.CLOSE_BUTTON_CAPTION));
		cancelButton.setDescription(ServerTranslate.translate(LanguageCodes.CLOSE_BUTTON_TOOLTIP));
		setCaption(ServerTranslate.translate(LanguageCodes.SELECT_TREE_OBJECT));
		setModal(true);
		setContent(generateContent(form, loadFilter ));
	}

	private Component generateContent(BaseForm form, Class<?>... loadFilter) {
		VerticalLayout layout = new VerticalLayout();
		// Create content
		treeObjectTable = new TableTreeObjectLabel();
		treeObjectTable.setSelectable(true);
		treeObjectTable.setSizeFull();
		//treeObjectTable.setRootElement(form);
		treeObjectTable.loadTreeObject(form, null, loadFilter);		

		layout.setSizeFull();
		layout.setMargin(true);
		
		tableWithSearch = new TableWithSearch(treeObjectTable, new FilterTreeObjectTableContainsNameLabel());
		tableWithSearch.setSizeFull();
		
		layout.addComponent(tableWithSearch);
		treeObjectTable.uncollapseAll();
		
		return layout;
	}

	public void select(TreeObject selected) {
		collapseFrom(Category.class);
		if (treeObjectTable != null) {
			treeObjectTable.setValue(selected);
		}
	}

	public TreeObject getSelectedTreeObject() {
		return (TreeObject) treeObjectTable.getValue();
	}

	public void clearSelection() {
		if (treeObjectTable != null) {
			treeObjectTable.setValue(null);
		}
	}

	/**
	 * Collapse the tree in a specific hierarchy level to inner levels. The level is specified by a class.
	 * 
	 * @param collapseFrom
	 */
	public void collapseFrom(Class<?> collapseFrom) {
		treeObjectTable.collapseFrom(collapseFrom);
	}
}
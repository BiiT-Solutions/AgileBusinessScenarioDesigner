package com.biit.abcd.webpages.components;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.TreeObject;
import com.biit.abcd.webpages.elements.decisiontable.FormQuestionTable;
import com.vaadin.ui.Component;

public abstract class SelectionTableWindow extends AcceptCancelWindow {
	private static final long serialVersionUID = 6781910083959136654L;
	private FormQuestionTable formQuestionTable;

	public SelectionTableWindow(TreeObject treeObject) {
		super();
		setContent(generateContent(treeObject));
		cancelButton.setCaption(ServerTranslate.tr(LanguageCodes.CLOSE_BUTTON_CAPTION));
		cancelButton.setDescription(ServerTranslate.tr(LanguageCodes.CLOSE_BUTTON_TOOLTIP));
		setWidth("50%");
		setHeight("75%");
		setResizable(false);
	}

	public abstract Component generateContent(TreeObject treeObject);

	public FormTreeTable getTable() {
		return formQuestionTable;
	}
}

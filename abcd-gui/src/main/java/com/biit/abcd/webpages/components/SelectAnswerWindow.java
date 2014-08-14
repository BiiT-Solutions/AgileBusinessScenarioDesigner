package com.biit.abcd.webpages.components;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.Question;
import com.biit.form.TreeObject;
import com.vaadin.ui.Component;

public class SelectAnswerWindow extends SelectTreeObjectTableWindow {
	private static final long serialVersionUID = -5510653106309311210L;

	public SelectAnswerWindow(Question question) {
		super(question, false);
		setCaption(ServerTranslate.translate(LanguageCodes.CONDITION_TABLE_EDIT_CONDITION_CAPTION));
	}

	@Override
	public Component generateContent(TreeObject treeObject) {
		setTable(new SelectFormAnswerTable());
		getTable().setSizeFull();
		if (treeObject instanceof Question) {
			getTable().setRootElement(treeObject);
			getTable().setSelectable(true);
		}

		return getTable();
	}

	public Answer getSelectedTableValue() {
		return ((SelectFormAnswerTable) getTable()).getValue();
	}

}

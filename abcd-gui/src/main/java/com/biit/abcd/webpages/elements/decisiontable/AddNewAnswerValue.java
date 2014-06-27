package com.biit.abcd.webpages.elements.decisiontable;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.TreeObject;
import com.biit.abcd.webpages.components.SelectionTableWindow;
import com.vaadin.ui.Component;

public class AddNewAnswerValue extends SelectionTableWindow {
	private static final long serialVersionUID = -5510653106309311210L;

	public AddNewAnswerValue(Question question) {
		super(question, false);
		setCaption(ServerTranslate.translate(LanguageCodes.CONDITION_TABLE_EDIT_CONDITION_CAPTION));
	}

	@Override
	public Component generateContent(TreeObject treeObject) {
		setTable(new FormAnswerTable());
		getTable().setSizeFull();
		if (treeObject instanceof Question) {
			getTable().setRootElement(treeObject);
			getTable().setSelectable(true);
		}

		return getTable();
	}

	public Answer getSelectedTableValue() {
		return ((FormAnswerTable) getTable()).getValue();
	}

}

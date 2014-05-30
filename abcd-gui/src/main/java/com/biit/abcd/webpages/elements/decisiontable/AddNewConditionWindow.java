package com.biit.abcd.webpages.elements.decisiontable;

import java.util.Collection;

import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.TreeObject;
import com.biit.abcd.webpages.components.SelectionTableWindow;
import com.vaadin.ui.Component;

public class AddNewConditionWindow extends SelectionTableWindow {
	private static final long serialVersionUID = 6781910083959136654L;
	private FormQuestionTable formQuestionTable;

	public AddNewConditionWindow(Form form) {
		super(form);
	}

	public Question getSelectedQuestion() {
		return (Question) formQuestionTable.getValue();
	}

	public void disableQuestion(Question question) {
		formQuestionTable.disableQuestion(question);
	}

	public void disableQuestions(Collection<Question> collection) {
		formQuestionTable.disableQuestions(collection);
	}

	@Override
	public Component generateContent(TreeObject treeObject) {
		formQuestionTable = new FormQuestionTable();
		formQuestionTable.setSizeFull();
		if (treeObject instanceof Form) {
			formQuestionTable.setRootElement((Form) treeObject);
			formQuestionTable.setSelectable(true);
		}

		return formQuestionTable;
	}
}

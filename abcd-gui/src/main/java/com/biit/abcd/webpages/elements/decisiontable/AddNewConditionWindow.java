package com.biit.abcd.webpages.elements.decisiontable;

import java.util.Collection;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Question;
import com.vaadin.ui.Component;

public class AddNewConditionWindow extends AcceptCancelWindow {

	private static final long serialVersionUID = 6781910083959136654L;

	private FormQuestionTable formQuestionTable;

	public AddNewConditionWindow(Form form) {
		super();
		setContent(generateContent(form));
		cancelButton.setCaption(ServerTranslate.tr(LanguageCodes.CLOSE_BUTTON_CAPTION));
		cancelButton.setDescription(ServerTranslate.tr(LanguageCodes.CLOSE_BUTTON_TOOLTIP));
		setWidth("50%");
		setHeight("75%");
		setResizable(false);
		center();
	}

	public Component generateContent(Form form) {

		formQuestionTable = new FormQuestionTable();
		formQuestionTable.setForm(form);
		formQuestionTable.setSizeFull();
		formQuestionTable.setSelectable(true);

		return formQuestionTable;
	}

	public FormQuestionTable getFormQuestionTable() {
		return formQuestionTable;
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
}

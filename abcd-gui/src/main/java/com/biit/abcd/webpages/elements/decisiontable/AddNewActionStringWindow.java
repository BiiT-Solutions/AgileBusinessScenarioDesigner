package com.biit.abcd.webpages.elements.decisiontable;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.rules.Action;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;

public class AddNewActionStringWindow extends AcceptCancelWindow {
	private static final long serialVersionUID = 8131952730660382409L;
	private FormAnswerTable formAnswerTable;
	private TextArea textArea;

	public AddNewActionStringWindow(Action action) {
		super();
		setWidth("50%");
		setHeight("75%");
		setContent(generateContent(action));
		setResizable(false);
		setCaption(ServerTranslate.tr(LanguageCodes.CONDITION_TABLE_EDIT_ACTION_CAPTION));
	}

	public Component generateContent(Action action) {
		VerticalLayout layout = new VerticalLayout();
		textArea = new TextArea(ServerTranslate.tr(LanguageCodes.CONDITION_TABLE_EDITOR_ACTION_EDITOR_TEXTAREA_CAPTION));
		textArea.setValue((String) action.getExpression());
		textArea.setSizeFull();
		layout.addComponent(textArea);
		layout.setSizeFull();
		layout.setMargin(true);
		return layout;
	}

	public Answer getSelectedTableValue() {
		return formAnswerTable.getValue();
	}

	public String getExpression() {
		return textArea.getValue();
	}
}

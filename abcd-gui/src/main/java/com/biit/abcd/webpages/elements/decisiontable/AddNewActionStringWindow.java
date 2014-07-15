package com.biit.abcd.webpages.elements.decisiontable;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.Answer;
import com.biit.abcd.persistence.entity.rules.ActionExpression;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.SelectFormAnswerTable;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;

@Deprecated
public class AddNewActionStringWindow extends AcceptCancelWindow {
	private static final long serialVersionUID = 8131952730660382409L;
	private SelectFormAnswerTable formAnswerTable;
	private TextArea textArea;

	public AddNewActionStringWindow(ActionExpression action) {
		super();
		setWidth("50%");
		setHeight("75%");
		setContent(generateContent(action));
		setResizable(false);
		setCaption(ServerTranslate.translate(LanguageCodes.CONDITION_TABLE_EDIT_ACTION_CAPTION));
	}

	public Component generateContent(ActionExpression action) {
		VerticalLayout layout = new VerticalLayout();
		textArea = new TextArea(ServerTranslate.translate(LanguageCodes.CONDITION_TABLE_EDITOR_ACTION_EDITOR_TEXTAREA_CAPTION));
		textArea.setValue(action.getExpressionAsString());
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

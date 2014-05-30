package com.biit.abcd.webpages.components;

import com.biit.abcd.language.AnswerFormatUi;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

public class VariableWindow extends AcceptCancelWindow {
	private static final long serialVersionUID = -5976767693799864405L;
	private static final String WIDTH = "400px";
	private static final String HEIGHT = "400px";
	private static final String FIELD_WIDTH = "400px";

	private TextField variableName;
	private ComboBox variableFormat;

	public VariableWindow() {
		super();
		setHeight(HEIGHT);
		setWidth(WIDTH);
		setModal(true);
		setClosable(false);
		setDraggable(false);
		setResizable(false);
		setContent(generateContent());
	}

	private Component generateContent() {
		HorizontalLayout rootLayout = new HorizontalLayout();
		rootLayout.setSizeFull();
		rootLayout.setMargin(true);

		FormLayout formLayout = new FormLayout();
		formLayout.setSizeUndefined();
		formLayout.setSpacing(true);

		variableName = new TextField("name");

		variableFormat = new ComboBox("nanana");
		variableFormat.setImmediate(true);
		for (AnswerFormatUi answerFormatUi : AnswerFormatUi.values()) {
			variableFormat.addItem(answerFormatUi.getAnswerFormat());
			variableFormat.setItemCaption(answerFormatUi.getAnswerFormat(),
					ServerTranslate.tr(answerFormatUi.getLanguageCode()));
		}

		variableName.setWidth(FIELD_WIDTH);
		variableFormat.setWidth(FIELD_WIDTH);

		formLayout.addComponent(variableName);
		formLayout.addComponent(variableFormat);

		rootLayout.addComponent(formLayout);
		rootLayout.setComponentAlignment(formLayout, Alignment.MIDDLE_CENTER);

		return rootLayout;
	}

	public GlobalVariable getValue() {
		GlobalVariable globalVariable = new GlobalVariable();
		if (variableName.getValue() != null && !variableName.getValue().isEmpty()) {
			globalVariable.setName(variableName.getValue());
			globalVariable.setFormat(((AnswerFormatUi) variableFormat.getValue()).getAnswerFormat());
			return globalVariable;
		}
		//MessageManager.showError()
		return null;
	}
}

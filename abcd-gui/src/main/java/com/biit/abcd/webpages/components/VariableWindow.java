package com.biit.abcd.webpages.components;

import com.biit.abcd.MessageManager;
import com.biit.abcd.language.AnswerFormatUi;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.AnswerFormat;
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
	private static final String HEIGHT = "250px";
	private static final String FIELD_WIDTH = "150px";

	private TextField variableName;
	private ComboBox variableFormat;

	public VariableWindow(String title) {
		super();
		setHeight(HEIGHT);
		setWidth(WIDTH);
		setModal(true);
		setClosable(false);
		setDraggable(false);
		setResizable(false);
		setContent(generateContent());
		setCaption(title);
	}

	private Component generateContent() {
		HorizontalLayout rootLayout = new HorizontalLayout();
		rootLayout.setSizeFull();
		rootLayout.setMargin(true);

		FormLayout formLayout = new FormLayout();
		formLayout.setSizeUndefined();
		formLayout.setSpacing(true);

		variableName = new TextField(ServerTranslate.translate(LanguageCodes.GLOBAL_VARIABLE_NAME));

		variableFormat = new ComboBox(ServerTranslate.translate(LanguageCodes.GLOBAL_VARIABLE_TYPE));
		variableFormat.setImmediate(true);
		for (AnswerFormatUi answerFormatUi : AnswerFormatUi.values()) {
			variableFormat.addItem(answerFormatUi.getAnswerFormat());
			variableFormat.setItemCaption(answerFormatUi.getAnswerFormat(),
					ServerTranslate.translate(answerFormatUi.getLanguageCode()));
		}
		variableFormat.setNullSelectionAllowed(false);
		variableFormat.setValue(AnswerFormatUi.values()[0].getAnswerFormat());

		variableName.setWidth(FIELD_WIDTH);
		variableFormat.setWidth(FIELD_WIDTH);
		variableName.focus();

		formLayout.addComponent(variableName);
		formLayout.addComponent(variableFormat);

		rootLayout.addComponent(formLayout);
		rootLayout.setComponentAlignment(formLayout, Alignment.MIDDLE_CENTER);

		return rootLayout;
	}

	public GlobalVariable getValue() {
		if (variableName.getValue() == null || variableName.getValue().isEmpty()) {
			MessageManager.showWarning(LanguageCodes.WARNING_TITLE, LanguageCodes.WARNING_VARIABLE_NAME_WRONG);
			return null;
		}
		
		GlobalVariable globalVariable = new GlobalVariable((AnswerFormat) variableFormat.getValue());
		globalVariable.setName(variableName.getValue());
		return globalVariable;
	}
	
	public void setValue(GlobalVariable value){
		if (value != null ) {
			variableName.setValue(value.getName());
			variableFormat.setValue(value.getFormat());
		}
	}
	
	public void disableTypeEdition(){
		variableFormat.setEnabled(false);
	}
}

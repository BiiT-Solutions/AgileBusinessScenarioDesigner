package com.biit.abcd.webpages.components;

import com.biit.abcd.MessageManager;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

public class TableCellLabelEditWindow extends AcceptCancelWindow {
	private static final long serialVersionUID = 2997027596037432825L;
	private static final String WIDTH = "400px";
	private static final String HEIGHT = "250px";
	private static final String FIELD_WIDTH = "150px";

	private TextField variableName;

	public TableCellLabelEditWindow(String title) {
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

		variableName = new TextField(ServerTranslate.translate(LanguageCodes.FORM_PROPERTIES_TECHNICAL_NAME));
		variableName.setWidth(FIELD_WIDTH);

		formLayout.addComponent(variableName);

		rootLayout.addComponent(formLayout);
		rootLayout.setComponentAlignment(formLayout, Alignment.MIDDLE_CENTER);

		return rootLayout;
	}

	public String getValue() {
		if (variableName.getValue() == null || variableName.getValue().isEmpty()) {
			MessageManager.showWarning(LanguageCodes.WARNING_TITLE, LanguageCodes.WARNING_VARIABLE_NAME_WRONG);
			return null;
		}
		return variableName.getValue();
	}
	
	public void setValue(String value){
		if (value != null ) {
			variableName.setValue(value);
		}
	}
}

package com.biit.abcd.webpages.components;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class StringInputWindow extends AcceptCancelWindow {
	private static final long serialVersionUID = 361486551550136464L;
	private static final String width = "300px";
	private static final String height = "180px";

	private TextField textField;

	public StringInputWindow(String inputFieldCaption) {
		super();
		setContent(generateContent(inputFieldCaption));
		setResizable(false);
		setDraggable(false);
		setClosable(false);
		setModal(true);
		setWidth(width);
		setHeight(height);
	}

	public String getValue() {
		return textField.getValue();
	}

	private Component generateContent(String inputFieldCaption) {
		textField = new TextField(inputFieldCaption);
		textField.focus();

		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();

		rootLayout.addComponent(textField);
		rootLayout.setComponentAlignment(textField, Alignment.MIDDLE_CENTER);
		return rootLayout;
	}
}

package com.biit.abcd.webpages.components;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class DroolsSubmittedFormWindow extends AcceptCancelWindow {

	private static final long serialVersionUID = -2904744459099883988L;

	private String appName;
	private String formName;
	private String documentId;

	public DroolsSubmittedFormWindow() {
		super();
		setCaption("Submitted form information");
		setWidth("40%");
		setHeight("40%");
		setClosable(false);
		setModal(true);
		setResizable(false);
		setContent(generateContent());
	}

	private Component generateContent() {
		VerticalLayout layout = new VerticalLayout();
		// Create content
		TextField appNameField = new TextField("App name: ");
		appNameField.setImmediate(true);
		appNameField.setWidth("100%");
		appNameField.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -2834862614448446156L;

			@Override
			public void valueChange(final ValueChangeEvent event) {
				appName = String.valueOf(event.getProperty().getValue());
			}
		});
		appNameField.setValue("WebForms");

		TextField formNameField = new TextField("Form name: ");
		formNameField.setImmediate(true);
		formNameField.setWidth("100%");
		formNameField.setInputPrompt("Form Name");
		formNameField.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 4853902617302026183L;

			@Override
			public void valueChange(final ValueChangeEvent event) {
				formName = String.valueOf(event.getProperty().getValue());
			}
		});
		formNameField.setValue("De_Haagse_Passage_v2");

		TextField documentIdField = new TextField("Form ID: ");
		documentIdField.setImmediate(true);
		documentIdField.setWidth("100%");
		documentIdField.setInputPrompt("Doc ID");
		documentIdField.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 5884042251295904822L;

			@Override
			public void valueChange(final ValueChangeEvent event) {
				documentId = String.valueOf(event.getProperty().getValue());
			}
		});
		documentIdField.setValue("370023c797b9b9b691ed0e64a559f6adb7971df5");

		layout.addComponent(appNameField);
		layout.addComponent(formNameField);
		layout.addComponent(documentIdField);
		layout.setSizeFull();
		layout.setMargin(true);
		return layout;
	}

	public String getFormInfo(){
		return appName + "::" + formName + "::" + documentId;
	}

}

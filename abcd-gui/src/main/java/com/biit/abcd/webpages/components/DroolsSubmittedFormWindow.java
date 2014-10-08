package com.biit.abcd.webpages.components;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.webpages.elements.formmanager.SaveDroolsRulesAction;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class DroolsSubmittedFormWindow extends AcceptCancelWindow {

	private static final long serialVersionUID = -2904744459099883988L;

	private String orbeonAppName;
	private String orbeonFormName;
	private String orbeonDocumentId;

	public DroolsSubmittedFormWindow() {
		super();
		setCaption(ServerTranslate.translate(LanguageCodes.SUBMITTED_FORM_INFORMATION_CAPTION));
		setWidth("40%");
		setHeight("40%");
		setClosable(false);
		setModal(true);
		setResizable(false);
		setContent(generateContent());
	}

	@Override
	protected void generateAcceptCancelButton() {
		acceptButton = new SaveAsButton(LanguageCodes.ACCEPT_BUTTON_CAPTION, ThemeIcon.ACCEPT,
				LanguageCodes.ACCEPT_BUTTON_TOOLTIP, IconSize.SMALL, new SaveDroolsRulesAction());
		((SaveAsButton) acceptButton).addSaveActionListener(new SaveActionListener() {
			@Override
			public void saveAction() {
				fireAcceptActionListeners();
				close();
			}
		});

		cancelButton = new IconButton(LanguageCodes.CANCEL_BUTTON_CAPTION, ThemeIcon.CANCEL,
				LanguageCodes.CANCEL_BUTTON_TOOLTIP, IconSize.SMALL, new ClickListener() {
					private static final long serialVersionUID = -6302237054661116415L;

					@Override
					public void buttonClick(ClickEvent event) {
						fireCancelActionListeners();
						close();
					}
				});
	}

	private Component generateContent() {
		VerticalLayout layout = new VerticalLayout();
		// Create content
		TextField appNameField = new TextField("App name: ");
		appNameField.setImmediate(true);
		appNameField.setWidth("100%");
		appNameField.setInputPrompt("WebForms");
		appNameField.setId("droolsWebFormsInput");
		appNameField.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -2834862614448446156L;

			@Override
			public void valueChange(final ValueChangeEvent event) {
				orbeonAppName = String.valueOf(event.getProperty().getValue());
			}
		});
		// appNameField.setValue("WebForms");

		TextField formNameField = new TextField("Form name: ");
		formNameField.setImmediate(true);
		formNameField.setWidth("100%");
		formNameField.setInputPrompt("Form Name");
		formNameField.setId("droolsFormNameInput");
		formNameField.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 4853902617302026183L;

			@Override
			public void valueChange(final ValueChangeEvent event) {
				orbeonFormName = String.valueOf(event.getProperty().getValue());
			}
		});
		// formNameField.setValue("De_Haagse_Passage_v2");

		TextField documentIdField = new TextField("Form ID: ");
		documentIdField.setImmediate(true);
		documentIdField.setWidth("100%");
		documentIdField.setInputPrompt("Form ID");
		documentIdField.setId("droolsFormIdInput");
		documentIdField.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = 5884042251295904822L;

			@Override
			public void valueChange(final ValueChangeEvent event) {
				orbeonDocumentId = String.valueOf(event.getProperty().getValue());
			}
		});
		// documentIdField.setValue("370023c797b9b9b691ed0e64a559f6adb7971df5");

		layout.addComponent(appNameField);
		layout.addComponent(formNameField);
		layout.addComponent(documentIdField);
		layout.setSizeFull();
		layout.setMargin(true);
		return layout;
	}

	public String getOrbeonAppName() {
		return orbeonAppName;
	}

	public String getOrbeonFormName() {
		return orbeonFormName;
	}

	public String getOrbeonDocumentId() {
		return orbeonDocumentId;
	}

}

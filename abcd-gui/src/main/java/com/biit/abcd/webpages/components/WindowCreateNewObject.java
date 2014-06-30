package com.biit.abcd.webpages.components;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public abstract class WindowCreateNewObject extends Window {
	private static final long serialVersionUID = 2068576862509582763L;
	private static final int WINDOW_WIDTH = 500;
	private static final int WINDOW_HEIGHT = 170;
	private FormWebPageComponent parentWindow;

	public WindowCreateNewObject(FormWebPageComponent parentWindow, LanguageCodes windowCaption,
			LanguageCodes inputFieldCaption) {
		super();
		this.setParentWindow(parentWindow);
		setWidth(Math.min(WINDOW_WIDTH, UI.getCurrent().getPage().getBrowserWindowWidth()), Unit.PIXELS);
		setHeight(Math.min(WINDOW_HEIGHT, UI.getCurrent().getPage().getBrowserWindowHeight()), Unit.PIXELS);
		this.setCaption(ServerTranslate.translate(windowCaption));

		setContent(generateContent(inputFieldCaption));
		setResizable(false);
		setModal(true);
		center();
	}

	public AbstractOrderedLayout generateContent(LanguageCodes inputFieldCaption) {
		VerticalLayout mainLayout = new VerticalLayout();

		final TextField inputTextField = new TextField(ServerTranslate.translate(inputFieldCaption));
		inputTextField.setWidth("100%");

		mainLayout.addComponent(inputTextField);
		mainLayout.setExpandRatio(inputTextField, 1.0f);

		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setSpacing(true);

		Button save = new IconButton(LanguageCodes.WINDOW_NEWFORM_SAVEBUTTON_LABEL, ThemeIcon.ACCEPT,
				LanguageCodes.WINDOW_NEWFORM_SAVEBUTTON_TOOLTIP, IconSize.SMALL, new ClickListener() {
					private static final long serialVersionUID = -3292565406584483547L;

					@Override
					public void buttonClick(ClickEvent event) {
						acceptAction(inputTextField);
					}
				});

		buttonLayout.addComponent(save);
		buttonLayout.setComponentAlignment(save, Alignment.MIDDLE_CENTER);

		Button cancel = new IconButton(LanguageCodes.WINDOW_NEWFORM_CANCELBUTTON_LABEL, ThemeIcon.CANCEL, null,
				IconSize.SMALL, new ClickListener() {
					private static final long serialVersionUID = 521904682248680077L;

					@Override
					public void buttonClick(ClickEvent event) {
						close();
					}
				});

		buttonLayout.addComponent(cancel);
		buttonLayout.setComponentAlignment(cancel, Alignment.MIDDLE_CENTER);

		mainLayout.addComponent(buttonLayout);
		mainLayout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_CENTER);

		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);
		mainLayout.setSizeFull();

		return mainLayout;
	}

	public abstract void acceptAction(TextField inputTextField);

	public FormWebPageComponent getParentWindow() {
		return parentWindow;
	}

	private void setParentWindow(FormWebPageComponent parentWindow) {
		this.parentWindow = parentWindow;
	}

}

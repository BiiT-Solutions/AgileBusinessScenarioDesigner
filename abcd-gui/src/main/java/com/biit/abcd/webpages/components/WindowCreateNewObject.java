package com.biit.abcd.webpages.components;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public abstract class WindowCreateNewObject extends AcceptCancelWindow {
	private static final long serialVersionUID = 2068576862509582763L;
	private static final int WINDOW_WIDTH = 500;
	private static final int WINDOW_HEIGHT = 170;
	private FormWebPageComponent parentWindow;
	private TextField inputTextField;

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

		addAcceptActionListener(new AcceptActionListener() {

			@Override
			public void acceptAction(AcceptCancelWindow window) {
				concreteAcceptAction(inputTextField);
			}
		});
	}

	public AbstractOrderedLayout generateContent(LanguageCodes inputFieldCaption) {
		VerticalLayout mainLayout = new VerticalLayout();

		inputTextField = new TextField(ServerTranslate.translate(inputFieldCaption));
		inputTextField.setWidth("100%");
		inputTextField.addShortcutListener(new ShortcutListener("Enter as Accept", ShortcutAction.KeyCode.ENTER, null) {
			private static final long serialVersionUID = -9055249857540860785L;

			@Override
			public void handleAction(Object sender, Object target) {
				concreteAcceptAction(inputTextField);
			}
		});

		mainLayout.addComponent(inputTextField);
		mainLayout.setExpandRatio(inputTextField, 1.0f);

		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);
		mainLayout.setSizeFull();
		mainLayout.setMargin(false);

		inputTextField.focus();

		return mainLayout;
	}

	public abstract void concreteAcceptAction(TextField inputTextField);

	public FormWebPageComponent getParentWindow() {
		return parentWindow;
	}

	private void setParentWindow(FormWebPageComponent parentWindow) {
		this.parentWindow = parentWindow;
	}

}

package com.biit.abcd.webpages.components;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (GUI)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

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

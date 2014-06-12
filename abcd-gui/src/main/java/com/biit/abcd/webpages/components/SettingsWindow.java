package com.biit.abcd.webpages.components;

import com.biit.abcd.ApplicationFrame;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.webpages.WebMap;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class SettingsWindow extends PopupWindow{

	private static final long serialVersionUID = 4258182015635300330L;
	private static final String width = "300px";

	public SettingsWindow() {
		setClosable(true);
		setResizable(false);
		setDraggable(false);
		setModal(true);
		center();
		setWidth(width);
		setHeight(null);
		setContent(generateContent());
	}
	
	private Component generateContent(){
		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setWidth("100%");
		rootLayout.setHeight(null);
		
		Button globalConstantsButton= new Button(ServerTranslate.tr(LanguageCodes.SETTINGS_GLOBAL_CONSTANTS), new ClickListener() {
			private static final long serialVersionUID = 5662848461729745562L;

			@Override
			public void buttonClick(ClickEvent event) {
				ApplicationFrame.navigateTo(WebMap.GLOBAL_VARIABLES);
				close();
			}
		});
		Button logoutButton = new Button(ServerTranslate.tr(LanguageCodes.SETTINGS_LOG_OUT), new ClickListener() {
			private static final long serialVersionUID = -1121572145945309858L;

			@Override
			public void buttonClick(ClickEvent event) {
				ApplicationFrame.navigateTo(WebMap.LOGIN_PAGE);
				close();
			}
		});		
		Button closeButton = new Button(ServerTranslate.tr(LanguageCodes.SETTINGS_CLOSE), new ClickListener() {
			private static final long serialVersionUID = -1121572145945309858L;

			@Override
			public void buttonClick(ClickEvent event) {
				close();
			}
		});
		
		globalConstantsButton.setWidth("100%");
		logoutButton.setWidth("100%");
		closeButton.setWidth("100%");
		
		rootLayout.addComponent(globalConstantsButton);
		rootLayout.addComponent(logoutButton);
		rootLayout.addComponent(closeButton);
		
		return rootLayout;
	}
}

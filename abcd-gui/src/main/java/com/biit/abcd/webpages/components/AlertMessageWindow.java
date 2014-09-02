package com.biit.abcd.webpages.components;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.vaadin.ui.Label;

public class AlertMessageWindow extends AcceptCancelWindow {
	private static final long serialVersionUID = -5412952550536335088L;

	public AlertMessageWindow(LanguageCodes code) {
		super();
		setContent(new Label(ServerTranslate.translate(code)));		
		setModal(true);
		setWidth("300px");
		setHeight("180px");
		setResizable(false);
	}

}

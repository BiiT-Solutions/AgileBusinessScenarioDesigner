package com.biit.abcd.webpages.components;

import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.themes.BaseTheme;

public class IconOnlyButton extends Button{

	private static final long serialVersionUID = 755173150169409609L;

	public IconOnlyButton(Resource icon) {
		super();
		setStyleName(BaseTheme.BUTTON_LINK);
		addStyleName("IconOnlyButton");
		setIcon(icon);
	}
}
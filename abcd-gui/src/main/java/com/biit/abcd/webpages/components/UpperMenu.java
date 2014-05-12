package com.biit.abcd.webpages.components;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;

public class UpperMenu extends Panel {
	private HorizontalLayout menuLayout;

	protected UpperMenu() {
		defineUpperMenu();
	}

	private void defineUpperMenu() {
		menuLayout = new HorizontalLayout();
		setContent(menuLayout);
		setWidth("100%");
		setHeight("80px");
		menuLayout.setSpacing(true);
		menuLayout.setWidth("100%");
	}

	public HorizontalLayout getMenuLayout() {
		return menuLayout;
	}
}

package com.biit.abcd.webpages.components;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;

public class UpperMenu extends Panel {

	protected UpperMenu(){
		defineUpperMenu();
	}
	
	private void defineUpperMenu() {
		HorizontalLayout menuLayout = new HorizontalLayout();
		setContent(menuLayout);
		setWidth("100%");
		setHeight("60px");
		menuLayout.setSpacing(true);
		menuLayout.setWidth("100%");
	}
}

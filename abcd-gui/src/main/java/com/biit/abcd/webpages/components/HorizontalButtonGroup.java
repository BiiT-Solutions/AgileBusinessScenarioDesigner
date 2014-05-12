package com.biit.abcd.webpages.components;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;

public class HorizontalButtonGroup extends CustomComponent {

	private static final long serialVersionUID = 4862986305501412362L;
	private static String CLASSNAME = "v-horizontal-button-group";
	private HorizontalLayout rootLayout;

	public HorizontalButtonGroup() {
		setStyleName(CLASSNAME);
		
		rootLayout = new HorizontalLayout();
		rootLayout.setSizeFull();
		setCompositionRoot(rootLayout);
		setSizeFull();
	}

	public void addIconButton(IconButton button) {
		rootLayout.addComponent(button);
		button.setSizeFull();
	}

}

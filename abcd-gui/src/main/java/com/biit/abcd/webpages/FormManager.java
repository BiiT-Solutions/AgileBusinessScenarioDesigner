package com.biit.abcd.webpages;

import com.biit.abcs.component.WebPageComponent;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;

public class FormManager extends WebPageComponent {
	private static final long serialVersionUID = 8306642137791826056L;

	private VerticalLayout rootLayout;
	
	public FormManager() {
		rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();
		setCompositionRoot(rootLayout);
		
		setSizeFull();
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

}

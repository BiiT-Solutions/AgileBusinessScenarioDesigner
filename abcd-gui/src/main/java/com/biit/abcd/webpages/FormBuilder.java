package com.biit.abcd.webpages;

import com.biit.abcd.webpages.components.FormTreeTable;
import com.biit.abcd.webpages.components.WebPageComponent;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;

public class FormBuilder extends WebPageComponent {

	private static final long serialVersionUID = 3237410805898133935L;

	private VerticalLayout rootLayout;
	private FormTreeTable formTreeTable;

	public FormBuilder() {
		rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();
		setCompositionRoot(rootLayout);
		setSizeFull();

		formTreeTable = new FormTreeTable();
		formTreeTable.setSizeFull();
		rootLayout.addComponent(formTreeTable);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

}

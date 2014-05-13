package com.biit.abcd.webpages.elements.treetable;

import com.biit.abcd.persistence.entity.TreeObject;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;

public abstract class PropertiesComponent extends CustomComponent {
	private static final long serialVersionUID = 4900379725073491238L;

	private FormLayout rootLayout;

	public PropertiesComponent() {
		rootLayout = new FormLayout();
		rootLayout.setImmediate(true);
		
		setCompositionRoot(rootLayout);
		setSizeFull();
	}
	
	protected FormLayout getFormLayout(){
		return rootLayout;
	}
	
	public abstract void setElement(TreeObject element);

}

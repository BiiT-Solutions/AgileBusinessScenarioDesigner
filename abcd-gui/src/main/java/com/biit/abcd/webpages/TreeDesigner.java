package com.biit.abcd.webpages;

import java.util.List;

import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.security.DActivity;
import com.biit.abcd.webpages.components.FormTreeTable;
import com.biit.abcd.webpages.components.FormWebPageComponent;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

public class TreeDesigner extends FormWebPageComponent {
	private static final long serialVersionUID = 3237410805898133935L;

	private FormTreeTable formTreeTable;

	private Form form;

	public TreeDesigner() {
		formTreeTable = new FormTreeTable();
		formTreeTable.setSizeFull();
		getWorkingAreaLayout().addComponent(formTreeTable);
	}

	@Override
	public void securedEnter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setForm(Form form) {
		this.form = form;
	}

	@Override
	public Form getForm() {
		return form;
	}

	@Override
	public List<DActivity> accessAuthorizationsRequired() {
		return null;
	}

}

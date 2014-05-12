package com.biit.abcd.webpages;

import java.util.List;

import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.TreeObject;
import com.biit.abcd.security.DActivity;
import com.biit.abcd.webpages.components.FormWebPageComponent;
import com.biit.abcd.webpages.elements.treetable.FormTreeTable;
import com.biit.abcd.webpages.elements.treetable.TreeTableUpperMenu;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

public class TreeDesigner extends FormWebPageComponent {
	private static final long serialVersionUID = 3237410805898133935L;
	private FormTreeTable formTreeTable;
	private Form form;
	private TreeTableUpperMenu upperMenu;

	public TreeDesigner() {
		this.upperMenu = createUpperMenu();
		setUpperMenu(upperMenu);
	}

	@Override
	public void securedEnter(ViewChangeEvent event) {
		formTreeTable = new FormTreeTable();
		formTreeTable.setSizeFull();
		getWorkingAreaLayout().addComponent(formTreeTable);
		formTreeTable.addValueChangeListener(new ValueChangeListener() {

			private static final long serialVersionUID = 5598877051361847210L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				updateUpperMenu(formTreeTable.getValue());
			}
		});
	}

	@Override
	public void setForm(Form form) {
		this.form = form;
		formTreeTable.setForm(form);
	}

	@Override
	public Form getForm() {
		return form;
	}

	@Override
	public List<DActivity> accessAuthorizationsRequired() {
		return null;
	}

	private void updateUpperMenu(TreeObject selectedObject) {
		upperMenu.setEnabledButtons(selectedObject);
	}

	private TreeTableUpperMenu createUpperMenu() {
		TreeTableUpperMenu upperMenu = new TreeTableUpperMenu();
		return upperMenu;
	}

}

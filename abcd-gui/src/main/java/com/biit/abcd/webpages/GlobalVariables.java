package com.biit.abcd.webpages;

import java.util.List;

import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.security.DActivity;
import com.biit.abcd.webpages.components.FormWebPageComponent;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;

public class GlobalVariables extends FormWebPageComponent {
	private static final long serialVersionUID = 6042328256995069412L;

	private HorizontalLayout rootLayout;
	private Table variableTable;
	private Table variableData;

	public GlobalVariables() {
		rootLayout = new HorizontalLayout();
		rootLayout.setSizeFull();
		rootLayout.setMargin(true);
		rootLayout.setSpacing(true);

		variableTable = new Table();
		variableData = new Table();
		
		variableTable.setSizeFull();
		variableData.setSizeFull();

		rootLayout.addComponent(variableTable);
		rootLayout.addComponent(variableData);

		getWorkingAreaLayout().addComponent(rootLayout);
	}

	@Override
	public void securedEnter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<DActivity> accessAuthorizationsRequired() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setForm(Form form) {
		// TODO Auto-generated method stub

	}

	@Override
	public Form getForm() {
		// TODO Auto-generated method stub
		return null;
	}

}

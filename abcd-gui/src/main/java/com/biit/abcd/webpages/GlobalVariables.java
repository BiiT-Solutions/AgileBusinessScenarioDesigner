package com.biit.abcd.webpages;

import java.util.List;

import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.security.DActivity;
import com.biit.abcd.webpages.components.FormWebPageComponent;
import com.biit.abcd.webpages.components.UpperMenu;
import com.biit.abcd.webpages.elements.globalvariables.GlobalVariablesTable;
import com.biit.abcd.webpages.elements.globalvariables.GlobalVariablesUpperMenu;
import com.biit.abcd.webpages.elements.globalvariables.VariableDataTable;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;

public class GlobalVariables extends FormWebPageComponent {
	private static final long serialVersionUID = 6042328256995069412L;

	private HorizontalLayout rootLayout;
	private GlobalVariablesTable variableTable;
	private VariableDataTable variableData;

	public GlobalVariables() {
		rootLayout = new HorizontalLayout();
		rootLayout.setSizeFull();
		rootLayout.setMargin(true);
		rootLayout.setSpacing(true);

		variableTable = new GlobalVariablesTable();
		variableData = new VariableDataTable();

		variableTable.setSizeFull();
		variableData.setSizeFull();

		rootLayout.addComponent(variableTable);
		rootLayout.addComponent(variableData);

		getWorkingAreaLayout().addComponent(rootLayout);
		setUpperMenu(createUpperMenu());
	}

	private UpperMenu createUpperMenu() {
		GlobalVariablesUpperMenu upperMenu = new GlobalVariablesUpperMenu();
		upperMenu.addAddVariableButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = -4843889679428803021L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub

			}
		});
		upperMenu.addRemoveVariableButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = -6942516382918522958L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub

			}
		});
		upperMenu.addAddValueButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = 2087675518126425145L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub

			}
		});
		upperMenu.addRemoveValueButtonClickListener(new ClickListener() {
			private static final long serialVersionUID = 9099439032797611211L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub

			}
		});
		return upperMenu;
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

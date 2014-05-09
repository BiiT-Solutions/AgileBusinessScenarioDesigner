package com.biit.abcd.webpages;

import java.util.Arrays;
import java.util.List;

import com.biit.abcd.security.DActivity;
import com.biit.abcd.webpages.components.SecuredWebPageComponent;
import com.biit.abcd.webpages.elements.formTable.FormsCollapsibleTable;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class FormManager extends SecuredWebPageComponent {
	private static final long serialVersionUID = 8306642137791826056L;
	private FormsCollapsibleTable formTable;

	private VerticalLayout rootLayout;

	public FormManager() {
		rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();
		setCompositionRoot(rootLayout);

		setSizeFull();
	}

	@Override
	public void enter(ViewChangeEvent event) {
		Panel mainPanel = new Panel();

		rootLayout.addComponent(mainPanel);
		rootLayout.setComponentAlignment(mainPanel, Alignment.MIDDLE_CENTER);

		VerticalLayout rootLayout = new VerticalLayout();
		rootLayout.setMargin(true);
		rootLayout.setSpacing(false);

		createTable();

		rootLayout.addComponent(formTable);
		rootLayout.setComponentAlignment(formTable, Alignment.MIDDLE_CENTER);

		// Selected last row must be done after creation of buttons.
		formTable.selectLastUsedForm();

		mainPanel.setContent(rootLayout);
		mainPanel.setWidth("98%");
		mainPanel.setHeight("98%");
	}

	private void createTable() {
		formTable = new FormsCollapsibleTable();
		formTable.initTable();
		formTable.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -119450082492122880L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				// updateButtons();
			}
		});
	}

	@Override
	public List<DActivity> accessAuthorizationsRequired() {
		return Arrays.asList(DActivity.READ);
	}

}

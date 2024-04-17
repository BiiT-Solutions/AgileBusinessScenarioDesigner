package com.biit.abcd.webpages.components;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

public class SelectDiagramWindow extends AcceptCancelWindow {
	private static final long serialVersionUID = -6869984694892715683L;
	private VerticalLayout rootLayout;
	private SelectDiagramTable selectDiagramTable;
	
	public SelectDiagramWindow() {
		setWidth("50%");
		setHeight("50%");
		setClosable(false);
		setModal(true);
		setResizable(false);

		setContent(generateComponent());
	}

	private Component generateComponent() {
		rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();
		rootLayout.setMargin(true);
		rootLayout.setSpacing(true);
		rootLayout.setImmediate(true);
		
		selectDiagramTable = new SelectDiagramTable();
		selectDiagramTable.setSizeFull();
		selectDiagramTable.addRows(UserSessionHandler.getFormController().getForm().getDiagrams());

		selectDiagramTable.sort();
		rootLayout.addComponent(selectDiagramTable);

		return rootLayout;
	}
	
	public Diagram getSelectedDiagram(){
		return selectDiagramTable.getSelectedDiagram();
	}
}
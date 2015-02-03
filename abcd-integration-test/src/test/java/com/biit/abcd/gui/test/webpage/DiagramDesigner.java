package com.biit.abcd.gui.test.webpage;

import com.biit.abcd.gui.test.window.NewDiagramWindow;
import com.vaadin.testbench.elements.TableElement;
import com.vaadin.testbench.elements.TreeTableElement;

public class DiagramDesigner extends LeftTreeTableWebpage {

	private static final String DIAGRAM_TABLE_ID = "diagram-table";
	private NewDiagramWindow newDiagramWindow;

	public DiagramDesigner() {
		super();
		newDiagramWindow = new NewDiagramWindow();
		addWindow(newDiagramWindow);
	}
	
	public void newDiagram(String name){
		getNewButton().click();
		newDiagramWindow.setNameAndAccept(name);
	}

	@Override
	protected String getTableId() {
		return DIAGRAM_TABLE_ID;
	}

	@Override
	public TableElement getTable() {
		TreeTableElement query = $(TreeTableElement.class).id(getTableId());
		return query;
	}
	
}

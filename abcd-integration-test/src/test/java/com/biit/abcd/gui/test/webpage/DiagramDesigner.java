package com.biit.abcd.gui.test.webpage;

import com.biit.abcd.gui.test.window.NewDiagramWindow;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.TableElement;
import com.vaadin.testbench.elements.TreeTableElement;

public class DiagramDesigner extends LeftTreeTableWebpage {

	private static final String DIAGRAM_TABLE_ID = "diagram-table";
	private NewDiagramWindow newDiagramWindow;
	private static final String SAVE_BUTTON = "Save";

	public DiagramDesigner() {
		super();
		newDiagramWindow = new NewDiagramWindow();
		addWindow(newDiagramWindow);
	}
	
	public void newDiagram(String name){
		getNewButton().click();
		newDiagramWindow.setNameAndAccept(name);
	}
	
	public void removeDiagram(int row){
		selectRow(row);
		getRemoveButton().click();
		getProceedWindow().clickAccept();
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

	public void save() {
		$(ButtonElement.class).caption(SAVE_BUTTON).first().click();
	}
}

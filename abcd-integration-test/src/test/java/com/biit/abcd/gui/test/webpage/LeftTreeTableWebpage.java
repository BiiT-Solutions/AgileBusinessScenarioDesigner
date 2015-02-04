package com.biit.abcd.gui.test.webpage;

import com.vaadin.testbench.ElementQuery;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.TableElement;

public abstract class LeftTreeTableWebpage extends AbcdCommonWebpage {

	private static final String BUTTON_NEW = "New";
	private static final String BUTTON_REMOVE = "Remove";

	public LeftTreeTableWebpage() {
		super();
	}

	public ButtonElement getNewButton() {
		ElementQuery<ButtonElement> button = $(ButtonElement.class).caption(BUTTON_NEW);
		if (button.exists()) {
			return button.first();
		} else {
			return null;
		}
	}
	
	public ButtonElement getRemoveButton() {
		ElementQuery<ButtonElement> button = $(ButtonElement.class).caption(BUTTON_REMOVE);
		if (button.exists()) {
			return button.first();
		} else {
			return null;
		}
	}

	public abstract TableElement getTable();

	protected abstract String getTableId();

	public void selectRow(int row) {
		TableElement diagramTable = getTable();
		diagramTable.getCell(row, 0).click();
		diagramTable.getCell(row, 0).waitForVaadin();
	}

	public boolean isRowSelected(int diagramRow) {
		return getTable().getRow(diagramRow).getAttribute(CSS_CLASS).contains(TABLE_ROW_SELECTED);
	}

}

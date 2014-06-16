package com.biit.abcd.webpages.elements.formulaeditor;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;

public class FormulaEditor extends CustomComponent {
	private static final long serialVersionUID = 6180990439200708260L;
	private static final String CLASSNAME_FORMULA_LAYOUT = "v-formula-layout";

	private VerticalLayout rootLayout;
	private VerticalLayout formulaLayout;
	private FormulaPortComponent clickedFormulaPort;

	public FormulaEditor() {
		rootLayout = new VerticalLayout();
		rootLayout.setSizeFull();
		rootLayout.setMargin(true);

		formulaLayout = new VerticalLayout();
		formulaLayout.setSizeFull();
		formulaLayout.setMargin(true);
		formulaLayout.setStyleName(CLASSNAME_FORMULA_LAYOUT);

		addFormulaExpression(new ExpresionRoot());

		rootLayout.addComponent(formulaLayout);

		setCompositionRoot(rootLayout);
		setSizeFull();
		setImmediate(true);
	}

	public void addFormulaPort(FormulaPortComponent formulaPort) {
		formulaPort.addFormulaPortClickListener(new FormulaPortClickListener() {
			@Override
			public void formulaPortClicked(FormulaPortComponent formulaPort, LayoutClickEvent listener) {
				System.out.println("Formula port Listener: " + formulaPort.getConnectorId());
				setFormulaPortClicked(formulaPort);
			}
		});
		formulaLayout.addComponent(formulaPort);
	}

	public void addFormulaExpression(FormulaExpressionComponent formulaExpressionComponent) {
		formulaExpressionComponent.addFormulaPortClickListener(new FormulaPortClickListener() {
			@Override
			public void formulaPortClicked(FormulaPortComponent formulaPort, LayoutClickEvent listener) {
				// System.out.println("Formula expresion Listener: "+formulaPort.getConnectorId());
				setFormulaPortClicked(formulaPort);
			}
		});
		formulaLayout.addComponent(formulaExpressionComponent);
	}

	private void setFormulaPortClicked(FormulaPortComponent formulaPort) {
		// Removed selected state from old clicked port, and add state to new
		// port.
		if (clickedFormulaPort != null) {
			clickedFormulaPort.setSelected(false);
		}
		clickedFormulaPort = formulaPort;

		if (clickedFormulaPort != null) {
			clickedFormulaPort.setSelected(true);
		}
	}

}

package com.biit.abcd.webpages.elements.formulaeditor;

import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.vaadin.ui.UI;

public class ExpresionEquals extends FormulaExpressionComponent {

	private static final long serialVersionUID = 5629505297232110967L;

	private FormulaPortComponent port1;
	private FormulaPortComponent port2;

	public ExpresionEquals() {
		super();
		port1 = new FormulaPortComponent(FormulaElementType.getAnyType());
		port1.addFormulaPortClickListener(new CustomFormulaPortClickListener());
		port2 = new FormulaPortComponent(FormulaElementType.getAnyType());
		port2.addFormulaPortClickListener(new CustomFormulaPortClickListener());

		addPort(port1);
		addText("==");
		addPort(port2);
	}

	private class CustomFormulaPortClickListener implements FormulaPortClickListener {
		@Override
		public void formulaPortClicked(final FormulaPortComponent formulaPort) {
			PortContentEditor portContentEditor = new PortContentEditor(formulaPort.getAcceptedTypes());
			portContentEditor.addAcceptAcctionListener(new AcceptActionListener() {
				@Override
				public void acceptAction(AcceptCancelWindow window) {
					PortContentEditor portContentEditor = (PortContentEditor) window;
					formulaPort.setValue(portContentEditor.getValue());
					window.close();
				}
			});
			UI.getCurrent().addWindow(portContentEditor);
		}
	}
}

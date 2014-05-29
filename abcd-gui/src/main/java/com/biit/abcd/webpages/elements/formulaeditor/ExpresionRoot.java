package com.biit.abcd.webpages.elements.formulaeditor;

import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.vaadin.ui.UI;

public class ExpresionRoot extends FormulaExpressionComponent {
	private static final long serialVersionUID = 5306098446259586162L;
	private FormulaPortComponent port1;

	public ExpresionRoot() {
		super();
		port1 = new FormulaPortComponent(FormulaElementType.getAnyType());
		port1.addFormulaPortClickListener(new CustomFormulaPortClickListener());

		addText("ROOT[");
		addPort(port1);
		addText("]");
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

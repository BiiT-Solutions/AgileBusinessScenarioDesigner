package com.biit.abcd.webpages.elements.formulaeditor;

public class CustomFormulaPortClickListener extends ChangeValuePortClickListener {
	public CustomFormulaPortClickListener(FormulaPortComponent port) {
		super(new PortContentEditor(port.getAcceptedTypes()), true);
	}
}

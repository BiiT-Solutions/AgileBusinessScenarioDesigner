package com.biit.abcd.webpages.elements.formulaeditor.operators;

import com.biit.abcd.webpages.elements.formulaeditor.CustomFormulaPortClickListener;
import com.biit.abcd.webpages.elements.formulaeditor.FormulaExpressionComponent;
import com.biit.abcd.webpages.elements.formulaeditor.FormulaPortComponent;
import com.biit.abcd.webpages.elements.formulaeditor.Type;

public class OperatorNot extends FormulaExpressionComponent {
	private static final long serialVersionUID = -6555193604954455261L;
	private FormulaPortComponent port;

	public OperatorNot() {
		super();
		port = new FormulaPortComponent(Type.getAnyType());
		port.addFormulaPortClickListener(new CustomFormulaPortClickListener(port));

		addText("(!");
		addPort(port);
		addText(")");
	}

}
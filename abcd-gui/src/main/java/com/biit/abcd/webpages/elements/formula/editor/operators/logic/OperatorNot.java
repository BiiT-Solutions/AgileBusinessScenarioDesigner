package com.biit.abcd.webpages.elements.formula.editor.operators.logic;

import com.biit.abcd.webpages.elements.formula.editor.CustomFormulaPortClickListener;
import com.biit.abcd.webpages.elements.formula.editor.FormulaExpressionComponent;
import com.biit.abcd.webpages.elements.formula.editor.FormulaPortComponent;
import com.biit.abcd.webpages.elements.formula.editor.Type;

public class OperatorNot extends FormulaExpressionComponent {
	private static final long serialVersionUID = -6555193604954455261L;
	private FormulaPortComponent port;

	public OperatorNot() {
		super();
		port = new FormulaPortComponent(Type.getComparisonAndLogic());
		port.addFormulaPortClickListener(new CustomFormulaPortClickListener(port));

		addText("(!");
		addPort(port);
		addText(")");
	}

	@Override
	public Type getReturnType() {
		return Type.LOGIC;
	}
}
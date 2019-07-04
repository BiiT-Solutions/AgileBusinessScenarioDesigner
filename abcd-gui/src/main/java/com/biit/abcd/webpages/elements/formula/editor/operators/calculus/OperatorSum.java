package com.biit.abcd.webpages.elements.formula.editor.operators.calculus;

import com.biit.abcd.webpages.elements.formula.editor.CustomFormulaPortClickListener;
import com.biit.abcd.webpages.elements.formula.editor.FormulaExpressionComponent;
import com.biit.abcd.webpages.elements.formula.editor.FormulaPortComponent;
import com.biit.abcd.webpages.elements.formula.editor.Type;
import com.vaadin.ui.Label;

public class OperatorSum extends FormulaExpressionComponent {
	private static final long serialVersionUID = -5720791716017062179L;
	private FormulaPortComponent leftPort;
	private FormulaPortComponent rightPort;
	protected Label middleText;

	public OperatorSum() {
		super();
		leftPort = new FormulaPortComponent(Type.CALCULATION);
		leftPort.addFormulaPortClickListener(new CustomFormulaPortClickListener(leftPort));
		rightPort = new FormulaPortComponent(Type.CALCULATION);
		rightPort.addFormulaPortClickListener(new CustomFormulaPortClickListener(rightPort));

		addText("( ");
		addPort(leftPort);
		middleText = addText(" + ");
		addPort(rightPort);
		addText(" )");
	}

	@Override
	public Type getReturnType() {
		return Type.CALCULATION;
	}


}

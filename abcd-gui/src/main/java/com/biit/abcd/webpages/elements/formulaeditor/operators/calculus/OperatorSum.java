package com.biit.abcd.webpages.elements.formulaeditor.operators.calculus;

import com.biit.abcd.webpages.elements.formulaeditor.CustomFormulaPortClickListener;
import com.biit.abcd.webpages.elements.formulaeditor.FormulaExpressionComponent;
import com.biit.abcd.webpages.elements.formulaeditor.FormulaPortComponent;
import com.biit.abcd.webpages.elements.formulaeditor.Type;
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

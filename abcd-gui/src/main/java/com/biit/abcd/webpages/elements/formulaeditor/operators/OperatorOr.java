package com.biit.abcd.webpages.elements.formulaeditor.operators;

import com.biit.abcd.webpages.elements.formulaeditor.CustomFormulaPortClickListener;
import com.biit.abcd.webpages.elements.formulaeditor.FormulaExpressionComponent;
import com.biit.abcd.webpages.elements.formulaeditor.FormulaPortComponent;
import com.biit.abcd.webpages.elements.formulaeditor.Type;

public class OperatorOr extends FormulaExpressionComponent {
	private static final long serialVersionUID = 4734340794207026748L;
	private FormulaPortComponent leftPort;
	private FormulaPortComponent rightPort;

	public OperatorOr() {
		super();
		leftPort = new FormulaPortComponent(Type.getAnyType());
		leftPort.addFormulaPortClickListener(new CustomFormulaPortClickListener(leftPort));
		rightPort = new FormulaPortComponent(Type.getAnyType());
		rightPort.addFormulaPortClickListener(new CustomFormulaPortClickListener(rightPort));

		addText("( ");
		addPort(leftPort);
		addText(" OR ");
		addPort(rightPort);
		addText(" )");
	}

}
package com.biit.abcd.webpages.elements.formulaeditor.operators;

import com.biit.abcd.webpages.elements.formulaeditor.CustomFormulaPortClickListener;
import com.biit.abcd.webpages.elements.formulaeditor.FormulaExpressionComponent;
import com.biit.abcd.webpages.elements.formulaeditor.FormulaPortComponent;
import com.biit.abcd.webpages.elements.formulaeditor.Type;

public class OperatorAnd extends FormulaExpressionComponent {

	private static final long serialVersionUID = -5767367051092400779L;
	private FormulaPortComponent leftPort;
	private FormulaPortComponent rightPort;

	public OperatorAnd() {
		super();
		leftPort = new FormulaPortComponent(Type.getAnyType());
		leftPort.addFormulaPortClickListener(new CustomFormulaPortClickListener(leftPort));
		rightPort = new FormulaPortComponent(Type.getAnyType());
		rightPort.addFormulaPortClickListener(new CustomFormulaPortClickListener(rightPort));

		addText("( ");
		addPort(leftPort);
		addText(" AND ");
		addPort(rightPort);
		addText(" )");
	}

}
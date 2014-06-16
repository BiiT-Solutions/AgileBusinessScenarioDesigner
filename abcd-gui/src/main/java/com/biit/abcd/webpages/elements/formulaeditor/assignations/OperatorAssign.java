package com.biit.abcd.webpages.elements.formulaeditor.assignations;

import com.biit.abcd.webpages.elements.formulaeditor.CustomFormulaPortClickListener;
import com.biit.abcd.webpages.elements.formulaeditor.FormulaExpressionComponent;
import com.biit.abcd.webpages.elements.formulaeditor.FormulaPortComponent;
import com.biit.abcd.webpages.elements.formulaeditor.Type;
import com.vaadin.ui.Label;

public class OperatorAssign extends FormulaExpressionComponent {

	private static final long serialVersionUID = 550860447581821953L;

	private FormulaPortComponent leftPort;
	protected Label assignText;
	private FormulaPortComponent rightPort;

	public OperatorAssign() {
		super();
		leftPort = new FormulaPortComponent(Type.VARIABLE);
		leftPort.addFormulaPortClickListener(new CustomFormulaPortClickListener(leftPort));
		rightPort = new FormulaPortComponent(Type.CALCULATION);
		rightPort.addFormulaPortClickListener(new CustomFormulaPortClickListener(rightPort));

		addPort(leftPort);
		assignText = addText("=");
		addPort(rightPort);
	}

	@Override
	public Type getReturnType() {
		return Type.CALCULATION;
	}

}

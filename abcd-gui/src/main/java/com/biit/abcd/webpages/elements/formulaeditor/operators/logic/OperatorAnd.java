package com.biit.abcd.webpages.elements.formulaeditor.operators.logic;

import com.biit.abcd.webpages.elements.formulaeditor.CustomFormulaPortClickListener;
import com.biit.abcd.webpages.elements.formulaeditor.FormulaExpressionComponent;
import com.biit.abcd.webpages.elements.formulaeditor.FormulaPortComponent;
import com.biit.abcd.webpages.elements.formulaeditor.Type;
import com.vaadin.ui.Label;

public class OperatorAnd extends FormulaExpressionComponent {

	private static final long serialVersionUID = -5767367051092400779L;
	private FormulaPortComponent leftPort;
	protected Label logicLabel; 
	private FormulaPortComponent rightPort;

	public OperatorAnd() {
		super();
		leftPort = new FormulaPortComponent(Type.getComparisonAndLogic());
		leftPort.addFormulaPortClickListener(new CustomFormulaPortClickListener(leftPort));
		rightPort = new FormulaPortComponent(Type.getComparisonAndLogic());
		rightPort.addFormulaPortClickListener(new CustomFormulaPortClickListener(rightPort));

		addText("( ");
		addPort(leftPort);
		logicLabel = addText(" AND ");
		addPort(rightPort);
		addText(" )");
	}

	@Override
	public Type getReturnType() {
		return Type.LOGIC;
	}
}
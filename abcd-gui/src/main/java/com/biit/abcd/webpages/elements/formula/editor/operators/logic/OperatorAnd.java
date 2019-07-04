package com.biit.abcd.webpages.elements.formula.editor.operators.logic;

import com.biit.abcd.webpages.elements.formula.editor.CustomFormulaPortClickListener;
import com.biit.abcd.webpages.elements.formula.editor.FormulaExpressionComponent;
import com.biit.abcd.webpages.elements.formula.editor.FormulaPortComponent;
import com.biit.abcd.webpages.elements.formula.editor.Type;
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
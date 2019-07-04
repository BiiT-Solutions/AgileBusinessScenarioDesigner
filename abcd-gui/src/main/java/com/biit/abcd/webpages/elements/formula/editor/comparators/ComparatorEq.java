package com.biit.abcd.webpages.elements.formula.editor.comparators;

import com.biit.abcd.webpages.elements.formula.editor.CustomFormulaPortClickListener;
import com.biit.abcd.webpages.elements.formula.editor.FormulaExpressionComponent;
import com.biit.abcd.webpages.elements.formula.editor.FormulaPortComponent;
import com.biit.abcd.webpages.elements.formula.editor.Type;
import com.vaadin.ui.Label;



public class ComparatorEq extends FormulaExpressionComponent {

	private static final long serialVersionUID = 5629505297232110967L;

	private FormulaPortComponent port1;
	protected Label comparatorLabel;
	private FormulaPortComponent port2;

	public ComparatorEq() {
		super();
		port1 = new FormulaPortComponent(Type.getComparisonAndLogic());
		port1.addFormulaPortClickListener(new CustomFormulaPortClickListener(port1));
		port2 = new FormulaPortComponent(Type.getComparisonAndLogic());
		port2.addFormulaPortClickListener(new CustomFormulaPortClickListener(port2));

		addPort(port1);
		comparatorLabel = addText("==");
		addPort(port2);
	}
	
	@Override
	public Type getReturnType() {
		return Type.COMPARISON;
	}
}

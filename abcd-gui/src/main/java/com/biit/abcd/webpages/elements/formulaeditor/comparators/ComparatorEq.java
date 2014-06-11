package com.biit.abcd.webpages.elements.formulaeditor.comparators;

import com.biit.abcd.webpages.elements.formulaeditor.CustomFormulaPortClickListener;
import com.biit.abcd.webpages.elements.formulaeditor.FormulaExpressionComponent;
import com.biit.abcd.webpages.elements.formulaeditor.FormulaPortComponent;
import com.biit.abcd.webpages.elements.formulaeditor.Type;
import com.vaadin.ui.Label;



public class ComparatorEq extends FormulaExpressionComponent {

	private static final long serialVersionUID = 5629505297232110967L;

	private FormulaPortComponent port1;
	protected Label comparatorLabel;
	private FormulaPortComponent port2;

	public ComparatorEq() {
		super();
		port1 = new FormulaPortComponent(Type.getAnyType());
		port1.addFormulaPortClickListener(new CustomFormulaPortClickListener(port1));
		port2 = new FormulaPortComponent(Type.getAnyType());
		port2.addFormulaPortClickListener(new CustomFormulaPortClickListener(port2));

		addPort(port1);
		comparatorLabel = addText("==");
		addPort(port2);
	}
}

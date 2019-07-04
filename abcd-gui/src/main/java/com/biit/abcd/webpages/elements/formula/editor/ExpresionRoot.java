package com.biit.abcd.webpages.elements.formula.editor;

public class ExpresionRoot extends FormulaExpressionComponent {
	private static final long serialVersionUID = 5306098446259586162L;
	private FormulaPortComponent port1;
	private FormulaPortComponent port2;

	public ExpresionRoot() {
		super();
		port1 = new FormulaPortComponent(Type.getVoidComparisonLogicOrReference());
		port1.addFormulaPortClickListener(new CustomFormulaPortClickListener(port1));
		port2 = new FormulaPortComponent(Type.getVoidAssignationCalculation());
		port2.addFormulaPortClickListener(new CustomFormulaPortClickListener(port2));

		addText("WHEN [");
		addPort(port1);
		addText("]");
		addLine();
		addText("THEN [");
		addPort(port2);
		addText(" ]");
	}

	@Override
	public Type getReturnType() {
		return Type.EXPRESSION;
	}
}

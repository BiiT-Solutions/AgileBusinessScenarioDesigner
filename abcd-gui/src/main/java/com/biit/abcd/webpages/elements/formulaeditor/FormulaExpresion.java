package com.biit.abcd.webpages.elements.formulaeditor;

import com.biit.abcd.logger.AbcdLogger;

public enum FormulaExpresion {

	// CONSTANT(ExpresionConstant.class),
	// ASSIGNATION,
	// Logic Operator
	EQ(ExpresionEquals.class);
	// NEQ,
	// GT,
	// LT,
	// GE,
	// LE,
	// AND,
	// OR,
	// NOT,
	// XOR,
	// NAND,
	// PARENTHESIS,
	// //Math operator
	// ABS,
	// SUM,
	// SUB,
	// DIV,
	// MOD,

	private Class<? extends FormulaExpressionComponent> expresionClass;

	private FormulaExpresion(Class<? extends FormulaExpressionComponent> expresionClass) {
		this.expresionClass = expresionClass;
	}

	public FormulaExpressionComponent getNewFormulaExpressionComponent() {
		try {
			return expresionClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			AbcdLogger.errorMessage(FormulaExpresion.class.getName(), e);
			return null;
		}
	}

	public Class<? extends FormulaExpressionComponent> getExpresionClass() {
		return expresionClass;
	}
}

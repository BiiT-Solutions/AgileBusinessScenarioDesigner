package com.biit.abcd.webpages.elements.formulaeditor;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.webpages.elements.formulaeditor.assignations.AssignDiv;
import com.biit.abcd.webpages.elements.formulaeditor.assignations.AssignMod;
import com.biit.abcd.webpages.elements.formulaeditor.assignations.AssignMul;
import com.biit.abcd.webpages.elements.formulaeditor.assignations.AssignSub;
import com.biit.abcd.webpages.elements.formulaeditor.assignations.AssignSum;
import com.biit.abcd.webpages.elements.formulaeditor.assignations.OperatorAssign;
import com.biit.abcd.webpages.elements.formulaeditor.comparators.ComparatorEq;
import com.biit.abcd.webpages.elements.formulaeditor.comparators.ComparatorGe;
import com.biit.abcd.webpages.elements.formulaeditor.comparators.ComparatorGt;
import com.biit.abcd.webpages.elements.formulaeditor.comparators.ComparatorLe;
import com.biit.abcd.webpages.elements.formulaeditor.comparators.ComparatorLt;
import com.biit.abcd.webpages.elements.formulaeditor.comparators.ComparatorNe;
import com.biit.abcd.webpages.elements.formulaeditor.operators.OperatorAnd;
import com.biit.abcd.webpages.elements.formulaeditor.operators.OperatorDiv;
import com.biit.abcd.webpages.elements.formulaeditor.operators.OperatorMod;
import com.biit.abcd.webpages.elements.formulaeditor.operators.OperatorMul;
import com.biit.abcd.webpages.elements.formulaeditor.operators.OperatorNot;
import com.biit.abcd.webpages.elements.formulaeditor.operators.OperatorOr;
import com.biit.abcd.webpages.elements.formulaeditor.operators.OperatorSub;
import com.biit.abcd.webpages.elements.formulaeditor.operators.OperatorSum;

public enum FormulaExpresion {

	// CONSTANT(ExpresionConstant.class),
	// Assignation Need value / Return value
	ASSIGN(OperatorAssign.class),
	ASSIGN_SUM(AssignSum.class),
	ASSIGN_SUB(AssignSub.class),
	ASSIGN_MUL(AssignMul.class),
	ASSIGN_DIV(AssignDiv.class),
	ASSIGN_MOD(AssignMod.class),
	// Comparation Need value / Return logic
	COMP_EQ(ComparatorEq.class), 
	COMP_NE(ComparatorNe.class),
	COMP_LT(ComparatorLt.class),
	COMP_GT(ComparatorGt.class),
	COMP_LE(ComparatorLe.class),
	COMP_GE(ComparatorGe.class),
	// Need value / Return value
	OP_SUM(OperatorSum.class),
	OP_SUB(OperatorSub.class),
	OP_MUL(OperatorMul.class),
	OP_DIV(OperatorDiv.class),
	OP_MOD(OperatorMod.class),
	// Need logic / Return logic
	OP_AND(OperatorAnd.class),
	OP_OR(OperatorOr.class), 
	OP_NOT(OperatorNot.class);	

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

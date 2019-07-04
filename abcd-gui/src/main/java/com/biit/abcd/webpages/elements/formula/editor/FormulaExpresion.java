package com.biit.abcd.webpages.elements.formula.editor;

import java.util.HashSet;
import java.util.Set;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.webpages.elements.formula.editor.assignations.AssignDiv;
import com.biit.abcd.webpages.elements.formula.editor.assignations.AssignMod;
import com.biit.abcd.webpages.elements.formula.editor.assignations.AssignMul;
import com.biit.abcd.webpages.elements.formula.editor.assignations.AssignSub;
import com.biit.abcd.webpages.elements.formula.editor.assignations.AssignSum;
import com.biit.abcd.webpages.elements.formula.editor.assignations.OperatorAssign;
import com.biit.abcd.webpages.elements.formula.editor.comparators.ComparatorEq;
import com.biit.abcd.webpages.elements.formula.editor.comparators.ComparatorGe;
import com.biit.abcd.webpages.elements.formula.editor.comparators.ComparatorGt;
import com.biit.abcd.webpages.elements.formula.editor.comparators.ComparatorLe;
import com.biit.abcd.webpages.elements.formula.editor.comparators.ComparatorLt;
import com.biit.abcd.webpages.elements.formula.editor.comparators.ComparatorNe;
import com.biit.abcd.webpages.elements.formula.editor.operators.calculus.OperatorDiv;
import com.biit.abcd.webpages.elements.formula.editor.operators.calculus.OperatorMod;
import com.biit.abcd.webpages.elements.formula.editor.operators.calculus.OperatorMul;
import com.biit.abcd.webpages.elements.formula.editor.operators.calculus.OperatorSub;
import com.biit.abcd.webpages.elements.formula.editor.operators.calculus.OperatorSum;
import com.biit.abcd.webpages.elements.formula.editor.operators.logic.OperatorAnd;
import com.biit.abcd.webpages.elements.formula.editor.operators.logic.OperatorNot;
import com.biit.abcd.webpages.elements.formula.editor.operators.logic.OperatorOr;

public enum FormulaExpresion {

	// CONSTANT(ExpresionConstant.class),
	// Assignation Need variable | Need value | Return value
	ASSIGN(OperatorAssign.class),
	ASSIGN_SUM(AssignSum.class),
	ASSIGN_SUB(AssignSub.class),
	ASSIGN_MUL(AssignMul.class),
	ASSIGN_DIV(AssignDiv.class),
	ASSIGN_MOD(AssignMod.class),
	// Comparation Need value or logic / Return logic
	COMP_EQ(ComparatorEq.class), 
	COMP_NE(ComparatorNe.class),
	COMP_LT(ComparatorLt.class),
	COMP_GT(ComparatorGt.class),
	COMP_LE(ComparatorLe.class),
	COMP_GE(ComparatorGe.class),
	// Calculus Operation Need value / Return value
	OP_SUM(OperatorSum.class),
	OP_SUB(OperatorSub.class),
	OP_MUL(OperatorMul.class),
	OP_DIV(OperatorDiv.class),
	OP_MOD(OperatorMod.class),
	// Logic Operation Need logic / Return logic
	OP_AND(OperatorAnd.class),
	OP_OR(OperatorOr.class), 
	OP_NOT(OperatorNot.class);
	
	public static Set<FormulaExpresion> getAssignationExpression(){
		Set<FormulaExpresion> expressions = new HashSet<>();
		expressions.add(ASSIGN);
		expressions.add(ASSIGN_SUM);
		expressions.add(ASSIGN_SUB);
		expressions.add(ASSIGN_MUL);
		expressions.add(ASSIGN_DIV);
		expressions.add(ASSIGN_MOD);
		return expressions;
	}
	
	public static Set<FormulaExpresion> getComparationExpression(){
		Set<FormulaExpresion> expressions = new HashSet<>();
		expressions.add(COMP_EQ);
		expressions.add(COMP_NE);
		expressions.add(COMP_LT);
		expressions.add(COMP_GT);
		expressions.add(COMP_LE);
		expressions.add(COMP_GE);
		return expressions;
	}
	
	public static Set<FormulaExpresion> getOperationExpression(){
		Set<FormulaExpresion> expressions = new HashSet<>();
		expressions.add(OP_SUM);
		expressions.add(OP_SUB);
		expressions.add(OP_MUL);
		expressions.add(OP_DIV);
		expressions.add(OP_MOD);
		return expressions;
	}
	
	public static Set<FormulaExpresion> getLogicExpression(){
		Set<FormulaExpresion> expressions = new HashSet<>();
		expressions.add(OP_AND);
		expressions.add(OP_OR);
		expressions.add(OP_NOT);
		return expressions;
	}
	
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

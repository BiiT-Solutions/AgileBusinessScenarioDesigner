package com.biit.abcd.persistence.entity.expressions;

import java.util.Comparator;
import java.util.List;

/**
 * Order an expression chain by the tree objects that compose the expression. Used in Table Decision Rules. Example:
 * question1 = answer1
 * question1 = answer2
 * question2 = answer1
 * question2 = answer2
 * .... 
 */
public class ExpressionChainHierarchyComparator implements Comparator<ExpressionChain> {

	@Override
	public int compare(ExpressionChain expressionChain1, ExpressionChain expressionChain2) {
		if (expressionChain1 != null && expressionChain2.getExpression() != null) {
			List<Expression> expressions1 = expressionChain1.getExpressions();
			List<Expression> expressions2 = expressionChain2.getExpressions();

			// Compare expressions one by one.
			for (int i = 0; i < Math.min(expressions1.size(), expressions2.size()); i++) {
				int comparation = 0;
				if (expressions1.get(i) instanceof ExpressionValueTreeObjectReference
						&& expressions2.get(i) instanceof ExpressionValueTreeObjectReference) {
					comparation = ((ExpressionValueTreeObjectReference) expressions1.get(i)).getReference().compareTo(
							((ExpressionValueTreeObjectReference) expressions2.get(i)).getReference());
				} else if (expressions1.get(i) instanceof ExpressionChain
						&& expressions2.get(i) instanceof ExpressionChain) {
					comparation = new ExpressionChainHierarchyComparator().compare(
							(ExpressionChain) expressions1.get(i), (ExpressionChain) expressions2.get(i));
				}
				if (comparation != 0) {
					return comparation;
				}
			}
			// No differences among TreeObjects. Use representation as comparation
			return expressionChain1.getRepresentation().compareTo(expressionChain2.getRepresentation());
		}
		if (expressionChain1 != null) {
			return -1;
		}
		return 1;
	}
}

package com.biit.abcd.webpages.entity.comparator;

import java.io.Serializable;
import java.util.Comparator;

import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.biit.form.utils.TreeObjectHierarchyComparator;

public class TableRuleRowComparator implements Comparator<TableRuleRow>, Serializable {
	private static final long serialVersionUID = 2071837707152586614L;

	@Override
	public int compare(TableRuleRow o1, TableRuleRow o2) {
		if (o1.getConditions().getExpressions().size() > 0 && o2.getConditions().getExpressions().size() > 0) {
			Expression expression1 = o1.getConditions().getExpressions().get(0);
			Expression expression2 = o2.getConditions().getExpressions().get(0);
			if (expression1 instanceof ExpressionValueTreeObjectReference) {
				if (expression2 instanceof ExpressionValueTreeObjectReference) {
					return new TreeObjectHierarchyComparator().compare(
							((ExpressionValueTreeObjectReference) expression1).getReference(),
							((ExpressionValueTreeObjectReference) expression2).getReference());
				}
				// First null values.
				return 1;
			} else {
				if (expression2 instanceof ExpressionValueTreeObjectReference) {
					// First null values.
					return -1;
				}
				return 0;
			}
		} else {
			// First empty expressions.
			return o1.getConditions().getExpressions().size() - o2.getConditions().getExpressions().size();
		}
	}
}

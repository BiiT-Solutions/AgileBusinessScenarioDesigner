package com.biit.abcd.persistence.entity.expressions;

import java.util.Comparator;

public class ExpressionSort implements Comparator<Expression> {
	@Override
	public int compare(Expression o1, Expression o2) {
		return (o1.getSortSeq() < o2.getSortSeq() ? -1 : (o1 == o2 ? 0 : 1));
	}
}

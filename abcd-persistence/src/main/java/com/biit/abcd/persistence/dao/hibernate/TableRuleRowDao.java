package com.biit.abcd.persistence.dao.hibernate;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.ITableRuleRowDao;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.biit.persistence.dao.hibernate.GenericDao;

@Repository
public class TableRuleRowDao extends GenericDao<TableRuleRow> implements ITableRuleRowDao {
	
	public TableRuleRowDao() {
		super(TableRuleRow.class);
	}

	@Override
	protected void initializeSets(List<TableRuleRow> elements) {
		// Nothing to do, all eager.
	}

	@Override
	public TableRuleRow makePersistent(TableRuleRow entity) {
		// For solving Hibernate bug
		// https://hibernate.atlassian.net/browse/HHH-1268 we cannot use the
		// list of children
		// with @Orderby or @OrderColumn we use our own order manager.
		entity.getConditions().updateChildrenSortSeqs();
		entity.getAction().updateChildrenSortSeqs();
		return super.makePersistent(entity);
	}

	@Override
	public List<TableRuleRow> getAll() {
		List<TableRuleRow> result = super.getAll();
		// For solving Hibernate bug
		// https://hibernate.atlassian.net/browse/HHH-1268 we cannot use the
		// list of children
		// with @Orderby or @OrderColumn we use our own order manager.
		sortChildren(result);
		return result;
	}

	private void sortChildren(List<TableRuleRow> rules) {
		for (TableRuleRow rule : rules) {
			sortChildren(rule.getConditions());
			sortChildren(rule.getAction());
		}
	}

	private void sortChildren(ExpressionChain expressionChain) {
		Collections.sort(expressionChain.getExpressions(), new ExpressionSort());
		for (Expression child : expressionChain.getExpressions()) {
			sortChildren(child);
		}
	}

	private void sortChildren(Expression expression) {
		if (expression instanceof ExpressionChain) {
			ExpressionChain expressionChain = (ExpressionChain) expression;
			Collections.sort(expressionChain.getExpressions(), new ExpressionSort());
			for (Expression child : expressionChain.getExpressions()) {
				sortChildren(child);
			}
		}
	}

	class ExpressionSort implements Comparator<Expression> {
		@Override
		public int compare(Expression o1, Expression o2) {
			return (o1.getSortSeq() < o2.getSortSeq() ? -1 : (o1 == o2 ? 0 : 1));
		}
	}
}

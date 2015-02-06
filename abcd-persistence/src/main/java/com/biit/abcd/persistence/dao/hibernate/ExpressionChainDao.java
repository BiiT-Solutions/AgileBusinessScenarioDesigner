package com.biit.abcd.persistence.dao.hibernate;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.IExpressionChainDao;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionSort;
import com.biit.persistence.dao.exceptions.ElementCannotBePersistedException;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.persistence.dao.hibernate.GenericDao;

@Repository
public class ExpressionChainDao extends GenericDao<ExpressionChain> implements IExpressionChainDao {

	public ExpressionChainDao() {
		super(ExpressionChain.class);
	}

	@Override
	protected void initializeSets(List<ExpressionChain> rules) {
	}

	@Override
	public ExpressionChain makePersistent(ExpressionChain entity) throws UnexpectedDatabaseException, ElementCannotBePersistedException {
		// For solving Hibernate bug
		// https://hibernate.atlassian.net/browse/HHH-1268 we cannot use the
		// list of children
		// with @Orderby or @OrderColumn we use our own order manager.
		entity.updateChildrenSortSeqs();
		return super.makePersistent(entity);
	}

	@Override
	public List<ExpressionChain> getAll() throws UnexpectedDatabaseException {
		List<ExpressionChain> result = super.getAll();
		// For solving Hibernate bug
		// https://hibernate.atlassian.net/browse/HHH-1268 we cannot use the
		// list of children
		// with @Orderby or @OrderColumn we use our own order manager.
		sortChildren(result);
		return result;
	}

	private void sortChildren(List<ExpressionChain> expressionChains) {
		for (ExpressionChain expressionChain : expressionChains) {
			sortChildren(expressionChain);
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
}

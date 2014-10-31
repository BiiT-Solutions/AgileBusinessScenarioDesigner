package com.biit.abcd.persistence.dao.hibernate;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.IRuleDao;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.ExpressionSort;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.persistence.dao.hibernate.GenericDao;

@Repository
public class RuleDao extends GenericDao<Rule> implements IRuleDao {

	public RuleDao() {
		super(Rule.class);
	}

	@Override
	protected void initializeSets(List<Rule> rules) {
	}

	@Override
	public Rule makePersistent(Rule entity) {
		// For solving Hibernate bug
		// https://hibernate.atlassian.net/browse/HHH-1268 we cannot use the
		// list of children
		// with @Orderby or @OrderColumn we use our own order manager.
		entity.getConditions().updateChildrenSortSeqs();
		entity.getActions().updateChildrenSortSeqs();
		return super.makePersistent(entity);
	}

	@Override
	public List<Rule> getAll() {
		List<Rule> result = super.getAll();
		// For solving Hibernate bug
		// https://hibernate.atlassian.net/browse/HHH-1268 we cannot use the
		// list of children
		// with @Orderby or @OrderColumn we use our own order manager.
		sortChildren(result);
		return result;
	}

	private void sortChildren(List<Rule> rules) {
		for (Rule rule : rules) {
			sortChildren(rule.getConditions());
			sortChildren(rule.getActions());
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

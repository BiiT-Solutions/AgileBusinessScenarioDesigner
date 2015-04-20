package com.biit.abcd.persistence.dao.hibernate;

import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.ITableRuleRowDao;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;

@Repository
public class TableRuleRowDao extends AnnotatedGenericDao<TableRuleRow,Long> implements ITableRuleRowDao {

	public TableRuleRowDao() {
		super(TableRuleRow.class);
	}

//	@Override
//	protected void initializeSets(List<TableRuleRow> elements) {
//		// Nothing to do, all eager.
//	}
//
//	@Override
//	public TableRuleRow makePersistent(TableRuleRow entity) throws UnexpectedDatabaseException,
//			ElementCannotBePersistedException {
//		// For solving Hibernate bug
//		// https://hibernate.atlassian.net/browse/HHH-1268 we cannot use the
//		// list of children
//		// with @Orderby or @OrderColumn we use our own order manager.
//		entity.getConditions().updateChildrenSortSeqs();
//		entity.getAction().updateChildrenSortSeqs();
//		return super.makePersistent(entity);
//	}
//
//	@Override
//	public List<TableRuleRow> getAll() throws UnexpectedDatabaseException {
//		List<TableRuleRow> result = super.getAll();
//		// For solving Hibernate bug
//		// https://hibernate.atlassian.net/browse/HHH-1268 we cannot use the
//		// list of children
//		// with @Orderby or @OrderColumn we use our own order manager.
//		sortChildren(result);
//		return result;
//	}
//
//	private void sortChildren(List<TableRuleRow> rules) {
//		for (TableRuleRow rule : rules) {
//			sortChildren(rule.getConditions());
//			sortChildren(rule.getAction());
//		}
//	}
//
//	private void sortChildren(ExpressionChain expressionChain) {
//		Collections.sort(expressionChain.getExpressions(), new ExpressionSort());
//		for (Expression child : expressionChain.getExpressions()) {
//			sortChildren(child);
//		}
//	}
//
//	private void sortChildren(Expression expression) {
//		if (expression instanceof ExpressionChain) {
//			ExpressionChain expressionChain = (ExpressionChain) expression;
//			Collections.sort(expressionChain.getExpressions(), new ExpressionSort());
//			for (Expression child : expressionChain.getExpressions()) {
//				sortChildren(child);
//			}
//		}
//	}
}

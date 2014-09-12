package com.biit.abcd.persistence.dao.hibernate;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.biit.abcd.persistence.dao.IFormDao;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.biit.form.BaseForm;
import com.biit.form.persistence.dao.hibernate.TreeObjectDao;

@Transactional
@Repository
public class FormDao extends TreeObjectDao<Form> implements IFormDao {

	public FormDao() {
		super(Form.class);
	}

	@Override
	@Transactional
	public Form makePersistent(Form entity) {
		// For solving Hibernate bug
		// https://hibernate.atlassian.net/browse/HHH-1268 we cannot use the
		// list of children
		// with @Orderby or @OrderColumn we use our own order manager.

		// Sort the expressions
		Set<ExpressionChain> expressionChainList = entity.getExpressionChain();
		if (expressionChainList != null && !expressionChainList.isEmpty()) {
			for (ExpressionChain expressionChain : expressionChainList) {
				expressionChain.updateChildrenSortSeqs();
			}
		}
		// Sort the rules
		Set<Rule> rulesList = entity.getRules();
		if (rulesList != null && !rulesList.isEmpty()) {
			for (Rule rule : rulesList) {
				rule.getConditionChain().updateChildrenSortSeqs();
				rule.getActionChain().updateChildrenSortSeqs();
			}
		}
		// Sort the table rule rows
		Set<TableRule> tableRules = entity.getTableRules();
		if (tableRules != null && !tableRules.isEmpty()) {
			for (TableRule tableRule : tableRules) {
				List<TableRuleRow> tableRuleRows = tableRule.getRules();
				if (tableRuleRows != null && !tableRuleRows.isEmpty()) {
					for (TableRuleRow tableRuleRow : tableRuleRows) {
						tableRuleRow.getConditionChain().updateChildrenSortSeqs();
						tableRuleRow.getActionChain().updateChildrenSortSeqs();
					}
				}
			}
		}
		if (rulesList != null && !rulesList.isEmpty()) {
			for (Rule rule : rulesList) {
				System.out.println("RULE ID: " + rule.getId() + " -- UUID: " + rule.getComparationId());
				System.out.println("RULE CONDITION ID: " + rule.getConditionChain().getId()
						+ " -- CONDITION CHAIN UUID: " + rule.getConditionChain().getComparationId());
			}
		}
		return super.makePersistent(entity);
	}

	@Override
	public int getLastVersion(Form form) {
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Integer> cq = criteriaBuilder.createQuery(Integer.class);
		Root<Form> root = cq.from(getType());
		cq.select(criteriaBuilder.max(root.<Integer> get("version")));
		cq.where(criteriaBuilder.equal(root.get("name"), form.getName()));
		return getEntityManager().createQuery(cq).getSingleResult();
	}

	@Override
	public Form getForm(String name) {
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<BaseForm> criteriaQuery = criteriaBuilder.createQuery(BaseForm.class);
		Root<Form> root = criteriaQuery.from(getType());
		criteriaQuery.select(root);
		criteriaQuery.where(criteriaBuilder.equal(root.get("name"), name));
		List<BaseForm> results = getEntityManager().createQuery(criteriaQuery).getResultList();
		if (!results.isEmpty()) {
			return (Form) results.get(0);
		}
		return null;
	}

	@Override
	public int getLastVersion(Long formId) {
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Integer> cq = criteriaBuilder.createQuery(Integer.class);
		Root<Form> root = cq.from(getType());
		cq.select(criteriaBuilder.max(root.<Integer> get("version")));
		cq.where(criteriaBuilder.equal(root.get("ID"), formId));
		return getEntityManager().createQuery(cq).getSingleResult();
	}

	@Override
	public List<Form> getAll() {
		List<Form> result = super.getAll();
		// For solving Hibernate bug
		// https://hibernate.atlassian.net/browse/HHH-1268 we cannot use the
		// list of children
		// with @Orderby or @OrderColumn we use our own order manager.
		sortChildren(result);
		return result;
	}

	protected void sortChildren(List<Form> forms) {
		for (Form form : forms) {
			// Sort the expressions
			Set<ExpressionChain> expressionChainList = form.getExpressionChain();
			if (expressionChainList != null && !expressionChainList.isEmpty()) {
				for (ExpressionChain expressionChain : expressionChainList) {
					sortChildren(expressionChain);
				}
			}
			// Sort the rules
			Set<Rule> rulesList = form.getRules();
			if (rulesList != null && !rulesList.isEmpty()) {
				for (Rule rule : rulesList) {
					sortChildren(rule.getConditionChain());
					sortChildren(rule.getActionChain());
				}
			}
			// Sort the table rule rows
			Set<TableRule> tableRules = form.getTableRules();
			if (tableRules != null && !tableRules.isEmpty()) {
				for (TableRule tableRule : tableRules) {
					List<TableRuleRow> tableRuleRows = tableRule.getRules();
					if (tableRuleRows != null && !tableRuleRows.isEmpty()) {
						for (TableRuleRow tableRuleRow : tableRuleRows) {
							sortChildren(tableRuleRow.getConditionChain());
							sortChildren(tableRuleRow.getActionChain());
						}
					}
				}
			}
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

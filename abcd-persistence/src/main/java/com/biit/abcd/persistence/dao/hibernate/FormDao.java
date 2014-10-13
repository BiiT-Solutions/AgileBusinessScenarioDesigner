package com.biit.abcd.persistence.dao.hibernate;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.dao.IFormDao;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.biit.form.persistence.dao.hibernate.TreeObjectDao;
import com.biit.persistence.entity.StorableObject;

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
		Set<ExpressionChain> expressionChainList = entity.getExpressionChains();
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
		return super.makePersistent(entity);
	}

	@Override
	protected void initializeSets(List<Form> forms) {
		super.initializeSets(forms);
		for (Form form : forms) {
			// Initializes the sets for lazy-loading (within the same session)
			Hibernate.initialize(form.getChildren());
			Hibernate.initialize(form.getDiagrams());
			Hibernate.initialize(form.getTableRules());
			Hibernate.initialize(form.getCustomVariables());
			Hibernate.initialize(form.getExpressionChains());
			Hibernate.initialize(form.getRules());
			Hibernate.initialize(form.getTestScenarios());
		}
	}

	/**
	 * Get all elements that has a null value in the ID parameter before persisting.
	 * 
	 * @param form
	 * @return
	 */
	@Override
	public Set<StorableObject> getElementsWithNullIds(Form form) {
		Set<StorableObject> elementsWithNullIds = new HashSet<>();
		elementsWithNullIds.addAll(super.getElementsWithNullIds(form));

		Set<StorableObject> diagrams = new HashSet<>();
		for (Diagram diagram : form.getDiagrams()) {
			diagrams.add(diagram);
			// Add also diagram objects.
			for (StorableObject child : diagram.getDiagramObjects()) {
				diagrams.add(child);
			}
		}
		elementsWithNullIds.addAll(getElementsWithNullIds(diagrams));

		Set<StorableObject> tableRules = new HashSet<>();
		for (TableRule tableRule : form.getTableRules()) {
			tableRules.add(tableRule);
		}
		elementsWithNullIds.addAll(getElementsWithNullIds(tableRules));

		return elementsWithNullIds;
	}

	@Override
	public int getLastVersion(Form form) {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			Criteria criteria = session.createCriteria(Form.class);
			criteria.setProjection(Projections.max("version"));
			criteria.add(Restrictions.eq("label", form.getLabel()));
			criteria.add(Restrictions.eq("organizationId", form.getOrganizationId()));
			Integer maxVersion = (Integer) criteria.uniqueResult();
			session.getTransaction().commit();
			return maxVersion;
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
	}

	@Override
	public int getLastVersion(String formLabel, Long organizationId) {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			Criteria criteria = session.createCriteria(Form.class);
			criteria.setProjection(Projections.max("version"));
			criteria.add(Restrictions.eq("label", formLabel));
			criteria.add(Restrictions.eq("organizationId", organizationId));
			Integer maxVersion = (Integer) criteria.uniqueResult();
			session.getTransaction().commit();
			return maxVersion;
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
	}

	@Override
	@Cacheable(value = "forms", key = "#id")
	public Form read(Long id) {
		AbcdLogger.info(FormDao.class.getName(), getSessionFactory().getStatistics().toString());
		Form form = super.read(id);
		AbcdLogger.info(FormDao.class.getName(), getSessionFactory().getStatistics().toString());
		return form;
	}

	@Override
	@Caching(evict = { @CacheEvict(value = "forms", key = "#form.label"),
			@CacheEvict(value = "forms", key = "#form.id"),
			@CacheEvict(value = "forms", key = "#form.label, #form.organizationId") })
	public void makeTransient(Form form) {
		super.makeTransient(form);
	}

	@Override
	@Cacheable(value = "forms", key = "#label")
	public Form getForm(String label, Long organizationId) {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			Criteria criteria = session.createCriteria(Form.class);
			criteria.add(Restrictions.eq("label", label));
			criteria.add(Restrictions.eq("organizationId", organizationId));
			// get form with max version.
			criteria.addOrder(Order.desc("version"));
			criteria.setMaxResults(1);
			@SuppressWarnings("unchecked")
			List<Form> results = criteria.list();
			initializeSets(results);
			session.getTransaction().commit();
			if (!results.isEmpty()) {
				return (Form) results.get(0);
			}
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
		return null;
	}

	@Override
	public Form getForm(String label, Integer version, Long organizationId) {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			Criteria criteria = session.createCriteria(Form.class);
			criteria.add(Restrictions.eq("version", version));
			criteria.add(Restrictions.eq("label", label));
			criteria.add(Restrictions.eq("organizationId", organizationId));
			@SuppressWarnings("unchecked")
			List<Form> results = criteria.list();
			initializeSets(results);
			session.getTransaction().commit();
			if (!results.isEmpty()) {
				return (Form) results.get(0);
			}
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
		return null;
	}

	@Override
	@Cacheable(value = "forms")
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
			Set<ExpressionChain> expressionChainList = form.getExpressionChains();
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

	@Override
	public boolean exists(String label, Long organizationId) {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			Criteria criteria = session.createCriteria(getType());
			criteria.setProjection(Projections.rowCount());
			criteria.add(Restrictions.eq("label", label));
			criteria.add(Restrictions.eq("organizationId", organizationId));
			int rows = ((Long) criteria.uniqueResult()).intValue();
			session.getTransaction().commit();
			return rows > 0;
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
	}
}

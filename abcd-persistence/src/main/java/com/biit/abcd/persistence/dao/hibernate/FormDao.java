package com.biit.abcd.persistence.dao.hibernate;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.dao.IFormDao;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.expressions.Expression;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.biit.form.persistence.dao.hibernate.BaseFormDao;
import com.biit.persistence.entity.StorableObject;

@Repository
public class FormDao extends BaseFormDao<Form> implements IFormDao {

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
		// Update previous versions validTo.
		if (entity.getVersion() > 0) {
			// 84600000 milliseconds in a day
			Timestamp validTo = new Timestamp(entity.getAvailableFrom().getTime() - 84600000);
			updateValidTo(entity.getLabel(), entity.getVersion() - 1, entity.getOrganizationId(), validTo);
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
	 * Get all elements that has a null value in the ID parameter before persisting. Hibernate Rollback does not set to
	 * null the id of the elements that finally hasn't been persisted.
	 * 
	 * @param form
	 * @return
	 */
	@Override
	public Set<StorableObject> getElementsWithNullIds(Form form) {
		Set<StorableObject> elementsWithNullIds = new HashSet<>();

		Set<StorableObject> formElements = form.getAllInnerStorableObjects();
		elementsWithNullIds.addAll(getElementsWithNullIds(formElements));
		return elementsWithNullIds;
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
		return super.getForm(label, organizationId);
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

	@Override
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

	/**
	 * Updates the validTo field of a form defined by its label and version.
	 * 
	 * @param label
	 * @param version
	 * @param validTo
	 * @return
	 */
	public int updateValidTo(String label, int version, Long organizationId, Timestamp validTo) {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			String hql = "update Form set availableTo = :availableTo where label = :label and version = :version and organizationId = :organizationId";
			Query query = session.createQuery(hql);
			query.setString("label", label);
			query.setLong("version", version);
			query.setLong("organizationId", organizationId);
			query.setTimestamp("availableTo", validTo);
			int rowCount = query.executeUpdate();
			session.getTransaction().commit();
			return rowCount;
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
	}

	public int updateValidFrom(String label, int version, Long organizationId, Timestamp validFrom) {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			String hql = "update Form set availableFrom = :availableFrom where label = :label and version = :version and organizationId = :organizationId";
			Query query = session.createQuery(hql);
			query.setString("label", label);
			query.setLong("version", version);
			query.setLong("organizationId", organizationId);
			query.setTimestamp("availableFrom", validFrom);
			int rowCount = query.executeUpdate();
			session.getTransaction().commit();
			return rowCount;
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
	}
}

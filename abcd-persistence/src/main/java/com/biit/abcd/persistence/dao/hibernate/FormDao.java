package com.biit.abcd.persistence.dao.hibernate;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.dao.ICustomVariableDao;
import com.biit.abcd.persistence.dao.IFormDao;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.FormWorkStatus;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.abcd.persistence.entity.diagram.DiagramLink;
import com.biit.abcd.persistence.entity.diagram.DiagramObject;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.abcd.persistence.entity.rules.TableRule;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.biit.form.persistence.dao.hibernate.BaseFormDao;
import com.biit.persistence.dao.exceptions.ElementCannotBePersistedException;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.ElementCannotBeRemovedException;

@Repository
public class FormDao extends BaseFormDao<Form> implements IFormDao {

	@Autowired
	private ICustomVariableDao customVariableDao;

	public FormDao() {
		super(Form.class);
	}

	@Override
	@Transactional
	@CachePut(value = "forms", key = "#form.getId()", condition = "#form.getId() != null")
	public Form makePersistent(Form form) throws UnexpectedDatabaseException, ElementCannotBePersistedException {
		// For solving Hibernate bug
		// https://hibernate.atlassian.net/browse/HHH-1268 we cannot use the
		// list of children
		// with @Orderby or @OrderColumn we use our own order manager.

		// Sort the expressions
		Set<ExpressionChain> expressionChainList = form.getExpressionChains();
		if (expressionChainList != null && !expressionChainList.isEmpty()) {
			for (ExpressionChain expressionChain : expressionChainList) {
				expressionChain.updateChildrenSortSeqs();
			}
		}

		// Sort the rules
		Set<Rule> rulesList = form.getRules();
		if (rulesList != null && !rulesList.isEmpty()) {
			for (Rule rule : rulesList) {
				rule.getConditions().updateChildrenSortSeqs();
				rule.getActions().updateChildrenSortSeqs();
			}
		}
		// Sort the table rule rows
		Set<TableRule> tableRules = form.getTableRules();
		if (tableRules != null && !tableRules.isEmpty()) {
			for (TableRule tableRule : tableRules) {
				List<TableRuleRow> tableRuleRows = tableRule.getRules();
				if (tableRuleRows != null && !tableRuleRows.isEmpty()) {
					for (TableRuleRow tableRuleRow : tableRuleRows) {
						tableRuleRow.getConditions().updateChildrenSortSeqs();
						tableRuleRow.getAction().updateChildrenSortSeqs();
					}
				}
			}
		}

		Set<Diagram> diagrams = form.getDiagrams();
		if (diagrams != null && !diagrams.isEmpty()) {
			for (Diagram diagram : diagrams) {
				Set<DiagramObject> nodes = diagram.getDiagramObjects();
				if (nodes != null && !nodes.isEmpty()) {
					for (DiagramObject node : nodes) {
						if (node instanceof DiagramLink) {
							DiagramLink nodeLink = (DiagramLink) node;
							nodeLink.getExpressionChain().updateChildrenSortSeqs();
						}
					}
				}
			}
		}

		// Update previous versions validTo.
		if (form.getVersion() > 0) {
			// 84600000 milliseconds in a day
			Timestamp validTo = new Timestamp(form.getAvailableFrom().getTime() - 84600000);
			updateValidTo(getId(form.getLabel(), form.getOrganizationId(), form.getVersion() - 1), validTo);
		}
		Form storedForm = super.makePersistent(form);

		// CustomVariables must be removed after expressions are removed.

		// For avoiding "ObjectDeletedException: deleted object would be re-saved by
		// cascade (remove deleted object from
		// associations)" launch when removing a customvariable and other is renamed as this
		// one, we need to disable
		// orphanRemoval=true of children and implement ourselves.
		try {
			purgeCustomVariablesToDelete(storedForm.getCustomVariablesToDelete());
		} catch (ElementCannotBeRemovedException e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}
		storedForm.setCustomVariablesToDelete(new HashSet<CustomVariable>());

		return storedForm;
	}

	private Set<CustomVariable> purgeCustomVariablesToDelete(Set<CustomVariable> variablesToDelete)
			throws UnexpectedDatabaseException, ElementCannotBeRemovedException {
		Set<CustomVariable> customVariablesToDelete = new HashSet<>();
		customVariablesToDelete.addAll(variablesToDelete);
		for (CustomVariable customVariable : customVariablesToDelete) {
			if (customVariable.getId() != null) {
				customVariableDao.makeTransient(customVariable);
			}
		}
		return customVariablesToDelete;
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
	public Form read(Long id) throws UnexpectedDatabaseException {
		// AbcdLogger.debug(FormDao.class.getName(), getSessionFactory().getStatistics().toString());
		Form form = super.read(id);
		// AbcdLogger.debug(FormDao.class.getName(), getSessionFactory().getStatistics().toString());
		return form;
	}

	@Override
	@Caching(evict = { @CacheEvict(value = "forms", key = "#form.getId()") })
	public void makeTransient(Form form) throws UnexpectedDatabaseException, ElementCannotBeRemovedException {
		// Set all current custom variables to delete.
		for (CustomVariable customVariable : new HashSet<>(form.getCustomVariables())) {
			form.remove(customVariable);
			// Remove form reference in database BUT not remove custom variable. It is still used in expressions.
			try {
				customVariableDao.makePersistent(customVariable);
			} catch (ElementCannotBePersistedException e) {
				AbcdLogger.errorMessage(this.getClass().getName(), e);
			}
		}

		super.makeTransient(form);

		purgeCustomVariablesToDelete(form.getCustomVariablesToDelete());
		form.setCustomVariablesToDelete(new HashSet<CustomVariable>());
	}

	@Override
	public List<Form> getAll() throws UnexpectedDatabaseException {
		List<Form> result = super.getAll();
		return result;
	}

	/**
	 * Updates the validTo field of a form defined by its label and version.
	 * 
	 * @param label
	 * @param version
	 * @param validTo
	 * @return
	 * @throws UnexpectedDatabaseException
	 */
	@Caching(evict = { @CacheEvict(value = "forms", key = "#id") })
	private int updateValidTo(Long id, Timestamp validTo) throws UnexpectedDatabaseException {
		if (id != null) {
			Session session = getSessionFactory().getCurrentSession();
			session.beginTransaction();
			try {
				String hql = "update Form set availableTo = CASE WHEN :availableTo > availableFrom THEN :availableTo ELSE availableFrom END where ID = :id";
				Query query = session.createQuery(hql);
				query.setLong("id", id);
				query.setTimestamp("availableTo", validTo);
				int rowCount = query.executeUpdate();
				session.getTransaction().commit();
				return rowCount;
			} catch (RuntimeException e) {
				session.getTransaction().rollback();
				throw new UnexpectedDatabaseException(e.getMessage(), e);
			}
		}
		return 0;
	}

	@Override
	@Caching(evict = { @CacheEvict(value = "forms", key = "#id") })
	public int updateFormStatus(Long id, FormWorkStatus formStatus) throws UnexpectedDatabaseException {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			String hql = "update Form set status = :formStatus where ID = :id";
			Query query = session.createQuery(hql);
			query.setLong("id", id);
			query.setString("formStatus", formStatus.toString());
			int rowCount = query.executeUpdate();
			session.getTransaction().commit();
			return rowCount;
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw new UnexpectedDatabaseException(e.getMessage(), e);
		}
	}

	@Override
	@Caching(evict = { @CacheEvict(value = "forms", allEntries = true) })
	public void evictAllCache() {
		super.evictAllCache();
	}
}

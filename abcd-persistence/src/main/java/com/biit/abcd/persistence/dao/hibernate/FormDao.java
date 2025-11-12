package com.biit.abcd.persistence.dao.hibernate;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (Persistence)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.abcd.persistence.dao.IFormDao;
import com.biit.abcd.persistence.dao.exceptions.MultiplesFormsFoundException;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.FormWorkStatus;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.persistence.entity.exceptions.ElementCannotBeRemovedException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FormDao extends AnnotatedGenericDao<Form, Long> implements IFormDao {

	public FormDao() {
		super(Form.class);
	}

	@Override
	@Transactional(value = "abcdTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
	@CachePut(value = "springFormCache", key = "#form?.getId()", condition = "#form.getId() != null")
	public Form makePersistent(Form form) {
		form.updateChildrenSortSeqs();
		// Update previous versions validTo.
		if (form.getVersion() > 0) {
			// 84600000 milliseconds in a day
			Timestamp validTo = new Timestamp(form.getAvailableFrom().getTime() - 84600000);
			updateValidTo(form.getLabel(), form.getVersion() - 1, form.getOrganizationId(), validTo);
		}

		return super.makePersistent(form);
	}

	@Override
	@Transactional(value = "abcdTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
	@Caching(evict = { @CacheEvict(value = "springFormCache", key = "#form?.getId()") })
	public void makeTransient(Form form) throws ElementCannotBeRemovedException {
		super.makeTransient(form);
	}

	@Override
	@Transactional(value = "abcdTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
	@Caching(evict = { @CacheEvict(value = "springFormCache", key = "#form?.getId()") })
	// @CachePut(value = "springFormCache", key = "#form.getId()", condition =
	// "#form.getId() != null")
	public Form merge(Form form) {
		return super.merge(form);
	}

	@Override
	@Transactional(value = "abcdTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
	@Cacheable(value = "springFormCache", key = "#id")
	public Form get(Long id) {
		Form form = super.get(id);
		// If we want to use spring cache, we need to load all elements before
		// exiting this method.
		if (form != null) {
			form.initializeSets();
		}
		return form;
	}

	@Override
	@Transactional(value = "abcdTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = true)
	public Form get(String label, Integer version, Long organizationId) throws MultiplesFormsFoundException {
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Form> criteriaQuery = criteriaBuilder.createQuery(Form.class);
		// Metamodel of the entity table
		Metamodel metamodel = getEntityManager().getMetamodel();
		EntityType<Form> formMetamodel = metamodel.entity(Form.class);
		Root<Form> typesRoot = criteriaQuery.from(Form.class);

		final List<Predicate> predicates = new ArrayList<>();
		predicates.add(criteriaBuilder.equal(typesRoot.get(formMetamodel.getSingularAttribute("label", String.class)), label));

		if (version != null) {
			predicates.add(criteriaBuilder.equal(typesRoot.get(formMetamodel.getSingularAttribute("version", Integer.class)), version));
		}

		if (organizationId != null) {
			predicates.add(criteriaBuilder.equal(typesRoot.get(formMetamodel.getSingularAttribute("organizationId", Long.class)), organizationId));
		}

		criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[]{})));

		try {
			List<Form> resultForms = getEntityManager().createQuery(criteriaQuery).getResultList();

			if (resultForms.size() > 1) {
				throw new MultiplesFormsFoundException();
			} else if (resultForms.isEmpty()) {
				return null;
			}
			Form form = resultForms.get(0);
			form.initializeSets();
			return form;
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	@Transactional(value = "abcdTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
	@Caching(evict = { @CacheEvict(value = "springFormCache", key = "#id") })
	public int updateFormStatus(Long id, FormWorkStatus formStatus) throws UnexpectedDatabaseException {
		Query query = getEntityManager().createQuery("UPDATE Form set status = :formStatus WHERE id = :id");
		query.setParameter("id", id);
		query.setParameter("formStatus", formStatus);

		return query.executeUpdate();
	}

	@Override
	@Caching(evict = { @CacheEvict(value = "springFormCache", allEntries = true) })
	public void evictAllCache() {
		super.evictAllCache();
	}

	@Override
	@Transactional(value = "abcdTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
	public Form getForm(String label, Integer version, Long organizationId) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Form> cq = cb.createQuery(Form.class);
		// Metamodel of the entity table
		Metamodel m = getEntityManager().getMetamodel();
		EntityType<Form> formMetamodel = m.entity(Form.class);
		Root<Form> form = cq.from(Form.class);

		cq.where(cb.and(cb.equal(form.get(formMetamodel.getSingularAttribute("label", String.class)), label),
				cb.equal(form.get(formMetamodel.getSingularAttribute("version", Integer.class)), version),
				cb.equal(form.get(formMetamodel.getSingularAttribute("organizationId", Long.class)), organizationId)));

		try {
			Form formResult = getEntityManager().createQuery(cq).getSingleResult();
			if (formResult != null) {
				formResult.initializeSets();
			}
			return formResult;
		} catch (NoResultException nre) {
			return null;
		}
	}

	@Override
	@Transactional(value = "abcdTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = true)
	public boolean exists(String label, Integer version, Long organizationId, Long id) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		// Metamodel of the entity table
		Metamodel m = getEntityManager().getMetamodel();
		EntityType<Form> formMetamodel = m.entity(Form.class);
		Root<Form> form = cq.from(Form.class);

		cq.select(cb.count(form));
		cq.where(cb.and(cb.equal(form.get(formMetamodel.getSingularAttribute("label", String.class)), label),
				cb.equal(form.get(formMetamodel.getSingularAttribute("version", Integer.class)), version),
				cb.equal(form.get(formMetamodel.getSingularAttribute("organizationId", Long.class)), organizationId),
				cb.notEqual(form.get(formMetamodel.getSingularAttribute("id", Long.class)), id)));

		return getEntityManager().createQuery(cq).getSingleResult() > 0;
	}

	@Override
	@Transactional(value = "abcdTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = true)
	public boolean exists(String label, long organizationId) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		// Metamodel of the entity table
		Metamodel m = getEntityManager().getMetamodel();
		EntityType<Form> formMetamodel = m.entity(Form.class);
		Root<Form> form = cq.from(Form.class);

		cq.select(cb.count(form));
		cq.where(cb.and(cb.equal(form.get(formMetamodel.getSingularAttribute("label", String.class)), label),
				cb.equal(form.get(formMetamodel.getSingularAttribute("organizationId", Long.class)), organizationId)));
		return getEntityManager().createQuery(cq).getSingleResult() > 0;
	}

	/**
	 * Updates the validTo field of a form defined by its label and version.
	 *
	 * @param label          the label of the element to update.
	 * @param version        the version of the element to update.
	 * @param organizationId the organization id.
	 * @param validTo        the date until this element is valid.
	 * @return the number of entities updated.
	 */
	@CacheEvict(value = "springFormCache", key = "#form.label, #form.organizationId")
	@Transactional(value = "abcdTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
	public int updateValidTo(String label, int version, Long organizationId, Timestamp validTo) {
		Query query = getEntityManager().createQuery(
				"UPDATE Form SET availableTo = CASE WHEN :availableTo > availableFrom THEN :availableTo ELSE availableFrom END where label = :label and version = :version and organization_id = :organization_id");
		query.setParameter("label", label);
		query.setParameter("version", version);
		query.setParameter("organization_id", organizationId);
		query.setParameter("availableTo", validTo);

		return query.executeUpdate();
	}

	@Override
	public List<Form> getAll(Long organizationId) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Form> cq = cb.createQuery(Form.class);
		// Metamodel of the entity table
		Metamodel m = getEntityManager().getMetamodel();
		EntityType<Form> formMetamodel = m.entity(Form.class);
		Root<Form> form = cq.from(Form.class);

		cq.where(cb.equal(form.get(formMetamodel.getSingularAttribute("organizationId", Long.class)), organizationId));

		List<Form> formResults = getEntityManager().createQuery(cq).getResultList();
		if (formResults != null) {
			for (Form formResult : formResults) {
				formResult.initializeSets();
			}
		}
		return formResults;
	}
}

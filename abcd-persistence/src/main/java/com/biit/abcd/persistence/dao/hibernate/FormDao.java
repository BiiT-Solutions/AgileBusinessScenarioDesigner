package com.biit.abcd.persistence.dao.hibernate;

import java.sql.Timestamp;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.biit.abcd.persistence.dao.ICustomVariableDao;
import com.biit.abcd.persistence.dao.IFormDao;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.FormWorkStatus;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;


@Repository
public class FormDao extends AnnotatedGenericDao<Form,Long> implements IFormDao {

	@Autowired
	private ICustomVariableDao customVariableDao;

	public FormDao() {
		super(Form.class);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
	@CachePut(value = "abcdforms", key = "#form.getId()", condition = "#form.getId() != null")
	public void makePersistent(Form entity) {
		entity.updateChildrenSortSeqs();
		
		// Update previous versions validTo.
		if (entity.getVersion() > 0) {
			// 84600000 milliseconds in a day
			Timestamp validTo = new Timestamp(entity.getAvailableFrom().getTime() - 84600000);
			updateValidTo(entity.getLabel(), entity.getVersion() - 1, entity.getOrganizationId(), validTo);
		}
		
		super.makePersistent(entity);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
	@Caching(evict = { @CacheEvict(value = "abcdforms", key = "#form.getId()") })
	public void makeTransient(Form entity) {
		super.makeTransient(entity);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
	@Caching(evict = { @CacheEvict(value = "abcdforms", key = "#form.getId()") })
	public Form merge(Form entity) {
		return super.merge(entity);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
	@CachePut(value = "abcdforms", key = "#id")
	public Form get(Long id) {
		return super.get(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
	@Caching(evict = { @CacheEvict(value = "abcdforms", key = "#id") })
	public int updateFormStatus(Long id, FormWorkStatus formStatus) throws UnexpectedDatabaseException {
		
		Query query = getEntityManager().createQuery("UPDATE Form set status = :formStatus WHERE ID = :id");
		query.setParameter("id", id);
		query.setParameter("formStatus", formStatus);

		return query.executeUpdate();
	}
	
	@Override
	@Caching(evict = { @CacheEvict(value = "abcdforms", allEntries = true) })
	public void evictAllCache() {
		super.evictAllCache();
	}

	@Override
	public Form getForm(String label, Integer version, Long organizationId) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Form> cq = cb.createQuery(Form.class);
		//Metamodel of the entity table
		Metamodel m = getEntityManager().getMetamodel();
		EntityType<Form> formMetamodel = m.entity(Form.class);
		Root<Form> form = cq.from(Form.class);
		
		cq.where(cb.and
				(cb.equal(form.get(formMetamodel.getSingularAttribute("label", String.class)),label),
				cb.equal(form.get(formMetamodel.getSingularAttribute("version", Integer.class)),version),
				cb.equal(form.get(formMetamodel.getSingularAttribute("organizationId", Long.class)),organizationId)
				));
		
		return getEntityManager().createQuery(cq).getSingleResult();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = true)
	public boolean exists(String label, Integer version, Long organizationId, Long id) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		//Metamodel of the entity table
		Metamodel m = getEntityManager().getMetamodel();
		EntityType<Form> formMetamodel = m.entity(Form.class);
		Root<Form> form = cq.from(Form.class);
		
		cq.select(cb.count(form));
		cq.where(cb.and
				(cb.equal(form.get(formMetamodel.getSingularAttribute("label", String.class)),label),
				cb.equal(form.get(formMetamodel.getSingularAttribute("version", Integer.class)),version),
				cb.equal(form.get(formMetamodel.getSingularAttribute("organizationId", Long.class)),organizationId),
				cb.notEqual(form.get(formMetamodel.getSingularAttribute("id", Long.class)),id)
				));
		
		return getEntityManager().createQuery(cq).getSingleResult()>0;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = true)
	public boolean exists(String label, long organizationId) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		//Metamodel of the entity table
		Metamodel m = getEntityManager().getMetamodel();
		EntityType<Form> formMetamodel = m.entity(Form.class);
		Root<Form> form = cq.from(Form.class);
		
		cq.select(cb.count(form));
		cq.where(cb.and
				(cb.equal(form.get(formMetamodel.getSingularAttribute("label", String.class)),label),
				cb.equal(form.get(formMetamodel.getSingularAttribute("organizationId", Long.class)),organizationId)
				));
		return getEntityManager().createQuery(cq).getSingleResult()>0;
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
	@CacheEvict(value = "forms", key = "#form.label, #form.organizationId")
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
	public int updateValidTo(String label, int version, Long organizationId, Timestamp validTo) {
		Query query = getEntityManager().createQuery("UPDATE Form SET availableTo = CASE WHEN :availableTo > availableFrom THEN :availableTo ELSE availableFrom END where label = :label and version = :version and organizationId = :organizationId");
		query.setParameter("label", label);
		query.setParameter("version", version);
		query.setParameter("organizationId", organizationId);
		query.setParameter("availableTo", validTo);
		
		return query.executeUpdate();
	}
	
}

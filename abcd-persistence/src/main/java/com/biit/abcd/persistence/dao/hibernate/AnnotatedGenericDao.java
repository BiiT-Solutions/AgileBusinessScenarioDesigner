package com.biit.abcd.persistence.dao.hibernate;

import com.biit.persistence.dao.IJpaGenericDao;
import com.biit.persistence.dao.jpa.GenericDao;
import com.biit.persistence.entity.exceptions.ElementCannotBeRemovedException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

public abstract class AnnotatedGenericDao<EntityClass, PrimaryKeyClass extends Serializable> extends
		GenericDao<EntityClass, PrimaryKeyClass> implements IJpaGenericDao<EntityClass, PrimaryKeyClass> {

	// PersistenceContextType.EXTENDED needed for using Lazy Loading in Vaadin.
	// http://stackoverflow.com/questions/7977547/vaadin-jpa-lazy-loading
	@PersistenceContext(unitName = "abcdPersistenceUnit")
	@Qualifier(value = "abcdManagerFactory")
	private EntityManager entityManager;

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

	public AnnotatedGenericDao(Class<EntityClass> entityClass) {
		super(entityClass);
	}

	@Override
	@Transactional(value = "abcdTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
	public EntityClass makePersistent(EntityClass entity) {
		return super.makePersistent(entity);
	}

	@Override
	@Transactional(value = "abcdTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
	public EntityClass merge(EntityClass entity) {
		return super.merge(entity);
	}

	@Override
	@Transactional(value = "abcdTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
	public void makeTransient(EntityClass entity) throws ElementCannotBeRemovedException {
		super.makeTransient(entity);
	}

	@Override
	@Transactional(value = "abcdTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
	public EntityClass get(PrimaryKeyClass id) {
		return super.get(id);
	}

	@Override
	@Transactional(value = "abcdTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = true)
	public int getRowCount() {
		return super.getRowCount();
	}

	@Override
	@Transactional(value = "abcdTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = true)
	public List<EntityClass> getAll() {
		return super.getAll();
	}

	@Override
	public void evictAllCache() {
		// Clear first level cache.
		getEntityManager().clear();
		super.evictAllCache();
	}
}

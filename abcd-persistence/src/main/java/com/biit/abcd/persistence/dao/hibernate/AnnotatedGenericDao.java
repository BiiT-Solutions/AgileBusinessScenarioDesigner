package com.biit.abcd.persistence.dao.hibernate;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.sf.ehcache.CacheManager;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.persistence.dao.IJpaGenericDao;
import com.biit.persistence.dao.jpa.GenericDao;
import com.biit.persistence.entity.exceptions.ElementCannotBeRemovedException;

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

	public void printCacheStatistics() {
		SessionFactory sessionFactory = getEntityManager().unwrap(Session.class).getSessionFactory();
		AbcdLogger.debug(this.getClass().getName(), sessionFactory.getStatistics().toString());
		// Spring cache.
		CacheManager cacheManager = CacheManager.getInstance();
		String[] cacheNames = cacheManager.getCacheNames();
		for (int i = 0; i < cacheNames.length; i++) {
			String cacheName = cacheNames[i];
			AbcdLogger.debug(this.getClass().getName(), cacheName + ": "
					+ cacheManager.getCache(cacheName).getStatistics().toString());
		}
	}
}

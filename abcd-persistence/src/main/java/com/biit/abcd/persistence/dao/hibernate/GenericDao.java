package com.biit.abcd.persistence.dao.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;

import com.biit.abcd.persistence.dao.IGenericDao;

public abstract class GenericDao<T> implements IGenericDao<T> {

	private Class<T> type;

	@Autowired
	private SessionFactory sessionFactory = null;

	public GenericDao(Class<T> type) {
		this.type = type;
	}

	public Class<T> getType() {
		return type;
	}

	protected SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	protected void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public T makePersistent(T entity) {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			session.saveOrUpdate(entity);
			session.flush();
			session.getTransaction().commit();
			return entity;
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
	}

	@Override
	public void makeTransient(T entity) {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			session.delete(entity);
			session.getTransaction().commit();
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
	}

	@Override
	public int getRowCount() {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			Criteria criteria = session.createCriteria(getType());
			criteria.setProjection(Projections.rowCount());
			int rows = ((Long) criteria.uniqueResult()).intValue();
			session.getTransaction().commit();
			return rows;
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
	}

	@Override
	public List<T> getAll() {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			@SuppressWarnings("unchecked")
			List<T> forms = session.createQuery("from " + getType().getSimpleName()).list();
			initializeSets(forms);
			session.getTransaction().commit();
			return forms;
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
	}

	/**
	 * When using lazy loading, the sets must have a proxy to avoid a
	 * "LazyInitializationException: failed to lazily initialize a collection of..." error. This procedure must be
	 * called before closing the session.
	 * 
	 * @param forms
	 */
	protected abstract void initializeSets(List<T> forms);
}

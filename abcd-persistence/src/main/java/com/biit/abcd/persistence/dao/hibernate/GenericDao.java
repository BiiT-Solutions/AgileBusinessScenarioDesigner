package com.biit.abcd.persistence.dao.hibernate;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;

import com.biit.abcd.persistence.dao.IGenericDao;
import com.biit.abcd.persistence.entity.TreeObject;

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

	private void setCreationInfo(T entity) {
		TreeObject treeObject = (TreeObject) entity;
		if (treeObject.getCreationTime() == null) {
			treeObject.setCreationTime(new Timestamp(new Date().getTime()));
		}
	}

	private void setUpdateInfo(T entity) {
		TreeObject treeObject = (TreeObject) entity;
		treeObject.setUpdateTime(new Timestamp(new Date().getTime()));
	}

	@Override
	public T makePersistent(T entity) {
		// For solving Hibernate bug https://hibernate.atlassian.net/browse/HHH-1268
		if (entity instanceof TreeObject) {
			((TreeObject) entity).updateChildrenSortSeqs();
		}
		setCreationInfo(entity);
		setUpdateInfo(entity);
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
			session.flush();
			session.getTransaction().commit();
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
	}

	@Override
	public T read(Long id) {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			@SuppressWarnings("unchecked")
			T object = (T) session.get(getType(), id);
			initializeSet(object);
			session.getTransaction().commit();
			if (object != null && object instanceof TreeObject) {
				sortChildren((TreeObject) object);
			}
			return object;
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
			List<T> objects = session.createQuery("from " + getType().getSimpleName()).list();
			initializeSets(objects);
			session.getTransaction().commit();
			sortChildren(objects);
			return objects;
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
	 * @param planningEvent
	 */
	private void initializeSet(T element) {
		List<T> elements = new ArrayList<>();
		elements.add(element);
		initializeSets(elements);
	}

	/**
	 * When using lazy loading, the sets must have a proxy to avoid a
	 * "LazyInitializationException: failed to lazily initialize a collection of..." error. This procedure must be
	 * called before closing the session.
	 * 
	 * @param elements
	 */
	protected abstract void initializeSets(List<T> elements);

	protected void sortChildren(List<T> treeObjects) {
		for (T treeObject : treeObjects) {
			if (treeObject instanceof TreeObject) {
				sortChildren((TreeObject) treeObject);
			}
		}
	}

	protected void sortChildren(TreeObject treeObject) {
		Collections.sort(treeObject.getChildren(), new ChildrenSort());
		System.out.println(treeObject + " -> " + treeObject.getChildren());
		for (TreeObject child : treeObject.getChildren()) {
			sortChildren(child);
		}
	}

	class ChildrenSort implements Comparator<TreeObject> {

		@Override
		public int compare(TreeObject o1, TreeObject o2) {
			return (o1.getSortSeq() < o2.getSortSeq() ? -1 : (o1 == o2 ? 0 : 1));
		}
	}
}

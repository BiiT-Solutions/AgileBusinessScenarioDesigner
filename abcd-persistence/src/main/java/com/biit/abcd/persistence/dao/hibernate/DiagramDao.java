package com.biit.abcd.persistence.dao.hibernate;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.IDiagramDao;
import com.biit.abcd.persistence.entity.Diagram;
import com.biit.abcd.persistence.entity.Form;

@Repository
public class DiagramDao implements IDiagramDao {

	@Autowired
	private SessionFactory sessionFactory = null;

	public DiagramDao() {
	}

	protected SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	protected void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public List<Diagram> getAll() {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			@SuppressWarnings("unchecked")
			List<Diagram> diagrams = session.createQuery("from Diagram").list();
			session.getTransaction().commit();
			return diagrams;
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
	}

	private void setCreationInfo(Diagram entity) {
		if (entity.getCreationTime() == null) {
			entity.setCreationTime(new Timestamp(new Date().getTime()));
		}
	}

	private void setUpdateInfo(Diagram entity) {
		entity.setUpdateTime(new Timestamp(new Date().getTime()));
	}

	@Override
	public Diagram makePersistent(Diagram entity) {
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
	public void makeTransient(Diagram entity) {
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
			Criteria criteria = session.createCriteria(Diagram.class);
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
	public Diagram read(Long id) {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			Diagram diagram = (Diagram) session.get(Diagram.class, id);
			session.getTransaction().commit();
			return diagram;
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
	}

	@Override
	public Diagram read(Form form) {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			Criteria criteria = session.createCriteria(Diagram.class);
			criteria.add(Restrictions.eq("form", form));
			@SuppressWarnings("unchecked")
			List<Diagram> results = criteria.list();
			// initialize(results);
			session.getTransaction().commit();
			if (results.size() > 0) {
				return results.get(0);
			}
			return null;
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
	}
}

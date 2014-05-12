package com.biit.abcd.persistence.dao.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.IFormDao;
import com.biit.abcd.persistence.entity.Form;

@Repository
public class FormDao extends GenericDao<Form> implements IFormDao {

	public FormDao() {
		super(Form.class);
	}

	@Override
	protected void initializeSets(List<Form> forms) {
		for (Form form : forms) {
			// Initializes the sets for lazy-loading (within the same session)
			Hibernate.initialize(form.getChildren());
		}
	}

	@Override
	public int getLastVersion(Form form) {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			Criteria criteria = session.createCriteria(Form.class);
			criteria.setProjection(Projections.max("version"));
			criteria.add(Restrictions.eq("name", form.getName()));
			Integer maxVersion = (Integer) criteria.uniqueResult();
			session.getTransaction().commit();
			return maxVersion;
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
	}

	@Override
	public Form getForm(String name) {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			Criteria criteria = session.createCriteria(Form.class);
			criteria.add(Restrictions.eq("name", name));
			@SuppressWarnings("unchecked")
			List<Form> results = criteria.list();
			Form form = (Form) results.get(0);
			session.getTransaction().commit();
			return form;
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
	}
}

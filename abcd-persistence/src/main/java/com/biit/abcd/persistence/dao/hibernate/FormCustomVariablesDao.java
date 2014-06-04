package com.biit.abcd.persistence.dao.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.biit.abcd.persistence.dao.IFormCustomVariablesDao;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.FormCustomVariables;

public class FormCustomVariablesDao extends GenericDao<FormCustomVariables> implements IFormCustomVariablesDao {

	public FormCustomVariablesDao() {
		super(FormCustomVariables.class);
	}

	@Override
	protected void initializeSets(List<FormCustomVariables> elements) {
		// Do nothing
	}

	@Override
	@SuppressWarnings("unchecked")
	public FormCustomVariables getFormCustomVariables(Form form) {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			Criteria criteria = session.createCriteria(FormCustomVariables.class);
			criteria.add(Restrictions.eq("form", form.getName()));
			List<FormCustomVariables> results = criteria.list();
			session.getTransaction().commit();
			if (!results.isEmpty()) {
				FormCustomVariables formCustomVariables = (FormCustomVariables) results.get(0);
				return formCustomVariables;
			}
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
		return null;
	}

}

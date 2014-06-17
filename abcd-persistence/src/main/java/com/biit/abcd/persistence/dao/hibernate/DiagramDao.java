package com.biit.abcd.persistence.dao.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.IDiagramDao;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.diagram.Diagram;

@Repository
public class DiagramDao extends GenericDao<Diagram> implements IDiagramDao {

	@Autowired
	private SessionFactory sessionFactory = null;

	public DiagramDao() {
		super(Diagram.class);
	}

	@Override
	protected void initializeSets(List<Diagram> elements) {
		for (Diagram diagram : elements) {
			// Initializes the sets for lazy-loading (within the same session)
			Hibernate.initialize(diagram.getDiagramObjects());
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
			initializeSets(results);
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

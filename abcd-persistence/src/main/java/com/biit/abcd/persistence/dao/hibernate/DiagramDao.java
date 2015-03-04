package com.biit.abcd.persistence.dao.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.IDiagramDao;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.diagram.Diagram;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.persistence.dao.hibernate.GenericDao;

@Repository
public class DiagramDao extends GenericDao<Diagram> implements IDiagramDao {

	public DiagramDao() {
		super(Diagram.class);
	}

	@Override
	protected void initializeSets(List<Diagram> elements) {
	}

	@Override
	public Diagram read(Form form) throws UnexpectedDatabaseException {
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
			throw new UnexpectedDatabaseException(e.getMessage(), e);
		}
	}

}

package com.biit.abcd.persistence.dao.hibernate;

import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.IDiagramDao;
import com.biit.abcd.persistence.entity.diagram.Diagram;


@Repository
public class DiagramDao extends AnnotatedGenericDao<Diagram, Long>  implements IDiagramDao {

	public DiagramDao() {
		super(Diagram.class);
	}

//	@Override
//	public Diagram read(Form form) throws UnexpectedDatabaseException {
//		Session session = getSessionFactory().getCurrentSession();
//		session.beginTransaction();
//		try {
//			Criteria criteria = session.createCriteria(Diagram.class);
//			criteria.add(Restrictions.eq("form", form));
//			@SuppressWarnings("unchecked")
//			List<Diagram> results = criteria.list();
//			initializeSets(results);
//			session.getTransaction().commit();
//			if (results.size() > 0) {
//				return results.get(0);
//			}
//			return null;
//		} catch (RuntimeException e) {
//			session.getTransaction().rollback();
//			throw new UnexpectedDatabaseException(e.getMessage(), e);
//		}
//	}

}

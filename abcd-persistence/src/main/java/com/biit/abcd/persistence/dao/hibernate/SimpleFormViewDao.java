package com.biit.abcd.persistence.dao.hibernate;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.ISimpleFormViewDao;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.FormWorkStatus;
import com.biit.abcd.persistence.entity.SimpleFormView;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;

@Repository
public class SimpleFormViewDao implements ISimpleFormViewDao {

	private Class<SimpleFormView> type;

	private SessionFactory sessionFactory = null;

	public SimpleFormViewDao() {
		this.type = SimpleFormView.class;

	}

	public Class<SimpleFormView> getType() {
		return type;
	}

	@Override
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Override
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public int getRowCount() throws UnexpectedDatabaseException {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			Criteria criteria = session.createCriteria(Form.class);
			criteria.setProjection(Projections.rowCount());
			int rows = ((Long) criteria.uniqueResult()).intValue();
			session.getTransaction().commit();
			return rows;
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw new UnexpectedDatabaseException(e.getMessage(), e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SimpleFormView> getAll() {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		SQLQuery query = session
				.createSQLQuery("SELECT tf.ID, tf.name, tf.label, tf.version, tf.creationTime, tf.createdBy, tf.updateTime, tf.updatedBy, tf.comparationId, tf.availableFrom, tf.availableTo, tf.organizationId, max.maxversion, tf.status "
						+ "FROM tree_forms tf INNER JOIN "
						+ "(SELECT MAX(version) AS maxversion, label, organizationId FROM tree_forms "
						+ "GROUP BY label, organizationId) AS max  ON max.label = tf.label and max.organizationId = tf.organizationId "
						+ "ORDER BY label, tf.version DESC");

		List<Object[]> rows = query.setCacheable(true).list();

		session.getTransaction().commit();

		List<SimpleFormView> formViews = new ArrayList<>();
		for (Object[] row : rows) {
			SimpleFormView formView = new SimpleFormView();
			formView.setId(((BigInteger) row[0]).longValue());
			formView.setName((String) row[1]);
			formView.setLabel((String) row[2]);
			formView.setVersion((Integer) row[3]);
			formView.setCreationTime((Timestamp) row[4]);
			if (row[4] != null) {
				formView.setCreatedBy(((Double) row[5]).longValue());
			}
			formView.setUpdateTime((Timestamp) row[6]);
			if (row[6] != null) {
				formView.setUpdatedBy(((Double) row[7]).longValue());
			}
			formView.setComparationId((String) row[8]);
			formView.setAvailableFrom((Timestamp) row[9]);
			formView.setAvailableTo((Timestamp) row[10]);
			formView.setOrganizationId(((Double) row[11]).longValue());
			formView.setLastVersion(((Integer) row[12]).equals((Integer) row[3]));
			if (row[13] != null) {
				formView.setStatus(FormWorkStatus.getFromString((String) row[13]));
			}
			formViews.add(formView);
		}

		return formViews;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SimpleFormView> getSimpleFormViewByLabelAndOrganization(String label, Long organizationId) {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		SQLQuery query = session
				.createSQLQuery("SELECT tf.ID, tf.name, tf.label, tf.version, tf.creationTime, tf.createdBy, tf.updateTime, tf.updatedBy, tf.comparationId, tf.availableFrom, tf.availableTo, tf.organizationId, max.maxversion, tf.status "
						+ "FROM tree_forms tf INNER JOIN "
						+ "(SELECT MAX(version) AS maxversion, label, organizationId FROM tree_forms "
						+ "GROUP BY label, organizationId) AS max  ON max.label = tf.label and max.organizationId = tf.organizationId "
						+ "WHERE tf.label='"
						+ label
						+ "' AND tf.organizationId='"
						+ organizationId
						+ "' ORDER BY label, tf.version DESC");

		List<Object[]> rows = query.list();

		session.getTransaction().commit();

		List<SimpleFormView> formViews = new ArrayList<>();
		for (Object[] row : rows) {
			SimpleFormView formView = new SimpleFormView();
			formView.setId(((BigInteger) row[0]).longValue());
			formView.setName((String) row[1]);
			formView.setLabel((String) row[2]);
			formView.setVersion((Integer) row[3]);
			formView.setCreationTime((Timestamp) row[4]);
			if (row[4] != null) {
				formView.setCreatedBy(((Double) row[5]).longValue());
			}
			formView.setUpdateTime((Timestamp) row[6]);
			if (row[6] != null) {
				formView.setUpdatedBy(((Double) row[7]).longValue());
			}
			formView.setComparationId((String) row[8]);
			formView.setAvailableFrom((Timestamp) row[9]);
			formView.setAvailableTo((Timestamp) row[10]);
			formView.setOrganizationId(((Double) row[11]).longValue());
			formView.setLastVersion(((Integer) row[12]).equals((Integer) row[3]));
			if (row[13] != null) {
				formView.setStatus(FormWorkStatus.getFromString((String) row[13]));
			}
			formViews.add(formView);
		}

		return formViews;
	}

}

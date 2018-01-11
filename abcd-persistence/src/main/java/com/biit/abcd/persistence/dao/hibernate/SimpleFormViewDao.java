package com.biit.abcd.persistence.dao.hibernate;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.ISimpleFormViewDao;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.FormWorkStatus;
import com.biit.abcd.persistence.entity.SimpleFormView;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;

@Repository
public class SimpleFormViewDao implements ISimpleFormViewDao {

	private Class<SimpleFormView> type;

	@PersistenceContext(unitName = "abcdPersistenceUnit")
	@Qualifier(value = "abcdManagerFactory")
	private EntityManager entityManager;

	public SimpleFormViewDao() {
		this.type = SimpleFormView.class;

	}

	public Class<SimpleFormView> getType() {
		return type;
	}

	@Override
	public int getRowCount() throws UnexpectedDatabaseException {				
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> query = cb.createQuery(Long.class);
		Root<Form> root = query.from(Form.class);

		query.select(cb.count(root));
		return entityManager.createQuery(query).getSingleResult().intValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SimpleFormView> getAll() {
		Query query = entityManager.createNativeQuery("SELECT tf.ID, tf.name, tf.label, tf.version, tf.creation_time, tf.created_by, tf.update_time, tf.updated_by, tf.comparation_id, tf.availableFrom, tf.availableTo, tf.organization_id, max.maxversion, tf.status "
				+ "FROM tree_forms tf INNER JOIN "
				+ "(SELECT MAX(version) AS maxversion, label, organization_id FROM tree_forms "
				+ "GROUP BY label, organization_id) AS max  ON max.label = tf.label and max.organization_id = tf.organization_id "
				+ "ORDER BY label, tf.version DESC");
		
		
		List<Object[]> queries = query.getResultList();
		List<SimpleFormView> formViews = new ArrayList<>();
		for (Object[] row : queries) {
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
		Query query = entityManager.createNativeQuery("SELECT tf.ID, tf.name, tf.label, tf.version, tf.creation_time, tf.created_by, tf.update_time, tf.updated_by, tf.comparation_id, tf.availableFrom, tf.availableTo, tf.organization_id, max.maxversion, tf.status "
				+ "FROM tree_forms tf INNER JOIN "
				+ "(SELECT MAX(version) AS maxversion, label, organization_id FROM tree_forms "
				+ "GROUP BY label, organization_id) AS max  ON max.label = tf.label and max.organization_id = tf.organization_id "
				+ "WHERE tf.label='"
				+ label
				+ "' AND tf.organization_id='"
				+ organizationId
				+ "' ORDER BY label, tf.version DESC");
		
		
		List<Object[]> queries = query.getResultList();
		List<SimpleFormView> formViews = new ArrayList<>();
		for (Object[] row : queries) {
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
	public List<SimpleFormView> getSimpleFormViewByOrganization(Long organizationId) {
		Query query = entityManager.createNativeQuery("SELECT tf.ID, tf.name, tf.label, tf.version, tf.creation_time, tf.created_by, tf.update_time, tf.updated_by, tf.comparation_id, tf.availableFrom, tf.availableTo, tf.organization_id, max.maxversion, tf.status "
				+ "FROM tree_forms tf INNER JOIN "
				+ "(SELECT MAX(version) AS maxversion, label, organization_id FROM tree_forms "
				+ "GROUP BY label, organization_id) AS max  ON max.label = tf.label and max.organization_id = tf.organization_id "
				+ "WHERE tf.organization_id='"
				+ organizationId
				+ "' ORDER BY label, tf.version DESC");
		
		
		List<Object[]> queries = query.getResultList();
		List<SimpleFormView> formViews = new ArrayList<>();
		for (Object[] row : queries) {
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

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.ISimpleTestScenarioViewDao;
import com.biit.abcd.persistence.entity.SimpleTestScenarioView;
import com.biit.abcd.persistence.entity.testscenarios.TestScenario;

@Repository
public class SimpleTestScenarioViewDao implements ISimpleTestScenarioViewDao {

	private Class<SimpleTestScenarioView> type;

	@Autowired
	private SessionFactory sessionFactory = null;

	public SimpleTestScenarioViewDao() {
		this.type = SimpleTestScenarioView.class;

	}

	public Class<SimpleTestScenarioView> getType() {
		return type;
	}

	protected SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Override
	public int getRowCount() {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			Criteria criteria = session.createCriteria(TestScenario.class);
			criteria.setProjection(Projections.rowCount());
			int rows = ((Long) criteria.uniqueResult()).intValue();
			session.getTransaction().commit();
			return rows;
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SimpleTestScenarioView> getAll() {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		SQLQuery query = session
				.createSQLQuery("SELECT ts.ID, ts.comparationId, ts.creationTime, ts.createdBy, ts.updateTime, ts.updatedBy, ts.name, ts.formLabel, ts.formOrganizationId, ts.formVersion FROM test_scenario ts");

		List<Object[]> rows = query.list();
		session.getTransaction().commit();

		List<SimpleTestScenarioView> testScenarioViews = new ArrayList<>();
		for (Object[] row : rows) {
			SimpleTestScenarioView testScenarioView = new SimpleTestScenarioView();
			testScenarioView.setId(((BigInteger) row[0]).longValue());
			testScenarioView.setComparationId((String) row[1]);
			testScenarioView.setCreationTime((Timestamp) row[2]);
			if (row[3] != null) {
				testScenarioView.setCreatedBy(((Double) row[3]).longValue());
			}
			testScenarioView.setUpdateTime((Timestamp) row[4]);
			if (row[5] != null) {
				testScenarioView.setUpdatedBy(((Double) row[5]).longValue());
			}
			testScenarioView.setName((String) row[6]);
			testScenarioView.setFormLabel((String) row[7]);
			testScenarioView.setFormOrganizationId(((Double) row[8]).longValue());
			testScenarioView.setFormVersion((Integer) row[9]);
			testScenarioViews.add(testScenarioView);
		}
		return testScenarioViews;
	}
}

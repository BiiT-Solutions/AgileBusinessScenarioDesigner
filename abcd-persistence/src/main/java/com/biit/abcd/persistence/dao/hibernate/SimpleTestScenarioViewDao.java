package com.biit.abcd.persistence.dao.hibernate;

import java.math.BigInteger;
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
	public List<SimpleTestScenarioView> getSimpleTestScenariosByFormId(Long formId) {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		SQLQuery query = session
				.createSQLQuery("SELECT tests.ID, tests.name, tests.formId, formsOfTest.form_version FROM test_scenario AS tests JOIN (SELECT t2.id form_Id, t2.version form_version FROM tree_forms AS t2 JOIN tree_forms AS t1 ON t1.organizationId=t2.organizationId AND t1.label=t2.label WHERE t1.id="
						+ formId +") AS formsOfTest WHERE tests.formId=formsOfTest.form_Id;");

		List<Object[]> rows = query.list();
		session.getTransaction().commit();

		List<SimpleTestScenarioView> testScenarioViews = new ArrayList<>();
		for (Object[] row : rows) {
			SimpleTestScenarioView testScenarioView = new SimpleTestScenarioView();
			testScenarioView.setId(((BigInteger) row[0]).longValue());
			testScenarioView.setName((String) row[1]);
			testScenarioView.setFormId(((BigInteger) row[2]).longValue());
			testScenarioView.setFormVersion((Integer) row[3]);
			testScenarioViews.add(testScenarioView);
		}
		return testScenarioViews;
	}
}

package com.biit.abcd.persistence.dao.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.ITestScenarioDao;
import com.biit.abcd.persistence.entity.testscenarios.TestScenario;
import com.biit.persistence.dao.hibernate.GenericDao;

@Repository
public class TestScenarioDao extends GenericDao<TestScenario> implements ITestScenarioDao {

	public TestScenarioDao() {
		super(TestScenario.class);
	}

	@Override
	protected void initializeSets(List<TestScenario> elements) {
	}
	
	@Override	
	public TestScenario getTestScenarioById(Long scenarioId) {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			Criteria criteria = session.createCriteria(TestScenario.class);
			criteria.add(Restrictions.eq("id", scenarioId));
			TestScenario testScenario = (TestScenario) criteria.uniqueResult();
			session.getTransaction().commit();
			return testScenario;
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
	}
}

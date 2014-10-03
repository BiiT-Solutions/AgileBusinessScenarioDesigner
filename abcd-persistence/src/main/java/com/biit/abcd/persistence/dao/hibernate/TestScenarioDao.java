package com.biit.abcd.persistence.dao.hibernate;

import java.util.List;

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
}

package com.biit.abcd.persistence.dao;

import com.biit.abcd.persistence.entity.testscenarios.TestScenario;
import com.biit.persistence.dao.IGenericDao;

public interface ITestScenarioDao extends IGenericDao<TestScenario> {

	public TestScenario getTestScenarioById(Long scenarioId);

}

package com.biit.abcd.persistence.dao;

import java.util.List;

import com.biit.abcd.persistence.entity.testscenarios.TestScenario;
import com.biit.persistence.dao.IGenericDao;

public interface ITestScenarioDao extends IGenericDao<TestScenario> {

	TestScenario getTestScenarioById(Long scenarioId);

	List<TestScenario> getTestScenarioByForm(Long formId);

	List<TestScenario> getTestScenarioByForm(String formLabel, Long formOrganizationId);

}

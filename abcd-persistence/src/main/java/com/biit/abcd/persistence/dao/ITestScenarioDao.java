package com.biit.abcd.persistence.dao;

import java.util.List;

import com.biit.abcd.persistence.entity.testscenarios.TestScenario;
import com.biit.persistence.dao.IJpaGenericDao;

public interface ITestScenarioDao extends IJpaGenericDao<TestScenario,Long> {

	List<TestScenario> getTestScenarioByForm(Long formId);

	List<TestScenario> getTestScenarioByForm(String formLabel, Long formOrganizationId);

}

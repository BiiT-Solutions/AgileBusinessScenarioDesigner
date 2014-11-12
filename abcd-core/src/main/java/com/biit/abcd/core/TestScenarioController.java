package com.biit.abcd.core;

import java.util.List;

import com.biit.abcd.persistence.dao.ITestScenarioDao;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.testscenarios.TestScenario;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;

public class TestScenarioController {
	private List<TestScenario> testScenarios = null;
	private ITestScenarioDao testScenarioDao;
	private TestScenario lastAccessTestScenario;

	public TestScenarioController(SpringContextHelper helper) {
		testScenarioDao = (ITestScenarioDao) helper.getBean("testScenarioDao");
	}

	/**
	 * Read all test scenarios from database. This method is synchronized.
	 * 
	 * @return
	 */
	public List<TestScenario> getTestScenarios(Form form) {
		if (testScenarios == null) {
			synchronized (TestScenarioController.class) {
				testScenarios = testScenarioDao.getTestScenarioByFormId(form.getId());
			}
		}
		return testScenarios;
	}

	/**
	 * Return the test scenario with the Id passed
	 * 
	 * @return
	 */
	public TestScenario getTestScenarioById(Long scenarioId) {
		TestScenario testScenario = null;
		synchronized (TestScenarioController.class) {
			testScenario = testScenarioDao.getTestScenarioById(scenarioId);
		}
		return testScenario;
	}

	public void setTestScenarios(List<TestScenario> testScenarios, Form form) {
		getTestScenarios(form).clear();
		getTestScenarios(form).addAll(testScenarios);
		clearWorkVariables();
	}

	/**
	 * Remove all old test scenarios and store all the new ones. This method is synchronized.
	 * 
	 * @throws UnexpectedDatabaseException
	 */
	public void update(List<TestScenario> testScenarios, Form form) throws UnexpectedDatabaseException {
		synchronized (TestScenarioController.class) {
			// Remove unused variables.
			if (this.testScenarios != null) {
				for (TestScenario testScenario : this.testScenarios) {
					if (!testScenarios.contains(testScenario)) {
						testScenarioDao.makeTransient(testScenario);
					}
				}
			}
			for (TestScenario testScenario : testScenarios) {
				testScenarioDao.makePersistent(testScenario);
			}
		}
	}

	public TestScenario getLastAccessTestScenario() {
		return lastAccessTestScenario;
	}

	public void setLastAccessTestScenario(TestScenario lastAccessTestScenario) {
		this.lastAccessTestScenario = lastAccessTestScenario;
	}

	public void clearWorkVariables() {
		this.testScenarios = null;
		this.lastAccessTestScenario = null;
	}
}

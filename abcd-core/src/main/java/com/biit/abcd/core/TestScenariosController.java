package com.biit.abcd.core;

import java.util.List;

import com.biit.abcd.persistence.dao.ITestScenarioDao;
import com.biit.abcd.persistence.entity.testscenarios.TestScenario;

public class TestScenariosController {
	private List<TestScenario> testScenarios = null;
	private ITestScenarioDao testScenarioDao;
	private TestScenario lastAccessTestScenario;

	public TestScenariosController(SpringContextHelper helper) {
		testScenarioDao = (ITestScenarioDao) helper.getBean("testScenarioDao");
	}

	/**
	 * Read all test scenarios from database. This method is synchronized.
	 * 
	 * @return
	 */
	public List<TestScenario> getTestScenarios() {
		if (testScenarios == null) {
			synchronized (TestScenariosController.class) {
				if (testScenarios == null) {
					testScenarios = testScenarioDao.getAll();
				}
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
		synchronized (TestScenariosController.class) {
			testScenario = testScenarioDao.getTestScenarioById(scenarioId);
		}
		return testScenario;
	}

	public void setTestScenarios(List<TestScenario> testScenarios) {
		getTestScenarios().clear();
		getTestScenarios().addAll(testScenarios);
	}

	/**
	 * Remove all old test scenarios and store all the new ones. This method is
	 * synchronized.
	 */
	public void update(List<TestScenario> testScenarios) {
		synchronized (TestScenariosController.class) {
			// Remove unused variables.
			for (TestScenario testScenario : this.testScenarios) {
				if (!testScenarios.contains(testScenario)) {
					testScenarioDao.makeTransient(testScenario);
				}
			}

			for (TestScenario testScenario : testScenarios) {
				testScenarioDao.makePersistent(testScenario);
			}
			setTestScenarios(testScenarios);
		}
	}

	public TestScenario getLastAccessTestScenario() {
		return lastAccessTestScenario;
	}

	public void setLastAccessTestScenario(TestScenario lastAccessTestScenario) {
		this.lastAccessTestScenario = lastAccessTestScenario;
	}
}

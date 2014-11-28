package com.biit.abcd.core;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	public void update(List<TestScenario> testScenariosFromTable, Form form) throws UnexpectedDatabaseException {
		synchronized (TestScenarioController.class) {
			// Remove unused variables.
			if (testScenarios != null) {
				Set<TestScenario> testScenariosToRemove = new HashSet<TestScenario>();
				// Remove it from the database
				for (TestScenario testScenarioFromMemory : testScenarios) {
					if (!testScenariosFromTable.contains(testScenarioFromMemory)) {
						testScenariosToRemove.add(testScenarioFromMemory);
						testScenarioDao.makeTransient(testScenarioFromMemory);
					}
				}
				// Remove it from memory
				for(TestScenario testScenarioToRemove : testScenariosToRemove){
					testScenarios.remove(testScenarioToRemove);
				}
			}
			
			for (TestScenario testScenario : testScenariosFromTable) {
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

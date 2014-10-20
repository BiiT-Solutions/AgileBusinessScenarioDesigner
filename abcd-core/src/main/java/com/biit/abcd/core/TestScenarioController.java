package com.biit.abcd.core;

import java.util.List;

import com.biit.abcd.persistence.dao.ITestScenarioDao;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.testscenarios.TestScenario;

public class TestScenarioController {
	private List<TestScenario> testScenarios = null;
	private ITestScenarioDao testScenarioDao;
	private TestScenario lastAccessTestScenario;

	public TestScenarioController(SpringContextHelper helper) {
		testScenarioDao = (ITestScenarioDao) helper.getBean("testScenarioDao");
	}

	// /**
	// * Read all test scenarios from database. This method is synchronized.
	// *
	// * @return
	// */
	// public List<TestScenario> getTestScenarios() {
	// if (testScenarios == null) {
	// synchronized (TestScenarioController.class) {
	// if (testScenarios == null) {
	// testScenarios =
	// testScenarioDao.getTestScenarioByFormLabelVersionOrganizationId(label,
	// version,
	// organizationId);
	// }
	// }
	// }
	// return testScenarios;
	// }

	/**
	 * Read all test scenarios from database. This method is synchronized.
	 * 
	 * @return
	 */
	public List<TestScenario> getTestScenarios(Form form) {

		System.out.println("FORM LABEL: " + form.getLabel());
		System.out.println("FORM VERSION: " + form.getVersion());
		System.out.println("FORM ORGANIZATION: " + form.getOrganizationId());

		if (testScenarios == null) {
			synchronized (TestScenarioController.class) {
				if (testScenarios == null) {
					testScenarios = testScenarioDao.getTestScenarioByFormLabelVersionOrganizationId(form.getLabel(),
							form.getVersion(), form.getOrganizationId());
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
	 * Remove all old test scenarios and store all the new ones. This method is
	 * synchronized.
	 */
	public void update(List<TestScenario> testScenarios, Form form) {
		synchronized (TestScenarioController.class) {
			// Remove unused variables.
			for (TestScenario testScenario : this.testScenarios) {
				if (!testScenarios.contains(testScenario)) {
					testScenarioDao.makeTransient(testScenario);
				}
			}

			for (TestScenario testScenario : testScenarios) {
				testScenarioDao.makePersistent(testScenario);
			}
			setTestScenarios(testScenarios, form);
		}
	}

	public void removeTestScenario(TestScenario testScenario) {
		synchronized (TestScenarioController.class) {
			testScenarioDao.makeTransient(testScenario);
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

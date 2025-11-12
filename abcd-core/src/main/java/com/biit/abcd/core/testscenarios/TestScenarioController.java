package com.biit.abcd.core.testscenarios;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (Core)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.biit.abcd.core.SpringContextHelper;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.dao.ITestScenarioDao;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.testscenarios.TestScenario;
import com.biit.abcd.persistence.utils.Exceptions.TestScenarioNotEqualsException;
import com.biit.persistence.dao.exceptions.ElementCannotBePersistedException;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.persistence.entity.exceptions.ElementCannotBeRemovedException;

public class TestScenarioController {
	private List<TestScenario> testScenarios = null;
	private ITestScenarioDao testScenarioDao;
	private TestScenario lastAccessTestScenario;
	private boolean unsavedChanges;

	public TestScenarioController(SpringContextHelper helper) {
		testScenarioDao = (ITestScenarioDao) helper.getBean("testScenarioDao");
		unsavedChanges = false;
	}

	/**
	 * Read all test scenarios from database. This method is synchronized.
	 *
	 * @return the list of test scenarios.
	 */
	public List<TestScenario> getTestScenarios(Form form) {
		if (testScenarios == null) {
			synchronized (TestScenarioController.class) {
				testScenarios = testScenarioDao.getTestScenarioByForm(form.getId());
			}
		}
		return testScenarios;
	}

	public void addTestScenario(Form form, TestScenario testScenario) {
		getTestScenarios(form).add(testScenario);
		unsavedChanges = true;
	}

	/**
	 * Return the test scenario with the Id passed
	 *
	 * @return a test scenario.
	 */
	public TestScenario getTestScenarioById(Long scenarioId) {
		TestScenario testScenario = null;
		synchronized (TestScenarioController.class) {
			testScenario = testScenarioDao.get(scenarioId);
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
	 *
	 * @throws UnexpectedDatabaseException
	 * @throws ElementCannotBeRemovedException
	 * @throws ElementCannotBePersistedException
	 */
	public void update(List<TestScenario> testScenariosFromTable, Form form) throws UnexpectedDatabaseException,
			ElementCannotBeRemovedException, ElementCannotBePersistedException {
		synchronized (TestScenarioController.class) {
			// Remove unused variables.
			if (getTestScenarios(form) != null) {
				Set<TestScenario> testScenariosToRemove = new HashSet<TestScenario>();
				// Remove it from the database
				for (TestScenario testScenarioFromMemory : testScenarios) {
					if (!testScenariosFromTable.contains(testScenarioFromMemory)) {
						testScenariosToRemove.add(testScenarioFromMemory);
						testScenarioDao.makeTransient(testScenarioFromMemory);
					}
				}
				// Remove it from memory
				for (TestScenario testScenarioToRemove : testScenariosToRemove) {
					testScenarios.remove(testScenarioToRemove);
				}
			}

			for (TestScenario testScenario : testScenariosFromTable) {
				if (testScenario.getId() == null) {
					try {
						TestScenario savedTestScenario = testScenarioDao.makePersistent(testScenario);
						testScenarios.set(testScenarios.indexOf(testScenario), savedTestScenario);
					} catch (Exception e) {
						AbcdLogger.errorMessage(this.getClass().getName(), "Impossible to store TestScenario '"
								+ testScenario + "' with id '" + testScenario.getId() + "'.");
						throw e;
					}
				} else {
					TestScenario mergedScenario = testScenarioDao.merge(testScenario);
					testScenarios.set(testScenarios.indexOf(testScenario), mergedScenario);
				}
			}
			unsavedChanges = false;
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

	public boolean isUnsavedChanges() {
		return unsavedChanges;
	}

	public void setUnsavedChanges(boolean unsavedChanges) {
		this.unsavedChanges = unsavedChanges;
	}

	public void checkUnsavedChanges() throws TestScenarioNotEqualsException {
		if (unsavedChanges) {
			throw new TestScenarioNotEqualsException("Exists unsaved changes in Test Scenarios.");
		}
	}
}

package com.biit.abcd.core;


public class TestScenariosController {
	// private List<TestScenario> testScenarios = null;
	// private ITestScenarioDao testScenarioDao;
	// private TestScenario lastAccessTestScenario;
	//
	// public TestScenariosController(SpringContextHelper helper) {
	// testScenarioDao = (ITestScenarioDao) helper.getBean("testScenarioDao");
	// }
	//
	// /**
	// * Read all test scenarios from database. This method is synchronized.
	// *
	// * @return
	// */
	// public List<TestScenario> getTestScenarios() {
	// if (testScenarios == null) {
	// synchronized (TestScenariosController.class) {
	// if (testScenarios == null) {
	// testScenarios = testScenarioDao.getAll();
	// }
	// }
	// }
	// return testScenarios;
	// }
	//
	// public void setTestScenarios(List<TestScenario> testScenarios) {
	// getTestScenarios().clear();
	// getTestScenarios().addAll(testScenarios);
	// }
	//
	// /**
	// * Remove all old test scenarios and store all the new ones. This method
	// is synchronized.
	// */
	// public void update(List<TestScenario> testScenarios) {
	// synchronized (TestScenariosController.class) {
	// // Remove unused variables.
	// for (TestScenario testScenario : this.testScenarios) {
	// if (!testScenarios.contains(testScenario)) {
	// testScenarioDao.makeTransient(testScenario);
	// }
	// }
	//
	// for (TestScenario testScenario : testScenarios) {
	// testScenarioDao.makePersistent(testScenario);
	// }
	// setTestScenarios(testScenarios);
	// }
	// }
	//
	// public TestScenario getLastAccessTestScenario() {
	// return lastAccessTestScenario;
	// }
	//
	// public void setLastAccessTestScenario(TestScenario
	// lastAccessTestScenario) {
	// this.lastAccessTestScenario = lastAccessTestScenario;
	// }
}

package com.biit.abcd.webpages.components.testscenario;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.testscenarios.TestScenario;
import com.biit.abcd.webpages.TestScenarioEditor;
import com.biit.abcd.webpages.components.WindowCreateNewObject;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.vaadin.ui.TextField;

public class WindowNewTestScenario extends WindowCreateNewObject {
	private static final long serialVersionUID = -7289531658379874556L;

	public WindowNewTestScenario(TestScenarioEditor parentWindow, LanguageCodes windowCaption,
			LanguageCodes inputFieldCaption) {
		super(parentWindow, windowCaption, inputFieldCaption);
	}

	@Override
	public void acceptAction(TextField inputTextField) {
		for (TestScenario existingScenarios : UserSessionHandler.getTestScenariosController().getTestScenarios()) {
			if (existingScenarios.getName().equals(inputTextField.getValue())) {
				MessageManager.showError(LanguageCodes.ERROR_REPEATED_TEST_SCENARIO_NAME);
				return;
			}
		}
		try {
			TestScenario testScenario = new TestScenario();
			testScenario.setName(inputTextField.getValue());
			testScenario.setCreatedBy(UserSessionHandler.getUser());
			testScenario.setUpdatedBy(UserSessionHandler.getUser());
			testScenario.setUpdateTime();
			UserSessionHandler.getTestScenariosController().getTestScenarios().add(testScenario);
			((TestScenarioEditor) getParentWindow()).addTestScenarioToMenu(testScenario);
			((TestScenarioEditor) getParentWindow()).sortTableMenu();

			AbcdLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress()
					+ "' has created a " + testScenario.getClass() + " with 'Name: " + testScenario.getName() + "'.");
			close();
			
		} catch (FieldTooLongException e) {
			AbcdLogger.warning(this.getClass().getName(), e.toString());
			MessageManager.showWarning(ServerTranslate.translate(LanguageCodes.WARNING_TITLE),
					ServerTranslate.translate(LanguageCodes.TEST_SCENARIOS_WARNING_NAME_TOO_LONG));
		}
	}
}
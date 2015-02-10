package com.biit.abcd.webpages.elements.testscenario;

import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.testscenarios.TestScenario;
import com.biit.abcd.webpages.TestScenarioEditor;
import com.biit.abcd.webpages.components.WindowCreateNewObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.vaadin.ui.TextField;

public class WindowNewTestScenario extends WindowCreateNewObject {
	private static final long serialVersionUID = -7289531658379874556L;

	public WindowNewTestScenario(TestScenarioEditor parentWindow, LanguageCodes windowCaption,
			LanguageCodes inputFieldCaption) {
		super(parentWindow, windowCaption, inputFieldCaption);
	}

	@Override
	public void acceptAction(TextField inputTextField) {
		for (TestScenario existingScenarios : UserSessionHandler.getTestScenariosController().getTestScenarios(
				UserSessionHandler.getFormController().getForm())) {
			if (existingScenarios.getName().equals(inputTextField.getValue())) {
				MessageManager.showError(LanguageCodes.ERROR_REPEATED_TEST_SCENARIO_NAME);
				return;
			}
		}
		try {
			TestScenario testScenario = new TestScenario(inputTextField.getValue(), UserSessionHandler
					.getFormController().getForm());
			UserSessionHandler.getTestScenariosController()
					.getTestScenarios(UserSessionHandler.getFormController().getForm()).add(testScenario);
			((TestScenarioEditor) getParentWindow()).addTestScenarioToMenu(testScenario);
			((TestScenarioEditor) getParentWindow()).sortTableMenu();

			AbcdLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress()
					+ "' has created a " + testScenario.getClass() + " with 'Name: " + testScenario.getName() + "'.");
			close();

		} catch (FieldTooLongException e) {
			AbcdLogger.warning(this.getClass().getName(), e.toString());
			MessageManager.showWarning(LanguageCodes.WARNING_TITLE,LanguageCodes.TEST_SCENARIOS_WARNING_NAME_TOO_LONG);
		} catch (CharacterNotAllowedException e) {
			AbcdLogger.warning(this.getClass().getName(), e.toString());
			MessageManager.showWarning(LanguageCodes.WARNING_TITLE,LanguageCodes.TEST_SCENARIOS_WARNING_CHARACTER_NOT_ALLOWED);
		} catch (NotValidStorableObjectException e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		} catch (NotValidChildException e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		} catch (ElementIsReadOnly e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}
	}
}
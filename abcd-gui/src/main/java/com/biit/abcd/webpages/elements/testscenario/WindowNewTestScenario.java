package com.biit.abcd.webpages.elements.testscenario;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (GUI)
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

import java.util.List;

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
	private List<TestScenario> testsScenarios;

	public WindowNewTestScenario(TestScenarioEditor parentWindow, LanguageCodes windowCaption,
			LanguageCodes inputFieldCaption, List<TestScenario> testsScenarios) {
		super(parentWindow, windowCaption, inputFieldCaption);
		this.testsScenarios = testsScenarios;
	}

	@Override
	public void concreteAcceptAction(TextField inputTextField) {
		for (TestScenario existingScenario : testsScenarios) {
			if (existingScenario.getName().equals(inputTextField.getValue())) {
				MessageManager.showError(LanguageCodes.ERROR_REPEATED_TEST_SCENARIO_NAME);
				return;
			}
		}
		try {
			TestScenario testScenario = new TestScenario(inputTextField.getValue(), UserSessionHandler
					.getFormController().getForm());
			UserSessionHandler.getTestScenariosController().addTestScenario(
					UserSessionHandler.getFormController().getForm(), testScenario);
			((TestScenarioEditor) getParentWindow()).addTestScenarioToMenu(testScenario);
			((TestScenarioEditor) getParentWindow()).sortTableMenu();

			AbcdLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress()
					+ "' has created a " + testScenario.getClass() + " with 'Name: " + testScenario.getName() + "'.");
			close();

		} catch (FieldTooLongException e) {
			AbcdLogger.warning(this.getClass().getName(), e.getMessage());
			MessageManager.showWarning(LanguageCodes.WARNING_TITLE, LanguageCodes.TEST_SCENARIOS_WARNING_NAME_TOO_LONG);
		} catch (CharacterNotAllowedException e) {
			AbcdLogger.warning(this.getClass().getName(), e.getMessage());
			MessageManager.showWarning(LanguageCodes.WARNING_TITLE,
					LanguageCodes.TEST_SCENARIOS_WARNING_CHARACTER_NOT_ALLOWED);
		} catch (NotValidStorableObjectException e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		} catch (NotValidChildException e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		} catch (ElementIsReadOnly e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}
	}
}

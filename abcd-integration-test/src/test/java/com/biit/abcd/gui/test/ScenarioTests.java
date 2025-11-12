package com.biit.abcd.gui.test;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (Test)
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

import java.util.Random;

import org.testng.annotations.Test;

@Test(groups = "scenarioEditor")
public class ScenarioTests extends AbcdTester {

	private static final String ABCD_DATABASE_URL = "jdbc:mysql://vagrant-mysql.biit-solutions.com:3306/abcdtest?useUnicode=true&characterEncoding=UTF-8";;
	private static final String ABCD_DATABASE_USER = "testuser";
	private static final String ABCD_DATABASE_PASS = "my-password";
	private static final String ABCD_DATABASE_STRUCTURE = "./src/test/resources/database/abcd_base.sql";
	private static final String ABCD_DATABASE_STRUCTURE_OUTPUT = "./src/test/resources/database/abcd_base.out";
	private static final String ABCD_DATABASE_STRUCTURE_ERROR_OUTPUT = "./src/test/resources/database/abcd_base.error";
	private static final String ABCD_DATABASE_CONTENT = "./src/test/resources/database/data.sql";
	private static final String ABCD_DATABASE_CONTENT_OUTPUT = "./src/test/resources/database/data.out";
	private static final String ABCD_DATABASE_CONTENT_ERROR_OUTPUT = "./src/test/resources/database/data.error";
	private static final String ABCD_DATABASE_DROP = "./src/test/resources/database/abcd_drop_database.sql";
	private static final String ABCD_DATABASE_DROP_OUTPUT = "./src/test/resources/database/abcd_drop_database.out";
	private static final String ABCD_DATABASE_DROP_ERROR_OUTPUT = "./src/test/resources/database/abcd_drop_database.error";
	private static final Integer SELECTED_FORM_ROW = 1;
	private static final Integer MAX_TEST_SCENARIOS_NUMBER = 8;
	private static final Integer MAX_CATEGORIES_NUMBER = 10;
	private static final String NEW_TEST_SCENARIO_NAME = "New_test_scenario_2";
	private static final Integer FINANCIEN_CATEGORY_INDEX = 2;
	private static final String FINANCIEN_CATEGORY_COMBOBOX_CAPTION_INKOMEN = "Inkomen";
	private static final String FINANCIEN_CATEGORY_COMBOBOX_CAPTION_BRON = "Bron";
	private static final String FINANCIEN_CATEGORY_COMBOBOX_CAPTION_SCHULDEN = "Schulden";
	private static final String FINANCIEN_CATEGORY_COMBOBOX_CAPTION_UITGAVEN = "Uitgaven";
	private static final String FINANCIEN_CATEGORY_COMBOBOX_CAPTION_OVERZICHT = "Overzicht";
	private static final String FINANCIEN_CATEGORY_COMBOBOX_VALUE_INKOMEN = "Financien.Inkomen.Onvoldoende";
	private static final String FINANCIEN_CATEGORY_COMBOBOX_VALUE_BRON = "Inkomsten.Bron.Werk";
	private static final String FINANCIEN_CATEGORY_COMBOBOX_VALUE_SCHULDEN = "Financien.Schulden.BlijvenGelijk";
	private static final String FINANCIEN_CATEGORY_COMBOBOX_VALUE_UITGAVEN = "Financien.Uitgaven.OnverstandigWelVeroorloven";
	private static final String FINANCIEN_CATEGORY_COMBOBOX_VALUE_OVERZICHT = "Financien.Beheer.Overzicht.VeelVerrassingen";
	private final Random randomGenerator = new Random();

	private void fillDatabase() {
		// Remove old database.
		executeSqlDump(ABCD_DATABASE_URL, ABCD_DATABASE_USER, ABCD_DATABASE_PASS, ABCD_DATABASE_DROP, ABCD_DATABASE_DROP_OUTPUT,
				ABCD_DATABASE_DROP_ERROR_OUTPUT);
		// Add new data.
		executeSqlDump(ABCD_DATABASE_URL, ABCD_DATABASE_USER, ABCD_DATABASE_PASS, ABCD_DATABASE_STRUCTURE, ABCD_DATABASE_STRUCTURE_OUTPUT,
				ABCD_DATABASE_STRUCTURE_ERROR_OUTPUT);
		executeSqlDump(ABCD_DATABASE_URL, ABCD_DATABASE_USER, ABCD_DATABASE_PASS, ABCD_DATABASE_CONTENT, ABCD_DATABASE_CONTENT_OUTPUT,
				ABCD_DATABASE_CONTENT_ERROR_OUTPUT);
		cleanCache();
	}

	private Integer getRandomInteger(Integer limit) {
		return randomGenerator.nextInt(limit);
	}

	private void selectRandomCategories() {
		// +1 To avoid selecting the form instead the category
		getTestScenarioCreator().selectCategory(getRandomInteger(MAX_CATEGORIES_NUMBER + 1));
		getTestScenarioCreator().selectCategory(getRandomInteger(MAX_CATEGORIES_NUMBER + 1));
		getTestScenarioCreator().selectCategory(getRandomInteger(MAX_CATEGORIES_NUMBER + 1));
		getTestScenarioCreator().selectCategory(getRandomInteger(MAX_CATEGORIES_NUMBER + 1));
		getTestScenarioCreator().selectCategory(getRandomInteger(MAX_CATEGORIES_NUMBER + 1));
	}

	@Test
	public void checkTestScenarios() {
		fillDatabase();
		loginFormEdit1();
		getFormManager().clickInFormTable(SELECTED_FORM_ROW);
		getFormManager().goToTestScenarioCreator();
		getTestScenarioCreator().selectTestScenario(getRandomInteger(MAX_TEST_SCENARIOS_NUMBER));
		selectRandomCategories();
		getTestScenarioCreator().selectTestScenario(getRandomInteger(MAX_TEST_SCENARIOS_NUMBER));
		selectRandomCategories();
		getTestScenarioCreator().selectTestScenario(getRandomInteger(MAX_TEST_SCENARIOS_NUMBER));
		selectRandomCategories();
		getTestScenarioCreator().logOut();
	}

	@Test(dependsOnMethods = "checkTestScenarios")
	public void deleteTestScenarios() {
		loginFormEdit1();
		getFormManager().clickInFormTable(SELECTED_FORM_ROW);
		getFormManager().goToTestScenarioCreator();
		getTestScenarioCreator().selectTestScenario(getRandomInteger(MAX_TEST_SCENARIOS_NUMBER));
		getTestScenarioCreator().clickRemoveButton();
		getTestScenarioCreator().clickAcceptProceed();
		getTestScenarioCreator().save();
		getTestScenarioCreator().logOut();
	}

	@Test(dependsOnMethods = "deleteTestScenarios")
	public void createTestScenarios() {
		loginFormEdit1();
		getFormManager().clickInFormTable(SELECTED_FORM_ROW);
		getFormManager().goToTestScenarioCreator();
		getTestScenarioCreator().createTestScenario(NEW_TEST_SCENARIO_NAME);
		getTestScenarioCreator().save();
		getTestScenarioCreator().selectCategory(FINANCIEN_CATEGORY_INDEX);
		getTestScenarioCreator().setComboBoxValue(FINANCIEN_CATEGORY_COMBOBOX_CAPTION_INKOMEN, FINANCIEN_CATEGORY_COMBOBOX_VALUE_INKOMEN);
		getTestScenarioCreator().setComboBoxValue(FINANCIEN_CATEGORY_COMBOBOX_CAPTION_BRON, FINANCIEN_CATEGORY_COMBOBOX_VALUE_BRON);
		getTestScenarioCreator().setComboBoxValue(FINANCIEN_CATEGORY_COMBOBOX_CAPTION_SCHULDEN, FINANCIEN_CATEGORY_COMBOBOX_VALUE_SCHULDEN);
		getTestScenarioCreator().setComboBoxValue(FINANCIEN_CATEGORY_COMBOBOX_CAPTION_UITGAVEN, FINANCIEN_CATEGORY_COMBOBOX_VALUE_UITGAVEN);
		getTestScenarioCreator().setComboBoxValue(FINANCIEN_CATEGORY_COMBOBOX_CAPTION_OVERZICHT, FINANCIEN_CATEGORY_COMBOBOX_VALUE_OVERZICHT);
		getTestScenarioCreator().save();
		getTestScenarioCreator().logOut();
	}

}

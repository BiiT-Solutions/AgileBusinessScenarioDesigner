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

import java.util.HashMap;

import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.core.testscenarios.validator.TestScenarioValidator;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.testscenarios.TestScenario;
import com.biit.abcd.persistence.entity.testscenarios.TestScenarioCategory;
import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.themes.Runo;

public class TestScenarioMainLayout extends HorizontalLayout {
	private static final long serialVersionUID = -3526986076061463631L;
	private static final String PANEL_STYLE = "v-test-scenario-background-panel";
	private TestScenarioTable treeTestTable;
	private HashMap<String, TreeObject> originalReferenceTreeObjectMap;
	private Panel editorBackground;

	public TestScenarioMainLayout() {
		super();
		setSizeFull();
		setStyleName(Runo.PANEL_LIGHT);
	}

	public void setContent(Form form, TestScenario testScenario) throws NotValidChildException, FieldTooLongException,
			CharacterNotAllowedException {
		removeAllComponents();
		if (form != null && testScenario != null) {
			TestScenarioValidator testScenarioValidator = new TestScenarioValidator();
			testScenarioValidator.checkAndModifyTestScenarioStructure(form, testScenario.getTestScenarioForm());
			originalReferenceTreeObjectMap = form.getOriginalReferenceTreeObjectMap();
			createContent(form, testScenario);
		}
	}

	/**
	 * Creates the content for the complete test scenario definition
	 *
	 * @param form
	 * @param testScenario
	 * @throws NotValidChildException
	 * @throws CharacterNotAllowedException
	 * @throws FieldTooLongException
	 */
	private void createContent(Form form, TestScenario testScenario) throws NotValidChildException,
			FieldTooLongException, CharacterNotAllowedException {
		if (form != null && testScenario != null) {
			createTreeTable(testScenario);
			createEditForm();
			addComponent(treeTestTable);
			addComponent(editorBackground);
			setExpandRatio(treeTestTable, 1);
			setExpandRatio(editorBackground, 3);
		}
	}

	private void createTreeTable(TestScenario testScenario) {
		treeTestTable = new TestScenarioTable();
		treeTestTable.setSizeFull();
		treeTestTable.setRootElement(testScenario.getTestScenarioForm());
		treeTestTable.setSelectable(true);
		treeTestTable.setNullSelectionAllowed(false);
		treeTestTable.setImmediate(true);
		treeTestTable.setValue(testScenario.getTestScenarioForm());
		treeTestTable.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -1835114202597377993L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				Object valueSelected = event.getProperty().getValue();
				if (valueSelected instanceof TestScenarioCategory) {
					CustomCategoryEditor customCategory = new CustomCategoryEditor(originalReferenceTreeObjectMap,
							(TreeObject) valueSelected);
					customCategory.addFieldValueChangeListener(new FieldValueChangedListener() {
						@Override
						public void valueChanged(Field<?> field) {
							UserSessionHandler.getTestScenariosController().setUnsavedChanges(true);
						}
					});
					editorBackground.setContent(customCategory);
				} else {
					editorBackground.setContent(null);
				}
			}
		});
	}

	private Component createEditForm() {
		editorBackground = new Panel();
		editorBackground.setSizeFull();
		editorBackground.setStyleName(PANEL_STYLE);
		return editorBackground;
	}

	public TestScenarioTable getTreeTestTable() {
		return treeTestTable;
	}

	public void setTreeTestTable(TestScenarioTable treeTestTable) {
		this.treeTestTable = treeTestTable;
	}

	public void refreshTable(TreeObject treeObject) {
		this.treeTestTable.setRootElement(treeObject);
	}
}

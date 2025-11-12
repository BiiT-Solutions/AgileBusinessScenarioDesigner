package com.biit.abcd.webpages;

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

import com.biit.abcd.ApplicationFrame;
import com.biit.abcd.MessageManager;
import com.biit.abcd.authentication.UserSessionHandler;
import com.biit.abcd.core.security.AbcdActivity;
import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.testscenarios.TestScenario;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.abcd.webpages.components.AcceptCancelWindow.AcceptActionListener;
import com.biit.abcd.webpages.components.AlertMessageWindow;
import com.biit.abcd.webpages.components.FormWebPageComponent;
import com.biit.abcd.webpages.components.HorizontalCollapsiblePanel;
import com.biit.abcd.webpages.elements.testscenario.SelectTestScenarioTableEditable;
import com.biit.abcd.webpages.elements.testscenario.TestScenarioEditorUpperMenu;
import com.biit.abcd.webpages.elements.testscenario.TestScenarioMainLayout;
import com.biit.abcd.webpages.elements.testscenario.WindowNewTestScenario;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.UI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestScenarioEditor extends FormWebPageComponent {
    private static final long serialVersionUID = -6743796589244668454L;
    private static final List<AbcdActivity> activityPermissions = new ArrayList<>(Arrays.asList(AbcdActivity.READ));
    private TestScenarioMainLayout testScenarioForm;
    private SelectTestScenarioTableEditable tableSelectTestScenario;
    private TestScenarioEditorUpperMenu testScenarioUpperMenu;

    public TestScenarioEditor() {
        super();
    }

    @Override
    protected void initContent() {
        // If there is no form, then go back to form manager.
        if (UserSessionHandler.getFormController().getForm() == null) {
            AbcdLogger.warning(this.getClass().getName(), "No Form selected, redirecting to Form Manager.");
            ApplicationFrame.navigateTo(WebMap.FORM_MANAGER);
            return;
        }

        initUpperMenu();

        updateButtons(true);

        // Create container
        HorizontalCollapsiblePanel collapsibleLayout = new HorizontalCollapsiblePanel(false);
        collapsibleLayout.setSizeFull();

        // Create menu
        tableSelectTestScenario = new SelectTestScenarioTableEditable();
        tableSelectTestScenario.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                try {
                    UserSessionHandler.getTestScenariosController().setLastAccessTestScenario(getSelectedTestScenario());
                    refreshTestScenario();
                } catch (FieldTooLongException | CharacterNotAllowedException e) {
                    AbcdLogger.errorMessage(this.getClass().getName(), e);
                }
            }

        });
        collapsibleLayout.createMenu(tableSelectTestScenario);

        // Create empty form
        testScenarioForm = new TestScenarioMainLayout();
        collapsibleLayout.setContent(testScenarioForm);
        getWorkingAreaLayout().addComponent(collapsibleLayout);

        if (UserSessionHandler.getTestScenariosController().getTestScenarios(UserSessionHandler.getFormController().getForm()) != null) {

            UserSessionHandler.getTestScenariosController().clearWorkVariables();
            // Add tables
            tableSelectTestScenario.updateTestScenarios(UserSessionHandler.getTestScenariosController().getTestScenarios(
                    UserSessionHandler.getFormController().getForm()));

            sortTableMenu();

            if (UserSessionHandler.getTestScenariosController().getLastAccessTestScenario() != null) {
                tableSelectTestScenario.setSelectedTestScenario(UserSessionHandler.getTestScenariosController().getLastAccessTestScenario());
            } else {
                // Select the first one if available.
                if (!UserSessionHandler.getTestScenariosController().getTestScenarios(UserSessionHandler.getFormController().getForm()).isEmpty()) {

                    List<TestScenario> testScenarios = (UserSessionHandler.getTestScenariosController().getTestScenarios(
                            UserSessionHandler.getFormController().getForm()));
                    if (!testScenarios.isEmpty()) {
                        tableSelectTestScenario.setSelectedTestScenario(testScenarios.get(0));
                    }
                }
            }
            // Create form panel content
            try {
                refreshTestScenario();
            } catch (FieldTooLongException | CharacterNotAllowedException e) {
                AbcdLogger.errorMessage(this.getClass().getName(), e);
            }

        } else {
            AbcdLogger.warning(this.getClass().getName(), "No Form selected, redirecting to Form Manager.");
            MessageManager.showError(LanguageCodes.ERROR_UNEXPECTED_ERROR);
            ApplicationFrame.navigateTo(WebMap.FORM_MANAGER);
        }

        // Disable the bottom buttons
        getBottomMenu().setEnabled(false);
    }

    private void initUpperMenu() {
        final TestScenarioEditor thisPage = this;

        testScenarioUpperMenu = new TestScenarioEditorUpperMenu();

        testScenarioUpperMenu.addSaveButtonClickListener((ClickListener) event -> save());

        testScenarioUpperMenu.addNewTestScenarioButtonClickListener((ClickListener) event -> UI.getCurrent().addWindow(
                new WindowNewTestScenario(thisPage, LanguageCodes.TEST_SCENARIOS_EDITOR_NEW_WINDOW_CAPTION,
                        LanguageCodes.TEST_SCENARIOS_EDITOR_NEW_WINDOW_FIELD_CAPTION, tableSelectTestScenario.getTestScenarios())));

        testScenarioUpperMenu.addRemoveTestScenarioButtonClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                if (getSelectedTestScenario() != null) {
                    final AlertMessageWindow windowAccept = new AlertMessageWindow(LanguageCodes.WARNING_TEST_SCENARIO_DELETION);
                    windowAccept.addAcceptActionListener(new AcceptActionListener() {
                        @Override
                        public void acceptAction(AcceptCancelWindow window) {
                            try {
                                TestScenario testScenario = getSelectedTestScenario();
                                removeSelectedTestScenario();
                                AbcdLogger.info(this.getClass().getName(), "User '" + UserSessionHandler.getUser().getEmailAddress() + "' has removed a "
                                        + testScenario.getClass() + " with name '" + testScenario.getName() + "'.");
                                windowAccept.close();
                            } catch (FieldTooLongException | CharacterNotAllowedException e) {
                                AbcdLogger.errorMessage(this.getClass().getName(), e);
                            }

                        }
                    });
                    windowAccept.showCentered();
                }
            }
        });
        setUpperMenu(testScenarioUpperMenu);
    }

    private TestScenario getSelectedTestScenario() {
        return tableSelectTestScenario.getSelectedTestScenario();
    }

    private void save() {
        try {
            UserSessionHandler.getTestScenariosController()
                    .update(tableSelectTestScenario.getTestScenarios(), UserSessionHandler.getFormController().getForm());
            refreshScenarios();
            refreshTestScenario();
            MessageManager.showInfo(LanguageCodes.INFO_DATA_STORED);
        } catch (Exception e) {
            MessageManager.showError(LanguageCodes.ERROR_UNEXPECTED_ERROR);
            AbcdLogger.errorMessage(TestScenarioEditor.class.getName(), e);
        }
    }

    private void refreshScenarios() {
        TestScenario selectedScenario = (TestScenario) tableSelectTestScenario.getValue();
        tableSelectTestScenario.setValue(null);
        tableSelectTestScenario.removeAllItems();

        UserSessionHandler.getTestScenariosController().clearWorkVariables();
        // Add tables
        tableSelectTestScenario.updateTestScenarios(UserSessionHandler.getTestScenariosController().getTestScenarios(
                UserSessionHandler.getFormController().getForm()));

        sortTableMenu();

        if (selectedScenario != null) {
            for (TestScenario testScenario : UserSessionHandler.getTestScenariosController().getTestScenarios(UserSessionHandler.getFormController().getForm())) {
                if (testScenario.getComparationId().equals(selectedScenario.getComparationId())) {
                    tableSelectTestScenario.setSelectedTestScenario(testScenario);
                }
            }
        }
    }

    @Override
    public List<AbcdActivity> accessAuthorizationsRequired() {
        return activityPermissions;
    }

    private void removeSelectedTestScenario() throws FieldTooLongException, CharacterNotAllowedException {
        tableSelectTestScenario.removeSelectedRow();
        refreshTestScenario();
        UserSessionHandler.getTestScenariosController().setUnsavedChanges(true);
    }

    public void addTestScenarioToMenu(TestScenario testScenario) throws FieldTooLongException, CharacterNotAllowedException {
        tableSelectTestScenario.addRow(testScenario);
        tableSelectTestScenario.setSelectedTestScenario(testScenario);
        refreshTestScenario();
    }

    public void sortTableMenu() {
        tableSelectTestScenario.sort();
    }

    public void selectComponent(TestScenario testScenario) {
        tableSelectTestScenario.setSelectedTestScenario(testScenario);
    }

    private void refreshTestScenario() throws FieldTooLongException, CharacterNotAllowedException {
        try {
            testScenarioForm.setContent(UserSessionHandler.getFormController().getForm(), getSelectedTestScenario());
        } catch (NotValidChildException e) {
            AbcdLogger.errorMessage(this.getClass().getName(), e);
        }
    }
}

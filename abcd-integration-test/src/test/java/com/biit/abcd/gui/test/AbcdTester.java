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

import com.biit.abcd.gui.test.StatusPreservingTests.Scope;
import com.biit.abcd.gui.test.webpage.*;
import com.biit.abcd.gui.test.webpage.FormDesigner.AnswerFormat;
import com.biit.abcd.gui.test.webpage.FormDesigner.AnswerType;
import com.biit.gui.tester.VaadinGuiTester;
import com.vaadin.testbench.elements.ButtonElement;

public class AbcdTester extends VaadinGuiTester {

    private static final String NAME_CAT_1 = "category_1";
    private static final String NAME_CAT_2 = "category_2";
    private static final String NAME_QUESTION = "question";
    private static final String NAME_GROUP_1 = "group_1";
    private static final String NAME_GROUP_2 = "group_2";
    private static final String FORM_MANAGER_BUTTON = "Forms";

    protected static final String VAR_1 = "var1";
    protected static final String VAR_2 = "var2";
    protected static final String VAR_3 = "var3";

    protected static final String ABCD_READ_BIIT1 = "abcd_read@biit1.com";
    protected static final String ABCD_FORM_EDIT_BIIT1 = "abcd_form_edit@biit1.com";
    protected static final String ABCD_FORM_ADMIN_BIIT1 = "abcd_form_admin@biit1.com";
    protected static final String ABCD_APP_ADMIN_BIIT1 = "abcd_app_admin@biit1.com";
    protected static final String ABCD_GLOBAL_CONST_BIIT1 = "abcd_global_const@biit1.com";

    protected static final String ABCD_READ_BIIT2 = "abcd_read@biit2.com";
    protected static final String ABCD_FORM_EDIT_BIIT2 = "abcd_form_edit@biit2.com";
    protected static final String ABCD_FORM_ADMIN_BIIT2 = "abcd_form_admin@biit2.com";
    protected static final String ABCD_APP_ADMIN_BIIT2 = "abcd_app_admin@biit2.com";
    protected static final String ABCD_GLOBAL_CONST_BIIT2 = "abcd_global_const@biit2.com";

    private static final String CATEGORY_1 = "category_1";
    private static final String QUESTION_1 = "question_1";
    private static final String QUESTION_2 = "question_2";
    private static final String QUESTION_3 = "question_3";
    private static final String QUESTION_4 = "question_4";
    private static final String ANSWER_1 = "answer_1";
    private static final String ANSWER_2 = "answer_2";
    private static final String ANSWER_3 = "answer_3";
    private static final String ANSWER_4 = "answer_4";
    private static final String ANSWER_5 = "answer_5";
    private static final String ANSWER_6 = "answer_6";

    private static final String TEXT_VAR_VALUE = "ASD";
    private static final String DATE_VAR_VALUE = "5/11/11";
    private static final String VALID_FROM = "4/10/14";
    private static final String VALID_TO = "4/10/16";

    protected final String USER_PASSWORD = "my-password";

    private final Login loginPage;
    private final FormManager formManager;
    private final FormDesigner formDesigner;
    private final FormVariables formVariables;
    private final DiagramDesigner diagramDesigner;
    private final RuleExpression ruleExpression;
    private final RuleEditor ruleEditor;
    private final RuleTableEditor ruleTableEditor;
    private final GlobalVariables globalVariables;
    private final TestScenarioCreator testScenarioCreator;

    public AbcdTester() {
        super();
        loginPage = new Login();
        formManager = new FormManager();
        globalVariables = new GlobalVariables();
        formDesigner = new FormDesigner();
        formVariables = new FormVariables();
        diagramDesigner = new DiagramDesigner();
        ruleExpression = new RuleExpression();
        ruleEditor = new RuleEditor();
        ruleTableEditor = new RuleTableEditor();
        testScenarioCreator = new TestScenarioCreator();
        addWebpage(loginPage);
        addWebpage(formManager);
        addWebpage(globalVariables);
        addWebpage(formVariables);
        addWebpage(formDesigner);
        addWebpage(diagramDesigner);
        addWebpage(ruleExpression);
        addWebpage(ruleEditor);
        addWebpage(ruleTableEditor);
        addWebpage(testScenarioCreator);
    }

    public Login getLoginPage() {
        return loginPage;
    }

    public FormManager getFormManager() {
        return formManager;
    }

    public void login(String username, String password) {
        mainPage();
        loginPage.login(username, password);
    }

    public void loginRead1() {
        login(ABCD_READ_BIIT1, USER_PASSWORD);
    }

    public void loginFormAdmin1() {
        login(ABCD_FORM_ADMIN_BIIT1, USER_PASSWORD);
    }

    public void login(String username) {
        login(username, USER_PASSWORD);
    }

    public void loginFormEdit1() {
        login(ABCD_FORM_EDIT_BIIT1, USER_PASSWORD);
    }

    public void loginAppAdmin1() {
        login(ABCD_APP_ADMIN_BIIT1, USER_PASSWORD);
    }

    public void createForm(String formname, String username) {
        createForm(formname, username, USER_PASSWORD);
    }

    public void createForm(String formname, String username, String password) {
        login(username, password);
        getFormManager().createNewForm(formname);
    }

    public void openForm(int formNumber, String username) {
        login(username, USER_PASSWORD);
        getFormManager().selectForm(formNumber);
        getFormManager().clickFormDesigner();
    }

    public void deleteForm(int formNumber, String username) {
        login(username, USER_PASSWORD);
        getFormManager().deleteForm(formNumber);
    }

    public void cleanCache() {
        loginAppAdmin1();
        sleep(500);
        getFormDesigner().clickClearCache();
        getFormDesigner().logOut();
    }

    public void deleteAllForms() {
        loginFormAdmin1();
        getFormManager().deleteAllCreatedForms();
        getFormManager().logOut();
        cleanCache();
        sleep(250);
    }

    public FormDesigner getFormDesigner() {
        return formDesigner;
    }

    public TestScenarioCreator getTestScenarioCreator() {
        return testScenarioCreator;
    }

    public void generateTestForm() {
        getFormManager().goToDesigner(1);
        // Click on first row to deselect
        getFormDesigner().clickInTableRow(0);
        getFormDesigner().createCategory(0, NAME_CAT_1);
        getFormDesigner().clickInTableRow(1);
        getFormDesigner().createBlockOfAllKindOfQuestions(1, NAME_QUESTION);
        getFormDesigner().createGroup(1, NAME_GROUP_1);
        getFormDesigner().clickInTableRow(26);
        getFormDesigner().createBlockOfAllKindOfQuestions(26, NAME_QUESTION);
        getFormDesigner().createGroup(26, NAME_GROUP_2);
        getFormDesigner().clickInTableRow(51);
        getFormDesigner().createBlockOfAllKindOfQuestions(51, NAME_QUESTION);
        getFormDesigner().createCategory(1, NAME_CAT_2);
        getFormDesigner().clickInTableRow(1);
        getFormDesigner().createBlockOfAllKindOfQuestions(76, NAME_QUESTION);
        getFormDesigner().save();
    }

    public void clickFormManager() {
        $(ButtonElement.class).caption(FORM_MANAGER_BUTTON).first().click();
    }

    public RuleExpression getRuleExpression() {
        return ruleExpression;
    }

    public RuleEditor getRuleEditor() {
        return ruleEditor;
    }

    public RuleTableEditor getRuleTableEditor() {
        return ruleTableEditor;
    }

    public FormVariables getFormVariables() {
        return formVariables;
    }

    public GlobalVariables getGlobalVariables() {
        return globalVariables;
    }

    public DiagramDesigner getDiagramDesigner() {
        return diagramDesigner;
    }

    protected void createSampleForm() {
        getFormManager().clickFormDesigner();

        getFormDesigner().clickInTableRow(0);
        getFormDesigner().createCategory(0, CATEGORY_1);
        getFormDesigner().clickInTableRow(0);
        getFormDesigner().createQuestion(1, QUESTION_1, AnswerType.INPUT_FIELD, AnswerFormat.NUMBER);
        getFormDesigner().createQuestion(1, QUESTION_2, AnswerType.INPUT_FIELD, AnswerFormat.NUMBER);
        getFormDesigner().createRadioButton(1, 4, QUESTION_3, ANSWER_1, ANSWER_2, ANSWER_3);
        getFormDesigner().createRadioButton(1, 8, QUESTION_4, ANSWER_4, ANSWER_5, ANSWER_6);

        getFormDesigner().save();
    }

    protected void createFormVariables() {
        getFormManager().clickFormVariables();

        getFormVariables().addVariable(0, VAR_1, AnswerFormat.NUMBER, Scope.FORM, "0.1");
        getFormVariables().addVariable(1, VAR_2, AnswerFormat.NUMBER, Scope.CATEGORY, "0.2");
        getFormVariables().addVariable(2, VAR_3, AnswerFormat.NUMBER, Scope.CATEGORY, "0.3");
        getFormDesigner().save();
    }

    protected void createGlobalVariables() {
        login(ABCD_GLOBAL_CONST_BIIT1);

        getFormManager().goToGlobalVariables();
        getGlobalVariables().createGlobalVariable(VAR_1, AnswerFormat.TEXT, TEXT_VAR_VALUE, VALID_FROM, VALID_TO);
        getGlobalVariables().createGlobalVariable(VAR_2, AnswerFormat.DATE, DATE_VAR_VALUE, VALID_FROM, VALID_TO);

        getGlobalVariables().save();

        getGlobalVariables().logOut();
    }

    protected void removeGlobalVariables() {
        login(ABCD_GLOBAL_CONST_BIIT1);

        getFormManager().goToGlobalVariables();
        do {
            try {
                getGlobalVariables().removeVariable();
                getGlobalVariables().clickRow(0);
            } catch (Exception e) {
                break;
            }
        } while (true);

        getGlobalVariables().save();

        getGlobalVariables().logOut();
    }

    protected void removeForms() {
        do {
            try {
                getFormManager().deleteForm(0);
            } catch (Exception e) {
                break;
            }
        } while (true);
    }

    protected void sleep(long milis) {
        try {
            Thread.sleep(milis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

package com.biit.abcd.gui.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.gui.test.webpage.FormDesigner.AnswerFormat;
import com.vaadin.testbench.elements.NotificationElement;

@Test(groups = "ruleExpression")
public class RuleExpressionTests extends AbcdTester {

	private static final String FORM_1 = "form_1";
	private static final String RULE_1 = "rule_1";
	
	private static final int TREE_ELEMENT_TAB = 0;
	private static final int MATHEMATICAL_OPERATIONS_TAB = 1;
	private static final int GENERIC_ELEMENT_TAB = 2;
	private static final int GLOBAL_CONSTANT_TAB = 3;
	private static final String TEST_STRING_VALUE = "0.5";
	private static final String TEST_STRING_VALUE_2 = "0.6";
	private static final String MIN = "Min";
	private static final String COMMA_CAPTION = ",";
	private static final String RIGHTPAR_CAPTION = ")";
	private static final String ASSIGN_CAPTION = "=";
	private static final String TEST_STRING_TOKEN_0 = "Categories.var2";
	private static final String TEST_STRING_TOKEN_1 = "=";
	private static final String TEST_STRING_TOKEN_2 = "MIN(";
	private static final String TEST_STRING_TOKEN_3 = "question_1";
	private static final String TEST_STRING_TOKEN_4 = ",";
	private static final String TEST_STRING_TOKEN_5 = "question_2";
	private static final String TEST_STRING_TOKEN_6 = ")";

	@Test
	public void createEmptyRuleExpression() {
		login(ABCD_FORM_ADMIN_BIIT1);
		getFormManager().createNewForm(FORM_1);
		getFormManager().clickRuleExpressionEditor();

		getRuleExpression().newRuleExpression(RULE_1);

		getRuleExpression().save();

		getRuleExpression().goToFormManager();
		getFormManager().deleteForm();
		getFormManager().logOut();
	}

	@Test
	public void tryToCreateTwoRulesWithSameName() {
		login(ABCD_FORM_ADMIN_BIIT1);
		getFormManager().createNewForm(FORM_1);
		getFormManager().clickRuleExpressionEditor();

		getRuleExpression().newRuleExpression(RULE_1);
		getRuleExpression().newRuleExpression(RULE_1);

		NotificationElement notification = $(NotificationElement.class).first();
		checkNotificationIsError(notification);
		notification.close();
		getRuleExpression().getNewRuleExpressionWindow().clickCancel();

		getRuleExpression().save();

		getRuleExpression().goToFormManager();
		getFormManager().deleteForm();
		getFormManager().logOut();
	}

	@Test
	public void addTreeElementToExpressionSaveAndChangeToOtherElement() {
		login(ABCD_FORM_ADMIN_BIIT1);
		getFormManager().createNewForm(FORM_1);
		createSampleForm();

		getFormDesigner().clickRuleExpressionEditor();

		getRuleExpression().newRuleExpression(RULE_1);
		getRuleExpression().toggleSelectElementRow(1);
		getRuleExpression().addElement(2);

		getRuleExpression().save();

		getRuleExpression().doubleClickOnToken(0);
		getRuleExpression().getSelectAnElement().selectAndAcceptElement(3);

		getRuleExpression().save();

		// Cleanup
		getRuleExpression().goToFormManager();
		getFormManager().deleteForm();
		getFormManager().logOut();
	}

	@Test
	public void addTreeElementVariable() {
		login(ABCD_FORM_ADMIN_BIIT1);
		getFormManager().createNewForm(FORM_1);

		createFormVariables();

		getFormVariables().clickRuleExpressionEditor();

		getRuleExpression().toggleSelectElementRow(0);
		getRuleExpression().newRuleExpression(RULE_1);
		getRuleExpression().addVariable(VAR_1);

		getRuleExpression().save();

		// Cleanup
		getRuleExpression().goToFormManager();
		getFormManager().deleteForm();
		getFormManager().logOut();
	}

	@Test
	public void addTreeElementVariableAndChangeIt() {
		login(ABCD_FORM_ADMIN_BIIT1);
		getFormManager().createNewForm(FORM_1);

		createFormVariables();

		getFormVariables().clickRuleExpressionEditor();

		getRuleExpression().toggleSelectElementRow(0);
		getRuleExpression().newRuleExpression(RULE_1);
		getRuleExpression().addVariable(VAR_1);

		getRuleExpression().save();

		getRuleExpression().doubleClickOnToken(0);
		getRuleExpression().getSelectAVariable().selectVariableAndAcceptElement(VAR_1);

		getRuleExpression().save();

		// Cleanup
		getRuleExpression().goToFormManager();
		getFormManager().deleteForm();
		getFormManager().logOut();
	}

	@Test
	public void addTreeElementVariableAndTryToUseElement() {
		login(ABCD_FORM_ADMIN_BIIT1);
		getFormManager().createNewForm(FORM_1);

		createSampleForm();
		createFormVariables();

		getFormVariables().clickRuleExpressionEditor();

		getRuleExpression().selectElement(0);
		getRuleExpression().newRuleExpression(RULE_1);
		getRuleExpression().addVariable(VAR_1);

		getRuleExpression().save();

		getRuleExpression().doubleClickOnToken(0);
		getRuleExpression().getSelectAVariable().toggleElement(1);
		getRuleExpression().getSelectAVariable().selectElement(2);
		getRuleExpression().getSelectAVariable().clickAccept();

		NotificationElement notification = $(NotificationElement.class).first();
		checkNotificationIsError(notification);
		notification.close();

		getRuleExpression().getSelectAVariable().clickCancel();

		getRuleExpression().save();

		// Cleanup
		getRuleExpression().goToFormManager();
		getFormManager().deleteForm();
		getFormManager().logOut();
	}

	@Test
	public void addGenericElement() {
		login(ABCD_FORM_ADMIN_BIIT1);
		getFormManager().createNewForm(FORM_1);

		getFormVariables().clickRuleExpressionEditor();
		getRuleExpression().newRuleExpression(RULE_1);

		getRuleExpression().changeToTab(2);
		getRuleExpression().addGenericElement(0);

		getRuleExpression().save();

		// Cleanup
		getRuleExpression().goToFormManager();
		getFormManager().deleteForm();
		getFormManager().logOut();
	}

	// We can't modify a token of generic element.

	@Test
	public void addGenericVariableElement() {
		login(ABCD_FORM_ADMIN_BIIT1);
		getFormManager().createNewForm(FORM_1);

		createSampleForm();
		createFormVariables();

		getFormVariables().clickRuleExpressionEditor();
		getRuleExpression().newRuleExpression(RULE_1);

		getRuleExpression().changeToTab(2);
		getRuleExpression().addGenericElementVariable(0, VAR_2);

		getRuleExpression().save();

		getRuleExpression().doubleClickOnToken(0);
		getRuleExpression().getSelectGenericVariable().selectAndAccept(0, VAR_3);

		getRuleExpression().save();

		// Cleanup
		getRuleExpression().goToFormManager();
		getFormManager().deleteForm();
		getFormManager().logOut();
	}

	@Test
	public void addInputValueElement() {
		login(ABCD_FORM_ADMIN_BIIT1);
		getFormManager().createNewForm(FORM_1);

		createSampleForm();
		createFormVariables();

		getFormVariables().clickRuleExpressionEditor();
		getRuleExpression().newRuleExpression(RULE_1);

		getRuleExpression().changeToTab(MATHEMATICAL_OPERATIONS_TAB);
		getRuleExpression().addInputValue(AnswerFormat.NUMBER, TEST_STRING_VALUE);

		Assert.assertEquals(getRuleExpression().getToken(0).getText(), TEST_STRING_VALUE);

		getRuleExpression().save();

		// Cleanup
		getRuleExpression().goToFormManager();
		getFormManager().deleteForm();
		getFormManager().logOut();
	}

	@Test
	public void editInputValueElement() {
		login(ABCD_FORM_ADMIN_BIIT1);
		getFormManager().createNewForm(FORM_1);

		createSampleForm();
		createFormVariables();

		getFormVariables().clickRuleExpressionEditor();
		getRuleExpression().newRuleExpression(RULE_1);

		getRuleExpression().changeToTab(MATHEMATICAL_OPERATIONS_TAB);
		getRuleExpression().addInputValue(AnswerFormat.NUMBER, TEST_STRING_VALUE);

		Assert.assertEquals(getRuleExpression().getToken(0).getText(), TEST_STRING_VALUE);

		getRuleExpression().editInputValue(0, AnswerFormat.NUMBER, TEST_STRING_VALUE_2);
		Assert.assertEquals(getRuleExpression().getToken(0).getText(), TEST_STRING_VALUE_2);

		getRuleExpression().save();

		// Cleanup
		getRuleExpression().goToFormManager();
		getFormManager().deleteForm();
		getFormManager().logOut();
	}

	@Test
	public void addMinExpression() {
		login(ABCD_FORM_ADMIN_BIIT1);
		getFormManager().createNewForm(FORM_1);

		createSampleForm();
		createFormVariables();

		getFormVariables().clickRuleExpressionEditor();
		getRuleExpression().newRuleExpression(RULE_1);

		getRuleExpression().changeToTab(GENERIC_ELEMENT_TAB);
		getRuleExpression().addGenericElementVariable(0, VAR_2);

		getRuleExpression().changeToTab(MATHEMATICAL_OPERATIONS_TAB);
		getRuleExpression().clickButton(ASSIGN_CAPTION);
		getRuleExpression().clickButton(MIN);

		getRuleExpression().changeToTab(TREE_ELEMENT_TAB);
		getRuleExpression().toggleSelectElementRow(1);
		getRuleExpression().addElement(2);

		getRuleExpression().changeToTab(MATHEMATICAL_OPERATIONS_TAB);
		getRuleExpression().clickButton(COMMA_CAPTION);
		getRuleExpression().save();
		
		getRuleExpression().goToFormManager();
		getFormManager().clickRuleExpressionEditor();
		getRuleExpression().toggleSelectElementRow(1);
		getRuleExpression().addElement(3);

		getRuleExpression().changeToTab(MATHEMATICAL_OPERATIONS_TAB);
		getRuleExpression().clickButton(RIGHTPAR_CAPTION);

		Assert.assertTrue(getRuleExpression().isValid());

		getRuleExpression().save();

		getRuleExpression().goToFormManager();
		getFormManager().clickRuleExpressionEditor();

		Assert.assertTrue(getRuleExpression().isValid());
		Assert.assertEquals(getRuleExpression().getToken(0).getText(), TEST_STRING_TOKEN_0);
		Assert.assertEquals(getRuleExpression().getToken(1).getText(), TEST_STRING_TOKEN_1);
		Assert.assertEquals(getRuleExpression().getToken(2).getText(), TEST_STRING_TOKEN_2);
		Assert.assertEquals(getRuleExpression().getToken(3).getText(), TEST_STRING_TOKEN_3);
		Assert.assertEquals(getRuleExpression().getToken(4).getText(), TEST_STRING_TOKEN_4);
		Assert.assertEquals(getRuleExpression().getToken(5).getText(), TEST_STRING_TOKEN_5);
		Assert.assertEquals(getRuleExpression().getToken(6).getText(), TEST_STRING_TOKEN_6);

		// Cleanup
		getRuleExpression().goToFormManager();
		getFormManager().deleteForm();
		getFormManager().logOut();
	}

	@Test
	public void addGlobalVariableElement() {
		createGlobalVariables();
		
		login(ABCD_FORM_ADMIN_BIIT1);
		removeForms();
		getFormManager().createNewForm(FORM_1);

		createSampleForm();
		createFormVariables();

		getFormVariables().clickRuleExpressionEditor();
		getRuleExpression().newRuleExpression(RULE_1);

		getRuleExpression().changeToTab(GLOBAL_CONSTANT_TAB);
		getRuleExpression().clickButton(VAR_1);

		getRuleExpression().save();

		// Cleanup
		getRuleExpression().goToFormManager();
		getFormManager().deleteForm();
		getFormManager().logOut();
		
		removeGlobalVariables();
	}
	
	@Test
	public void editGlobalVariableElement() {
		createGlobalVariables();
		
		login(ABCD_FORM_ADMIN_BIIT1);
		removeForms();
		getFormManager().createNewForm(FORM_1);

		createSampleForm();
		createFormVariables();

		getFormVariables().clickRuleExpressionEditor();
		getRuleExpression().newRuleExpression(RULE_1);

		getRuleExpression().changeToTab(GLOBAL_CONSTANT_TAB);
		getRuleExpression().clickButton(VAR_1);
		
		getRuleExpression().doubleClickOnToken(0);
		getRuleExpression().getSelectGlobalConstantWindow().selectAndAcceptGlobalConstant(VAR_2);
		Assert.assertEquals(getRuleExpression().getToken(0).getText(), VAR_2);

		getRuleExpression().save();

		// Cleanup
		getRuleExpression().goToFormManager();
		getFormManager().deleteForm();
		getFormManager().logOut();
		
		removeGlobalVariables();
	}
	
}

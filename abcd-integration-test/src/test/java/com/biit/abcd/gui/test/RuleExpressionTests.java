package com.biit.abcd.gui.test;

import org.testng.annotations.Test;

import com.biit.abcd.gui.test.StatusPreservingTests.Scope;
import com.biit.abcd.gui.test.webpage.FormDesigner.AnswerFormat;
import com.biit.abcd.gui.test.webpage.FormDesigner.AnswerType;
import com.vaadin.testbench.elements.NotificationElement;

@Test(groups = "ruleExpression")
public class RuleExpressionTests extends AbcdTester{

	private static final String FORM_1 = "form_1";
	private static final String RULE_1 = "rule_1";
	private static final String CATEGORY_1 = "category_1";
	private static final String QUESTION_1 = "question_1";
	private static final String QUESTION_2 = "question_2";
	private static final String VAR_1 = "var1";
	private static final String VAR_2 = "var2";
	private static final String VAR_3 = "var3";
	
	private void createSampleForm(){
		getFormManager().clickFormDesigner();
		
		getFormDesigner().clickInTableRow(0);
		getFormDesigner().createCategory(0, CATEGORY_1);
		getFormDesigner().clickInTableRow(0);
		getFormDesigner().createQuestion(1, QUESTION_1, AnswerType.INPUT_FIELD, AnswerFormat.NUMBER);
		getFormDesigner().createQuestion(1, QUESTION_2, AnswerType.INPUT_FIELD, AnswerFormat.NUMBER);
		getFormDesigner().save();
	}
	
	private void createFormVariables(){
		getFormManager().clickFormVariables();
		
		getFormVariables().addVariable(0, VAR_1, AnswerFormat.NUMBER, Scope.FORM, "0.1");
		getFormVariables().addVariable(1, VAR_2, AnswerFormat.NUMBER, Scope.CATEGORY, "0.2");
		getFormVariables().addVariable(2, VAR_3, AnswerFormat.NUMBER, Scope.CATEGORY, "0.3");
		getFormDesigner().save();
	}

	@Test
	public void createEmptyRuleExpression(){
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
	public void tryToCreateTwoRulesWithSameName(){
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
	public void addTreeElementToExpressionSaveAndChangeToOtherElement(){
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
		
		//Cleanup
		getRuleExpression().goToFormManager();
		getFormManager().deleteForm();
		getFormManager().logOut();
	}
	
	@Test
	public void addTreeElementVariable(){
		login(ABCD_FORM_ADMIN_BIIT1);
		getFormManager().createNewForm(FORM_1);

		createFormVariables();
		
		getFormVariables().clickRuleExpressionEditor();
		
		getRuleExpression().toggleSelectElementRow(0);
		getRuleExpression().newRuleExpression(RULE_1);
		getRuleExpression().addVariable(VAR_1);
		
		getRuleExpression().save();
		
		//Cleanup
		getRuleExpression().goToFormManager();
		getFormManager().deleteForm();
		getFormManager().logOut();
	}
	
	@Test
	public void addTreeElementVariableAndChangeIt(){
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
		
		//Cleanup
		getRuleExpression().goToFormManager();
		getFormManager().deleteForm();
		getFormManager().logOut();
	}
	
	@Test
	public void addTreeElementVariableAndTryToUseElement(){
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
		
		//Cleanup
		getRuleExpression().goToFormManager();
		getFormManager().deleteForm();
		getFormManager().logOut();
	}
	
	@Test
	public void addGenericElement(){
		login(ABCD_FORM_ADMIN_BIIT1);
		getFormManager().createNewForm(FORM_1);

		getFormVariables().clickRuleExpressionEditor();
		getRuleExpression().newRuleExpression(RULE_1);
		
		getRuleExpression().changeToTab(2);
		getRuleExpression().addGenericElement(0);
		
		getRuleExpression().save();
		
		//Cleanup
		getRuleExpression().goToFormManager();
		getFormManager().deleteForm();
		getFormManager().logOut();
	}
	
	//We can't modify a token of generic element.
	
	@Test
	public void addGenericVariableElement(){
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
		
		//Cleanup
		getRuleExpression().goToFormManager();
		getFormManager().deleteForm();
		getFormManager().logOut();
	}
	
}

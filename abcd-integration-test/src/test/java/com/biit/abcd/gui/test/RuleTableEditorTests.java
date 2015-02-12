package com.biit.abcd.gui.test;

import org.testng.annotations.Test;

import com.vaadin.testbench.elements.NotificationElement;

@Test(groups = "ruleTableEditor")
public class RuleTableEditorTests extends AbcdTester {

	private static final String FORM_1 = "form_1";
	private static final String TABLE_1 = "table_1";

//	@Test
//	public void testAddAndRemoveTable(){
//		login(ABCD_FORM_ADMIN_BIIT1);
//		getFormManager().createNewForm(FORM_1);
//		getFormManager().clickRuleTableEditor();
//
//		getRuleTableEditor().newRuleTable(TABLE_1);
//
//		getRuleTableEditor().save();
//		
//		getRuleTableEditor().removeTable(0);
//		
//		getRuleTableEditor().save();
//
//		getRuleTableEditor().goToFormManager();
//		getFormManager().deleteForm();
//		getFormManager().logOut();
//	}
//	
//	@Test
//	public void createTwoTablesWithSameName(){
//		login(ABCD_FORM_ADMIN_BIIT1);
//		getFormManager().createNewForm(FORM_1);
//		getFormManager().clickRuleTableEditor();
//
//		getRuleTableEditor().newRuleTable(TABLE_1);
//
//		getRuleTableEditor().newRuleTable(TABLE_1);
//		NotificationElement notification = $(NotificationElement.class).first();
//		checkNotificationIsError(notification);
//		notification.close();
//		
//		getRuleTableEditor().getNewRuleTableWindow().clickCancel();
//		getRuleTableEditor().save();		
//
//		getRuleTableEditor().goToFormManager();
//		getFormManager().deleteForm();
//		getFormManager().logOut();
//	}
//	
//	@Test
//	public void createATableAddRowWithQuestionAnswerAndAction(){
//		login(ABCD_FORM_ADMIN_BIIT1);
//		getFormManager().createNewForm(FORM_1);
//		
////		createSampleForm();
////		createFormVariables();
//		
//		getFormManager().clickRuleTableEditor();
//
//		getRuleTableEditor().newRuleTable(TABLE_1);
//
//		getRuleTableEditor().addRow();
//		getRuleTableEditor().addCol();
//		
//		getRuleTableEditor().getQuestionAnswerTable().getCell(0,0).click();
//		
//		//getRuleTableEditor().save();
//		
////		getRuleTableEditor().doubleClickCondition(0,0);	
//
////		getRuleTableEditor().goToFormManager();
////		getFormManager().deleteForm();
////		getFormManager().logOut();
//	}
}

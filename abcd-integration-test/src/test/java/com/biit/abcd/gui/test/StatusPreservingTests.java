package com.biit.abcd.gui.test;

import org.junit.Assert;
import org.testng.annotations.Test;

@Test(groups = "statusPreserving")
public class StatusPreservingTests extends AbcdTester{

	private static final String TEST_FORM_1 = "test_1";
	private static final String TEST_FORM_2 = "test_2";
	private static final int FORM_2_ROW = 3;
	private static final int FORM_1_ROW = 1;
	private static final String DIAGRAM_1 = "diag_1";
	private static final String DIAGRAM_2 = "diag_2";
	private static final int DIAGRAM_1_ROW = 0;
	private static final int DIAGRAM_2_ROW = 1;
	private static final String RULE_1 = "rule_1";
	private static final String RULE_2 = "rule_2";
	private static final int RULE_1_ROW = 0;
	private static final int RULE_2_ROW = 1;
	private static final String TABLE_1 = "table_1";
	private static final String TABLE_2 = "table_2";
	private static final int TABLE_1_ROW = 0;
	private static final int TABLE_2_ROW = 1;

	@Test
	public void formInUseIsSelectedWhenReturning(){
		login(ABCD_FORM_EDIT_BIIT1);
		getFormManager().createNewForm(TEST_FORM_1);
		getFormManager().createNewForm(TEST_FORM_2);
		
		getFormManager().clickFormDesigner();
		clickFormManager();
		Assert.assertTrue(getFormManager().isRowSelected(FORM_2_ROW));
		getFormManager().selectForm(FORM_1_ROW);
		getFormManager().clickFormDesigner();
		clickFormManager();
		Assert.assertTrue(getFormManager().isRowSelected(FORM_1_ROW));
		
		getFormManager().selectForm(FORM_2_ROW);
		getFormManager().clickFormVariables();
		clickFormManager();
		Assert.assertTrue(getFormManager().isRowSelected(FORM_2_ROW));
		
		getFormManager().clickDiagramDesigner();
		clickFormManager();
		Assert.assertTrue(getFormManager().isRowSelected(FORM_2_ROW));
		
		getFormManager().clickRuleExpressionEditor();
		clickFormManager();
		Assert.assertTrue(getFormManager().isRowSelected(FORM_2_ROW));
		
		getFormManager().clickRuleEditor();
		clickFormManager();
		Assert.assertTrue(getFormManager().isRowSelected(FORM_2_ROW));
		
		getFormManager().clickRuleTableEditor();
		clickFormManager();
		Assert.assertTrue(getFormManager().isRowSelected(FORM_2_ROW));
		
		getFormManager().logOut();
	
		deleteForm(FORM_2_ROW, ABCD_FORM_ADMIN_BIIT1);
		getFormManager().logOut();
		deleteForm(FORM_1_ROW, ABCD_FORM_ADMIN_BIIT1);
		getFormManager().logOut();
	}
	

	@Test
	public void elementsSelectedAreMaintainedCorrectly(){
		login(ABCD_FORM_EDIT_BIIT1);
		getFormManager().createNewForm(TEST_FORM_1);
		getFormManager().clickDiagramDesigner();
		
		//Add two diagrams
		getDiagramDesigner().newDiagram(DIAGRAM_1);
		getDiagramDesigner().newDiagram(DIAGRAM_2);
		getDiagramDesigner().selectRow(DIAGRAM_1_ROW);
		
		getDiagramDesigner().clickFormDesigner();
		getFormDesigner().clickDiagramDesigner();
		getDiagramDesigner().isRowSelected(DIAGRAM_1_ROW);
		getDiagramDesigner().selectRow(DIAGRAM_2_ROW);
		getDiagramDesigner().clickFormDesigner();
		getFormDesigner().clickDiagramDesigner();
		getDiagramDesigner().isRowSelected(DIAGRAM_2_ROW);

		//Rule expression
		getFormDesigner().clickRuleExpressionEditor();
		getRuleExpression().newRuleExpression(RULE_1);
		getRuleExpression().newRuleExpression(RULE_2);
		getRuleExpression().selectRow(RULE_1_ROW);
		
		getRuleExpression().clickFormDesigner();
		getFormDesigner().clickRuleExpressionEditor();
		getRuleExpression().isRowSelected(RULE_1_ROW);
		getRuleExpression().selectRow(RULE_2_ROW);
		getRuleExpression().clickFormDesigner();
		getFormDesigner().clickRuleExpressionEditor();
		getRuleExpression().isRowSelected(RULE_2_ROW);
				
		//Rule editor
		getFormDesigner().clickRuleEditor();
		getRuleEditor().newRule(RULE_1);
		getRuleEditor().newRule(RULE_2);
		getRuleEditor().selectRow(RULE_1_ROW);
		
		getRuleEditor().clickFormDesigner();
		getFormDesigner().clickRuleEditor();
		getRuleEditor().isRowSelected(RULE_1_ROW);
		getRuleEditor().selectRow(RULE_2_ROW);
		getRuleEditor().clickFormDesigner();
		getFormDesigner().clickRuleEditor();
		getRuleEditor().isRowSelected(RULE_2_ROW);
		
		//Rule Table editor
		getFormDesigner().clickRuleTableEditor();
		getRuleTableEditor().newRuleTable(TABLE_1);
		getRuleTableEditor().newRuleTable(TABLE_2);
		getRuleTableEditor().selectRow(TABLE_1_ROW);
		
		getRuleTableEditor().clickFormDesigner();
		getFormDesigner().clickRuleTableEditor();
		getRuleTableEditor().isRowSelected(TABLE_1_ROW);
		getRuleTableEditor().selectRow(TABLE_2_ROW);
		getRuleTableEditor().clickFormDesigner();
		getFormDesigner().clickRuleTableEditor();
		getRuleTableEditor().isRowSelected(TABLE_2_ROW);
		
	}
}

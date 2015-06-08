package com.biit.abcd.gui.test;

import java.util.Date;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.biit.abcd.gui.test.webpage.FormDesigner.AnswerFormat;
import com.biit.abcd.gui.test.webpage.FormDesigner.AnswerType;
import com.vaadin.testbench.By;

public class FormDesignerTests extends AbcdTester {

	private static final String NEW_FORM_NAME = "new_form";
	private static final String CATEGORY_NAME = "category";
	private static final String QUESTION_NAME = "question";
	private static final String GROUP_NAME = "group";
	private static final int HA_ROW = 18;
	private static final int H_ROW = 17;
	private static final int G_ROW = 16;
	private static final int QUESTION_10_ROW = 11;
	private static final int QUESTION_5_ROW = 6;
	private static final int GROUP_ROW = 2;
	private static final int CATEGORY_ROW = 1;
	private static final String BAD_TECHNICAL_NAME = ".asd";
	private static final CharSequence NOT_VALID_NAME = "Text '.asd' is not valid.";
	private static final CharSequence DUPLICATED_NAME = "An element with the same name already exist on the same level of the form.";
	private static final String ANSWER_NAME_1 = "answer1";
	private static final String ANSWER_NAME_2 = "answer2";
	private static final String SUBANSWER_NAME = "subanswer";
	private static final String QUESTION_NAME_1 = "question1";
	private static final String QUESTION_NAME_2 = "question2";
	private static final String QUESTION_NAME_3 = "question3";
	private static final CharSequence MOD = "_mod";

	@Test(groups = "formDesigner")
	public void generateCompleteFormWithDesigner() {
		String formName = NEW_FORM_NAME + "_" + (new Date()).getTime();

		createForm(formName, ABCD_FORM_ADMIN_BIIT1);
		
		generateTestForm();
	
		getFormDesigner().goToFormManager();
		getFormManager().deleteForm();
	}
	
	@Test(groups = "formDesigner")
	public void tryToCreateGroupQuestionAnswerSubanswerAtCategoryLevel() {
		String formName = NEW_FORM_NAME + "_" + (new Date()).getTime();
		createForm(formName, ABCD_FORM_ADMIN_BIIT1);
		
		getFormManager().clickFormDesigner();
		Assert.assertFalse(getFormDesigner().getGroupButton().isEnabled());
		Assert.assertFalse(getFormDesigner().getQuestionButton().isEnabled());
		Assert.assertFalse(getFormDesigner().getAnswerButton().isEnabled());
		Assert.assertFalse(getFormDesigner().getSubanswerButton().isEnabled());
	
		getFormDesigner().save();
		getFormDesigner().goToFormManager();
		getFormManager().deleteForm();
	}
	
	@Test(groups = "formDesigner")
	public void tryToCreateAnswerSubanswerAtCategoryLevel() {
		String formName = NEW_FORM_NAME + "_" + (new Date()).getTime();
		createForm(formName, ABCD_FORM_ADMIN_BIIT1);
		
		getFormManager().clickFormDesigner();
		getFormDesigner().clickInTableRow(0);
		getFormDesigner().createCategory(0, CATEGORY_NAME);
		Assert.assertFalse(getFormDesigner().getAnswerButton().isEnabled());
		Assert.assertFalse(getFormDesigner().getSubanswerButton().isEnabled());
	
		getFormDesigner().save();
		getFormDesigner().goToFormManager();
		getFormManager().deleteForm();
	}
	
	@Test(groups = "formDesigner")
	public void tryToCreateAnswerSubanswerAtGroupLevel() {
		String formName = NEW_FORM_NAME + "_" + (new Date()).getTime();
		createForm(formName, ABCD_FORM_ADMIN_BIIT1);
		
		getFormManager().clickFormDesigner();
		getFormDesigner().clickInTableRow(0);
		getFormDesigner().createCategory(0, CATEGORY_NAME);
		getFormDesigner().clickInTableRow(1);
		getFormDesigner().createGroup(1, CATEGORY_NAME);
		Assert.assertFalse(getFormDesigner().getAnswerButton().isEnabled());
		Assert.assertFalse(getFormDesigner().getSubanswerButton().isEnabled());
	
		getFormDesigner().save();
		getFormDesigner().goToFormManager();
		getFormManager().deleteForm();
	}
	
	@Test(groups = "formDesigner")
	public void tryToCreateAnswerSubanswerAtInputText() {
		String formName = NEW_FORM_NAME + "_" + (new Date()).getTime();
		createForm(formName, ABCD_FORM_ADMIN_BIIT1);
		
		getFormManager().clickFormDesigner();
		getFormDesigner().clickInTableRow(0);
		getFormDesigner().createCategory(0, CATEGORY_NAME);
		getFormDesigner().clickInTableRow(1);
		getFormDesigner().createQuestion(1, QUESTION_NAME, AnswerType.INPUT_FIELD, AnswerFormat.TEXT);

		Assert.assertFalse(getFormDesigner().getAnswerButton().isEnabled());
		Assert.assertFalse(getFormDesigner().getSubanswerButton().isEnabled());
	
		getFormDesigner().save();
		getFormDesigner().goToFormManager();
		getFormManager().deleteForm();
	}
	
	@Test(groups = "formDesigner")
	public void finishDesign() {
		String formName = NEW_FORM_NAME + "_" + (new Date()).getTime();
		createForm(formName, ABCD_FORM_ADMIN_BIIT1);
		
		getFormManager().clickFormDesigner();
		getFormDesigner().clickInTableRow(0);
		getFormDesigner().createCategory(0, CATEGORY_NAME);
		getFormDesigner().clickInTableRow(1);
		getFormDesigner().createBlockOfAllKindOfQuestions(1, QUESTION_NAME);

		getFormDesigner().finishDesign();

		getFormManager().deleteForm();
	}
	
	@Test(groups = "formDesigner")
	public void deleteElements() {
		String formName = NEW_FORM_NAME + "_" + (new Date()).getTime();
		createForm(formName, ABCD_FORM_ADMIN_BIIT1);
		
		getFormManager().clickFormDesigner();
		getFormDesigner().clickInTableRow(0);
		getFormDesigner().createCategory(0, CATEGORY_NAME);
		getFormDesigner().clickInTableRow(1);
		getFormDesigner().createGroup(1, GROUP_NAME);
		getFormDesigner().clickInTableRow(2);
		getFormDesigner().createBlockOfAllKindOfQuestions(2, QUESTION_NAME);
		getFormDesigner().save();
		
		//Remove subanswer
		getFormDesigner().remove(HA_ROW);
		//Remove answer with subanswer
		getFormDesigner().remove(H_ROW);
		//Remove answer
		getFormDesigner().remove(G_ROW);
		//Remove Question with answers
		getFormDesigner().remove(QUESTION_10_ROW);
		//Remove Question
		getFormDesigner().remove(QUESTION_5_ROW);
		//Remove Group
		getFormDesigner().remove(GROUP_ROW);
		//Remove Category
		getFormDesigner().remove(CATEGORY_ROW);
		
		getFormDesigner().save();
		getFormDesigner().goToFormManager();
		getFormManager().deleteForm();
	}
	
	
	@Test(groups = "formDesigner")
	public void notValidName() {
		String formName = NEW_FORM_NAME + "_" + (new Date()).getTime();
		createForm(formName, ABCD_FORM_ADMIN_BIIT1);
		
		getFormManager().clickFormDesigner();
		getFormDesigner().clickInTableRow(0);
		getFormDesigner().createCategory(0, CATEGORY_NAME);
		getFormDesigner().setTechnicalName(BAD_TECHNICAL_NAME);
		getFormDesigner().getTechnicalName().showTooltip();
		getFormDesigner().getTechnicalName().waitForVaadin();
		WebElement tooltip = findElement(By.className("v-tooltip"));
		Assert.assertTrue(tooltip.getText().contains(NOT_VALID_NAME));
		
		getFormDesigner().save();
		getFormDesigner().goToFormManager();
		getFormManager().deleteForm();
	}
	
	@Test(groups = "formDesigner")
	public void twoQuestionWithSameName() {
		String formName = NEW_FORM_NAME + "_" + (new Date()).getTime();
		createForm(formName, ABCD_FORM_ADMIN_BIIT1);
		
		getFormManager().clickFormDesigner();
		getFormDesigner().clickInTableRow(0);
		getFormDesigner().createCategory(0, CATEGORY_NAME);
		getFormDesigner().clickInTableRow(1);
		getFormDesigner().createQuestion(1, QUESTION_NAME, AnswerType.RADIO_BUTTON, null);
		getFormDesigner().createQuestion(1, QUESTION_NAME, AnswerType.RADIO_BUTTON, null);
		
		getFormDesigner().getTechnicalName().showTooltip();
		getFormDesigner().getTechnicalName().waitForVaadin();
		WebElement tooltip = findElement(By.className("v-tooltip"));
		Assert.assertTrue(tooltip.getText().contains(DUPLICATED_NAME));
		
		getFormDesigner().save();
		getFormDesigner().goToFormManager();
		getFormManager().deleteForm();
	}
	
	@Test(groups = "formDesigner")
	public void twoSubanswerWithSameNameInSameAnswer() {
		String formName = NEW_FORM_NAME + "_" + (new Date()).getTime();
		createForm(formName, ABCD_FORM_ADMIN_BIIT1);
		
		getFormManager().clickFormDesigner();
		getFormDesigner().clickInTableRow(0);
		getFormDesigner().createCategory(0, CATEGORY_NAME);
		getFormDesigner().clickInTableRow(1);
		getFormDesigner().createRadioButton(1, 2, QUESTION_NAME, ANSWER_NAME_1);
		getFormDesigner().clickInTableRow(0);
		getFormDesigner().createSubanswer(3, SUBANSWER_NAME);
		getFormDesigner().createSubanswer(3, SUBANSWER_NAME);
		
		getFormDesigner().getTechnicalName().showTooltip();
		getFormDesigner().getTechnicalName().waitForVaadin();
		WebElement tooltip = findElement(By.className("v-tooltip"));
		Assert.assertTrue(tooltip.getText().contains(DUPLICATED_NAME));
		
		getFormDesigner().save();
		getFormDesigner().goToFormManager();
		getFormManager().deleteForm();
	}
	
	@Test(groups = "formDesigner")
	public void twoSubanswerWithSameNameInDiferentAnswer() {
		String formName = NEW_FORM_NAME + "_" + (new Date()).getTime();
		createForm(formName, ABCD_FORM_ADMIN_BIIT1);
		
		getFormManager().clickFormDesigner();
		getFormDesigner().clickInTableRow(0);
		getFormDesigner().createCategory(0, CATEGORY_NAME);
		getFormDesigner().clickInTableRow(1);
		getFormDesigner().createRadioButton(1, 2, QUESTION_NAME, ANSWER_NAME_1, ANSWER_NAME_2);
		getFormDesigner().clickInTableRow(0);
		getFormDesigner().createSubanswer(3, SUBANSWER_NAME);
		getFormDesigner().createSubanswer(5, SUBANSWER_NAME);
		
		getFormDesigner().getTechnicalName().showTooltip();
		getFormDesigner().getTechnicalName().waitForVaadin();
		WebElement tooltip = findElement(By.className("v-tooltip"));
		Assert.assertTrue(tooltip.getText().contains(DUPLICATED_NAME));
		
		getFormDesigner().save();
		getFormDesigner().goToFormManager();
		getFormManager().deleteForm();
	}

	@Test(groups = "formDesigner")
	public void tryToMoveAQuestionWhenFirstElementOfGroup() {
		String formName = NEW_FORM_NAME + "_" + (new Date()).getTime();
		createForm(formName, ABCD_FORM_ADMIN_BIIT1);
		
		getFormManager().clickFormDesigner();
		getFormDesigner().clickInTableRow(0);
		getFormDesigner().createCategory(0, CATEGORY_NAME);
		getFormDesigner().clickInTableRow(1);
		getFormDesigner().createGroup(1, GROUP_NAME);
		getFormDesigner().clickInTableRow(2);
		
		getFormDesigner().createQuestion(2, QUESTION_NAME_1, AnswerType.INPUT_FIELD, AnswerFormat.TEXT);
		getFormDesigner().createQuestion(2, QUESTION_NAME_2, AnswerType.INPUT_FIELD, AnswerFormat.TEXT);
		getFormDesigner().createQuestion(2, QUESTION_NAME_3, AnswerType.INPUT_FIELD, AnswerFormat.TEXT);
		
		getFormDesigner().clickInTableRow(0);
		getFormDesigner().clickInTableRow(3);
		getFormDesigner().clickMoveUp();
		getFormDesigner().clickInTableRow(0);
		getFormDesigner().clickInTableRow(3);
		
		Assert.assertEquals(getFormDesigner().getTechnicalName().getValue(),QUESTION_NAME_1);
		
		getFormDesigner().clickMoveDown();
		
		getFormDesigner().clickInTableRow(0);
		getFormDesigner().clickInTableRow(3);
		Assert.assertNotEquals(getFormDesigner().getTechnicalName().getValue(),QUESTION_NAME_1);
		getFormDesigner().clickInTableRow(4);
		Assert.assertEquals(getFormDesigner().getTechnicalName().getValue(),QUESTION_NAME_1);
		
		getFormDesigner().save();
		getFormDesigner().goToFormManager();
		getFormManager().deleteForm();
	}
	
	@Test(groups = "formDesigner")
	public void tryToMoveAQuestionWhenLastElementOfGroup() {
		String formName = NEW_FORM_NAME + "_" + (new Date()).getTime();
		createForm(formName, ABCD_FORM_ADMIN_BIIT1);
		
		getFormManager().clickFormDesigner();
		getFormDesigner().clickInTableRow(0);
		getFormDesigner().createCategory(0, CATEGORY_NAME);
		getFormDesigner().clickInTableRow(1);
		getFormDesigner().createGroup(1, GROUP_NAME);
		getFormDesigner().clickInTableRow(2);
		
		getFormDesigner().createQuestion(2, QUESTION_NAME_1, AnswerType.INPUT_FIELD, AnswerFormat.TEXT);
		getFormDesigner().createQuestion(2, QUESTION_NAME_2, AnswerType.INPUT_FIELD, AnswerFormat.TEXT);
		getFormDesigner().createQuestion(2, QUESTION_NAME_3, AnswerType.INPUT_FIELD, AnswerFormat.TEXT);
				
		getFormDesigner().clickInTableRow(0);
		getFormDesigner().clickInTableRow(5);
		getFormDesigner().clickMoveDown();
		Assert.assertEquals(getFormDesigner().getTechnicalName().getValue(),QUESTION_NAME_3);
				
		getFormDesigner().clickInTableRow(0);
		getFormDesigner().clickInTableRow(5);
		getFormDesigner().clickMoveUp();
		getFormDesigner().clickInTableRow(0);
		getFormDesigner().clickInTableRow(5);
		Assert.assertNotEquals(getFormDesigner().getTechnicalName().getValue(),QUESTION_NAME_3);
		
		getFormDesigner().clickInTableRow(4);		
		Assert.assertEquals(getFormDesigner().getTechnicalName().getValue(),QUESTION_NAME_3);		
		
		getFormDesigner().save();
		getFormDesigner().goToFormManager();
		getFormManager().deleteForm();
	}
	
	@Test(groups = "formDesigner")
	public void tryToMoveAQuestionWhenMiddleElementOfGroup() {
		String formName = NEW_FORM_NAME + "_" + (new Date()).getTime();
		createForm(formName, ABCD_FORM_ADMIN_BIIT1);
		
		getFormManager().clickFormDesigner();
		getFormDesigner().clickInTableRow(0);
		getFormDesigner().createCategory(0, CATEGORY_NAME);
		getFormDesigner().clickInTableRow(1);
		getFormDesigner().createGroup(1, GROUP_NAME);
		getFormDesigner().clickInTableRow(2);
		
		getFormDesigner().createQuestion(2, QUESTION_NAME_1, AnswerType.INPUT_FIELD, AnswerFormat.TEXT);
		getFormDesigner().createQuestion(2, QUESTION_NAME_2, AnswerType.INPUT_FIELD, AnswerFormat.TEXT);
		getFormDesigner().createQuestion(2, QUESTION_NAME_3, AnswerType.INPUT_FIELD, AnswerFormat.TEXT);
				
		getFormDesigner().clickInTableRow(0);
		getFormDesigner().clickInTableRow(4);
		getFormDesigner().clickMoveDown();
		getFormDesigner().clickInTableRow(0);
		getFormDesigner().clickInTableRow(5);
		Assert.assertEquals(getFormDesigner().getTechnicalName().getValue(),QUESTION_NAME_2);
				
		getFormDesigner().clickInTableRow(0);
		getFormDesigner().clickInTableRow(4);
		getFormDesigner().clickMoveUp();
		getFormDesigner().clickInTableRow(0);
		getFormDesigner().clickInTableRow(3);
		Assert.assertEquals(getFormDesigner().getTechnicalName().getValue(),QUESTION_NAME_3);	
		
		getFormDesigner().save();
		getFormDesigner().goToFormManager();
		getFormManager().deleteForm();
	}
	
	@Test(groups = "formDesigner")
	public void tryToMoveAQuestionWhenFirstElementOfCategory() {
		String formName = NEW_FORM_NAME + "_" + (new Date()).getTime();
		createForm(formName, ABCD_FORM_ADMIN_BIIT1);
		
		getFormManager().clickFormDesigner();
		getFormDesigner().clickInTableRow(0);
		getFormDesigner().createCategory(0, CATEGORY_NAME);
		getFormDesigner().clickInTableRow(1);
		
		getFormDesigner().createQuestion(1, QUESTION_NAME_1, AnswerType.INPUT_FIELD, AnswerFormat.TEXT);
		getFormDesigner().createQuestion(1, QUESTION_NAME_2, AnswerType.INPUT_FIELD, AnswerFormat.TEXT);
		getFormDesigner().createQuestion(1, QUESTION_NAME_3, AnswerType.INPUT_FIELD, AnswerFormat.TEXT);
		
		getFormDesigner().clickInTableRow(0);
		getFormDesigner().clickInTableRow(2);
		getFormDesigner().clickMoveUp();
		getFormDesigner().clickInTableRow(0);
		getFormDesigner().clickInTableRow(2);
		
		Assert.assertEquals(getFormDesigner().getTechnicalName().getValue(),QUESTION_NAME_1);
		
		getFormDesigner().clickMoveDown();
		
		getFormDesigner().clickInTableRow(0);
		getFormDesigner().clickInTableRow(2);
		Assert.assertNotEquals(getFormDesigner().getTechnicalName().getValue(),QUESTION_NAME_1);
		getFormDesigner().clickInTableRow(3);
		Assert.assertEquals(getFormDesigner().getTechnicalName().getValue(),QUESTION_NAME_1);
		
		getFormDesigner().save();
		getFormDesigner().goToFormManager();
		getFormManager().deleteForm();
	}
	
	@Test(groups = "formDesigner")
	public void tryToMoveAQuestionWhenLastElementOfCategory() {
		String formName = NEW_FORM_NAME + "_" + (new Date()).getTime();
		createForm(formName, ABCD_FORM_ADMIN_BIIT1);
		
		getFormManager().clickFormDesigner();
		getFormDesigner().clickInTableRow(0);
		getFormDesigner().createCategory(0, CATEGORY_NAME);
		getFormDesigner().clickInTableRow(1);
		
		getFormDesigner().createQuestion(1, QUESTION_NAME_1, AnswerType.INPUT_FIELD, AnswerFormat.TEXT);
		getFormDesigner().createQuestion(1, QUESTION_NAME_2, AnswerType.INPUT_FIELD, AnswerFormat.TEXT);
		getFormDesigner().createQuestion(1, QUESTION_NAME_3, AnswerType.INPUT_FIELD, AnswerFormat.TEXT);
				
		getFormDesigner().clickInTableRow(0);
		getFormDesigner().clickInTableRow(4);
		getFormDesigner().clickMoveDown();
		Assert.assertEquals(getFormDesigner().getTechnicalName().getValue(),QUESTION_NAME_3);
				
		getFormDesigner().clickInTableRow(0);
		getFormDesigner().clickInTableRow(4);
		getFormDesigner().clickMoveUp();
		getFormDesigner().clickInTableRow(0);
		getFormDesigner().clickInTableRow(4);
		Assert.assertNotEquals(getFormDesigner().getTechnicalName().getValue(),QUESTION_NAME_3);
		
		getFormDesigner().clickInTableRow(3);		
		Assert.assertEquals(getFormDesigner().getTechnicalName().getValue(),QUESTION_NAME_3);		
		
		getFormDesigner().save();
		getFormDesigner().goToFormManager();
		getFormManager().deleteForm();
	}
	
	@Test(groups = "formDesigner")
	public void tryToMoveAQuestionWhenMiddleElementOfCategory() {
		String formName = NEW_FORM_NAME + "_" + (new Date()).getTime();
		createForm(formName, ABCD_FORM_ADMIN_BIIT1);
		
		getFormManager().clickFormDesigner();
		getFormDesigner().clickInTableRow(0);
		getFormDesigner().createCategory(0, CATEGORY_NAME);
		getFormDesigner().clickInTableRow(1);
		
		getFormDesigner().createQuestion(1, QUESTION_NAME_1, AnswerType.INPUT_FIELD, AnswerFormat.TEXT);
		getFormDesigner().createQuestion(1, QUESTION_NAME_2, AnswerType.INPUT_FIELD, AnswerFormat.TEXT);
		getFormDesigner().createQuestion(1, QUESTION_NAME_3, AnswerType.INPUT_FIELD, AnswerFormat.TEXT);
				
		getFormDesigner().clickInTableRow(0);
		getFormDesigner().clickInTableRow(3);
		getFormDesigner().clickMoveDown();
		getFormDesigner().clickInTableRow(0);
		getFormDesigner().clickInTableRow(4);
		Assert.assertEquals(getFormDesigner().getTechnicalName().getValue(),QUESTION_NAME_2);
				
		getFormDesigner().clickInTableRow(0);
		getFormDesigner().clickInTableRow(3);
		getFormDesigner().clickMoveUp();
		getFormDesigner().clickInTableRow(0);
		getFormDesigner().clickInTableRow(2);
		Assert.assertEquals(getFormDesigner().getTechnicalName().getValue(),QUESTION_NAME_3);	
		
		getFormDesigner().save();
		getFormDesigner().goToFormManager();
		getFormManager().deleteForm();
	}
	
	@Test(groups = "formDesigner")
	public void changeFormName() {
		String formName = NEW_FORM_NAME + "_" + (new Date()).getTime();
		createForm(formName, ABCD_FORM_ADMIN_BIIT1);
		getFormManager().clickFormDesigner();
		
		String currentName = getFormDesigner().getTechnicalName().getValue();
		getFormDesigner().getTechnicalName().setValue(currentName+MOD);
		getFormDesigner().save();
		getFormDesigner().goToFormManager();
		
		Assert.assertTrue(getFormManager().getFormName(1).contains(MOD));
		
		getFormManager().deleteForm();
	}
}

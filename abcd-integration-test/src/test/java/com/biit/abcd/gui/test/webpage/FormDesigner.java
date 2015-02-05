package com.biit.abcd.gui.test.webpage;

import org.testng.Assert;

import com.biit.abcd.gui.test.window.Proceed;
import com.vaadin.testbench.TestBenchElement;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.ComboBoxElement;
import com.vaadin.testbench.elements.FormLayoutElement;
import com.vaadin.testbench.elements.TextFieldElement;
import com.vaadin.testbench.elements.TreeTableElement;

public class FormDesigner extends AbcdCommonWebpage {

	private static final String SAVE_BUTTON = "Save";
	private static final String BUTTON_CATEGORY = "Category";
	private static final String BUTTON_GROUP = "Group";
	private static final String BUTTON_QUESTION = "Question";
	private static final String BUTTON_ANSWER = "Answer";
	private static final String BUTTON_SUBANSWER = "Subanswer";
	private static final String BUTTON_REMOVE = "Remove";
	private static final String TECHNICAL_NAME = "Technical Name";
	private static final String BUTTON_MOVE_UP = "Move Up";
	private static final String BUTTON_MOVE_DOWN = "Move Down";
	
	public enum AnswerType {
		INPUT_FIELD("Input Field"), RADIO_BUTTON("Radio Button"), MULTI_CHECKBOX("Multi Checkbox");

		private String value;

		AnswerType(String caption) {
			this.value = caption;
		}

		public String getValue() {
			return value;
		}
	}

	public enum AnswerFormat {

		TEXT("Text"), NUMBER("Number"), DATE("Date"), POSTAL_CODE("Postal Code"), ;

		private String value;

		AnswerFormat(String caption) {
			this.value = caption;
		}

		public String getValue() {
			return value;
		}
	}

	private final Proceed proceed;

	public FormDesigner() {
		super();
		proceed = new Proceed();
		addWindow(proceed);
	}

	@Override
	public String getWebpageUrl() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public ButtonElement getButtonByCaption(String caption){
		return $(ButtonElement.class).caption(caption).first();
	}
	
	public ButtonElement getCategoryButton(){
		return getButtonByCaption(BUTTON_CATEGORY);
	}
	
	public ButtonElement getGroupButton(){
		return getButtonByCaption(BUTTON_GROUP);
	}
	
	public ButtonElement getQuestionButton(){
		return getButtonByCaption(BUTTON_QUESTION);
	}
	
	public ButtonElement getAnswerButton(){
		return getButtonByCaption(BUTTON_ANSWER);
	}
	
	public ButtonElement getSubanswerButton(){
		return getButtonByCaption(BUTTON_SUBANSWER);
	}
	
	public ButtonElement getRemoveButton(){
		return getButtonByCaption(BUTTON_REMOVE);
	}

	public void finishDesign() {
		$(ButtonElement.class).caption("Finish Design").first().click();
		$(ButtonElement.class).caption("Finish Design").first().waitForVaadin();
		proceed.clickAccept();
	}
	
	public void clickRemoveButton(){
		getRemoveButton().click();
		getRemoveButton().waitForVaadin();	
	}

	public void clickNewCategory() {
		getCategoryButton().click();
		getCategoryButton().waitForVaadin();
	}

	private void clickNewGroup() {
		Assert.assertTrue(getGroupButton().isEnabled());
		getGroupButton().click();
		getGroupButton().waitForVaadin();
	}

	public void clickNewQuestion() {
		Assert.assertTrue(getQuestionButton().isEnabled());
		getQuestionButton().click();
		getQuestionButton().waitForVaadin();
	}

	public void clickNewAnswer() {
		Assert.assertTrue(getAnswerButton().isEnabled());
		getAnswerButton().click();
		getAnswerButton().waitForVaadin();
	}

	public void clickNewSubanswer() {
		Assert.assertTrue(getSubanswerButton().isEnabled());
		getSubanswerButton().click();
		getSubanswerButton().waitForVaadin();
	}

	public TreeTableElement getDesignTable() {
		return $(TreeTableElement.class).first();
	}

	private TestBenchElement getTableRow(int row) {
		while (true) {
			getDesignTable().scroll(0);
			getDesignTable().scroll(40 * (row + 1));
			try {
				return getDesignTable().getRow(row).getCell(0);
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
	}

	public void clickInTableRow(int row) {
		getTableRow(row).focus();
		getTableRow(row).click();
		getTableRow(row).waitForVaadin();
	}

	public TextFieldElement getTechnicalName() {
		return $(TextFieldElement.class).caption(TECHNICAL_NAME).first();
	}

	public void setTechnicalName(String technicalName) {
		getTechnicalName().setValue(technicalName);
		getTechnicalName().waitForVaadin();
		clickOnFormLayout();
		getTechnicalName().waitForVaadin();
	}

	public void createCategory(int row, String name) {
		clickInTableRow(row);
		clickNewCategory();
		setTechnicalName(name);
	}

	public void createGroup(int row, String name) {
		clickInTableRow(row);
		clickNewGroup();
		setTechnicalName(name);
	}

	private ComboBoxElement getAnswerType() {
		return $(ComboBoxElement.class).caption("Answer Type").first();
	}

	private ComboBoxElement getAnswerFormat() {
		return $(ComboBoxElement.class).caption("Answer Format").first();
	}

	private void setAnswerType(AnswerType type) {
		getAnswerType().selectByText(type.getValue());
		getAnswerType().waitForVaadin();
		clickOnFormLayout();
		getAnswerType().waitForVaadin();
	}

	private void clickOnFormLayout() {
		$(FormLayoutElement.class).first().click();
	}

	private void setAnswerFormat(AnswerFormat format) {
		getAnswerFormat().selectByText(format.getValue());
		getAnswerFormat().waitForVaadin();
		clickOnFormLayout();
		getAnswerFormat().waitForVaadin();
	}

	public void createMultiCheckbox(int rowToClick, int rowWhereWillAppear, String name, String... answers) {
		createQuestionWithAnswers(rowToClick, rowWhereWillAppear, name, AnswerType.MULTI_CHECKBOX, answers);
	}

	public void createRadioButton(int rowToClick, int rowWhereWillAppear, String name, String... answers) {
		createQuestionWithAnswers(rowToClick, rowWhereWillAppear, name, AnswerType.RADIO_BUTTON, answers);
	}

	private void createQuestionWithAnswers(int rowToClick, int rowWhereWillAppear, String name, AnswerType answerType,
			String... answers) {
		createQuestion(rowToClick, name, answerType, null);
		clickInTableRow(rowWhereWillAppear);
		for (String answer : answers) {
			createAnswer(rowWhereWillAppear, answer);
		}
	}

	public void createInputField(int row, String name, AnswerFormat format) {
		createQuestion(row, name, AnswerType.INPUT_FIELD, format);
	}

	public void createQuestion(int row, String name, AnswerType type, AnswerFormat format) {
		clickInTableRow(row);
		clickNewQuestion();
		setTechnicalName(name);
		setAnswerType(type);
		if (format != null) {
			setAnswerFormat(format);
		}
	}

	public void createAnswer(int row, String name) {
		clickInTableRow(row);
		clickNewAnswer();
		setTechnicalName(name);
	}

	public void createSubanswer(int row, String name) {
		clickInTableRow(row);
		clickNewSubanswer();
		setTechnicalName(name);
	}

	public void createBlockOfAllKindOfQuestions(int row, String name) {
		int blockRow = row;
		for (AnswerFormat format : AnswerFormat.values()) {
			createInputField(blockRow, name + "_" + row, format);
			row++;
		}

		createRadioButton(blockRow, row + 1, name + "_" + row, "A", "B", "C");
		row += 4;
		createMultiCheckbox(blockRow, row + 1, name + "_" + row, "D", "E", "F");
		row += 4;
		createRadioButton(blockRow, row + 1, name + "_" + row, "G", "H", "I");
		createSubanswer(row + 3, "HA");
		createSubanswer(row + 3, "HB");
		row += 6;
		// Don't know why but it was impossible to put it in first subanswer O.o
		createMultiCheckbox(blockRow, row + 1, name + "_" + row, "J", "K", "L");
		createSubanswer(row + 2, "JA");
		createSubanswer(row + 2, "JB");
		row += 6;
	}

	public void save() {
		$(ButtonElement.class).caption(SAVE_BUTTON).first().click();
	}

	public void remove(int row) {
		clickInTableRow(row);
		clickRemoveButton();
		getProceed().clickAccept();
	}

	public void clickMoveUp() {
		getMoveUpButton().click();
		getMoveUpButton().waitForVaadin();	
	}
	
	private TestBenchElement getMoveUpButton() {
		return getButtonByCaption(BUTTON_MOVE_UP);
	}
	
	private TestBenchElement getMoveDownButton() {
		return getButtonByCaption(BUTTON_MOVE_DOWN);
	}

	public void clickMoveDown() {
		getMoveDownButton().click();
		getMoveDownButton().waitForVaadin();	
	}

}

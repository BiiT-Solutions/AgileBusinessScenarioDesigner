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

	public void finishDesign() {
		$(ButtonElement.class).caption("Finish Design").first().click();
		$(ButtonElement.class).caption("Finish Design").first().waitForVaadin();
		proceed.clickAccept();
	}

	public void clickNewCategory() {
		$(ButtonElement.class).caption("Category").first().click();
		$(ButtonElement.class).caption("Category").first().waitForVaadin();
	}

	private void clickNewGroup() {
		Assert.assertTrue($(ButtonElement.class).caption("Group").first().isEnabled());
		$(ButtonElement.class).caption("Group").first().click();
		$(ButtonElement.class).caption("Group").first().waitForVaadin();
	}

	public void clickNewQuestion() {
		Assert.assertTrue($(ButtonElement.class).caption("Question").first().isEnabled());
		$(ButtonElement.class).caption("Question").first().click();
		$(ButtonElement.class).caption("Question").first().waitForVaadin();
	}

	public void clickNewAnswer() {
		Assert.assertTrue($(ButtonElement.class).caption("Answer").first().isEnabled());
		$(ButtonElement.class).caption("Answer").first().click();
		$(ButtonElement.class).caption("Answer").first().waitForVaadin();
	}

	public void clickNewSubanswer() {
		Assert.assertTrue($(ButtonElement.class).caption("Subanswer").first().isEnabled());
		$(ButtonElement.class).caption("Subanswer").first().click();
		$(ButtonElement.class).caption("Subanswer").first().waitForVaadin();
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

	private TextFieldElement getTechnicalName() {
		return $(TextFieldElement.class).caption("Technical Name").first();
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
		$(ButtonElement.class).caption("Save").first().click();
	}

	public void goToFormManager() {
		$(ButtonElement.class).caption("Forms").first().click();
	}

}

package com.biit.abcd.gui.test.webpage;

import com.biit.abcd.gui.test.window.Proceed;
import com.biit.gui.tester.VaadinGuiWebpage;
import com.vaadin.testbench.elements.ButtonElement;
import com.vaadin.testbench.elements.ComboBoxElement;
import com.vaadin.testbench.elements.TextFieldElement;
import com.vaadin.testbench.elements.TreeTableElement;

public class FormDesigner extends VaadinGuiWebpage {
	
	public enum AnswerType{
		INPUT_FIELD("Input Field"),
		RADIO_BUTTON("Radio Button"),
		MULTI_CHECKBOX("Multi Checkbox");
		
		private String value;
		
		AnswerType(String caption){
			this.value = caption;
		}
		
		public String getValue(){
			return value;
		}
	}
	
	public enum AnswerFormat{
		
		TEXT("Text"),
		NUMBER("Number"),
		DATE("Date"),
		POSTAL_CODE("Postal Code"),
		;
		
		private String value;
		
		AnswerFormat(String caption){
			this.value = caption;
		}
		
		public String getValue(){
			return value;
		}
	}

	private final Proceed proceed;
	
	public FormDesigner() {
		super();
		proceed= new Proceed();
		addWindow(proceed);
	}

	@Override
	public String getWebpageUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	public void finishDesign() {
		$(ButtonElement.class).caption("Finish Design").first().click();
		proceed.clickAccept();
	}
	
	public void clickNewCategory(){
		$(ButtonElement.class).caption("Category").first().click();
	}
	
	public void clickNewQuestion() {
		$(ButtonElement.class).caption("Question").first().click();
	}
	
	public void clickNewAnswer(){
		$(ButtonElement.class).caption("Answer").first().click();
	}
	
	public void clickNewSubanswer(){
		$(ButtonElement.class).caption("Subanswer").first().click();
	}
	
	public TreeTableElement getDesignTable(){
		return $(TreeTableElement.class).first();
	}
	
	public void clickInTableRow(int row){
		getDesignTable().getCell(row, 0);
	}
	
	private TextFieldElement getTechnicalName(){
		return $(TextFieldElement.class).caption("Technical Name").first();
	}
	
	public void setTechnicalName(String technicalName){
		getTechnicalName().setValue(technicalName);
	}

	public void createCategory(int row, String name) {
		clickInTableRow(row);
		clickNewCategory();
		setTechnicalName(name);
		clickInTableRow(row+1);
	}

	private ComboBoxElement getAnswerType() {
		return $(ComboBoxElement.class).caption("Answer Type").first();
	}
	
	private ComboBoxElement getAnswerFormat() {
		return $(ComboBoxElement.class).caption("Answer Format").first();
	}
	
	private void setAnswerType(AnswerType type) {
		getAnswerType().selectByText(type.getValue());
	}
	
	private void setAnswerFormat(AnswerFormat format) {
		getAnswerFormat().selectByText(format.getValue());
	}
	
	public void createMultiCheckbox(int row, String name, String...answers){
		createQuestion(row, name, AnswerType.MULTI_CHECKBOX, null);
		for(String answer: answers){
			row++;
			createAnswer(row, answer);
		}
	}
	
	public void createRadioButton(int row, String name, String...answers){
		createQuestion(row, name, AnswerType.RADIO_BUTTON, null);
		for(String answer: answers){
			row++;
			createAnswer(row, answer);
		}		
	}
	
	public void createInputField(int row, String name, AnswerFormat format){
		createQuestion(row, name, AnswerType.INPUT_FIELD, format);
	}

	public void createQuestion(int row, String name,AnswerType type, AnswerFormat format) {
		clickInTableRow(row);
		clickNewQuestion();
		setTechnicalName(name);
		setAnswerType(type);
		if(format!=null){
			setAnswerFormat(format);
		}
		clickInTableRow(row+1);
	}

	public void createAnswer(int row, String name){
		clickInTableRow(row);
		clickNewAnswer();
		setTechnicalName(name);
		clickInTableRow(row+1);
	}
	
	public void createSubanswer(int row, String name){
		clickInTableRow(row);
		clickNewSubanswer();
		setTechnicalName(name);
		clickInTableRow(row+1);
	}

	public void createBlockOfAllKindOfQuestions(int row,String name){
		int i = 0;
		for(AnswerFormat format:AnswerFormat.values()){
			System.out.println(row+i);
			createInputField(row+i,name+"_"+i,format);
			i++;
		}
		
		System.out.println(row+i);
		createRadioButton(row+i, name+"_"+i,"A","B","C");
		i+=4;
		System.out.println(row+i);
		createMultiCheckbox(row+i, name+"_"+i,"D","E","F");
		i+=4;
		System.out.println(row+i);
		createRadioButton(row+i, name+"_"+i,"G","H","I");
		System.out.println(row+i+1);
		createSubanswer(row+i+1, "HA");
		System.out.println(row+i+2);
		createSubanswer(row+i+2, "HB");
		i+=6;
		createMultiCheckbox(row+i, name+"_"+i,"J","K","L");
		createSubanswer(row+i+0, "JA");
		createSubanswer(row+i+1, "JB");
		i+=6;
	}

	public void save() {
		$(ButtonElement.class).caption("Save").first().click();
	}
	
}

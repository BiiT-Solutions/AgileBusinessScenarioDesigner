package com.biit.abcd.webpages.elements.testscenario;

import com.biit.abcd.language.LanguageCodes;
import com.biit.abcd.language.ServerTranslate;
import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.testscenarios.TestScenario;
import com.biit.abcd.webpages.components.AcceptCancelWindow;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.vaadin.ui.Component;

public class WindowLaunchTestScenarioCategory extends AcceptCancelWindow {
	private static final long serialVersionUID = -2672639049803001638L;
	private TestScenarioForm categoryForm;

	public WindowLaunchTestScenarioCategory(Category category) throws NotValidChildException, FieldTooLongException {
		setCaption(ServerTranslate.translate(LanguageCodes.LAUNCH_TEST_WINDOW_CAPTION));
		setWidth("30%");
		setHeight("50%");
		setClosable(false);
		setModal(true);
		setResizable(false);
		setContent(generateContent(category));
	}

	private Component generateContent(Category category) throws NotValidChildException, FieldTooLongException {
		categoryForm = new TestScenarioForm();
		categoryForm.createCategoryContent(new TestScenario(category.getName() + "_test"), category);
		return categoryForm;
	}
	
	public TestScenario getTestScenario(){
		return categoryForm.getTestScenario();
	}
	
	public Form getForm(){
		return categoryForm.getForm();
	}
}
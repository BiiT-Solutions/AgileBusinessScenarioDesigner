package com.biit.abcd.core.drools.facts.inputform;

import java.util.List;

import com.biit.orbeon.form.ICategory;
import com.biit.orbeon.form.IQuestion;
import com.biit.orbeon.form.ISubmittedForm;
import com.biit.orbeon.form.exceptions.CategoryDoesNotExistException;
import com.biit.orbeon.form.exceptions.QuestionDoesNotExistException;

/**
 * Needed to allow drools to manage variables in memory.<br>
 * It is used as the parent class of the submitted form, to allow this form to manage variable.<br>
 * 
 */
public class DroolsForm implements ISubmittedForm {

	private ISubmittedForm form;

	public DroolsForm(SubmittedForm submittedForm) {
		this.form = submittedForm;
	}

	@Override
	public List<ICategory> getCategories() {
		return form.getCategories();
	}

	@Override
	public void addCategory(ICategory category) {
		form.addCategory(category);

	}

	public ISubmittedForm getForm() {
		return form;
	}

	@Override
	public ICategory getCategory(String categoryName) throws CategoryDoesNotExistException {
		return form.getCategory(categoryName);
	}

	@Override
	public IQuestion getQuestion(String categoryName, String questionName) throws QuestionDoesNotExistException,
			CategoryDoesNotExistException {
		return form.getCategory(categoryName).getQuestion(questionName);
	}

	@Override
	public String getFormName() {
		return form.getFormName();
	}

	@Override
	public String getApplicationName() {
		return form.getApplicationName();
	}

	@Override
	public String getId() {
		return form.getId();
	}

	public void setForm(SubmittedForm submittedForm) {
		this.form = submittedForm;
	}

	@Override
	public ISubmittedForm getSubmittedForm() {
		return form;
	}

}

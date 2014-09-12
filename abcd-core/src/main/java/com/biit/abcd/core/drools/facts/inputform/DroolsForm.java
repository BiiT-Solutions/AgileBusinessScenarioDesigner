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

	private SubmittedForm submittedform;

	public DroolsForm(SubmittedForm submittedForm) {
		this.submittedform = submittedForm;
	}

	@Override
	public List<ICategory> getCategories() {
		return submittedform.getCategories();
	}

	@Override
	public void addCategory(ICategory category) {
		submittedform.addCategory(category);

	}

	@Override
	public SubmittedForm getSubmittedForm() {
		return submittedform;
	}

	@Override
	public ICategory getCategory(String categoryName) throws CategoryDoesNotExistException {
		return submittedform.getCategory(categoryName);
	}

	@Override
	public IQuestion getQuestion(String categoryName, String questionName) throws QuestionDoesNotExistException,
			CategoryDoesNotExistException {
		return submittedform.getCategory(categoryName).getQuestion(questionName);
	}

	@Override
	public String getFormName() {
		return submittedform.getFormName();
	}

	@Override
	public String getApplicationName() {
		return submittedform.getApplicationName();
	}

	@Override
	public String getId() {
		return submittedform.getId();
	}

	public void setSubmittedForm(SubmittedForm submittedForm) {
		this.submittedform = submittedForm;
	}

}

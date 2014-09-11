package com.biit.abcd.core.drools.facts.inputform;

import java.util.List;

import com.biit.orbeon.form.ICategory;
import com.biit.orbeon.form.ISubmittedForm;
import com.biit.orbeon.form.exceptions.CategoryDoesNotExistException;

/**
 * Needed to allow drools to manage variables in memory.<br>
 * It is used as the parent class of the submitted form, to allow this form to manage variable.<br>
 * 
 */
public class DroolsForm implements ISubmittedForm {

	private SubmittedForm submittedForm;

	public DroolsForm(SubmittedForm submittedForm){
		this.submittedForm = submittedForm;
	}
	
	@Override
	public List<ICategory> getCategories() {
		return submittedForm.getCategories();
	}

	@Override
	public void addCategory(ICategory category) {
		submittedForm.addCategory(category);
		
	}

	@Override
	public ICategory getCategory(String categoryText) throws CategoryDoesNotExistException {
		return submittedForm.getCategory(categoryText);
	}

	@Override
	public String getFormName() {
		return submittedForm.getFormName();
	}

	@Override
	public String getApplicationName() {
		return submittedForm.getApplicationName();
	}

	@Override
	public String getId() {
		return submittedForm.getId();
	}

	public SubmittedForm getSubmittedForm() {
		return submittedForm;
	}

	public void setSubmittedForm(SubmittedForm submittedForm) {
		this.submittedForm = submittedForm;
	}
}

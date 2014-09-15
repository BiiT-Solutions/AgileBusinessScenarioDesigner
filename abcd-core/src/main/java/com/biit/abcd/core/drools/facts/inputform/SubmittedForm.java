package com.biit.abcd.core.drools.facts.inputform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.biit.orbeon.form.ICategory;
import com.biit.orbeon.form.IQuestion;
import com.biit.orbeon.form.ISubmittedForm;
import com.biit.orbeon.form.exceptions.CategoryDoesNotExistException;
import com.biit.orbeon.form.exceptions.QuestionDoesNotExistException;

/**
 * Basic implementation of an Orbeon Form that includes categories and questions.
 * 
 */
public class SubmittedForm implements ISubmittedForm {

	private String applicationName;
	private List<ICategory> categories;
	private String formName;
	// TreeObject -> VarName --> Value
	private HashMap<Object, HashMap<String, Object>> formVariables;

	public SubmittedForm(String applicationName, String formName) {
		this.formName = formName;
		this.applicationName = applicationName;
		this.setCategories(new ArrayList<ICategory>());
	}

	public void addCategory(ICategory category) {
		if (this.categories == null) {
			this.setCategories(new ArrayList<ICategory>());
		}
		((Category) category).setParent(this);
		this.categories.add(category);
	}

	public String getApplicationName() {
		return this.applicationName;
	}

	public List<ICategory> getCategories() {
		return this.categories;
	}

	public ICategory getCategory(String categoryText) throws CategoryDoesNotExistException {
		for (ICategory category : this.getCategories()) {
			if (category.getText().equals(categoryText)) {
				return category;
			}
		}
		throw new CategoryDoesNotExistException("Category '" + categoryText + "' does not exists.");
	}

	public String getFormName() {
		return this.formName;
	}

	public String getId() {
		if ((this.getApplicationName() != null) && (this.getFormName() != null)) {
			return this.getApplicationName() + "/" + this.getFormName();
		}
		return null;
	}

	public Number getNumberVariableValue(Object treeObject, String varName) {
		if ((this.formVariables == null) || (this.formVariables.get(treeObject) == null)) {
			return null;
		}
		return (Number) this.formVariables.get(treeObject).get(varName);
	}

	public Number getNumberVariableValue(String varName) {
		return this.getNumberVariableValue(this, varName);
	}

	public Object getVariableValue(Object treeObject, String varName) {
		if ((this.formVariables == null) || (this.formVariables.get(treeObject) == null)) {
			return null;
		}
		return this.formVariables.get(treeObject).get(varName);
	}

	public Object getVariableValue(String varName) {
		return getVariableValue(this, varName);
	}

	public boolean hasScoreSet(Object treeObject, String varName) {
		if ((this.formVariables == null) || (this.formVariables.get(treeObject) == null)
				|| (this.formVariables.get(treeObject).get(varName) == null)) {
			return false;
		} else {
			return true;
		}
	}

	public boolean isScoreSet(String varName) {
		// Retrieve the form which will have the variables
		if (hasScoreSet(this, varName)) {
			return true;
		} else {
			return false;
		}
	}

	public void setCategories(List<ICategory> categories) {
		this.categories = categories;
	}

	public void setVariableValue(Object treeObject, String varName, Object value) {
		if (formVariables == null) {
			formVariables = new HashMap<Object, HashMap<String, Object>>();
		}
		if (formVariables.get(treeObject) == null) {
			formVariables.put(treeObject, new HashMap<String, Object>());
		}
		formVariables.get(treeObject).put(varName, value);
	}

	public void setVariableValue(String varName, Object value) {
		setVariableValue(this, varName, value);
	}

	public String toString() {
		return this.getFormName();
	}

	public HashMap<Object, HashMap<String, Object>> getFormVariables() {
		return formVariables;
	}

	public ISubmittedForm getSubmittedForm() {
		return this;
	}

	public IQuestion getQuestion(String categoryName, String questionName) throws QuestionDoesNotExistException,
			CategoryDoesNotExistException {
		return getCategory(categoryName).getQuestion(questionName);
	}

}

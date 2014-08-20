package com.biit.abcd.core.drools.facts.inputform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.biit.orbeon.form.ICategory;
import com.biit.orbeon.form.ISubmittedForm;
import com.biit.orbeon.form.exceptions.CategoryDoesNotExistException;

/**
 * Basic implementation of an Orbeon Form that includes categories and questions.
 *
 */
public class SubmittedForm implements ISubmittedForm {

	private String formName;
	private String applicationName;
	private List<ICategory> categories;
	private HashMap<Object, HashMap<String, Object>> formVariables;

	public SubmittedForm(String applicationName, String formName) {
		this.formName = formName;
		this.applicationName = applicationName;
		this.setCategories(new ArrayList<ICategory>());
	}

	@Override
	public List<ICategory> getCategories() {
		return this.categories;
	}

	public void setCategories(List<ICategory> categories) {
		this.categories = categories;
	}

	@Override
	public void addCategory(ICategory category) {
		if (this.categories == null) {
			this.setCategories(new ArrayList<ICategory>());
		}
		((Category)category).setParent(this);
		this.categories.add(category);
	}

	@Override
	public ICategory getCategory(String categoryText) throws CategoryDoesNotExistException {
		for (ICategory category : this.getCategories()) {
			if (category.getText().equals(categoryText)) {
				return category;
			}
		}
		throw new CategoryDoesNotExistException("Category '" + categoryText + "' does not exists.");
	}

	@Override
	public String getFormName() {
		return this.formName;
	}

	@Override
	public String getApplicationName() {
		return this.applicationName;
	}

	@Override
	public String getId() {
		if ((this.getApplicationName() != null) && (this.getFormName() != null)) {
			return this.getApplicationName() + "/" + this.getFormName();
		}
		return null;
	}

	@Override
	public String toString() {
		return this.getFormName();
	}

	public	Number getNumberVariableValue(Object treeObject, String varName){
		if((this.formVariables == null) || (this.formVariables.get(treeObject) == null)){
			return null;
		}
		return (Number) this.formVariables.get(treeObject).get(varName);
	}

	public	Object getVariableValue(Object treeObject, String varName){
		if((this.formVariables == null) || (this.formVariables.get(treeObject) == null)){
			return null;
		}
		return this.formVariables.get(treeObject).get(varName);
	}

	public void setVariableValue(Object treeObject, String varName, Object value){
		if(this.formVariables == null){
			this.formVariables = new HashMap<Object, HashMap<String, Object>>();
		}
		if(this.formVariables.get(treeObject) == null){
			this.formVariables.put(treeObject, new HashMap<String, Object>());
		}
		this.formVariables.get(treeObject).put(varName, value);
	}

	public boolean hasScoreSet(Object treeObject, String varName){
		if((this.formVariables == null) || (this.formVariables.get(treeObject) == null) || (this.formVariables.get(treeObject).get(varName) == null)){
			return false;
		} else {
			return true;
		}
	}

	public boolean isScoreSet() {
		if (this.getVariableValue(this, this.getFormName()) != null) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isScoreNotSet() {
		return !this.isScoreSet();
	}
}

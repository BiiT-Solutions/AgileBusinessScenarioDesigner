package com.biit.abcd.core.drools.facts.inputform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.biit.abcd.core.drools.facts.inputform.exceptions.CategoryDoesNotExistException;
import com.biit.abcd.core.drools.facts.interfaces.ICategory;
import com.biit.abcd.core.drools.facts.interfaces.ISubmittedForm;

/**
 * Basic implementation of an Orbeon Form that includes categories and questions.
 * 
 */
public class SubmittedForm implements ISubmittedForm {

	private String formName;
	private String applicationName;
	private List<ICategory> categories;
	private HashMap<Object, HashMap<String, Double>> formVariables;

	public SubmittedForm(String applicationName, String formName) {
		this.formName = formName;
		this.applicationName = applicationName;
		setCategories(new ArrayList<ICategory>());
	}

	@Override
	public List<ICategory> getCategories() {
		return categories;
	}

	public void setCategories(List<ICategory> categories) {
		this.categories = categories;
	}

	@Override
	public void addCategory(ICategory category) {
		if (categories == null) {
			setCategories(new ArrayList<ICategory>());
		}
		((Category)category).setParent(this);
		categories.add(category);
	}

	@Override
	public ICategory getCategory(String categoryText) throws CategoryDoesNotExistException {
		for (ICategory category : getCategories()) {
			if (category.getTag().equals(categoryText)) {
				return category;
			}
		}
		throw new CategoryDoesNotExistException("Category '" + categoryText + "' does not exists.");
	}

	@Override
	public String getFormName() {
		return formName;
	}

	@Override
	public String getApplicationName() {
		return applicationName;
	}

	@Override
	public String getId() {
		if ((getApplicationName() != null) && (getFormName() != null)) {
			return getApplicationName() + "/" + getFormName();
		}
		return null;
	}

	@Override
	public String toString() {
		return getFormName();
	}

	public Double getVariableValue(Object treeObject, String varName){
		if((formVariables == null) || (formVariables.get(treeObject) == null)){
			return null;
		}
		return formVariables.get(treeObject).get(varName);
	}

	public void setVariableValue(Object treeObject, String varName, Double value){
		if(formVariables == null){
			formVariables = new HashMap<Object, HashMap<String, Double>>();
		}
		if(formVariables.get(treeObject) == null){
			formVariables.put(treeObject, new HashMap<String, Double>());
		}
		formVariables.get(treeObject).put(varName, value);
	}

	public boolean hasScoreSet(Object treeObject){
		if((formVariables == null) || (formVariables.get(treeObject) == null)){
			return false;
		} else {
			return true;
		}
	}

	public boolean isScoreSet() {
		if (getVariableValue(this, getFormName()) != null) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isScoreNotSet() {
		return !isScoreSet();
	}
}

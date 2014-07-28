package com.biit.abcd.core.drools.facts.inputform;

import java.util.ArrayList;
import java.util.List;

import com.biit.abcd.core.drools.facts.inputform.exceptions.CategoryDoesNotExistException;
import com.biit.abcd.core.drools.facts.interfaces.ICategory;
import com.biit.abcd.core.drools.facts.interfaces.IInputForm;
import com.biit.abcd.core.drools.rules.VariablesMap;
import com.biit.abcd.persistence.entity.CustomVariableScope;

/**
 * Basic implementation of an Orbeon Form that includes categories and questions.
 * 
 */
public class InputForm implements IInputForm {

	private String formName;
	private String applicationName;
	private List<ICategory> categories;

	public InputForm(String applicationName, String formName) {
		this.formName = formName;
		this.applicationName = applicationName;
		setCategories(new ArrayList<ICategory>());
	}

	public List<ICategory> getCategories() {
		return categories;
	}

	public void setCategories(List<ICategory> categories) {
		this.categories = categories;
	}

	public void addCategory(ICategory category) {
		if (categories == null) {
			setCategories(new ArrayList<ICategory>());
		}
		categories.add(category);
	}

	public ICategory getCategory(String categoryText) throws CategoryDoesNotExistException {
		for (ICategory category : getCategories()) {
			if (category.getText().equals(categoryText)) {
				return category;
			}
		}
		throw new CategoryDoesNotExistException("Category '" + categoryText + "' does not exists.");
	}

	public String getFormName() {
		return formName;
	}

	public String getApplicationName() {
		return applicationName;
	}

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

	public Object getCustomVariable() {
		return VariablesMap.getInstance().getVariableValue(CustomVariableScope.FORM, getFormName());
	}

	public void setCustomVariable(Object value) {
		VariablesMap.getInstance().addVariableValue(CustomVariableScope.FORM, getFormName(), value);
	}
}

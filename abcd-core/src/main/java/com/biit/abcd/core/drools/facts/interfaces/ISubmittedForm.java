package com.biit.abcd.core.drools.facts.interfaces;

import java.util.List;

import com.biit.abcd.core.drools.facts.inputform.exceptions.CategoryDoesNotExistException;

public interface ISubmittedForm {

	String getFormName();

	String getApplicationName();

	String getId();

	List<ICategory> getCategories();

	void addCategory(ICategory category);

	/**
	 * Gets a category object from its text. If more than one category has the same text, returns the first one.
	 * 
	 * @param categoryText
	 * @return
	 * @throws CategoryDoesNotExistException
	 */
	ICategory getCategory(String categoryText) throws CategoryDoesNotExistException;
}

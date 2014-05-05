package com.biit.abcd.persistence.dao;

import java.util.List;

public interface IGenericDao<T> {

	/**
	 * Get all forms stored into the database.
	 * 
	 * @return
	 */
	List<T> getAll();

	/**
	 * Saves or update a form.
	 * 
	 * @param planningEvent
	 * @return
	 */
	T makePersistent(T entity);

	/**
	 * Delete the persistence of the object (but not the object).
	 * 
	 * 
	 * @param planningEvent
	 */
	void makeTransient(T entity);

	/**
	 * Gets the total number of forms.
	 * 
	 * @return
	 */
	int getRowCount();
}

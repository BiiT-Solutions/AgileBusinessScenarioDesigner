package com.biit.abcd.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.ehcache.util.FindBugsSuppressWarnings;

import com.biit.abcd.core.exceptions.DuplicatedVariableException;
import com.biit.abcd.persistence.dao.IGlobalVariablesDao;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.persistence.dao.exceptions.ElementCannotBePersistedException;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.persistence.entity.exceptions.ElementCannotBeRemovedException;

public class GlobalVariablesController {
	private List<GlobalVariable> globalVariables = null;
	private IGlobalVariablesDao globalVariablesDao;

	public GlobalVariablesController(SpringContextHelper helper) {
		globalVariablesDao = (IGlobalVariablesDao) helper.getBean("globalVariablesDao");
	}

	/**
	 * Read all global variables from database. This method is synchronized.
	 * 
	 * @return
	 * @throws UnexpectedDatabaseException
	 */
	@FindBugsSuppressWarnings("DC_DOUBLECHECK")
	public List<GlobalVariable> getGlobalVariables() throws UnexpectedDatabaseException {
		if (globalVariables == null) {
			synchronized (GlobalVariablesController.class) {
				if (globalVariables == null) {
					globalVariables = globalVariablesDao.getAll();
				}
			}
		}
		return globalVariables;
	}

	public void setGlobalVariables(List<GlobalVariable> globalVariables) throws UnexpectedDatabaseException {
		getGlobalVariables().clear();
		getGlobalVariables().addAll(globalVariables);
	}

	/**
	 * Remove all old global variables and store all the new ones. This method
	 * is synchronized.
	 * 
	 * @throws UnexpectedDatabaseException
	 * @throws ElementCannotBeRemovedException
	 * @throws ElementCannotBePersistedException
	 * @throws DuplicatedVariableException
	 */
	public void update(List<GlobalVariable> globalVariables) throws UnexpectedDatabaseException,
			ElementCannotBeRemovedException, ElementCannotBePersistedException, DuplicatedVariableException {
		synchronized (GlobalVariablesController.class) {
			checkDuplicatedVariables(globalVariables);
			// All old variables.
			Set<GlobalVariable> intersectionOfVariables = new HashSet<GlobalVariable>(this.globalVariables);
			// Intersection with new variables.
			intersectionOfVariables.retainAll(globalVariables);
			// Remove intersection from old variables give all variables to
			// delete.
			Set<GlobalVariable> variablesToRemove = new HashSet<GlobalVariable>(this.globalVariables);
			variablesToRemove.removeAll(intersectionOfVariables);
			checkFormUsingVariable(variablesToRemove);
			// Remove unused variables.
			for (GlobalVariable globalVariable : variablesToRemove) {
				globalVariablesDao.makeTransient(globalVariable);
			}

			for (GlobalVariable globalVariable : globalVariables) {
				globalVariablesDao.makePersistent(globalVariable);
			}
			setGlobalVariables(globalVariables);
		}
	}

	public void checkDuplicatedVariables(List<GlobalVariable> globalVariables) throws DuplicatedVariableException {
		List<GlobalVariable> customVariablesList = new ArrayList<>(globalVariables);
		for (int i = 0; i < customVariablesList.size(); i++) {
			for (int j = i + 1; j < customVariablesList.size(); j++) {
				if (customVariablesList.get(i).getName().equals(customVariablesList.get(j).getName())) {
					throw new DuplicatedVariableException("Duplicated global variable.");
				}
			}
		}
	}

	public void checkFormUsingVariable(GlobalVariable globalVariable) throws UnexpectedDatabaseException,
			ElementCannotBeRemovedException {
		Set<GlobalVariable> globalVariables = new HashSet<>();
		globalVariables.add(globalVariable);
		checkFormUsingVariable(globalVariables);
	}

	public void checkFormUsingVariable(Set<GlobalVariable> globalVariables) throws UnexpectedDatabaseException,
			ElementCannotBeRemovedException {
		int formsUsingVariable = globalVariablesDao.getFormNumberUsing(globalVariables);
		if (formsUsingVariable > 0) {
			throw new ElementCannotBeRemovedException("Global Variables is in use in '" + formsUsingVariable
					+ "' forms.");
		}
	}

}

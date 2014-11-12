package com.biit.abcd.core;

import java.util.List;

import com.biit.abcd.persistence.dao.IGlobalVariablesDao;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;

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
	 * Remove all old global variables and store all the new ones. This method is synchronized.
	 * 
	 * @throws UnexpectedDatabaseException
	 */
	public void update(List<GlobalVariable> globalVariables) throws UnexpectedDatabaseException {
		synchronized (GlobalVariablesController.class) {
			// Remove unused variables.
			for (GlobalVariable globalVariable : this.globalVariables) {
				if (!globalVariables.contains(globalVariable)) {
					globalVariablesDao.makeTransient(globalVariable);
				}
			}

			for (GlobalVariable globalVariable : globalVariables) {
				globalVariablesDao.makePersistent(globalVariable);
			}
			setGlobalVariables(globalVariables);
		}
	}

}

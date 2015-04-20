package com.biit.abcd.persistence.dao;

import java.util.Set;

import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.persistence.dao.IJpaGenericDao;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;

public interface IGlobalVariablesDao extends IJpaGenericDao<GlobalVariable,Long> {

	int getFormNumberUsing(GlobalVariable globalVariable) throws UnexpectedDatabaseException;

	int getFormNumberUsing(Set<GlobalVariable> globalVariables) throws UnexpectedDatabaseException;

}

package com.biit.abcd.persistence.dao.hibernate;

import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.IGlobalVariablesDao;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.persistence.dao.hibernate.GenericDao;

@Repository
public class GlobalVariablesDao extends GenericDao<GlobalVariable> implements IGlobalVariablesDao {

	public GlobalVariablesDao() {
		super(GlobalVariable.class);
	}

	@Override
	protected void initializeSets(List<GlobalVariable> elements) {
		for (GlobalVariable globalVariable : elements) {
			// Initializes the sets for lazy-loading (within the same session)
			Hibernate.initialize(globalVariable.getVariableData());
		}
	}
}

package com.biit.abcd.persistence.dao.hibernate;

import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.IGlobalVariablesDao;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.persistence.dao.hibernate.GenericDao;

@Repository
public class GlobalVariablesDao extends GenericDao<GlobalVariable> implements IGlobalVariablesDao {

	public GlobalVariablesDao() {
		super(GlobalVariable.class);
	}
}

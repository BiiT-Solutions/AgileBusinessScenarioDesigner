package com.biit.abcd.persistence.dao.hibernate;

import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.IFormCustomVariablesDao;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.persistence.dao.hibernate.GenericDao;

@Repository
public class FormCustomVariablesDao extends GenericDao<CustomVariable> implements IFormCustomVariablesDao {

	public FormCustomVariablesDao() {
		super(CustomVariable.class);
	}

}

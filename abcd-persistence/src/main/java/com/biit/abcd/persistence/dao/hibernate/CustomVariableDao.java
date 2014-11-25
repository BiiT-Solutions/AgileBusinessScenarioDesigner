package com.biit.abcd.persistence.dao.hibernate;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.ICustomVariableDao;
import com.biit.abcd.persistence.entity.CustomVariable;
import com.biit.persistence.dao.hibernate.GenericDao;

@Repository
public class CustomVariableDao extends GenericDao<CustomVariable> implements ICustomVariableDao {

	public CustomVariableDao() {
		super(CustomVariable.class);
	}

	@Override
	protected void initializeSets(List<CustomVariable> arg0) {
	}

}

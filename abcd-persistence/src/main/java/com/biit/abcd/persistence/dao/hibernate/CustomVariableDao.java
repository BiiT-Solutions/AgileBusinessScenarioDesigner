package com.biit.abcd.persistence.dao.hibernate;

import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.ICustomVariableDao;
import com.biit.abcd.persistence.entity.CustomVariable;


@Repository
public class CustomVariableDao extends AnnotatedGenericDao<CustomVariable, Long> implements ICustomVariableDao {

	public CustomVariableDao() {
		super(CustomVariable.class);
	}

}

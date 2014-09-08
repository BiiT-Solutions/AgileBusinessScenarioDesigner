package com.biit.abcd.persistence.dao.hibernate;

import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.IVariableDataDao;
import com.biit.abcd.persistence.entity.globalvariables.VariableData;
import com.biit.persistence.dao.hibernate.GenericDao;

@Repository
public class VariableDataDao extends GenericDao<VariableData> implements IVariableDataDao {

	public VariableDataDao() {
		super(VariableData.class);
	}
}

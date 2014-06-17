package com.biit.abcd.persistence.dao.hibernate;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.IVariableDataDao;
import com.biit.abcd.persistence.entity.globalvariables.VariableData;

@Repository
public class VariableDataDao extends GenericDao<VariableData> implements IVariableDataDao {

	public VariableDataDao() {
		super(VariableData.class);
	}

	@Override
	protected void initializeSets(List<VariableData> elements) {

	}

}

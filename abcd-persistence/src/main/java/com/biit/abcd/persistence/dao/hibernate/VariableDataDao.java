package com.biit.abcd.persistence.dao.hibernate;

import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.IVariableDataDao;
import com.biit.abcd.persistence.entity.globalvariables.VariableData;
import com.biit.form.persistence.dao.jpa.AnnotatedGenericDao;


@Repository
public class VariableDataDao extends AnnotatedGenericDao<VariableData,Long> implements IVariableDataDao {

	public VariableDataDao() {
		super(VariableData.class);
	}

}

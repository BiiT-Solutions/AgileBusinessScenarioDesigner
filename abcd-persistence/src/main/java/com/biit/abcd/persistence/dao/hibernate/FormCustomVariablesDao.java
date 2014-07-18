package com.biit.abcd.persistence.dao.hibernate;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.IFormCustomVariablesDao;
import com.biit.abcd.persistence.entity.CustomVariable;

@Repository
public class FormCustomVariablesDao extends GenericDao<CustomVariable> implements IFormCustomVariablesDao {

	public FormCustomVariablesDao() {
		super(CustomVariable.class);
	}

	@Override
	protected void initializeSets(List<CustomVariable> elements) {
		// for (CustomVariable customVariables : elements) {
		// // Initializes the sets for lazy-loading (within the same session)
		//
		// }
	}

}

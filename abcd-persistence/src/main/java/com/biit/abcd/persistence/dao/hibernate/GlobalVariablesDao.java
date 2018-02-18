package com.biit.abcd.persistence.dao.hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.IGlobalVariablesDao;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;

@Repository
public class GlobalVariablesDao extends AnnotatedGenericDao<GlobalVariable, Long> implements IGlobalVariablesDao {

	public GlobalVariablesDao() {
		super(GlobalVariable.class);
	}

	@Override
	public int getFormNumberUsing(GlobalVariable globalVariable) throws UnexpectedDatabaseException {
		if (globalVariable == null || globalVariable.getId() == null) {
			return 0;
		}

		try {
			Query query = getEntityManager().createQuery("SELECT count(*) FROM ExpressionValueCustomVariable WHERE variable=:globalVariableId");
			query.setParameter("globalVariableId", globalVariable.getId());
			Long count = (Long) query.getSingleResult();
			return count.intValue();
		} catch (RuntimeException e) {
			throw new UnexpectedDatabaseException(e.getMessage(), e);
		}
	}

	@Override
	public int getFormNumberUsing(Set<GlobalVariable> globalVariables) throws UnexpectedDatabaseException {
		if (globalVariables == null || globalVariables.isEmpty()) {
			return 0;
		}

		List<Long> ids = new ArrayList<>();
		for (GlobalVariable globalVariable : globalVariables) {
			if (globalVariable.getId() != null) {
				ids.add(globalVariable.getId());
			}
		}

		try {
			Query query = getEntityManager().createQuery("SELECT count(*) FROM ExpressionValueCustomVariable WHERE variable IN (:globalVariablesId)");
			query.setParameter("globalVariablesId", ids);
			Long count = (Long) query.getSingleResult();
			return count.intValue();
		} catch (RuntimeException e) {
			throw new UnexpectedDatabaseException(e.getMessage(), e);
		}
	}
}

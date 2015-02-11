package com.biit.abcd.persistence.dao.hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.IGlobalVariablesDao;
import com.biit.abcd.persistence.entity.globalvariables.GlobalVariable;
import com.biit.persistence.dao.exceptions.UnexpectedDatabaseException;
import com.biit.persistence.dao.hibernate.GenericDao;
import com.biit.persistence.entity.exceptions.ElementCannotBeRemovedException;

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

	@Override
	public void makeTransient(GlobalVariable globalVariable) throws UnexpectedDatabaseException,
			ElementCannotBeRemovedException {
		// Check the block is not linked.
		int formsUsingVariable =getFormNumberUsing(globalVariable);
		if (formsUsingVariable > 0) {
			throw new ElementCannotBeRemovedException("Global Variable is in use in '"+formsUsingVariable+"' forms.");
		}
		super.makeTransient(globalVariable);
	}

	@Override
	public int getFormNumberUsing(GlobalVariable globalVariable) throws UnexpectedDatabaseException {
		if (globalVariable == null || globalVariable.getId() == null) {
			return 0;
		}
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			Query query = session
					.createQuery("SELECT count(*) FROM ExpressionValueGlobalConstant WHERE globalVariable_ID=:globalVariableId");
			query.setLong("globalVariableId", globalVariable.getId());
			Long count = (Long) query.uniqueResult();
			session.getTransaction().commit();
			return count.intValue();
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
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

		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			Query query = session
					.createQuery("SELECT count(*) FROM ExpressionValueGlobalConstant WHERE globalVariable_ID IN (:globalVariablesId)");
			query.setParameterList("globalVariablesId", ids);

			Long count = (Long) query.uniqueResult();
			session.getTransaction().commit();
			return count.intValue();
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw new UnexpectedDatabaseException(e.getMessage(), e);
		}
	}
}

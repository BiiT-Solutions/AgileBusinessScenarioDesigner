package com.biit.abcd.persistence.dao.hibernate;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.biit.persistence.dao.IJpaGenericDao;
import com.biit.persistence.dao.jpa.GenericDao;


public abstract class AnnotatedGenericDao<EntityClass, PrimaryKeyClass extends Serializable> extends GenericDao<EntityClass, PrimaryKeyClass> implements IJpaGenericDao<EntityClass, PrimaryKeyClass>{
	
	@PersistenceContext(unitName = "defaultPersistenceUnit")
	@Qualifier(value = "abcdManagerFactory")
	private EntityManager entityManager;
	
	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}
	
	public AnnotatedGenericDao(Class<EntityClass> entityClass) {
		super(entityClass);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
	public void makePersistent(EntityClass entity) {
		super.makePersistent(entity);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
	public void makeTransient(EntityClass entity) {
		super.makeTransient(entity);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
	public EntityClass get(PrimaryKeyClass id) {
		return super.get(id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = true)
	public int getRowCount() {
		return super.getRowCount();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = true)
	public List<EntityClass> getAll() {
		return super.getAll();
	}
	
}

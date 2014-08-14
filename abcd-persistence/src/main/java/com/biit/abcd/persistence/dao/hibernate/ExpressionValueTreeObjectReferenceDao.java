package com.biit.abcd.persistence.dao.hibernate;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.IExpressionValueTreeObjectReferenceDao;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.persistence.dao.hibernate.GenericDao;

@Repository
public class ExpressionValueTreeObjectReferenceDao extends GenericDao<ExpressionValueTreeObjectReference> implements
		IExpressionValueTreeObjectReferenceDao {

	public ExpressionValueTreeObjectReferenceDao() {
		super(ExpressionValueTreeObjectReference.class);
	}

	@Override
	protected void initializeSets(List<ExpressionValueTreeObjectReference> elements) {

	}

}

package com.biit.abcd.persistence.dao.hibernate;

import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.IExpressionValueTreeObjectReferenceDao;
import com.biit.abcd.persistence.entity.expressions.ExpressionValueTreeObjectReference;
import com.biit.form.persistence.dao.jpa.AnnotatedGenericDao;


@Repository
public class ExpressionValueTreeObjectReferenceDao extends AnnotatedGenericDao<ExpressionValueTreeObjectReference,Long> implements
		IExpressionValueTreeObjectReferenceDao {

	public ExpressionValueTreeObjectReferenceDao() {
		super(ExpressionValueTreeObjectReference.class);
	}

}

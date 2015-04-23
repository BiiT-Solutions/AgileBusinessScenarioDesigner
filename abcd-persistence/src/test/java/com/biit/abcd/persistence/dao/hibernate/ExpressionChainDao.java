package com.biit.abcd.persistence.dao.hibernate;

import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.IExpressionChainDao;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;


@Repository
public class ExpressionChainDao extends AnnotatedGenericDao<ExpressionChain,Long> implements IExpressionChainDao {

	public ExpressionChainDao() {
		super(ExpressionChain.class);
	}

}

package com.biit.abcd.persistence.dao.hibernate;

import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.IExpressionChainDao;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;
import com.biit.persistence.dao.hibernate.GenericDao;

@Repository
public class ExpressionChainDao extends GenericDao<ExpressionChain> implements IExpressionChainDao {

	public ExpressionChainDao() {
		super(ExpressionChain.class);
	}

}

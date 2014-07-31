package com.biit.abcd.persistence.dao.hibernate;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.IExpressionChainDao;
import com.biit.abcd.persistence.entity.expressions.ExpressionChain;

@Repository
public class ExpressionChainDao extends GenericDao<ExpressionChain> implements IExpressionChainDao {

	public ExpressionChainDao() {
		super(ExpressionChain.class);
	}

	@Override
	protected void initializeSets(List<ExpressionChain> rules) {
	}

}

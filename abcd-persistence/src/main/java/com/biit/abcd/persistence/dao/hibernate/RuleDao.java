package com.biit.abcd.persistence.dao.hibernate;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.IRuleDao;
import com.biit.abcd.persistence.entity.expressions.Rule;

@Repository
public class RuleDao extends GenericDao<Rule> implements IRuleDao {

	public RuleDao() {
		super(Rule.class);
	}

	@Override
	protected void initializeSets(List<Rule> rules) {
	}

}

package com.biit.abcd.persistence.dao.hibernate;

import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.IRuleDao;
import com.biit.abcd.persistence.entity.expressions.Rule;
import com.biit.persistence.dao.hibernate.GenericDao;

@Repository
public class RuleDao extends GenericDao<Rule> implements IRuleDao {

	public RuleDao() {
		super(Rule.class);
	}

}

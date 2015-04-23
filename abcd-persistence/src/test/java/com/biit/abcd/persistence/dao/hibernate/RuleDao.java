package com.biit.abcd.persistence.dao.hibernate;

import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.IRuleDao;
import com.biit.abcd.persistence.entity.expressions.Rule;


@Repository
public class RuleDao extends AnnotatedGenericDao<Rule,Long> implements IRuleDao {

	public RuleDao() {
		super(Rule.class);
	}
}

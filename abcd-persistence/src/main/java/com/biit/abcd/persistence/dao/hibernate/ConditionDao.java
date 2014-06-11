package com.biit.abcd.persistence.dao.hibernate;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.IConditionDao;
import com.biit.abcd.persistence.entity.rules.Condition;

@Repository
public class ConditionDao extends GenericDao<Condition> implements IConditionDao {

	public ConditionDao() {
		super(Condition.class);
	}

	@Override
	protected void initializeSets(List<Condition> elements) {

	}

}

package com.biit.abcd.persistence.dao.hibernate;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.IActionDao;
import com.biit.abcd.persistence.entity.rules.ActionExpression;

@Repository
public class ActionDao extends GenericDao<ActionExpression> implements IActionDao {

	public ActionDao() {
		super(ActionExpression.class);
	}

	@Override
	protected void initializeSets(List<ActionExpression> elements) {

	}

}

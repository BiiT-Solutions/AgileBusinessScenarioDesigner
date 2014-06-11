package com.biit.abcd.persistence.dao.hibernate;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.IActionDao;
import com.biit.abcd.persistence.entity.rules.Action;

@Repository
public class ActionDao extends GenericDao<Action> implements IActionDao {

	public ActionDao() {
		super(Action.class);
	}

	@Override
	protected void initializeSets(List<Action> elements) {

	}

}

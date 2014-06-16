package com.biit.abcd.persistence.dao.hibernate;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.ITableRuleDao;
import com.biit.abcd.persistence.entity.rules.TableRule;

@Repository
public class TableRuleDao extends GenericDao<TableRule> implements ITableRuleDao {
	public TableRuleDao() {
		super(TableRule.class);
	}

	@Override
	protected void initializeSets(List<TableRule> elements) {
		// Nothing to do, all eager.
	}
}

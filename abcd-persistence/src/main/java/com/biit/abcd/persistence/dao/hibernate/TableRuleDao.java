package com.biit.abcd.persistence.dao.hibernate;

import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.ITableRuleDao;
import com.biit.abcd.persistence.entity.rules.TableRule;

@Repository
public class TableRuleDao extends AnnotatedGenericDao<TableRule,Long> implements ITableRuleDao {

	public TableRuleDao() {
		super(TableRule.class);
	}

}

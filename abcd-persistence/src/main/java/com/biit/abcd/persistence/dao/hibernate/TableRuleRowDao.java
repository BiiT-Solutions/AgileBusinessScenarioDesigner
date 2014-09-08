package com.biit.abcd.persistence.dao.hibernate;

import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.ITableRuleRowDao;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;
import com.biit.persistence.dao.hibernate.GenericDao;

@Repository
public class TableRuleRowDao extends GenericDao<TableRuleRow> implements ITableRuleRowDao {
	public TableRuleRowDao() {
		super(TableRuleRow.class);
	}
}

package com.biit.abcd.persistence.dao.hibernate;

import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.ITableRuleRowDao;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;


@Repository
public class TableRuleRowDao extends AnnotatedGenericDao<TableRuleRow,Long> implements ITableRuleRowDao {

	public TableRuleRowDao() {
		super(TableRuleRow.class);
	}

}

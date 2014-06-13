package com.biit.abcd.persistence.dao;

import java.util.List;

import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.rules.TableRuleRow;

public interface ITableRuleRowDao extends IGenericDao<TableRuleRow> {

	List<TableRuleRow> getFormTableRules(Form form);

}

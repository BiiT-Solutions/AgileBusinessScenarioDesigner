package com.biit.abcd.persistence.dao;

import java.util.List;

import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.rules.TableRule;

public interface ITableRuleDao extends IGenericDao<TableRule> {

	List<TableRule> getFormTableRules(Form form);

}

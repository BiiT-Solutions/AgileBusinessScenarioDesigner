package com.biit.abcd.persistence.dao.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.IActionDao;
import com.biit.abcd.persistence.dao.IConditionDao;
import com.biit.abcd.persistence.dao.IQuestionDao;
import com.biit.abcd.persistence.dao.ITableRuleDao;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.rules.TableRule;

@Repository
public class TableRuleDao extends GenericDao<TableRule> implements ITableRuleDao {

	@Autowired
	private IActionDao actionDao;

	@Autowired
	private IConditionDao conditionDao;

	@Autowired
	private IQuestionDao questionDao;

	public TableRuleDao() {
		super(TableRule.class);
	}

	@Override
	protected void initializeSets(List<TableRule> elements) {
		for (TableRule tableRule : elements) {
			Hibernate.initialize(tableRule.getConditions().keySet());
			Hibernate.initialize(tableRule.getActions());
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<TableRule> getFormTableRules(Form form) {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			Criteria criteria = session.createCriteria(TableRule.class);
			criteria.add(Restrictions.eq("form", form));
			List<TableRule> results = criteria.list();
			initializeSets(results);
			session.getTransaction().commit();
			return results;
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
	}
}

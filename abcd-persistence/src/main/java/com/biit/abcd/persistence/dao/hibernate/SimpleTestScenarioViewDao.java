package com.biit.abcd.persistence.dao.hibernate;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.ISimpleTestScenarioViewDao;
import com.biit.abcd.persistence.entity.SimpleTestScenarioView;

@Repository
public class SimpleTestScenarioViewDao implements ISimpleTestScenarioViewDao {

	private Class<SimpleTestScenarioView> type;

	@PersistenceContext(unitName = "abcdPersistenceUnit")
	@Qualifier(value = "abcdManagerFactory")
	private EntityManager entityManager;

	public SimpleTestScenarioViewDao() {
		this.type = SimpleTestScenarioView.class;

	}

	public Class<SimpleTestScenarioView> getType() {
		return type;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SimpleTestScenarioView> getSimpleTestScenariosByFormId(Long formId) {
		Query query = entityManager.createNativeQuery("SELECT tests.ID, tests.name, tests.formId, formsOfTest.form_version FROM test_scenario AS tests JOIN (SELECT t2.id form_Id, t2.version form_version FROM tree_forms AS t2 JOIN tree_forms AS t1 ON t1.organizationId=t2.organizationId AND t1.label=t2.label WHERE t1.id= ? ) AS formsOfTest WHERE tests.formId=formsOfTest.form_Id;");
		query.setParameter(1, formId);
		
		List<Object[]> queries = query.getResultList();
		List<SimpleTestScenarioView> testScenarioViews = new ArrayList<>();
		for(Object[] result: queries){
			SimpleTestScenarioView testScenarioView = new SimpleTestScenarioView();
			testScenarioView.setId( ((BigInteger) result[0]).longValue());
			testScenarioView.setName((String) result[1]);
			testScenarioView.setFormId(((BigInteger) result[2]).longValue());
			testScenarioView.setFormVersion((Integer) result[3]);
			testScenarioViews.add(testScenarioView);
		}
		
		return testScenarioViews;
	}
}

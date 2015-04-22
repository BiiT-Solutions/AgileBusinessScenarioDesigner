package com.biit.abcd.persistence.dao.hibernate;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.ITestScenarioDao;
import com.biit.abcd.persistence.entity.testscenarios.TestScenario;

@Repository
public class TestScenarioDao extends AnnotatedGenericDao<TestScenario,Long> implements ITestScenarioDao {

	public TestScenarioDao() {
		super(TestScenario.class);
	}

	@Override
	public List<TestScenario> getTestScenarioByForm(Long formId) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<TestScenario> cq = cb.createQuery(TestScenario.class);
		Metamodel m = getEntityManager().getMetamodel();
		EntityType<TestScenario> testScenarioType = m.entity(TestScenario.class);
		Root<TestScenario> root = cq.from(TestScenario.class);
		cq.where(cb.equal(root.get(testScenarioType.getSingularAttribute("formId",Long.class)),formId));
		return getEntityManager().createQuery(cq).getResultList();
	}

	@Override
	public List<TestScenario> getTestScenarioByForm(String formLabel,
			Long formOrganizationId) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<TestScenario> cq = cb.createQuery(TestScenario.class);
		Metamodel m = getEntityManager().getMetamodel();
		EntityType<TestScenario> testScenarioType = m.entity(TestScenario.class);
		Root<TestScenario> root = cq.from(TestScenario.class);
		cq.where(cb.and(
				cb.equal(root.get(testScenarioType.getSingularAttribute("formLabel",String.class)),formLabel),
				cb.equal(root.get(testScenarioType.getSingularAttribute("formOrganization",Long.class)),formOrganizationId)
				));
		return getEntityManager().createQuery(cq).getResultList();
	}
}

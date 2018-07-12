package com.biit.abcd.persistence.dao.hibernate;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.biit.abcd.persistence.dao.ITestScenarioDao;
import com.biit.abcd.persistence.entity.testscenarios.TestScenario;
import com.biit.persistence.entity.exceptions.ElementCannotBeRemovedException;

@Repository
public class TestScenarioDao extends AnnotatedGenericDao<TestScenario, Long> implements ITestScenarioDao {

	public TestScenarioDao() {
		super(TestScenario.class);
	}

	@Override
	@Transactional(value = "abcdTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
	@Cacheable(value = "springTestScenariosByForm", key = "#formId", condition = "#formId != null")
	public List<TestScenario> getTestScenarioByForm(Long formId) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<TestScenario> cq = cb.createQuery(TestScenario.class);
		Metamodel m = getEntityManager().getMetamodel();
		EntityType<TestScenario> testScenarioType = m.entity(TestScenario.class);
		Root<TestScenario> root = cq.from(TestScenario.class);
		cq.where(cb.equal(root.get(testScenarioType.getSingularAttribute("formId", Long.class)), formId));
		List<TestScenario> testScenarios = getEntityManager().createQuery(cq).getResultList();
		if (testScenarios != null) {
			for (TestScenario testScenario : testScenarios) {
				testScenario.initializeSets();
			}
		}
		return testScenarios;
	}

	@Override
	@Transactional(value = "abcdTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
	@Cacheable(value = "springTestScenariosByForm", key = "{#formLabel, #formOrganizationId}")
	public List<TestScenario> getTestScenarioByForm(String formLabel, Long formOrganizationId) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<TestScenario> cq = cb.createQuery(TestScenario.class);
		Metamodel m = getEntityManager().getMetamodel();
		EntityType<TestScenario> testScenarioType = m.entity(TestScenario.class);
		Root<TestScenario> root = cq.from(TestScenario.class);
		cq.where(cb.and(
				cb.equal(root.get(testScenarioType.getSingularAttribute("formLabel", String.class)), formLabel), cb
						.equal(root.get(testScenarioType.getSingularAttribute("formOrganization", Long.class)),
								formOrganizationId)));

		List<TestScenario> testScenarios = getEntityManager().createQuery(cq).getResultList();
		if (testScenarios != null) {
			for (TestScenario testScenario : testScenarios) {
				testScenario.initializeSets();
			}
		}
		return testScenarios;
	}

	@Override
	@Transactional(value = "abcdTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
	@Cacheable(value = "springTestScenarios", key = "#id", condition = "#id != null")
	public TestScenario get(Long id) {
		TestScenario testScenario = super.get(id);
		if (testScenario != null) {
			testScenario.initializeSets();
		}
		return testScenario;
	}

	@Override
	@Transactional(value = "abcdTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
	@Caching(evict = { @CacheEvict(value = "springTestScenarios", key = "#testScenario.getId()"),
			@CacheEvict(value = "springTestScenariosByForm", allEntries = true) })
	public void makeTransient(TestScenario testScenario) throws ElementCannotBeRemovedException {
		super.makeTransient(testScenario);
	}

	@Override
	@Transactional(value = "abcdTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
	@Caching(put = { @CachePut(value = "springTestScenarios", key = "#testScenario.getId()", condition = "#testScenario.getId() != null") }, evict = { @CacheEvict(value = "springTestScenariosByForm", allEntries = true) })
	public TestScenario makePersistent(TestScenario testScenario) {
		return super.makePersistent(testScenario);
	}

	@Override
	@Transactional(value = "abcdTransactionManager", propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
	@Caching(evict = { @CacheEvict(value = "springTestScenarios", key = "#testScenario?.getId()"),
			@CacheEvict(value = "springTestScenariosByForm", allEntries = true) })
	public TestScenario merge(TestScenario testScenario) {
		return super.merge(testScenario);
	}

	@Override
	@Caching(evict = { @CacheEvict(value = "springTestScenarios", allEntries = true),
			@CacheEvict(value = "springTestScenariosByForm", allEntries = true) })
	public void evictAllCache() {
		super.evictAllCache();
	}
}

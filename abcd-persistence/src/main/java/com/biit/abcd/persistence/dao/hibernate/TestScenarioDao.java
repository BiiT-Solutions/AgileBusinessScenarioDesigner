package com.biit.abcd.persistence.dao.hibernate;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.ITestScenarioDao;
import com.biit.abcd.persistence.entity.testscenarios.TestScenario;
import com.biit.abcd.persistence.entity.testscenarios.TestScenarioForm;
import com.biit.form.TreeObject;
import com.biit.persistence.dao.hibernate.GenericDao;

@Repository
public class TestScenarioDao extends GenericDao<TestScenario> implements ITestScenarioDao {

	public TestScenarioDao() {
		super(TestScenario.class);
	}

	@Override
	public TestScenario makePersistent(TestScenario entity) {
		purgeElementsToDelete(entity.getTestScenarioForm());
		entity.getTestScenarioForm().updateChildrenSortSeqs();
		return super.makePersistent(entity);
	}

	private void purgeElementsToDelete(TestScenarioForm entity) {
		Set<TreeObject> elementsToDelete = getElementsToDelete(entity);
		removeAll(elementsToDelete);
	}

	private Set<TreeObject> getElementsToDelete(TreeObject entity) {
		Set<TreeObject> elementsToDelete = new HashSet<>();
		elementsToDelete.addAll(entity.getElementsToDelete());
		entity.setElementsToDelete(new HashSet<TreeObject>());
		for (TreeObject child : entity.getChildren()) {
			elementsToDelete.addAll(getElementsToDelete(child));
			child.setElementsToDelete(new HashSet<TreeObject>());
		}
		return elementsToDelete;
	}

	public void removeAll(Set<TreeObject> entities) {
		for (TreeObject element : entities) {
			if (element.getId() != null) {
				makeTransient(element);
			}
		}
	}

	private void makeTransient(TreeObject entity) {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			session.delete(entity);
			session.flush();
			session.getTransaction().commit();
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
	}

	@Override
	protected void initializeSets(List<TestScenario> elements) {
		for (TestScenario testScenario : elements) {
			initializeTestScenario(testScenario);
		}
	}

	protected void initializeTestScenario(TestScenario testScenario) {
		Hibernate.initialize(testScenario);
		Hibernate.initialize(testScenario.getTestScenarioForm());
		initializeChildsSets(testScenario.getTestScenarioForm());
	}

	private void initializeChildsSets(TreeObject entity) {
		if (entity != null) {
			Hibernate.initialize(entity.getChildren());
			initializeChildsSets(entity.getChildren());
		}
	}

	private void initializeChildsSets(List<TreeObject> elements) {
		for (TreeObject entity : elements) {
			if (entity != null) {
				Hibernate.initialize(entity.getChildren());
				initializeChildsSets(entity.getChildren());
			}
		}
	}

	@Override
	public TestScenario getTestScenarioById(Long scenarioId) {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			Criteria criteria = session.createCriteria(TestScenario.class);
			criteria.add(Restrictions.eq("id", scenarioId));
			TestScenario testScenario = (TestScenario) criteria.uniqueResult();
			initializeTestScenario(testScenario);
			session.getTransaction().commit();
			sortTestScenarioTreeObjects(testScenario);
			return testScenario;
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TestScenario> getTestScenarioByFormLabelVersionOrganizationId(String label, Integer version,
			Long organizationId) {
		Session session = getSessionFactory().getCurrentSession();
		session.beginTransaction();
		try {
			Criteria criteria = session.createCriteria(TestScenario.class);
			criteria.add(Restrictions.eq("formLabel", label));
			criteria.add(Restrictions.eq("formVersion", version));
			criteria.add(Restrictions.eq("formOrganizationId", organizationId));
			List<TestScenario> testScenarios = criteria.list();
			initializeSets(testScenarios);
			session.getTransaction().commit();
			sortTestScenarioTreeObjects(testScenarios);
			return testScenarios;
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
	}
	
	protected void sortTestScenarioTreeObjects(List<TestScenario> testScenarios) {
		for (TestScenario testScenario : testScenarios) {
			sortTestScenarioTreeObjects(testScenario);
		}
	}
	
	protected void sortTestScenarioTreeObjects(TestScenario testScenario) {
		for (TreeObject treeObject : testScenario.getTestScenarioForm().getChildren()) {
			sortChildren((TreeObject) treeObject);
		}
	}
	
	protected void sortChildren(List<TreeObject> treeObjects) {
		for (TreeObject treeObject : treeObjects) {
			sortChildren((TreeObject) treeObject);
		}
	}

	protected void sortChildren(TreeObject treeObject) {
		if (treeObject != null) {
			Collections.sort(treeObject.getChildren(), new ChildrenSort());
			for (TreeObject child : treeObject.getChildren()) {
				sortChildren(child);
			}
		}
	}

	class ChildrenSort implements Comparator<TreeObject> {
		@Override
		public int compare(TreeObject o1, TreeObject o2) {
			return (o1.getSortSeq() < o2.getSortSeq() ? -1 : (o1 == o2 ? 0 : 1));
		}
	}
}

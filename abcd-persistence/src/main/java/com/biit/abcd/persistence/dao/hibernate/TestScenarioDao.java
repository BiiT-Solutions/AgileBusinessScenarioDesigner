package com.biit.abcd.persistence.dao.hibernate;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.biit.abcd.persistence.dao.ITestScenarioDao;
import com.biit.abcd.persistence.entity.testscenarios.TestScenario;

@Repository
public class TestScenarioDao extends AnnotatedGenericDao<TestScenario,Long> implements ITestScenarioDao {

	public TestScenarioDao() {
		super(TestScenario.class);
	}

	@Override
	public TestScenario getTestScenarioById(Long scenarioId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TestScenario> getTestScenarioByForm(Long formId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TestScenario> getTestScenarioByForm(String formLabel,
			Long formOrganizationId) {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public TestScenario makePersistent(TestScenario entity) throws UnexpectedDatabaseException,
//			ElementCannotBePersistedException {
//		purgeElementsToDelete(entity.getTestScenarioForm());
//		entity.getTestScenarioForm().updateChildrenSortSeqs();
//		return super.makePersistent(entity);
//	}
//
//	private void purgeElementsToDelete(TestScenarioForm entity) throws UnexpectedDatabaseException {
//		Set<TreeObject> elementsToDelete = getElementsToDelete(entity);
//		removeAll(elementsToDelete);
//	}
//
//	private Set<TreeObject> getElementsToDelete(TreeObject entity) {
//		Set<TreeObject> elementsToDelete = new HashSet<>();
//		elementsToDelete.addAll(entity.getElementsToDelete());
//		entity.setElementsToDelete(new ArrayList<TreeObject>());
//		for (TreeObject child : entity.getChildren()) {
//			elementsToDelete.addAll(getElementsToDelete(child));
//			child.setElementsToDelete(new ArrayList<TreeObject>());
//		}
//		return elementsToDelete;
//	}
//
//	@Override
//	public void makeTransient(TestScenario entity) throws UnexpectedDatabaseException, ElementCannotBeRemovedException {
//		super.makeTransient(entity);
//		makeTransient(entity.getTestScenarioForm());
//	}
//
//	public void removeAll(Set<TreeObject> entities) throws UnexpectedDatabaseException {
//		for (TreeObject element : entities) {
//			if (element.getId() != null) {
//				makeTransient(element);
//			}
//		}
//	}
//
//	private void makeTransient(TreeObject entity) throws UnexpectedDatabaseException {
//		// Remove also children of elementsToDelete due CascadeType.ALL has been removed from TreeObject
//		for (TreeObject child : entity.getAll(TreeObject.class)) {
//			// add to purge
//			entity.getElementsToDelete().add(child);
//			entity.getChildren().remove(child);
//			try {
//				child.setParent(null);
//			} catch (NotValidParentException e) {
//			}
//		}
//		super.deleteStorableObject(entity);
//		purgeElementsToDelete(entity);
//	}
//
//	private void purgeElementsToDelete(TreeObject entity) throws UnexpectedDatabaseException {
//		Set<TreeObject> elementsToDelete = getElementsToDelete(entity);
//		// Remove also children of elementsToDelete due CascadeType.ALL has been removed from TreeObject
//		for (StorableObject elementToDelete : new ArrayList<StorableObject>(elementsToDelete)) {
//			if (elementToDelete instanceof TreeObject) {
//				elementsToDelete.addAll(((TreeObject) elementToDelete).getAll(TreeObject.class));
//			}
//		}
//		removeAll(elementsToDelete);
//	}
//
//	@Override
//	protected void initializeSets(List<TestScenario> elements) {
//		for (TestScenario testScenario : elements) {
//			initializeTestScenario(testScenario);
//		}
//	}
//
//	protected void initializeTestScenario(TestScenario testScenario) {
//		Hibernate.initialize(testScenario);
//		Hibernate.initialize(testScenario.getTestScenarioForm());
//		initializeChildsSets(testScenario.getTestScenarioForm());
//	}
//
//	private void initializeChildsSets(TreeObject entity) {
//		if (entity != null) {
//			Hibernate.initialize(entity.getChildren());
//			initializeChildsSets(entity.getChildren());
//		}
//	}
//
//	private void initializeChildsSets(List<TreeObject> elements) {
//		for (TreeObject entity : elements) {
//			if (entity != null) {
//				Hibernate.initialize(entity.getChildren());
//				initializeChildsSets(entity.getChildren());
//			}
//		}
//	}
//
//	@Override
//	public TestScenario getTestScenarioById(Long scenarioId) {
//		Session session = getEntityManager().unwrap(Session.class)
//		session.beginTransaction();
//		try {
//			Criteria criteria = session.createCriteria(TestScenario.class);
//			criteria.add(Restrictions.eq("id", scenarioId));
//			TestScenario testScenario = (TestScenario) criteria.uniqueResult();
//			initializeTestScenario(testScenario);
//			session.getTransaction().commit();
//			return testScenario;
//		} catch (RuntimeException e) {
//			session.getTransaction().rollback();
//			throw e;
//		}
//	}
//
//	@SuppressWarnings("unchecked")
//	@Override
//	public List<TestScenario> getTestScenarioByForm(Long formId) {
//		Session session = getSessionFactory().getCurrentSession();
//		session.beginTransaction();
//		try {
//			Criteria criteria = session.createCriteria(TestScenario.class);
//			criteria.add(Restrictions.eq("formId", formId));
//			List<TestScenario> testScenarios = criteria.list();
//			initializeSets(testScenarios);
//			session.getTransaction().commit();
//			return testScenarios;
//		} catch (RuntimeException e) {
//			session.getTransaction().rollback();
//			throw e;
//		}
//	}
//
//	@SuppressWarnings("unchecked")
//	@Override
//	public List<TestScenario> getTestScenarioByForm(String formLabel, Long formOrganizationId) {
//		Session session = getSessionFactory().getCurrentSession();
//		session.beginTransaction();
//		try {
//			Criteria criteria = session.createCriteria(TestScenario.class);
//			criteria.add(Restrictions.eq("formLabel", formLabel));
//			criteria.add(Restrictions.eq("formOrganization", formOrganizationId));
//			List<TestScenario> testScenarios = criteria.list();
//			initializeSets(testScenarios);
//			session.getTransaction().commit();
//			return testScenarios;
//		} catch (RuntimeException e) {
//			session.getTransaction().rollback();
//			throw e;
//		}
//	}
}

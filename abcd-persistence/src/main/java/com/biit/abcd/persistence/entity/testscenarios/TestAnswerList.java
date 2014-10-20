package com.biit.abcd.persistence.entity.testscenarios;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

/**
 * Needed by hibernate to manage a list of elements inside a Map
 * 
 */
@Entity
@Table(name = "test_answer_list")
public class TestAnswerList extends StorableObject {

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	// For avoiding error org.hibernate.loader.MultipleBagFetchException: cannot
	// simultaneously fetch multiple bags
	// (http://stackoverflow.com/questions/4334970/hibernate-cannot-simultaneously-fetch-multiple-bags)
	@LazyCollection(LazyCollectionOption.FALSE)
	@BatchSize(size = 20)
	@OrderBy(value = "creationTime ASC")
	private List<TestAnswer> testAnswerList = null;

	public TestAnswerList() {
		testAnswerList = new ArrayList<TestAnswer>();
	}

	public List<TestAnswer> getTestAnswerList() {
		return testAnswerList;
	}

	public TestAnswer getTestAnswer(int index) {
		return testAnswerList.get(index);
	}

	public void setTestAnswerList(List<TestAnswer> testAnswerList) {
		this.testAnswerList = testAnswerList;
	}

	public void addTestAnswer(TestAnswer testAnswer) {
		testAnswerList.add(testAnswer);
	}

	public int size() {
		return testAnswerList.size();
	}

	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		Set<StorableObject> innerStorableObjects = new HashSet<>();
		List<TestAnswer> testAnswers = getTestAnswerList();
		if (testAnswers != null) {
			for (TestAnswer testAnswer : testAnswers) {
				innerStorableObjects.add(testAnswer);
			}
		}
		return innerStorableObjects;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof TestAnswerList) {
			super.copyBasicInfo(object);
			TestAnswerList testAnswerList = (TestAnswerList) object;
			List<TestAnswer> testAnswers = testAnswerList.getTestAnswerList();
			if (testAnswers != null) {
				for (TestAnswer testAnswer : testAnswers) {
					TestAnswer testAnswerCopy;
					try {
						testAnswerCopy = testAnswer.getClass().newInstance();
						testAnswerCopy.copyData(testAnswer);
						addTestAnswer(testAnswerCopy);
					} catch (InstantiationException | IllegalAccessException e) {
						throw new NotValidStorableObjectException("Object '" + object
								+ "' is not a valid instance of TestAnswerList.");
					}
				}
			}
		} else {
			throw new NotValidStorableObjectException("Object '" + object + "' is not an instance of TestAnswerList.");
		}
	}

	@Override
	public void resetIds() {
		super.resetIds();
		for (TestAnswer testAnswer : getTestAnswerList()) {
			testAnswer.resetIds();
		}
	}
}

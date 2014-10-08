package com.biit.abcd.persistence.entity.testscenarios;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.utils.INameAttribute;
import com.biit.form.TreeObject;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

/**
 * Class for defining a test scenario.
 * 
 */
@Entity
@Table(name = "test_scenario")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class TestScenario extends StorableObject implements INameAttribute {

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	// For avoiding error org.hibernate.loader.MultipleBagFetchException: cannot
	// simultaneously fetch multiple bags
	// (http://stackoverflow.com/questions/4334970/hibernate-cannot-simultaneously-fetch-multiple-bags)
	@LazyCollection(LazyCollectionOption.FALSE)
	private Map<TreeObject, TestAnswer> questionTestAnswerRelationship;
	@Column(unique = true, length = MAX_UNIQUE_COLUMN_LENGTH)
	private String name;

	public TestScenario() {
		super();
		questionTestAnswerRelationship = new HashMap<>();
	}

	public TestScenario(String name) throws FieldTooLongException {
		super();
		questionTestAnswerRelationship = new HashMap<>();
		setName(name);
	}

	@Override
	public void resetIds() {
		super.resetIds();
		if (questionTestAnswerRelationship != null) {
			for (TreeObject question : questionTestAnswerRelationship.keySet()) {
				questionTestAnswerRelationship.get(question).resetIds();
			}
		}
	}

	public void setName(String name) throws FieldTooLongException {
		if (name.length() > MAX_UNIQUE_COLUMN_LENGTH) {
			throw new FieldTooLongException("Name is limited to " + MAX_UNIQUE_COLUMN_LENGTH
					+ " characters due to database restrictions. ");
		}
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Map<TreeObject, TestAnswer> getData() {
		return questionTestAnswerRelationship;
	}

	public TestAnswer getTestAnswer(Question question) {
		return questionTestAnswerRelationship.get(question);
	}

	public boolean containsQuestion(Question question) {
		return questionTestAnswerRelationship.containsKey(question);
	}

	public void setData(Map<TreeObject, TestAnswer> questionTestAnswerRelation) {
		questionTestAnswerRelationship.clear();
		for (TreeObject question : questionTestAnswerRelation.keySet()) {
			addData(question, questionTestAnswerRelation.get(question));
		}
	}

	public void addData(TreeObject question, TestAnswer testAnswer) {
		questionTestAnswerRelationship.put(question, testAnswer);
	}

	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		Set<StorableObject> innerStorableObjects = new HashSet<>();
		for (TreeObject question : questionTestAnswerRelationship.keySet()) {
			innerStorableObjects.add(question);
			if (questionTestAnswerRelationship.get(question) != null) {
				innerStorableObjects.add(questionTestAnswerRelationship.get(question));
			}
		}
		return innerStorableObjects;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof TestScenario) {
			super.copyBasicInfo(object);
			TestScenario testScenario = (TestScenario) object;
			name = testScenario.getName();
			for (TreeObject question : testScenario.getData().keySet()) {
				TestAnswer testAnswer;
				try {
					testAnswer = testScenario.getData().get(question).getClass().newInstance();
					testAnswer.copyData(testScenario.getData().get(question));
					questionTestAnswerRelationship.put(question, testAnswer);
				} catch (InstantiationException | IllegalAccessException e) {
					throw new NotValidStorableObjectException("Object '" + object
							+ "' is not a valid instance of TestScenario.");
				}
			}
		} else {
			throw new NotValidStorableObjectException("Object '" + object + "' is not an instance of TestScenario.");
		}
	}
}

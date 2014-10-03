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
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

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
	private Map<Question, TestAnswer> questionTestAnswerRelationship;
	@Column(unique = true, length = MAX_UNIQUE_COLUMN_LENGTH)
	private String name;

	public TestScenario() {
		super();
		questionTestAnswerRelationship = new HashMap<Question, TestAnswer>();
	}

	public TestScenario(String name) throws FieldTooLongException {
		super();
		questionTestAnswerRelationship = new HashMap<Question, TestAnswer>();
		setName(name);
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

	public Map<Question, TestAnswer> getData() {
		return questionTestAnswerRelationship;
	}

	public TestAnswer getTestAnswer(Question question) {
		return questionTestAnswerRelationship.get(question);
	}

	public void setData(HashMap<Question, TestAnswer> questionTestAnswerRelation) {
		this.questionTestAnswerRelationship = questionTestAnswerRelation;
	}

	public void addData(Question question, TestAnswer testAnswer) {
		questionTestAnswerRelationship.put(question, testAnswer);
	}

	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		Set<StorableObject> innerStorableObjects = new HashSet<>();
		return innerStorableObjects;
	}

}

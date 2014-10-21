package com.biit.abcd.persistence.entity.testscenarios;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

import com.biit.abcd.persistence.utils.INameAttribute;
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
	private List<TestScenarioObject> testScenarioObjects;
	@Column(unique = true, length = MAX_UNIQUE_COLUMN_LENGTH)
	private String name;

	// Form information
	@Column(nullable = false)
	private String formLabel;
	@Column(nullable = false)
	private Integer formVersion;
	@Column(nullable = false, columnDefinition = "DOUBLE")
	private Long formOrganizationId;

	public TestScenario() {
		super();
		testScenarioObjects = new ArrayList<TestScenarioObject>();
	}

	public TestScenario(String name) throws FieldTooLongException {
		super();
		testScenarioObjects = new ArrayList<TestScenarioObject>();
		setName(name);
	}

	public List<TestScenarioObject> getTestScenarioObjects() {
		return testScenarioObjects;
	}

	public void setTestScenarioObjects(List<TestScenarioObject> testScenarioObjects) {
		this.testScenarioObjects.clear();
		this.testScenarioObjects.addAll(testScenarioObjects);
	}

	public void addTestScenarioObject(TestScenarioObject testScenarioObject) {
		if (getTestScenarioObjects() != null) {
			getTestScenarioObjects().add(testScenarioObject);
		}
	}

	@Override
	public void resetIds() {
		super.resetIds();
		for (TestScenarioObject testScenarioObject : getTestScenarioObjects()) {
			testScenarioObject.resetIds();
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

	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		Set<StorableObject> innerStorableObjects = new HashSet<>();
		for (TestScenarioObject testScenarioObject : getTestScenarioObjects()) {
			innerStorableObjects.add(testScenarioObject);
			innerStorableObjects.addAll(testScenarioObject.getAllInnerStorableObjects());
		}
		return innerStorableObjects;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		// TODO
	}

	public String getFormLabel() {
		return formLabel;
	}

	public void setFormLabel(String formLabel) {
		this.formLabel = formLabel;
	}

	public Integer getFormVersion() {
		return formVersion;
	}

	public void setFormVersion(Integer formVersion) {
		this.formVersion = formVersion;
	}

	public Long getFormOrganizationId() {
		return formOrganizationId;
	}

	public void setFormOrganizationId(Long formOrganizationId) {
		this.formOrganizationId = formOrganizationId;
	}
}

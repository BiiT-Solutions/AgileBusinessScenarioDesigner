package com.biit.abcd.persistence.entity.testscenarios;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.biit.abcd.persistence.entity.Category;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Group;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.utils.INameAttribute;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.NotValidChildException;
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

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@LazyCollection(LazyCollectionOption.FALSE)
	// private TestScenarioObject testScenarioForm;
	private TestScenarioForm testScenarioForm;
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
	}

	public TestScenario(String scenarioName, Form form) throws NotValidStorableObjectException, NotValidChildException,
			FieldTooLongException {
		super();
		setName(scenarioName);
		setFormLabel(form.getLabel());
		setFormVersion(form.getVersion());
		setFormOrganizationId(form.getOrganizationId());
		createTestScenarioForm(form, null);
	}

	public TestScenario(String name) throws FieldTooLongException {
		super();
		setName(name);
	}

	public TestScenarioForm getTestScenarioForm() {
		return testScenarioForm;
	}

	public void setTestScenarioForm(TestScenarioForm testScenarioForm) {
		this.testScenarioForm = testScenarioForm;
	}

	@Override
	public void resetIds() {
		super.resetIds();
		getTestScenarioForm().resetIds();
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
		if (getTestScenarioForm() != null) {
			innerStorableObjects.add(getTestScenarioForm());
			innerStorableObjects.addAll(getTestScenarioForm().getAllInnerStorableObjects());
		}
		return innerStorableObjects;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		// TODO
	}

	public String getFormLabel() {
		return testScenarioForm.getLabel();
	}

	public Integer getFormVersion() {
		return testScenarioForm.getVersion();
	}

	public Long getFormOrganizationId() {
		return testScenarioForm.getOrganizationId();
	}

	private void setFormLabel(String formLabel) {
		this.formLabel = formLabel;
	}

	private void setFormVersion(Integer formVersion) {
		this.formVersion = formVersion;
	}

	private void setFormOrganizationId(Long formOrganizationId) {
		this.formOrganizationId = formOrganizationId;
	}

	private void createTestScenarioForm(TreeObject formTreeObject, TreeObject testScenarioTreeObjectParent)
			throws NotValidChildException {
		if (formTreeObject instanceof Form) {
			testScenarioForm = new TestScenarioForm();
			// TODO Change to originalID
			testScenarioForm.setOriginalId(formTreeObject.getComparationId());
			testScenarioForm.setOrganizationId(((Form) formTreeObject).getOrganizationId());
			// Copy children
			for (TreeObject treeObject : formTreeObject.getChildren()) {
				createTestScenarioForm(treeObject, testScenarioForm);
			}
		} else if (formTreeObject instanceof Category) {
			TestScenarioCategory testScenarioCategory = new TestScenarioCategory();
			// TODO Copy the comparation ID
			testScenarioCategory.setOriginalId(formTreeObject.getComparationId());
			testScenarioTreeObjectParent.addChild(testScenarioCategory);
			// Copy children
			for (TreeObject treeObject : formTreeObject.getChildren()) {
				createTestScenarioForm(treeObject, testScenarioCategory);
			}
		} else if (formTreeObject instanceof Group) {
			TestScenarioGroup testScenarioGroup = new TestScenarioGroup();
			// TODO Copy the comparation ID
			testScenarioGroup.setOriginalId(formTreeObject.getComparationId());
			testScenarioTreeObjectParent.addChild(testScenarioGroup);
			// Copy children
			for (TreeObject treeObject : formTreeObject.getChildren()) {
				createTestScenarioForm(treeObject, testScenarioGroup);
			}
		} else if (formTreeObject instanceof Question) {
			TestScenarioQuestion testScenarioQuestion = new TestScenarioQuestion();
			// TODO Copy the comparation ID
			testScenarioQuestion.setOriginalId(formTreeObject.getComparationId());
			testScenarioTreeObjectParent.addChild(testScenarioQuestion);
		}
		// Any other tree object type not taken into account
	}
}

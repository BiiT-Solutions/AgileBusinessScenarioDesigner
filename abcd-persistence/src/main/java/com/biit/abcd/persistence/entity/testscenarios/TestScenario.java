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
import com.biit.form.exceptions.CharacterNotAllowedException;
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
			FieldTooLongException, CharacterNotAllowedException {
		super();
		setName(scenarioName);
		setFormLabel(form.getLabel());
		setFormVersion(form.getVersion());
		setFormOrganizationId(form.getOrganizationId());
		createTestScenarioForm(form, null);
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
		return formLabel;
	}

	public Integer getFormVersion() {
		return formVersion;
	}

	public Long getFormOrganizationId() {
		return formOrganizationId;
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
			throws NotValidChildException, FieldTooLongException, CharacterNotAllowedException {
		if (formTreeObject instanceof Form) {
			testScenarioForm = new TestScenarioForm();
			testScenarioForm.setOriginalReference(formTreeObject.getOriginalReference());
			testScenarioForm.setOrganizationId(((Form) formTreeObject).getOrganizationId());
			testScenarioForm.setName(formTreeObject.getName());

			System.out.println("FORM CREATED : " + testScenarioForm.getName());

			// Copy children
			for (TreeObject child : formTreeObject.getChildren()) {
				createTestScenarioForm(child, testScenarioForm);
			}
		} else if (formTreeObject instanceof Category) {
			TreeObject testScenarioCategory = addChild(formTreeObject, testScenarioTreeObjectParent,
					new TestScenarioCategory());

			System.out.println("CATEGORY CREATED : " + testScenarioCategory.getName());

			// Copy children
			for (TreeObject treeObject : formTreeObject.getChildren()) {
				createTestScenarioForm(treeObject, testScenarioCategory);
			}
		} else if (formTreeObject instanceof Group) {
			TreeObject testScenarioGroup = addChild(formTreeObject, testScenarioTreeObjectParent,
					new TestScenarioGroup());
			((TestScenarioGroup) testScenarioGroup).setRepeatable(((Group) formTreeObject).isRepeatable());

			System.out.println("GROUP CREATED : " + testScenarioGroup.getName());

			// Copy children
			for (TreeObject treeObject : formTreeObject.getChildren()) {
				createTestScenarioForm(treeObject, testScenarioGroup);
			}
		} else if (formTreeObject instanceof Question) {
			TreeObject testScenarioQuestion = addChild(formTreeObject, testScenarioTreeObjectParent,
					new TestScenarioQuestion());

			System.out.println("QUESTION CREATED : " + testScenarioQuestion.getName());
		}
		// Any other tree object type not taken into account
	}

	private TreeObject addChild(TreeObject formTreeObject, TreeObject testScenarioParent, TreeObject testScenarioChild)
			throws FieldTooLongException, CharacterNotAllowedException, NotValidChildException {
		testScenarioChild.setOriginalReference(formTreeObject.getOriginalReference());
		testScenarioChild.setName(formTreeObject.getName());
		testScenarioParent.addChild(testScenarioChild);
		return testScenarioChild;
	}
}

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
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
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
@Table(name = "test_scenario", uniqueConstraints = { @UniqueConstraint(columnNames = { "name", "formId" }) })
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Cache(region = "testScenarios", usage = CacheConcurrencyStrategy.READ_WRITE)
public class TestScenario extends StorableObject implements INameAttribute {

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@LazyCollection(LazyCollectionOption.FALSE)
	private TestScenarioForm testScenarioForm;
	@Column(length = MAX_UNIQUE_COLUMN_LENGTH)
	private String name;

	// Form information
	@Column(nullable = false)
	private Long formId;
	@Column(nullable = false, length = TreeObject.MAX_LABEL_LENGTH, columnDefinition = "varchar("
			+ TreeObject.MAX_LABEL_LENGTH + ")")
	private String formLabel;

	@Column(nullable = false, columnDefinition = "DOUBLE")
	private Long formOrganization;

	public TestScenario() {
		super();
	}

	public TestScenario(String scenarioName, Form form) throws NotValidStorableObjectException, NotValidChildException,
			FieldTooLongException, CharacterNotAllowedException {
		super();
		setName(scenarioName);
		setFormId(form.getId());
		setFormLabel(form.getLabel());
		setFormOrganization(form.getOrganizationId());
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

	@Override
	public void setName(String name) throws FieldTooLongException {
		if (name.length() > MAX_UNIQUE_COLUMN_LENGTH) {
			throw new FieldTooLongException("Name is limited to " + MAX_UNIQUE_COLUMN_LENGTH
					+ " characters due to database restrictions. ");
		}
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	public Long getFormId() {
		return formId;
	}

	public void setFormId(Long formId) {
		this.formId = formId;
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
		// Not necessary
	}

	private void createTestScenarioForm(TreeObject formTreeObject, TreeObject testScenarioTreeObjectParent)
			throws NotValidChildException, FieldTooLongException, CharacterNotAllowedException {
		if (formTreeObject instanceof Form) {
			testScenarioForm = new TestScenarioForm();
			testScenarioForm.setOriginalReference(formTreeObject.getOriginalReference());
			testScenarioForm.setOrganizationId(((Form) formTreeObject).getOrganizationId());
			testScenarioForm.setLabel(formTreeObject.getLabel());
			// Copy children
			for (TreeObject child : formTreeObject.getChildren()) {
				createTestScenarioForm(child, testScenarioForm);
			}
		} else if (formTreeObject instanceof Category) {
			TreeObject testScenarioCategory = addChild(formTreeObject, testScenarioTreeObjectParent,
					new TestScenarioCategory());
			// Copy children
			for (TreeObject treeObject : formTreeObject.getChildren()) {
				createTestScenarioForm(treeObject, testScenarioCategory);
			}
		} else if (formTreeObject instanceof Group) {
			TreeObject testScenarioGroup = addChild(formTreeObject, testScenarioTreeObjectParent,
					new TestScenarioGroup());
			((TestScenarioGroup) testScenarioGroup).setRepeatable(((Group) formTreeObject).isRepeatable());
			// Copy children
			for (TreeObject treeObject : formTreeObject.getChildren()) {
				createTestScenarioForm(treeObject, testScenarioGroup);
			}
		} else if (formTreeObject instanceof Question) {
			TreeObject testScenarioQuestion = addChild(formTreeObject, testScenarioTreeObjectParent,
					new TestScenarioQuestion());
			createTestScenatioAnswer((Question) formTreeObject, (TestScenarioQuestion) testScenarioQuestion);
		}
	}

	private TreeObject addChild(TreeObject formTreeObject, TreeObject testScenarioParent, TreeObject testScenarioChild)
			throws FieldTooLongException, CharacterNotAllowedException, NotValidChildException {
		testScenarioChild.setOriginalReference(formTreeObject.getOriginalReference());
		testScenarioChild.setName(formTreeObject.getName());
		testScenarioParent.addChild(testScenarioChild);
		return testScenarioChild;
	}

	private void createTestScenatioAnswer(Question formQuestion, TestScenarioQuestion testQuestion) {
		switch (formQuestion.getAnswerType()) {
		case RADIO:
			testQuestion.setTestAnswer(new TestAnswerRadioButton());
			break;
		case MULTI_CHECKBOX:
			testQuestion.setTestAnswer(new TestAnswerMultiCheckBox());
			break;
		case INPUT:
			switch (formQuestion.getAnswerFormat()) {
			case NUMBER:
				testQuestion.setTestAnswer(new TestAnswerInputNumber());
				break;
			case POSTAL_CODE:
				testQuestion.setTestAnswer(new TestAnswerInputPostalCode());
			case TEXT:
				testQuestion.setTestAnswer(new TestAnswerInputText());
				break;
			case DATE:
				testQuestion.setTestAnswer(new TestAnswerInputDate());
				break;
			}
			break;
		}
	}

	public String getFormLabel() {
		return formLabel;
	}

	public void setFormLabel(String formLabel) {
		this.formLabel = formLabel;
	}

	public Long getFormOrganization() {
		return formOrganization;
	}

	public void setFormOrganization(Long formOrganization) {
		this.formOrganization = formOrganization;
	}
}

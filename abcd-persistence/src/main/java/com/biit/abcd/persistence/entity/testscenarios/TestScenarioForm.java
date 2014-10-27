package com.biit.abcd.persistence.entity.testscenarios;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Question;
import com.biit.form.BaseForm;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.form.log.FormStructureLogger;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

@Entity
@Table(name = "test_scenario_form", uniqueConstraints = { @UniqueConstraint(columnNames = { "label", "version" }) })
@AttributeOverride(name = "label", column = @Column(length = StorableObject.MAX_UNIQUE_COLUMN_LENGTH))
public class TestScenarioForm extends BaseForm {

	public TestScenarioForm() {
		super();
	}

	/**
	 * Creates a test scenario with the structure copied from the form
	 * 
	 * @param form : form to be copied (only the structure)
	 */
	public TestScenarioForm(Form form) {
		super();
		try {
			createTestScenarioBasedOnForm(form);
		} catch (CharacterNotAllowedException | NotValidStorableObjectException | FieldTooLongException e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}
	}

	@Override
	public void resetIds() {
		super.resetIds();
	}

	public void createTestScenarioBasedOnForm(Form form) throws CharacterNotAllowedException,
			NotValidStorableObjectException, FieldTooLongException {
		generateCopy(form);
		resetIds();
		setCreatedBy(form.getCreatedBy());
		setUpdatedBy(form.getUpdatedBy());
		setCreationTime();
		setUpdateTime();

	}

	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		Set<StorableObject> innerStorableObjects = new HashSet<>();
		innerStorableObjects.addAll(super.getAllInnerStorableObjects());
		return innerStorableObjects;
	}

	public void generateCopy(Form form) throws NotValidStorableObjectException, CharacterNotAllowedException {
		try {
			for (TreeObject child : form.getChildren()) {
				TreeObject newChildInstance = generateCopy(child);
				addChild(newChildInstance);
			}
			copyBasicInfo(form);
			copyData(form);

		} catch (NotValidChildException e) {
			// This exceptions are impossible in a copy, but log it anyways.
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}
	}

	public TreeObject generateCopy(TreeObject treeObject) throws CharacterNotAllowedException,
			NotValidStorableObjectException {
		TreeObject newInstance = null;
		try {
			if (treeObject instanceof Question) {
				// Replaces the question for a test scenario question and
				// internally copies the data
				newInstance = new TestScenarioQuestion((Question) treeObject);
				for (TreeObject child : treeObject.getChildren()) {
					TreeObject newChildInstance = generateCopy(child);
					newInstance.addChild(newChildInstance);
				}
			} else {
				newInstance = treeObject.getClass().newInstance();
				for (TreeObject child : treeObject.getChildren()) {
					TreeObject newChildInstance = generateCopy(child);
					newInstance.addChild(newChildInstance);
				}
				newInstance.copyBasicInfo(treeObject);
				newInstance.copyData(treeObject);
			}
		} catch (InstantiationException | IllegalAccessException | NotValidChildException | FieldTooLongException e) {
			// This exceptions are impossible in a copy, but log it anyways.
			FormStructureLogger.errorMessage(this.getClass().getName(), e);
		}

		System.out.println("NEW INSTANCE: " + newInstance.getName());
		return newInstance;
	}
}

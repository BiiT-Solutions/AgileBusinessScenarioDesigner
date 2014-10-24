package com.biit.abcd.persistence.entity.testscenarios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.biit.abcd.logger.AbcdLogger;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.DependencyExistException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

/**
 * Class for defining the elements inside a test scenario
 * 
 */
@Entity
@Table(name = "test_scenario_object")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class TestScenarioObject extends TreeObject {

	private static final String DEFAULT_QUESTION_TECHNICAL_NAME = "";
	private String absoluteGenericPath;
	private String xmlTag;
	private static final List<Class<? extends TreeObject>> ALLOWED_CHILDS = new ArrayList<Class<? extends TreeObject>>(
			Arrays.asList(TestScenarioObject.class, TestScenarioQuestionAnswer.class));

	public TestScenarioObject() {
		super();
	}

	public TestScenarioObject(TreeObject treeObject) throws NotValidStorableObjectException, NotValidChildException {
		super();
		copyBasicInfo(treeObject);
		if (treeObject.getChildren() != null) {
			for (TreeObject child : treeObject.getChildren()) {
				addChild(new TestScenarioObject(child));
			}
		}
	}

	public TestScenarioObject(String name) {
		super();
		try {
			setName(name);
		} catch (FieldTooLongException | CharacterNotAllowedException e) {
			AbcdLogger.errorMessage(this.getClass().getName(), e);
		}
	}

	public String getAbsoluteGenericPath() {
		return absoluteGenericPath;
	}

	public void setAbsoluteGenericPath(String absoluteGenericPath) {
		this.absoluteGenericPath = absoluteGenericPath;
	}

	@Override
	public void checkDependencies() throws DependencyExistException {
		// No dependencies yet.
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		copyBasicInfo(object);
	}

	@Override
	protected List<Class<? extends TreeObject>> getAllowedChildren() {
		return ALLOWED_CHILDS;
	}

	@Override
	protected String getDefaultTechnicalName() {
		return DEFAULT_QUESTION_TECHNICAL_NAME;
	}

	public String getXmlTag() {
		return xmlTag;
	}

	public void setXmlTag(String xmlTag) {
		this.xmlTag = xmlTag;
	}
}

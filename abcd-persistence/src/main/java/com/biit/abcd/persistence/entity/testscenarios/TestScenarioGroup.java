package com.biit.abcd.persistence.entity.testscenarios;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.form.BaseRepeatableGroup;
import com.biit.form.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

@Entity
@Table(name = "test_scenario_group")
public class TestScenarioGroup extends BaseRepeatableGroup {

	private static final String DEFAULT_GROUP_NAME = "TestScenarioGroup";

	public TestScenarioGroup() {
		super();
	}

	@Override
	protected String getDefaultTechnicalName() {
		return DEFAULT_GROUP_NAME;
	}

	public TestScenarioGroup copyTestScenarioGroup() throws NotValidChildException, FieldTooLongException,
			CharacterNotAllowedException {
		TestScenarioGroup testScenarioGroup = new TestScenarioGroup();
		testScenarioGroup.setOriginalReference(getOriginalReference());
		testScenarioGroup.setName(getName());
		testScenarioGroup.setRepeatable(isRepeatable());
		// Copy children
		if ((getChildren() != null) && !getChildren().isEmpty()) {
			for (TreeObject treeObject : getChildren()) {
				if (treeObject instanceof TestScenarioGroup) {
					testScenarioGroup.addChild(((TestScenarioGroup) treeObject).copyTestScenarioGroup());

				} else if (treeObject instanceof TestScenarioQuestion) {
					testScenarioGroup.addChild(((TestScenarioQuestion) treeObject).copyTestScenarioQuestion());
				}
			}
		}
		return testScenarioGroup;
	}
}

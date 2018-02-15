package com.biit.abcd.persistence.entity.testscenarios;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.form.entity.BaseRepeatableGroup;
import com.biit.form.entity.TreeObject;
import com.biit.form.exceptions.CharacterNotAllowedException;
import com.biit.form.exceptions.ElementIsReadOnly;
import com.biit.form.exceptions.NotValidChildException;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

@Entity
@Table(name = "test_scenario_group")
@Cacheable(true)
public class TestScenarioGroup extends BaseRepeatableGroup {
	private static final long serialVersionUID = -1684961388128862471L;
	private static final String DEFAULT_GROUP_NAME = "TestScenarioGroup";
	
	@Column(name="add_enabled")
	private boolean addEnabled;

	public TestScenarioGroup() {
		super();
	}

	@Override
	protected String getDefaultTechnicalName() {
		return DEFAULT_GROUP_NAME;
	}

	/**
	 * Creates a copy of the group and its children.<br>
	 * The father needs a clean enabled attribute, but the children have to maintain their attribute value.
	 * 
	 * @param isChildren
	 * @return
	 * @throws NotValidChildException
	 * @throws FieldTooLongException
	 * @throws CharacterNotAllowedException
	 * @throws ElementIsReadOnly
	 */
	public TestScenarioGroup copyTestScenarioGroup(boolean isChildren) throws NotValidChildException,
			FieldTooLongException, CharacterNotAllowedException, ElementIsReadOnly {
		TestScenarioGroup testScenarioGroup = new TestScenarioGroup();
		testScenarioGroup.setOriginalReference(getOriginalReference());
		testScenarioGroup.setName(getName());
		testScenarioGroup.setRepeatable(isRepeatable());
		if (isChildren) {
			testScenarioGroup.setAddEnabled(isAddEnabled());
		} else {
			testScenarioGroup.setAddEnabled(true);
		}

		// Copy children
		if ((getChildren() != null) && !getChildren().isEmpty()) {
			for (TreeObject treeObject : getChildren()) {
				if (treeObject instanceof TestScenarioGroup) {
					testScenarioGroup.addChild(((TestScenarioGroup) treeObject).copyTestScenarioGroup(true));

				} else if (treeObject instanceof TestScenarioQuestion) {
					testScenarioGroup.addChild(((TestScenarioQuestion) treeObject).copyTestScenarioQuestion());
				}
			}
		}
		return testScenarioGroup;
	}

	public boolean isAddEnabled() {
		return addEnabled;
	}

	public void setAddEnabled(boolean addEnabled) {
		this.addEnabled = addEnabled;
	}
}

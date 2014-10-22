package com.biit.abcd.persistence.entity.testscenarios;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.persistence.entity.StorableObject;

/**
 * Class for defining the elements inside a test scenario
 * 
 */
@Entity
@Table(name = "test_scenario_repeated_group")
public class TestScenarioRepeatedGroup extends TestScenarioObject {

	private int groupIndex;

	public TestScenarioRepeatedGroup() {
		super();
	}

	public TestScenarioRepeatedGroup(String name) {
		super(name);
	}
	
	public int getGroupIndex() {
		return groupIndex;
	}

	public void setGroupIndex(int groupIndex) {
		this.groupIndex = groupIndex;
	}

	@Override
	public void copyData(StorableObject object) {
	}

	@Override
	protected String getDefaultTechnicalName() {
		return super.getDefaultTechnicalName();
	}

	@Override
	public void resetIds() {
		super.resetIds();
	}
}
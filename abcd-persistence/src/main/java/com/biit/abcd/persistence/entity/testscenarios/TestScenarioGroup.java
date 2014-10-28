package com.biit.abcd.persistence.entity.testscenarios;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.form.BaseRepeatableGroup;

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
}

package com.biit.abcd.persistence.entity.testscenarios;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.form.BaseRepeatableGroup;

@Entity
@Table(name = "test_scenario_group")
public class TestScenarioGroup extends BaseRepeatableGroup {

	private String originalId;
	private static final String DEFAULT_GROUP_NAME = "TestScenarioGroup";

	public TestScenarioGroup() {
		super();
	}

	public String getOriginalId() {
		return originalId;
	}

	public void setOriginalId(String originalId) {
		this.originalId = originalId;
	}
	
	@Override
	protected String getDefaultTechnicalName() {
		return DEFAULT_GROUP_NAME;
	}
}

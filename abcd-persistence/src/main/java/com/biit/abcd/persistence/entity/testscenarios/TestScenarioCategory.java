package com.biit.abcd.persistence.entity.testscenarios;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.form.BaseCategory;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

@Entity
@Table(name = "test_scenario_category")
public class TestScenarioCategory extends BaseCategory {

	private static final String DEFAULT_CATEGORY_NAME = "TestScenarioCategory";

	public TestScenarioCategory() {
		super();
	}

	@Override
	protected String getDefaultTechnicalName() {
		return DEFAULT_CATEGORY_NAME;
	}
	
	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		// TODO Auto-generated method stub
	}
}

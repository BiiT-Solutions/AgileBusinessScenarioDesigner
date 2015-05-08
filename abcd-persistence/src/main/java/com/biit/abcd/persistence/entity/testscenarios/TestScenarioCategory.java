package com.biit.abcd.persistence.entity.testscenarios;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.form.entity.BaseCategory;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

@Entity
@Table(name = "test_scenario_category")
@Cacheable(true)
public class TestScenarioCategory extends BaseCategory {
	private static final long serialVersionUID = 1339828980031764729L;
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

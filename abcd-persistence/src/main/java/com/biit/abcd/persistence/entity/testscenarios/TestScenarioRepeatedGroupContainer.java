package com.biit.abcd.persistence.entity.testscenarios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.form.TreeObject;
import com.biit.persistence.entity.StorableObject;

/**
 * Class for defining the elements inside a test scenario
 * 
 */
@Entity
@Table(name = "test_scenario_repeated_group_container")
public class TestScenarioRepeatedGroupContainer extends TestScenarioObject {

	private static final List<Class<? extends TreeObject>> ALLOWED_CHILDS = new ArrayList<Class<? extends TreeObject>>(
			Arrays.asList(TestScenarioRepeatedGroup.class));

	public TestScenarioRepeatedGroupContainer() {
		super();
	}

	public TestScenarioRepeatedGroupContainer(String name) {
		super(name);
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

	@Override
	protected List<Class<? extends TreeObject>> getAllowedChildren() {
		return ALLOWED_CHILDS;
	}
}
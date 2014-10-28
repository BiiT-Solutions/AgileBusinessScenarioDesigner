package com.biit.abcd.persistence.entity.testscenarios;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.form.BaseForm;

@Entity
@Table(name = "test_scenario_form")
public class TestScenarioForm extends BaseForm {
	
	String testFormName;

	private static final String DEFAULT_FORM_NAME = "TestScenarioForm";
	
	public TestScenarioForm() {
		super();
	}
	
	@Override
	public String getName() {
		return testFormName;
	}
	
	@Override
	public void setName(String name) {
		this.testFormName = name;
	}

	@Override
	protected String getDefaultTechnicalName() {
		return DEFAULT_FORM_NAME;
	}
}

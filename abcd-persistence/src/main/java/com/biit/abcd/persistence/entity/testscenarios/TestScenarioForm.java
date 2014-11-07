package com.biit.abcd.persistence.entity.testscenarios;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.form.BaseForm;

@Entity
@Table(name = "test_scenario_form")
public class TestScenarioForm extends BaseForm {
	

	private static final String DEFAULT_FORM_NAME = "TestScenarioForm";
	
	public TestScenarioForm() {
		super();
	}
	
	@Override
	protected String getDefaultTechnicalName() {
		return DEFAULT_FORM_NAME;
	}
}

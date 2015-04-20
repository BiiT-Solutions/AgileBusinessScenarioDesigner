package com.biit.abcd.persistence.entity.testscenarios;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.form.entity.BaseForm;

@Entity
@Table(name = "test_scenario_form")
public class TestScenarioForm extends BaseForm {
	private static final long serialVersionUID = 1571808179307329435L;
	private static final String DEFAULT_FORM_NAME = "TestScenarioForm";
	
	public TestScenarioForm() {
		super();
	}
	
	@Override
	protected String getDefaultTechnicalName() {
		return DEFAULT_FORM_NAME;
	}
}

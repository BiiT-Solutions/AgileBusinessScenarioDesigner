package com.biit.abcd.persistence.entity;

import javax.persistence.Cacheable;
import javax.persistence.Column;


/**
 * As Lazy is not correctly configured, we use this class to show basic form
 * information in the Launch Test scenario window.
 */
@Cacheable(true)
public class SimpleTestScenarioView {

	private Long id;
	
	private String name;
	
	@Column(name = "form_id")
	private Long formId;
	
	@Column(name = "form_version")
	private Integer formVersion;

	public SimpleTestScenarioView() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getFormId() {
		return formId;
	}

	public void setFormId(Long formId) {
		this.formId = formId;
	}
	
	public Integer getFormVersion() {
		return formVersion;
	}

	public void setFormVersion(Integer formVersion) {
		this.formVersion = formVersion;
	}

	@Override
	public String toString() {
		return getName();
	}
}

package com.biit.abcd.persistence.entity;

import java.sql.Timestamp;

/**
 * As Lazy is not correctly configured, we use this class to show basic form
 * information in the Launch Test scenario window.
 */
public class SimpleTestScenarioView {

	private Long id;
	private String comparationId;
	private Long createdBy;
	private Timestamp creationTime;
	private Timestamp updateTime;
	private Long updatedBy;
	private String name;

	public SimpleTestScenarioView() {
	}

	public SimpleTestScenarioView(Form form) {
		setId(form.getId());
		setComparationId(form.getComparationId());
		setCreatedBy(form.getCreatedBy());
		setCreationTime(form.getCreationTime());
		setUpdateTime(form.getUpdateTime());
		setUpdatedBy(form.getUpdatedBy());
		setName(form.getName());
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

	public Timestamp getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Timestamp creationTime) {
		this.creationTime = creationTime;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getComparationId() {
		return comparationId;
	}

	public void setComparationId(String comparationId) {
		this.comparationId = comparationId;
	}

	@Override
	public String toString() {
		return getName();
	}
}

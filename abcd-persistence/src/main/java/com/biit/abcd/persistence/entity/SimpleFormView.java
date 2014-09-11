package com.biit.abcd.persistence.entity;

import java.sql.Timestamp;

/**
 * As Lazy is not correctly configured, we use this class to show basic form information in the Form Manager.
 */
public class SimpleFormView {
	private String name;
	private String label;
	private Integer version;
	private Long id;
	private Timestamp creationTime;
	private Long createdBy;
	private Timestamp updateTime;
	private Long updatedBy;
	private String comparationId;
	private Timestamp availableFrom;
	private Timestamp availableTo;

	public SimpleFormView() {

	}

	public SimpleFormView(Form form) {
		setName(form.getName());
		setLabel(form.getLabel());
		setVersion(form.getVersion());
		setId(form.getId());
		setCreationTime(form.getCreationTime());
		setCreatedBy(form.getCreatedBy());
		setUpdateTime(form.getUpdateTime());
		setUpdatedBy(form.getUpdatedBy());
		setComparationId(form.getComparationId());
		setAvailableFrom(form.getAvailableFrom());
		setAvailableTo(form.getAvailableTo());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
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

	public Timestamp getAvailableFrom() {
		return availableFrom;
	}

	public void setAvailableFrom(Timestamp availableFrom) {
		this.availableFrom = availableFrom;
	}

	public Timestamp getAvailableTo() {
		return availableTo;
	}

	public void setAvailableTo(Timestamp availableTo) {
		this.availableTo = availableTo;
	}

}

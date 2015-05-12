package com.biit.abcd.persistence.entity;

import java.sql.Timestamp;

import javax.persistence.Cacheable;

import com.biit.form.entity.IBaseFormView;

/**
 * As Lazy is not correctly configured, we use this class to show basic form information in the Form Manager.
 */
@Cacheable(true)
public class SimpleFormView implements IBaseFormView {
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
	private long organizationId;
	private boolean isLastVersion;
	private FormWorkStatus status;

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
		setOrganizationId(form.getOrganizationId());
		setLastVersion(form.isLastVersion());
		setStatus(form.getStatus());
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

	@Override
	public String toString() {
		return getLabel();
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public boolean isLastVersion() {
		return isLastVersion;
	}

	public void setLastVersion(boolean isLastVersion) {
		this.isLastVersion = isLastVersion;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((comparationId == null) ? 0 : comparationId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		SimpleFormView other = (SimpleFormView) obj;
		if (comparationId == null) {
			if (other.comparationId != null) {
				return false;
			}
		} else if (!comparationId.equals(other.comparationId)) {
			return false;
		}
		return true;
	}

	public FormWorkStatus getStatus() {
		return status;
	}

	public void setStatus(FormWorkStatus status) {
		this.status = status;
	}

}

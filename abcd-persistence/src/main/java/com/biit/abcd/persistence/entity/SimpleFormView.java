package com.biit.abcd.persistence.entity;

import com.biit.abcd.serialization.FormDeserializer;
import com.biit.abcd.serialization.FormSerializer;
import com.biit.form.entity.IBaseFormView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.persistence.Cacheable;
import java.sql.Timestamp;

/**
 * As Lazy is not correctly configured, we use this class to show basic form information in the Form Manager.
 */
@JsonDeserialize(using = FormDeserializer.class)
@JsonSerialize(using = FormSerializer.class)
@Cacheable(true)
public class SimpleFormView implements IBaseFormView, Comparable<SimpleFormView> {
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

    public Timestamp getAvailableFrom() {
        return availableFrom;
    }

    public Timestamp getAvailableTo() {
        return availableTo;
    }

    public String getComparationId() {
        return comparationId;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public Timestamp getCreationTime() {
        return creationTime;
    }

    public Long getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getName() {
        return name;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public FormWorkStatus getStatus() {
        return status;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public Integer getVersion() {
        return version;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((comparationId == null) ? 0 : comparationId.hashCode());
        return result;
    }

    public boolean isLastVersion() {
        return isLastVersion;
    }

    public void setAvailableFrom(Timestamp availableFrom) {
        this.availableFrom = availableFrom;
    }

    public void setAvailableTo(Timestamp availableTo) {
        this.availableTo = availableTo;
    }

    public void setComparationId(String comparationId) {
        this.comparationId = comparationId;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public void setCreationTime(Timestamp creationTime) {
        this.creationTime = creationTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setLastVersion(boolean isLastVersion) {
        this.isLastVersion = isLastVersion;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public void setStatus(FormWorkStatus status) {
        this.status = status;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return getLabel();
    }

    public String toJson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        return gson.toJson(this);
    }

    @Override
    public int compareTo(SimpleFormView simpleForm) {
        return getName().compareTo(simpleForm.getName());
    }

}

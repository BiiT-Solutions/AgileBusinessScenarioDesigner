package com.biit.abcd.persistence.entity;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (Persistence)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.abcd.serialization.SimpleFormViewDeserializer;
import com.biit.abcd.serialization.SimpleFormViewSerializer;
import com.biit.form.entity.IBaseFormView;
import com.biit.form.jackson.serialization.ObjectMapperFactory;
import com.biit.persistence.entity.BaseStorableObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Cacheable;
import java.sql.Timestamp;

/**
 * As Lazy is not correctly configured, we use this class to show basic form information in the Form Manager.
 */
@JsonDeserialize(using = SimpleFormViewDeserializer.class)
@JsonSerialize(using = SimpleFormViewSerializer.class)
@Cacheable(true)
public class SimpleFormView extends BaseStorableObject implements IBaseFormView, Comparable<SimpleFormView> {
    private String name;
    private String label;
    private Integer version;
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
        if (getComparationId() == null) {
            if (other.getComparationId() != null) {
                return false;
            }
        } else if (!getComparationId().equals(other.getComparationId())) {
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

    public Integer getVersion() {
        return version;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((getComparationId() == null) ? 0 : getComparationId().hashCode());
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

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return getLabel();
    }

    public String toJson() {
        try {
            return ObjectMapperFactory.getObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int compareTo(SimpleFormView simpleForm) {
        return getName().compareTo(simpleForm.getName());
    }

    public static SimpleFormView get(Form form) {
        final SimpleFormView simpleFormView = new SimpleFormView();
        simpleFormView.setName(form.getName());
        simpleFormView.setLabel(form.getLabel());
        simpleFormView.setVersion(form.getVersion());
        simpleFormView.setAvailableFrom(form.getAvailableFrom());
        simpleFormView.setAvailableTo(form.getAvailableTo());
        simpleFormView.setOrganizationId(form.getOrganizationId());
        simpleFormView.setLastVersion(form.isLastVersion());
        simpleFormView.setStatus(form.getStatus());
        return simpleFormView;
    }
}

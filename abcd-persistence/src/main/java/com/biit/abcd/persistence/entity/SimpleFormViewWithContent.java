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

import com.biit.abcd.serialization.SimpleFormViewWithContentDeserializer;
import com.biit.abcd.serialization.SimpleFormViewWithContentSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Cacheable;

/**
 * As Lazy is not correctly configured, we use this class to show basic form information in the Form Manager.
 */
@JsonDeserialize(using = SimpleFormViewWithContentDeserializer.class)
@JsonSerialize(using = SimpleFormViewWithContentSerializer.class)
@Cacheable(true)
public class SimpleFormViewWithContent extends SimpleFormView {

    private String json;

    public SimpleFormViewWithContent() {

    }

    public SimpleFormViewWithContent(Form form) {
        super(form);
        setJson(form.toJson());
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
        SimpleFormViewWithContent other = (SimpleFormViewWithContent) obj;
        if (getComparationId() == null) {
            if (other.getComparationId() != null) {
                return false;
            }
        } else if (!getComparationId().equals(other.getComparationId())) {
            return false;
        }
        return true;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public static SimpleFormViewWithContent get(Form form) {
        final SimpleFormViewWithContent simpleFormView = new SimpleFormViewWithContent();
        simpleFormView.setName(form.getName());
        simpleFormView.setLabel(form.getLabel());
        simpleFormView.setVersion(form.getVersion());
        simpleFormView.setAvailableFrom(form.getAvailableFrom());
        simpleFormView.setAvailableTo(form.getAvailableTo());
        simpleFormView.setOrganizationId(form.getOrganizationId());
        simpleFormView.setJson(form.toJson());
        simpleFormView.setLastVersion(form.isLastVersion());
        simpleFormView.setStatus(form.getStatus());
        return simpleFormView;
    }
}

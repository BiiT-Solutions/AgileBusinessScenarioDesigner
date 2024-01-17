package com.biit.abcd.persistence.entity;

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

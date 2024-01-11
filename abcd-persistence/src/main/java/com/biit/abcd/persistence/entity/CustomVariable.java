package com.biit.abcd.persistence.entity;

import com.biit.abcd.serialization.CustomVariableDeserializer;
import com.biit.abcd.serialization.CustomVariableSerializer;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * Sets all user defined custom variables that will be used in drools conditions
 * and action.
 */
@Entity
@JsonDeserialize(using = CustomVariableDeserializer.class)
@JsonSerialize(using = CustomVariableSerializer.class)
// uniqueConstraints = { @UniqueConstraint(columnNames = { "form", "name",
// "scope" }) } removed due to an updating
// customvariables name problem if a new custom variable has the same name that
// a previously deleted one.
@Table(name = "custom_variables")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Cacheable(true)
public class CustomVariable extends StorableObject implements Comparable<CustomVariable> {
    private static final long serialVersionUID = 4678216833687584848L;

    @ManyToOne(optional = false)
    @JoinColumn(name = "form")
    private Form form;

    // MySQL unique keys are limited to 767 bytes that in utf8mb4 are ~190.
    @Column(length = MAX_UNIQUE_COLUMN_LENGTH)
    private String name;
    @Enumerated(EnumType.STRING)
    // MySQL unique keys are limited to 767 bytes that in utf8mb4 are ~190.
    @Column(length = MAX_UNIQUE_COLUMN_LENGTH)
    private CustomVariableScope scope;
    @Enumerated(EnumType.STRING)
    private CustomVariableType type;

    @Column(name = "default_value")
    private String defaultValue;

    public CustomVariable() {
        super();
    }

    public CustomVariable(Form form, String name, CustomVariableType type, CustomVariableScope scope,
                          String defaultValue) {
        this(form, name, type, scope);
        this.defaultValue = defaultValue;
    }

    public CustomVariable(Form form, String name, CustomVariableType type, CustomVariableScope scope) {
        super();
        this.form = form;
        this.name = name;
        this.scope = scope;
        setType(type);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CustomVariableScope getScope() {
        return scope;
    }

    public void setScope(CustomVariableScope scope) {
        this.scope = scope;
    }

    public CustomVariableType getType() {
        return type;
    }

    public void setType(CustomVariableType type) {
        this.type = type;
        if (defaultValue == null) {
            defaultValue = type.getDefaultValue();
        }
    }

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    @Override
    public String toString() {
        return getName();
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * Returns true if the custom variable compared has the same name and scope
     *
     * @param otherVariable
     * @return true if has the same name and scope.
     */
    public boolean hasSameNameAndScope(CustomVariable otherVariable) {
        return getName().equals(otherVariable.getName()) && getScope().equals(otherVariable.getScope());
    }

    /**
     * Has no inner elements. Returns an empty set.
     */
    @Override
    public Set<StorableObject> getAllInnerStorableObjects() {
        Set<StorableObject> innerStorableObjects = new HashSet<>();
        return innerStorableObjects;
    }

    /**
     * Parent form is not copied!
     */
    @Override
    public void copyData(StorableObject object) throws NotValidStorableObjectException {
        if (object instanceof CustomVariable) {
            super.copyBasicInfo(object);
            CustomVariable variable = (CustomVariable) object;
            name = variable.getName();
            scope = variable.getScope();
            type = variable.getType();
            defaultValue = variable.getDefaultValue();
        } else {
            throw new NotValidStorableObjectException("Object '" + object + "' is not an instance of CustomVariable.");
        }
    }

    public void remove() {
        if (form != null) {
            form.remove(this);
        }
    }

    @Override
    public int compareTo(CustomVariable customVariable) {
        return getName().compareTo(customVariable.getName());
    }
}

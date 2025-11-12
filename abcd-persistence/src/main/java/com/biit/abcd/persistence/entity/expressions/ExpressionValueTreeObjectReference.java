package com.biit.abcd.persistence.entity.expressions;

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

import com.biit.abcd.serialization.expressions.ExpressionValueTreeObjectReferenceDeserializer;
import com.biit.abcd.serialization.expressions.ExpressionValueTreeObjectReferenceSerializer;
import com.biit.form.entity.TreeObject;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@JsonDeserialize(using = ExpressionValueTreeObjectReferenceDeserializer.class)
@JsonSerialize(using = ExpressionValueTreeObjectReferenceSerializer.class)
@Table(name = "expression_value_tree_object_reference")
public class ExpressionValueTreeObjectReference extends ExpressionValue<TreeObject> {
    private static final long serialVersionUID = 3933694492937877414L;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reference")
    private TreeObject reference;

    @Enumerated(EnumType.STRING)
    private QuestionDateUnit unit = null;

    //For helping jackson
    @Transient
    private transient String referenceId;

    public ExpressionValueTreeObjectReference() {
        super();
    }

    public ExpressionValueTreeObjectReference(TreeObject reference) {
        super();
        setReference(reference);
    }

    public ExpressionValueTreeObjectReference(TreeObject reference, QuestionDateUnit unit) {
        super();
        setReference(reference);
        this.unit = unit;
    }

    public TreeObject getReference() {
        return reference;
    }

    public void setReference(TreeObject reference) {
        this.reference = reference;
        this.referenceId = (reference != null ? reference.getComparationId() : null);
    }

    public synchronized QuestionDateUnit getUnit() {
        return unit;
    }

    public synchronized void setUnit(QuestionDateUnit unit) {
        this.unit = unit;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    @Override
    public String getRepresentation(boolean showWhiteCharacter) {
        if (unit != null) {
            return reference + " (" + unit.getAbbreviature() + ")";
        }
        return "" + reference;
    }

    @Override
    protected String getExpression() {
        return "" + getReference();
    }

    @Override
    public TreeObject getValue() {
        return getReference();
    }

    @Override
    public void setValue(TreeObject value) {
        this.reference = value;
    }

    @Override
    public Set<StorableObject> getAllInnerStorableObjects() {
        Set<StorableObject> innerStorableObjects = new HashSet<>();
        if (reference != null) {
            innerStorableObjects.add(reference);
            if (!Objects.equals(reference.getComparationId(), this.getComparationId())) {
                innerStorableObjects.addAll(reference.getAllInnerStorableObjects());
            }
        }
        return innerStorableObjects;
    }

    @Override
    public void copyData(StorableObject object) throws NotValidStorableObjectException {
        if (object instanceof ExpressionValueTreeObjectReference) {
            super.copyData(object);
            ExpressionValueTreeObjectReference expressionValueTreeObjectReference = (ExpressionValueTreeObjectReference) object;
            setUnit(expressionValueTreeObjectReference.getUnit());
            setReferenceId(expressionValueTreeObjectReference.getReferenceId());
            // Rule tables can have empty expressions with null inside
            if (expressionValueTreeObjectReference.getValue() != null) {
                // Later the reference must be updated with current
                // TreeObject
                setValue(expressionValueTreeObjectReference.getValue());
            }
        } else {
            throw new NotValidStorableObjectException("Object '" + object
                    + "' is not an instance of ExpressionValueTreeObjectReference.");
        }
    }

    @Override
    public String toString() {
        if (getReference() != null) {
            return getReference().getName();
        }
        return super.toString();
    }
}

package com.biit.abcd.persistence.entity.globalvariables;

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

import com.biit.abcd.serialization.globalvariables.VariableDataDeserializer;
import com.biit.abcd.serialization.globalvariables.VariableDataSerializer;
import com.biit.drools.global.variables.exceptions.NotValidTypeInVariableData;
import com.biit.drools.global.variables.interfaces.IVariableData;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.sql.Timestamp;

@Entity
@JsonDeserialize(using = VariableDataDeserializer.class)
@JsonSerialize(using = VariableDataSerializer.class)
@Table(name = "global_variable_data")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Cacheable(true)
public abstract class VariableData extends StorableObject implements IVariableData {
    private static final long serialVersionUID = 6356292873575007675L;

    @Column(name = "valid_from")
    private Timestamp validFrom;

    @Column(name = "valid_to")
    private Timestamp validTo;

    // Attribute used for json deserialization due to parent abstract class
    @Transient
    private final String type = this.getClass().getName();

    @Override
    public abstract Object getValue();

    @Override
    public abstract void setValue(Object value) throws NotValidTypeInVariableData;

    public abstract boolean checkType(Object value);

    @Override
    public Timestamp getValidFrom() {
        return validFrom;
    }

    @Override
    public void setValidFrom(Timestamp validFrom) {
        this.validFrom = validFrom;
    }

    @Override
    public Timestamp getValidTo() {
        return validTo;
    }

    @Override
    public void setValidTo(Timestamp validTo) {
        this.validTo = validTo;
    }

    @Override
    public String toString() {
        if (getValue() != null) {
            return getValue().toString();
        }
        return "";
    }

    public String getType() {
        return type;
    }

    @Override
    public void copyData(StorableObject object) throws NotValidStorableObjectException {
        if (object instanceof VariableData) {
            super.copyBasicInfo(object);
            VariableData variableData = (VariableData) object;
            validFrom = variableData.getValidFrom();
            validTo = variableData.getValidTo();
        } else {
            throw new NotValidStorableObjectException("Object '" + object + "' is not an instance of VariableData.");
        }
    }

}

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

import com.biit.abcd.serialization.globalvariables.VariableDataPostalCodeDeserializer;
import com.biit.abcd.serialization.globalvariables.VariableDataPostalCodeSerializer;
import com.biit.drools.global.variables.exceptions.NotValidTypeInVariableData;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@JsonDeserialize(using = VariableDataPostalCodeDeserializer.class)
@JsonSerialize(using = VariableDataPostalCodeSerializer.class)
@Table(name = "global_variable_data_postalcode")
public class VariableDataPostalcode extends VariableData {
    private static final long serialVersionUID = 5350677749105057832L;

    @Column(name = "postalcode")
    private String postalcode;

    public VariableDataPostalcode() {
    }

    @Override
    public String getValue() {
        return postalcode;
    }

    @Override
    public void setValue(Object value) throws NotValidTypeInVariableData {
        if (checkType(value)) {
            postalcode = (String) value;
        } else {
            throw new NotValidTypeInVariableData("The type '" + value.getClass() + "' is not allowed in this variable.");
        }
    }

    @Override
    public boolean checkType(Object value) {
        if (value instanceof String) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Set<StorableObject> getAllInnerStorableObjects() {
        Set<StorableObject> innerStorableObjects = new HashSet<>();
        return innerStorableObjects;
    }

    @Override
    public void copyData(StorableObject object) throws NotValidStorableObjectException {
        if (object instanceof VariableDataPostalcode) {
            super.copyData(object);
            VariableDataPostalcode variableDataPostalCode = (VariableDataPostalcode) object;
            postalcode = variableDataPostalCode.getValue();
        } else {
            throw new NotValidStorableObjectException("Object '" + object + "' is not an instance of VariableDataPostalCode.");
        }
    }

}

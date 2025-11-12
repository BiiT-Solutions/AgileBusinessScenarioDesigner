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

import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.abcd.serialization.globalvariables.GlobalVariableDeserializer;
import com.biit.abcd.serialization.globalvariables.GlobalVariableSerializer;
import com.biit.drools.global.variables.exceptions.NotValidTypeInVariableData;
import com.biit.drools.global.variables.interfaces.IGlobalVariable;
import com.biit.drools.global.variables.interfaces.IVariableData;
import com.biit.drools.global.variables.type.DroolsGlobalVariableFormat;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@JsonDeserialize(using = GlobalVariableDeserializer.class)
@JsonSerialize(using = GlobalVariableSerializer.class)
@Table(name = "global_variables")
@Cacheable(true)
public class GlobalVariable extends StorableObject implements IGlobalVariable {
    private static final long serialVersionUID = 3463882037342518214L;

    @Column(unique = true, length = MAX_UNIQUE_COLUMN_LENGTH)
    private String name;

    private AnswerFormat format;

    @Fetch(value = FetchMode.SUBSELECT)
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinTable(name = "global_variables_variable_data", joinColumns = @JoinColumn(name = "global_variable", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "variable_data", referencedColumnName = "id"))
    private List<VariableData> variableData;

    public GlobalVariable() {
        variableData = new ArrayList<>();
    }

    public GlobalVariable(AnswerFormat format) {
        variableData = new ArrayList<>();
        setFormat(format);
    }

    @Override
    public void resetIds() {
        super.resetIds();
        if (variableData != null) {
            for (VariableData data : variableData) {
                data.resetIds();
            }
        }
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) throws FieldTooLongException {
        if (name.length() > MAX_UNIQUE_COLUMN_LENGTH) {
            throw new FieldTooLongException(
                    "Name is limited to " + MAX_UNIQUE_COLUMN_LENGTH + " characters due to database restrictions. ");
        }
        this.name = name;
    }

    public List<VariableData> getVariableData() {
        return variableData;
    }

    public void setVariableData(List<VariableData> variableData) {
        this.variableData = variableData;
    }

    public AnswerFormat getFormat() {
        return format;
    }

    public void setFormat(AnswerFormat format) {
        this.format = format;
    }

    public void updateValues(GlobalVariable newVariable) {
        try {
            setName(newVariable.getName());
        } catch (FieldTooLongException e) {
            // Impossible.
        }
    }

    /**
     * Creates a new variable data and adds it to the global variable
     *
     * @param value     the value of the variable data
     * @param validFrom starting time of the variable
     * @param validTo   finishing time of the variable
     * @throws NotValidTypeInVariableData if does not exist a type.
     */
    public void addVariableData(Object value, Timestamp validFrom, Timestamp validTo)
            throws NotValidTypeInVariableData {
        VariableData variableData = getNewInstanceVariableData();
        if (variableData != null) {
            variableData.setValue(value);
            variableData.setValidFrom(validFrom);
            variableData.setValidTo(validTo);
        }
        getVariableData().add(variableData);
    }

    private VariableData getNewInstanceVariableData() {
        switch (format) {
            case DATE:
                return new VariableDataDate();
            case NUMBER:
                return new VariableDataNumber();
            case POSTAL_CODE:
                return new VariableDataPostalcode();
            case TEXT:
            case MULTI_TEXT:
                return new VariableDataText();
        }
        return null;
    }

    @Override
    public Set<StorableObject> getAllInnerStorableObjects() {
        Set<StorableObject> innerStorableObjects = new HashSet<>();
        for (IVariableData child : variableData) {
            innerStorableObjects.add((VariableData) child);
            innerStorableObjects.addAll(((VariableData) child).getAllInnerStorableObjects());
        }
        return innerStorableObjects;
    }

    @Override
    public void copyData(StorableObject object) throws NotValidStorableObjectException {
        if (object instanceof GlobalVariable) {
            super.copyBasicInfo(object);
            GlobalVariable globalVariable = (GlobalVariable) object;
            name = globalVariable.getName();
            format = globalVariable.getFormat();

            variableData.clear();
            for (IVariableData child : getVariableData()) {
                VariableData data;
                try {
                    data = ((VariableData) child).getClass().newInstance();
                    data.copyData((VariableData) child);
                    variableData.add(data);
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new NotValidStorableObjectException(
                            "Object '" + object + "' is not a valid instance of GlobalVariable.");
                }
            }
        } else {
            throw new NotValidStorableObjectException("Object '" + object + "' is not an instance of GlobalVariable.");
        }
    }

    @Override
    public String toString() {
        return name + " (" + format + ") " + variableData;
    }

    @Override
    public DroolsGlobalVariableFormat getDroolsVariableFormat() {
        return answerFormatToDroolsFormat(getFormat());
    }

    @Override
    public Object getValue() {
        // First check if the data inside the variable has a valid date
        List<VariableData> varDataList = getVariableData();
        if ((varDataList != null) && !varDataList.isEmpty()) {
            for (IVariableData variableData : varDataList) {
                Timestamp currentTime = new Timestamp(new Date().getTime());
                Timestamp initTime = variableData.getValidFrom();
                Timestamp endTime = variableData.getValidTo();
                // Sometimes endtime can be null, meaning that the
                // variable data has no ending time
                if ((currentTime.after(initTime) && (endTime == null))
                        || (currentTime.after(initTime) && currentTime.before(endTime))) {
                    return variableData.getValue();
                }
            }
        }
        return null;
    }

    public static DroolsGlobalVariableFormat answerFormatToDroolsFormat(AnswerFormat answerFormat) {
        switch (answerFormat) {
            case DATE:
                return DroolsGlobalVariableFormat.DATE;
            case NUMBER:
                return DroolsGlobalVariableFormat.NUMBER;
            case POSTAL_CODE:
                return DroolsGlobalVariableFormat.POSTAL_CODE;
            case TEXT:
                return DroolsGlobalVariableFormat.TEXT;
            case MULTI_TEXT:
                return DroolsGlobalVariableFormat.MULTI_TEXT;
        }
        return null;
    }

    @Override
    public List<IVariableData> getGenericVariableData() {
        List<IVariableData> genericVariableDataList = new ArrayList<IVariableData>();
        genericVariableDataList.addAll(variableData);
        return genericVariableDataList;
    }
}

package com.biit.abcd.persistence.entity.globalvariables;

import java.sql.Timestamp;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.biit.drools.global.variables.exceptions.NotValidTypeInVariableData;
import com.biit.drools.global.variables.interfaces.IVariableData;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

@Entity
@Table(name = "global_variable_data")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Cacheable(true)
public abstract class VariableData extends StorableObject implements IVariableData{
	private static final long serialVersionUID = 6356292873575007675L;

	private Timestamp validFrom;

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
		return getValue().toString();
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

package com.biit.abcd.persistence.entity.globalvariables;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.biit.abcd.persistence.entity.globalvariables.exceptions.NotValidTypeInVariableData;
import com.biit.persistence.entity.StorableObject;

@Entity
@Table(name = "global_variable_data")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class VariableData extends StorableObject {

	private Timestamp validFrom;

	private Timestamp validTo;

	public abstract Object getValue();

	public abstract void setValue(Object value) throws NotValidTypeInVariableData;

	public abstract boolean checkType(Object value);

	// Attribute used for json deserialization due to parent abstract class
	@Transient
	private final String type = this.getClass().getName();

	public Timestamp getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(Timestamp validFrom) {
		this.validFrom = validFrom;
	}

	public Timestamp getValidTo() {
		return validTo;
	}

	public void setValidTo(Timestamp validTo) {
		this.validTo = validTo;
	}

	public boolean updateValues(VariableData editedVariable) throws NotValidTypeInVariableData {
		Object aux = editedVariable.getValue();
		if (aux != null) {
			setValue(editedVariable.getValue());
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return getValue().toString();
	}

	public String getType() {
		return type;
	}

}

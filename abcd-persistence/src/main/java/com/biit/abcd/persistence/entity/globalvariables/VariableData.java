package com.biit.abcd.persistence.entity.globalvariables;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.StorableObject;
import com.biit.abcd.persistence.entity.globalvariables.exceptions.NotValidTypeInVariableData;

@Entity
@Table(name = "GLOBAL_VARIABLE_DATA")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class VariableData extends StorableObject {

	private Timestamp validFrom;

	private Timestamp validTo;

	public abstract Object getValue();
	public abstract void setValue(Object value) throws NotValidTypeInVariableData;
	public abstract boolean checkType(Object value);

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
		if(aux != null){
			setValue(editedVariable.getValue());
			return true;
		}else{
			return false;
		}
	}
}

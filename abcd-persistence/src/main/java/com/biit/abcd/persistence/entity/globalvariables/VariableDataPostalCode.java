package com.biit.abcd.persistence.entity.globalvariables;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.globalvariables.exceptions.NotValidTypeInVariableData;

@Entity
@Table(name = "GLOBAL_VARIABLE_DATA_POSTALCODE")
public class VariableDataPostalCode extends VariableData {

	private String postalCode;

	@Override
	public String getValue() {
		return postalCode;
	}

	@Override
	public void setValue(Object value) throws NotValidTypeInVariableData {
		if (checkType(value)) {
			this.postalCode = (String) value;
		} else {
			throw new NotValidTypeInVariableData("The type '" + value.getClass() + "' is not allowed in this variable.");
		}
	}

	public boolean checkType(Object value) {
		if (value instanceof String)
			return true;
		else
			return false;
	}

}

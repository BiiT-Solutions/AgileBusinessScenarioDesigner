package com.biit.abcd.persistence.entity.globalvariables;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.globalvariables.exceptions.NotValidTypeInVariableData;

@Entity
@Table(name = "GLOBAL_VARIABLE_DATA_TEXT")
public class VariableDataText extends VariableData {

	private String value;

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public void setValue(Object value) throws NotValidTypeInVariableData {
		if (checkType(value)) {
			this.value = (String) value;
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

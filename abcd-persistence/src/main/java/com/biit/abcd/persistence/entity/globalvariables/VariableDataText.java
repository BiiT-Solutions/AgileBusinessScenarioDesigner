package com.biit.abcd.persistence.entity.globalvariables;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.globalvariables.exceptions.NotValidTypeInVariableData;

@Entity
@Table(name = "global_variable_data_text")
public class VariableDataText extends VariableData {

	private String value;

	public VariableDataText() {
	}

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

	@Override
	public boolean checkType(Object value) {
		if (value instanceof String) {
			return true;
		} else {
			return false;
		}
	}

}

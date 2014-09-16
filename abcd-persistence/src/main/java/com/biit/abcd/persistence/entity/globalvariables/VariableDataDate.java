package com.biit.abcd.persistence.entity.globalvariables;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.globalvariables.exceptions.NotValidTypeInVariableData;

@Entity
@Table(name = "global_variable_data_date")
public class VariableDataDate extends VariableData {

	private Timestamp value;
	// Attribute used for json deserialization due to parent abstract class
	private String isA = null;

	public VariableDataDate() {
		isA = "VariableDataDate";
	}

	@Override
	public Timestamp getValue() {
		return value;
	}

	@Override
	public void setValue(Object value) throws NotValidTypeInVariableData {
		if (checkType(value)) {
			this.value = new Timestamp(((Date) value).getTime());
		} else {
			throw new NotValidTypeInVariableData("The type '" + value.getClass() + "' is not allowed in this variable.");
		}
	}

	@Override
	public boolean checkType(Object value) {
		if (value instanceof Date) {
			return true;
		} else {
			return false;
		}
	}
}

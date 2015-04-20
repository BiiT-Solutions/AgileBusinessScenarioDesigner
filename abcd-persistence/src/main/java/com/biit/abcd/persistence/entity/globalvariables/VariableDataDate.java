package com.biit.abcd.persistence.entity.globalvariables;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.drools.global.variables.exceptions.NotValidTypeInVariableData;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

@Entity
@Table(name = "global_variable_data_date")
public class VariableDataDate extends VariableData {
	private static final long serialVersionUID = 4865657000916202191L;
	private Timestamp value;

	public VariableDataDate() {
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

	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		Set<StorableObject> innerStorableObjects = new HashSet<>();
		return innerStorableObjects;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof VariableDataDate) {
			super.copyData(object);
			VariableDataDate variableDataDate = (VariableDataDate) object;
			value = variableDataDate.getValue();
		} else {
			throw new NotValidStorableObjectException("Object '" + object + "' is not an instance of VariableDataDate.");
		}
	}

}

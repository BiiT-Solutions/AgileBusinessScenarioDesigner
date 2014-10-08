package com.biit.abcd.persistence.entity.globalvariables;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.globalvariables.exceptions.NotValidTypeInVariableData;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

@Entity
@Table(name = "global_variable_data_postalcode")
public class VariableDataPostalCode extends VariableData {

	private String postalCode;

	public VariableDataPostalCode() {
	}

	@Override
	public String getValue() {
		return postalCode;
	}

	@Override
	public void setValue(Object value) throws NotValidTypeInVariableData {
		if (checkType(value)) {
			postalCode = (String) value;
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
		if (object instanceof VariableDataPostalCode) {
			super.copyData(object);
			VariableDataPostalCode variableDataPostalCode = (VariableDataPostalCode) object;
			postalCode = variableDataPostalCode.getValue();
		} else {
			throw new NotValidStorableObjectException("Object '" + object + "' is not an instance of VariableDataPostalCode.");
		}
	}

}

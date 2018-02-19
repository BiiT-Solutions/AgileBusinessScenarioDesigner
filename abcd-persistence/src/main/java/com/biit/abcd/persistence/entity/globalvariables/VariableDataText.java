package com.biit.abcd.persistence.entity.globalvariables;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.drools.global.variables.exceptions.NotValidTypeInVariableData;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

@Entity
@Table(name = "global_variable_data_text")
public class VariableDataText extends VariableData {
	private static final long serialVersionUID = 5710769438060368212L;

	@Column(name = "variable_value")
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

	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		Set<StorableObject> innerStorableObjects = new HashSet<>();
		return innerStorableObjects;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof VariableDataText) {
			super.copyData(object);
			VariableDataText variableDataText = (VariableDataText) object;
			value = variableDataText.getValue();
		} else {
			throw new NotValidStorableObjectException("Object '" + object + "' is not an instance of VariableDataText.");
		}
	}

}

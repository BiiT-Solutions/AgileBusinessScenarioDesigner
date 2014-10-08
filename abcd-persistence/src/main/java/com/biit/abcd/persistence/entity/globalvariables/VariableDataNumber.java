package com.biit.abcd.persistence.entity.globalvariables;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.globalvariables.exceptions.NotValidTypeInVariableData;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

@Entity
@Table(name = "global_variable_data_number")
public class VariableDataNumber extends VariableData {

	private Double value;

	public VariableDataNumber() {
	}

	@Override
	public Double getValue() {
		return value;
	}

	@Override
	public void setValue(Object value) throws NotValidTypeInVariableData {
		if (!checkType(value)) {
			throw new NotValidTypeInVariableData("The type '" + value.getClass() + "' is not allowed in this variable.");
		}
	}

	@Override
	public boolean checkType(Object value) {
		Double aux = null;
		if (value instanceof String) {
			try {
				aux = Double.parseDouble((String) value);
			} catch (Exception e) {
			}
		} else {
			aux = (Double) value;
		}

		if (aux instanceof Double) {
			this.value = aux;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Removes trailing zeros.
	 */
	@Override
	public String toString() {
		return getValue().toString().indexOf(".") < 0 ? getValue().toString() : getValue().toString()
				.replaceAll("0*$", "").replaceAll("\\.$", "");
	}

	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		Set<StorableObject> innerStorableObjects = new HashSet<>();
		return innerStorableObjects;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof VariableDataNumber) {
			super.copyData(object);
			VariableDataNumber variableDataNumber = (VariableDataNumber) object;
			value = variableDataNumber.getValue();
		} else {
			throw new NotValidStorableObjectException("Object '" + object
					+ "' is not an instance of VariableDataNumber.");
		}
	}
}

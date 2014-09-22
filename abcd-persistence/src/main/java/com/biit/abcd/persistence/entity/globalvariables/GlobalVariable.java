package com.biit.abcd.persistence.entity.globalvariables;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.abcd.persistence.entity.globalvariables.exceptions.NotValidTypeInVariableData;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.FieldTooLongException;

@Entity
@Table(name = "global_variables")
public class GlobalVariable extends StorableObject {

	@Column(unique = true, length = MAX_UNIQUE_COLUMN_LENGTH)
	private String name;
	private AnswerFormat format;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinTable(name = "global_variable_data_set")
	private List<VariableData> data;

	public GlobalVariable() {
		data = new ArrayList<VariableData>();
	}

	public GlobalVariable(AnswerFormat format) {
		data = new ArrayList<VariableData>();
		setFormat(format);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) throws FieldTooLongException {
		if (name.length() > MAX_UNIQUE_COLUMN_LENGTH) {
			throw new FieldTooLongException("Name is limited to " + MAX_UNIQUE_COLUMN_LENGTH
					+ " characters due to database restrictions. ");
		}
		this.name = name;
	}

	public List<VariableData> getData() {
		return data;
	}

	public AnswerFormat getFormat() {
		return format;
	}

	private void setFormat(AnswerFormat format) {
		this.format = format;
	}

	public void updateValues(GlobalVariable newVariable) {
		try {
			setName(newVariable.getName());
		} catch (FieldTooLongException e) {
			// Impossible.
		}
	}

	/**
	 * Creates a new variable data and adds it to the global variable
	 * 
	 * @param value
	 *            : the value of the variable data
	 * @param validFrom
	 *            : starting time of the variable
	 * @param validTo
	 *            : finishing time of the variable
	 * @throws NotValidTypeInVariableData
	 */
	public void addVariableData(Object value, Timestamp validFrom, Timestamp validTo) throws NotValidTypeInVariableData {
		VariableData variableData = getNewInstanceVariableData();
		variableData.setValue(value);
		variableData.setValidFrom(validFrom);
		variableData.setValidTo(validTo);
		getData().add(variableData);
	}

	private VariableData getNewInstanceVariableData() {
		switch (format) {
		case DATE:
			return new VariableDataDate();
		case NUMBER:
			return new VariableDataNumber();
		case POSTAL_CODE:
			return new VariableDataPostalCode();
		default:
			return new VariableDataText();
		}
	}

	@Override
	public Set<StorableObject> getAllInnerStorableObjects() {
		Set<StorableObject> innerStorableObjects = new HashSet<>();
		for (VariableData child : data) {
			innerStorableObjects.add(child);
			innerStorableObjects.addAll(child.getAllInnerStorableObjects());
		}
		return innerStorableObjects;
	}
}

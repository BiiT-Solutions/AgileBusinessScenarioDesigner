package com.biit.abcd.persistence.entity.globalvariables;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.AnswerFormat;
import com.biit.abcd.persistence.entity.StorableObject;
import com.biit.abcd.persistence.entity.globalvariables.exceptions.NotValidTypeInVariableData;

@Entity
@Table(name = "GLOBAL_VARIABLES")
public class GlobalVariable extends StorableObject {

	private String name;
	private AnswerFormat format;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinTable(name = "GLOBAL_VARIABLE_DATA_SET")
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

	public void setName(String name) {
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
		setName(newVariable.getName());
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
}

package com.biit.abcd.persistence.entity.globalvariables;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
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
import com.biit.drools.global.variables.DroolsGlobalVariableFormat;
import com.biit.drools.global.variables.exceptions.NotValidTypeInVariableData;
import com.biit.drools.global.variables.interfaces.IGlobalVariable;
import com.biit.drools.global.variables.interfaces.IVariableData;
import com.biit.persistence.entity.StorableObject;
import com.biit.persistence.entity.exceptions.FieldTooLongException;
import com.biit.persistence.entity.exceptions.NotValidStorableObjectException;

@Entity
@Table(name = "global_variables")
public class GlobalVariable extends StorableObject implements IGlobalVariable {
	private static final long serialVersionUID = 3463882037342518214L;
	@Column(unique = true, length = MAX_UNIQUE_COLUMN_LENGTH)
	private String name;
	private AnswerFormat format;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinTable(name = "global_variable_data_set")
	private List<VariableData> variableData;

	public GlobalVariable() {
		variableData = new ArrayList<VariableData>();
	}

	public GlobalVariable(AnswerFormat format) {
		variableData = new ArrayList<VariableData>();
		setFormat(format);
	}

	@Override
	public void resetIds() {
		super.resetIds();
		if (variableData != null) {
			for (IVariableData data : variableData) {
				((VariableData) data).resetIds();
			}
		}
	}

	@Override
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

	public List<VariableData> getVariableData() {
		return variableData;
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
		getVariableData().add(variableData);
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
		for (IVariableData child : variableData) {
			innerStorableObjects.add((VariableData) child);
			innerStorableObjects.addAll(((VariableData) child).getAllInnerStorableObjects());
		}
		return innerStorableObjects;
	}

	@Override
	public void copyData(StorableObject object) throws NotValidStorableObjectException {
		if (object instanceof GlobalVariable) {
			super.copyBasicInfo(object);
			GlobalVariable globalVariable = (GlobalVariable) object;
			name = globalVariable.getName();
			format = globalVariable.getFormat();

			variableData.clear();
			for (IVariableData child : getVariableData()) {
				VariableData data;
				try {
					data = ((VariableData) child).getClass().newInstance();
					data.copyData((VariableData) child);
					variableData.add(data);
				} catch (InstantiationException | IllegalAccessException e) {
					throw new NotValidStorableObjectException("Object '" + object
							+ "' is not a valid instance of GlobalVariable.");
				}
			}
		} else {
			throw new NotValidStorableObjectException("Object '" + object + "' is not an instance of GlobalVariable.");
		}
	}

	@Override
	public String toString() {
		return name + " (" + format + ") " + variableData;
	}

	@Override
	public DroolsGlobalVariableFormat getDroolsVariableFormat() {
		return answerFormatToDroolsFormat(getFormat());
	}

	@Override
	public Object getValue() {
		// First check if the data inside the variable has a valid date
		List<VariableData> varDataList = getVariableData();
		if ((varDataList != null) && !varDataList.isEmpty()) {
			for (IVariableData variableData : varDataList) {
				Timestamp currentTime = new Timestamp(new Date().getTime());
				Timestamp initTime = variableData.getValidFrom();
				Timestamp endTime = variableData.getValidTo();
				// Sometimes endtime can be null, meaning that the
				// variable data has no ending time
				if ((currentTime.after(initTime) && (endTime == null))
						|| (currentTime.after(initTime) && currentTime.before(endTime))) {
					return variableData.getValue();
				}
			}
		}
		return null;
	}

	public static DroolsGlobalVariableFormat answerFormatToDroolsFormat(AnswerFormat answerFormat) {
		switch (answerFormat) {
		case DATE:
			return DroolsGlobalVariableFormat.DATE;
		case NUMBER:
			return DroolsGlobalVariableFormat.NUMBER;
		case POSTAL_CODE:
			return DroolsGlobalVariableFormat.POSTAL_CODE;
		case TEXT:
			return DroolsGlobalVariableFormat.TEXT;
		}
		return null;
	}

	@Override
	public List<IVariableData> getGenericVariableData() {
		List<IVariableData> genericVariableDataList = new ArrayList<IVariableData>();
		genericVariableDataList.addAll(variableData);
		return genericVariableDataList;
	}
}

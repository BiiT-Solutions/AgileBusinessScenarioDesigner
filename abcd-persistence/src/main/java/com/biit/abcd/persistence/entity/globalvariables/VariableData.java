package com.biit.abcd.persistence.entity.globalvariables;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.biit.abcd.persistence.entity.StorableObject;

@Entity
@Table(name = "GLOBAL_VARIABLE_DATA")
public class VariableData extends StorableObject {

	private String value;

	private Timestamp validFrom;

	private Timestamp validTo;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Timestamp getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(Timestamp validFrom) {
		this.validFrom = validFrom;
	}

	public Timestamp getValidTo() {
		return validTo;
	}

	public void setValidTo(Timestamp validTo) {
		this.validTo = validTo;
	}
}

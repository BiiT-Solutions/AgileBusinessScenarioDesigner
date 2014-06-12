package com.biit.abcd.persistence.entity.globalvariables;

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

	public void setFormat(AnswerFormat format) {
		this.format = format;
	}
}

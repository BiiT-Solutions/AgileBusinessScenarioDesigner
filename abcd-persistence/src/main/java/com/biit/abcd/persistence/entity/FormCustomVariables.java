package com.biit.abcd.persistence.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Sets all user defined custom variables that will be used in drools conditions and action.
 * 
 */
@Entity
@Table(name = "FORM_CUSTOM_VARIABLES")
public class FormCustomVariables extends StorableObject {

	@OneToOne
	private Form form;

	/**
	 * Here we only define the name of the variables and the level of each one. I.e. Score of category is defined as
	 * Map<"Score", Category.class> of intVariables
	 */
	@ElementCollection
	@CollectionTable(name = "FORM_CUSTOM_INT_VARIABLES")
	private Map<String, Class<?>> customIntegerVariables = new HashMap<>();
	@ElementCollection
	@CollectionTable(name = "FORM_CUSTOM_STRING_VARIABLES")
	private Map<String, Class<?>> customStringVariables = new HashMap<>();
	@ElementCollection
	@CollectionTable(name = "FORM_CUSTOM_DATE_VARIABLES")
	private Map<String, Class<?>> customDateVariables = new HashMap<>();

	public FormCustomVariables() {

	}

	public FormCustomVariables(Form form) {
		this.setForm(form);
	}

	public void addCustomIntegerVariable(String name, Class<?> level) {
		customIntegerVariables.put(name, level);
	}

	public void addCustomStringVariable(String name, Class<?> level) {
		customStringVariables.put(name, level);
	}

	public void addCustomDateVariable(String name, Class<?> level) {
		customDateVariables.put(name, level);
	}

	/**
	 * Return all int type custom variables of the selected level.
	 * 
	 * @param level
	 * @return
	 */
	public List<String> getCustomIntegerVariables(Class<?> level) {
		List<String> values = new ArrayList<>();
		for (String value : customIntegerVariables.keySet()) {
			if (customIntegerVariables.get(value).equals(level)) {
				values.add(value);
			}
		}
		return values;
	}

	/**
	 * Return all string custom variables of the selected level.
	 * 
	 * @param level
	 * @return
	 */
	public List<String> getCustomStringVariables(Class<?> level) {
		List<String> values = new ArrayList<>();
		for (String value : customStringVariables.keySet()) {
			if (customStringVariables.get(value).equals(level)) {
				values.add(value);
			}
		}
		return values;
	}

	/**
	 * Return all date custom variables of the selected level.
	 * 
	 * @param level
	 * @return
	 */
	public List<String> getCustomDateVariables(Class<?> level) {
		List<String> values = new ArrayList<>();
		for (String value : customDateVariables.keySet()) {
			if (customDateVariables.get(value).equals(level)) {
				values.add(value);
			}
		}
		return values;
	}

	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
	}

	public Map<String, Class<?>> getCustomIntegerVariables() {
		return customIntegerVariables;
	}

	public Map<String, Class<?>> getCustomStringVariables() {
		return customStringVariables;
	}

	public Map<String, Class<?>> getCustomDateVariables() {
		return customDateVariables;
	}
}

package com.biit.abcd.persistence.entity.rules;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.StorableObject;

/**
 * Specific rules created for managing decision tables.
 */
@Entity
@Table(name = "RULE_DECISION_TABLE")
public class TableRule extends StorableObject {

	private String name;

	//A list of columns of the table (NOT IMPLEMENTED YET)
	@ManyToMany(cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Question> conditionsHeader;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	// For avoiding error org.hibernate.loader.MultipleBagFetchException: cannot simultaneously fetch multiple bags
	// (http://stackoverflow.com/questions/4334970/hibernate-cannot-simultaneously-fetch-multiple-bags)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<TableRuleRow> rules;

	public TableRule() {
		rules = new ArrayList<>();
		conditionsHeader = new ArrayList<>();
	}

	public List<TableRuleRow> getRules() {
		return rules;
	}

	public void setRules(List<TableRuleRow> rules) {
		this.rules.clear();
		this.rules.addAll(rules);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return getName();
	}

	public List<Question> getConditionsHeader() {
		return conditionsHeader;
	}

	public void setConditionsHeader(List<Question> conditions) {
		this.conditionsHeader.clear();
		this.conditionsHeader.addAll(conditions);
	}

}

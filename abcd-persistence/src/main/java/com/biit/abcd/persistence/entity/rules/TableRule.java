package com.biit.abcd.persistence.entity.rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.biit.abcd.persistence.entity.Form;
import com.biit.abcd.persistence.entity.Question;
import com.biit.abcd.persistence.entity.StorableObject;

/**
 * Specific rules created for managing decision tables.
 */
@Entity
@Table(name = "RULE_DECISION_TABLE")
public class TableRule extends StorableObject {

	@ManyToOne
	private Form form;

	// Due to bug https://hibernate.atlassian.net/browse/HHH-8839, map must be LAZY.
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinTable(name = "RULE_DECISION_CONDITIONS_MAP", joinColumns = { @JoinColumn(name = "rule_decision_id") })
	@MapKeyColumn(name = "condition_id")
	private Map<Question, Condition> conditions;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	// For avoiding error org.hibernate.loader.MultipleBagFetchException: cannot simultaneously fetch multiple bags
	// (http://stackoverflow.com/questions/4334970/hibernate-cannot-simultaneously-fetch-multiple-bags)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Action> actions;

	public TableRule() {
		conditions = new HashMap<Question, Condition>();
		actions = new ArrayList<>();
	}

	public TableRule(Form form) {
		conditions = new HashMap<Question, Condition>();
		actions = new ArrayList<>();
		setForm(form);
	}

	public void putCondition(Question question, Condition condition) {
		conditions.put(question, condition);
	}

	public void removeCondition(Question question) {
		conditions.remove(question);
	}

	public Map<Question, Condition> getConditions() {
		return conditions;
	}

	public void addAction(Action action) {
		actions.add(action);
	}

	public List<Action> getActions() {
		return actions;
	}

	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
	}

}

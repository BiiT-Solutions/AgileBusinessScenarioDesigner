package com.biit.abcd.persistence.entity.rules;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

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

	@ElementCollection
	@CollectionTable(name = "TABLE_RULES_CONDITIONS")
	private Map<Question, Condition> conditions;

	@OneToOne(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true)
	private Action action;

	public TableRule() {
		conditions = new HashMap<Question, Condition>();
		action = new Action();
	}

	public TableRule(Form form) {
		conditions = new HashMap<Question, Condition>();
		action = new Action();
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

	public Action getAction() {
		return action;
	}

	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
	}

}
